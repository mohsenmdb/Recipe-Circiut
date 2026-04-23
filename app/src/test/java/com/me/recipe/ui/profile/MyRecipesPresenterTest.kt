package com.me.recipe.ui.profile

import android.app.Application
import androidx.paging.PagingSource
import androidx.paging.PagingState
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.search.ObservePagedVitrineNew
import com.me.recipe.domain.features.search.VitrinePagingKey
import com.me.recipe.ui.component.util.ErrorFormatterFake
import com.me.recipe.ui.profile.myrecipes.MyRecipesEvent
import com.me.recipe.ui.profile.myrecipes.MyRecipesPresenter
import com.me.recipe.ui.profile.myrecipes.MyRecipesScreen
import com.me.recipe.ui.recipe.RecipeScreen
import com.me.recipe.ui.search.SearchPresenter
import com.me.recipe.ui.search.SearchScreen
import com.me.recipe.ui.utils.lazyOfDagger
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class MyRecipesPresenterTest {

    @Test
    fun `present - creates search presenter in only my recipes mode`() = runTest {
        val searchPresenterFactory = RecordingSearchPresenterFactory()
        val navigator = FakeNavigator(ProfileScreen, MyRecipesScreen)

        createMyRecipesPresenter(
            navigator = navigator,
            searchPresenterFactory = searchPresenterFactory,
        ).test {
            val state = awaitItem()

            assertThat(searchPresenterFactory.createdScreen).isEqualTo(SearchScreen(onlyMyRecipes = true))
            assertThat(searchPresenterFactory.createdNavigator).isEqualTo(navigator)
            assertThat(state.message).isNull()
            assertThat(state.errors).isNull()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `present - when recipe is clicked then navigate to recipe screen`() = runTest {
        val navigator = FakeNavigator(ProfileScreen, MyRecipesScreen)
        val recipe = Recipe.Companion.testData()

        createMyRecipesPresenter(
            navigator = navigator,
        ).test {
            val state = awaitItem()

            state.eventSink(MyRecipesEvent.OnRecipeClick(recipe))

            assertThat(navigator.awaitNextScreen()).isEqualTo(
                RecipeScreen(
                    itemImage = recipe.image,
                    itemTitle = recipe.title,
                    itemId = recipe.id,
                    itemUid = recipe.uid,
                ),
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `present - when recipe is long clicked and clear message is clicked then message is removed`() =
        runTest {
            createMyRecipesPresenter().test {
                val initialState = awaitItem()

                initialState.eventSink(MyRecipesEvent.OnRecipeLongClick("Thai Soup"))

                val stateWithMessage = awaitItem()
                assertThat(stateWithMessage.message?.message?.text).isEqualTo("Thai Soup")

                stateWithMessage.eventSink(MyRecipesEvent.ClearMessage)

                val clearedState = awaitItem()
                assertThat(clearedState.message).isNull()

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `present - when navigate back is clicked then navigator pops`() = runTest {
        val navigator = FakeNavigator(ProfileScreen, MyRecipesScreen)

        createMyRecipesPresenter(
            navigator = navigator,
        ).test {
            val state = awaitItem()

            state.eventSink(MyRecipesEvent.OnNavigateBackClicked)

            assertThat(navigator.awaitPop().poppedScreen).isEqualTo(MyRecipesScreen)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createMyRecipesPresenter(
        screen: MyRecipesScreen = MyRecipesScreen,
        navigator: Navigator = FakeNavigator(ProfileScreen, MyRecipesScreen),
        searchPresenterFactory: SearchPresenter.Factory = RecordingSearchPresenterFactory(),
    ): MyRecipesPresenter {
        return MyRecipesPresenter(
            screen = screen,
            navigator = navigator,
            searchPresenterFactory = searchPresenterFactory,
        )
    }

    private class RecordingSearchPresenterFactory : SearchPresenter.Factory {
        var createdScreen: SearchScreen? = null
        var createdNavigator: Navigator? = null

        override fun create(screen: SearchScreen, navigator: Navigator): SearchPresenter {
            createdScreen = screen
            createdNavigator = navigator
            return SearchPresenter(
                screen = screen,
                navigator = navigator,
                errorFormatter = lazyOfDagger(ErrorFormatterFake()),
                pagingInteractor = lazyOfDagger(
                    ObservePagedVitrineNew(
                        pagingSource = TestRecipePagingSource(),
                    ),
                ),
            )
        }
    }

    private class TestRecipePagingSource : PagingSource<VitrinePagingKey, Recipe>() {
        override fun getRefreshKey(state: PagingState<VitrinePagingKey, Recipe>): VitrinePagingKey? = null

        override suspend fun load(params: LoadParams<VitrinePagingKey>): LoadResult<VitrinePagingKey, Recipe> {
            return LoadResult.Page(
                data = listOf(Recipe.Companion.testData()),
                prevKey = null,
                nextKey = null,
            )
        }
    }
}
