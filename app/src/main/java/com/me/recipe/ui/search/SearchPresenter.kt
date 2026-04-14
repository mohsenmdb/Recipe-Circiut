package com.me.recipe.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.search.ObservePagedVitrineNew
import com.me.recipe.domain.features.search.VitrinePagingKey
import com.me.recipe.domain.features.search.VitrinePagingSourceNew.Companion.FIRST_PAGE
import com.me.recipe.domain.util.ForceFresh
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.shared.utils.getFoodCategory
import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.ui.component.util.UiMessage
import com.me.recipe.ui.component.util.UiMessageManager
import com.me.recipe.ui.recipe.RecipeScreen
import com.me.recipe.ui.search.SearchEvent.ClearMessage
import com.me.recipe.ui.search.SearchEvent.NewSearchEvent
import com.me.recipe.ui.search.SearchEvent.OnChangeRecipeScrollPosition
import com.me.recipe.ui.search.SearchEvent.OnQueryChanged
import com.me.recipe.ui.search.SearchEvent.OnRecipeClick
import com.me.recipe.ui.search.SearchEvent.OnRecipeLongClick
import com.me.recipe.ui.search.SearchEvent.OnSelectedCategoryChanged
import com.me.recipe.ui.search.SearchEvent.SearchClearEvent
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import recipe.app.core.errorformater.ErrorFormatter

class SearchPresenter @AssistedInject constructor(
    @Assisted private val screen: SearchScreen,
    @Assisted internal val navigator: Navigator,
//    private val searchRecipesUseCase: Lazy<SearchRecipesUseCase>,
//    private val restoreRecipesUseCase: Lazy<RestoreRecipesUseCase>,
    private val errorFormatter: Lazy<ErrorFormatter>,
    private val pagingInteractor: Lazy<ObservePagedVitrineNew>,
    private val vitrinePagingSource: Lazy<PagingSource<VitrinePagingKey, Recipe>>,
) : Presenter<SearchState> {

    @Composable
    override fun present(): SearchState {
        val stableScope = rememberStableCoroutineScope()
        val uiMessageManager = rememberRetained { UiMessageManager() }
        val message by uiMessageManager.message.collectAsState(null)
        var errorDialogInfo by rememberRetained { mutableStateOf<GenericDialogInfo?>(null) }
        var forceRefresher by rememberRetained { mutableStateOf<ForceFresh?>(null) }

        var selectedCategory by rememberRetained { mutableStateOf<FoodCategory?>(null) }
        var searchText by rememberRetained { mutableStateOf("") }
        var query by rememberRetained { mutableStateOf(screen.query) }

        val retainedPagingInteractor = rememberRetained { pagingInteractor.get() }
        val items = retainedPagingInteractor.flow
            .rememberRetainedCachedPagingFlow(query)
            .collectAsLazyPagingItems()

        retainedPagingInteractor(
            ObservePagedVitrineNew.Params(
                pagingConfig = PAGING_CONFIG,
                forceRefresh = forceRefresher,
                key = VitrinePagingKey(
                    query = query,
                    page = FIRST_PAGE,
                    loadMore = false,
                ),
            ),
        )

//        LaunchedEffect(recipes?.exceptionOrNull()) {
//            if (recipes?.exceptionOrNull() != null) {
//                errorDialogInfo = GenericDialogInfo.Builder()
//                    .title(R.string.error)
//                    .description(errorFormatter.get().format(recipes?.exceptionOrNull()))
//                    .positive(
//                        PositiveAction(
//                            positiveBtnTxt = R.string.try_again,
//                            onPositiveAction = {
//                                forceRefresher = ForceFresh.refresh()
//                                isLoading = true
//                                errorDialogInfo = null
//                            },
//                        ),
//                    )
//                    .onDismiss { errorDialogInfo = null }
//                    .build()
//            }
//        }
        fun onSelectedCategoryChanged(category: String) {
            selectedCategory = getFoodCategory(category)
            query = category
        }
        fun onNewSearchEvent() {
            selectedCategory = null
            query = searchText
        }
        fun navigateToRecipePage(recipe: Recipe) {
            navigator.goTo(
                RecipeScreen(
                    itemImage = recipe.image,
                    itemTitle = recipe.title,
                    itemId = recipe.id,
                    itemUid = recipe.uid,
                ),
            )
        }
        return SearchState(
            items = items,
            selectedCategory = selectedCategory,
            query = searchText,
            message = message,
            errors = errorDialogInfo,
            eventSink = { event ->
                when (event) {
                    is OnSelectedCategoryChanged -> {
                        onSelectedCategoryChanged(event.category)
                    }
                    is OnQueryChanged -> {
                        searchText = event.query
                    }
                    SearchClearEvent -> {
                        searchText = ""
                        query = ""
                    }
                    NewSearchEvent -> onNewSearchEvent()
                    is OnRecipeClick -> navigateToRecipePage(event.recipe)
                    is OnChangeRecipeScrollPosition -> {}
                    is OnRecipeLongClick -> {
                        stableScope.launch { uiMessageManager.emitMessage(UiMessage.createSnackbar(event.title)) }
                    }
                    ClearMessage -> stableScope.launch { uiMessageManager.clearMessage() }
                }
            },
        )
    }

    @CircuitInject(SearchScreen::class, SingletonComponent::class)
    @AssistedFactory
    interface Factory {
        fun create(screen: SearchScreen, navigator: Navigator): SearchPresenter
    }
    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 1,
            initialLoadSize = 10,
        )
    }
}

@Composable
fun rememberRetainedCoroutineScope(): CoroutineScope {
    return rememberRetained("coroutine_scope") {
        RememberObserverHolder(
            value = CoroutineScope(context = Dispatchers.Main + Job()),
            onDestroy = CoroutineScope::cancel,
        )
    }.value
}

internal class RememberObserverHolder<T>(
    val value: T,
    private val onDestroy: (T) -> Unit,
) : RememberObserver {
    override fun onAbandoned() {
        onDestroy(value)
    }

    override fun onForgotten() {
        onDestroy(value)
    }

    override fun onRemembered() = Unit
}

@Composable
inline fun <T : Any> Flow<PagingData<T>>.rememberRetainedCachedPagingFlow(
    key: Any,
    scope: CoroutineScope = rememberRetainedCoroutineScope(),
): Flow<PagingData<T>> = rememberRetained(this, key, scope) { cachedIn(scope) }

inline fun CombinedLoadStates.appendErrorOrNull(): UiMessage? {
    return (append as? LoadState.Error)?.let { UiMessage.createSnackbar(it.error) }
}

inline fun CombinedLoadStates.prependErrorOrNull(): UiMessage? {
    return (prepend as? LoadState.Error)?.let { UiMessage.createSnackbar(it.error) }
}

inline fun CombinedLoadStates.refreshErrorOrNull(): UiMessage? {
    return (refresh as? LoadState.Error)?.let { UiMessage.createSnackbar(it.error) }
}

inline fun CombinedLoadStates.isRefreshing(): Boolean {
    return refresh == LoadState.Loading
}

inline fun CombinedLoadStates.isAppending(): Boolean {
    return append == LoadState.Loading
}

inline fun CombinedLoadStates.isEmpty(itemCount: Int): Boolean {
    return refresh is LoadState.NotLoading && itemCount == 0 && (append as? LoadState.NotLoading)?.endOfPaginationReached == true
}
