package com.me.recipe.ui.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.R
import com.me.recipe.shared.datastore.UserInfo
import com.me.recipe.ui.component.util.DefaultSnackbar
import com.me.recipe.ui.component.util.MessageEffect
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent

@CircuitInject(ProfileScreen::class, SingletonComponent::class)
@Composable
internal fun ProfileUi(
    state: ProfileState,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val containerColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.background,
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow),
    )
    MessageEffect(
        snackbarHostState = snackbarHostState,
        uiMessage = state.message,
        onClearMessage = { state.eventSink(ProfileEvent.ClearMessage) },
    )

    Scaffold(
        containerColor = containerColor,
        snackbarHost = {
            DefaultSnackbar(
                snackbarHostState = snackbarHostState,
                onAction = { snackbarHostState.currentSnackbarData?.performAction() },
            )
        },
    ) { padding ->
        Content(
            profile = state.profile,
            onLogoutClicked = { state.eventSink.invoke(ProfileEvent.OnLogoutClicked) },
            modifier = modifier.padding(padding),
        )
    }
}

@Composable
fun Content(profile: UserInfo, modifier: Modifier = Modifier, onLogoutClicked: OnClick) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        TitleText()
        Spacer(Modifier.height(50.dp))

        ProfileInfoText(
            title = stringResource(R.string.username),
            text = profile.username,
        )
        Spacer(Modifier.height(16.dp))

        ProfileInfoText(
            title = stringResource(R.string.firstName),
            text = profile.firstName,
        )
        Spacer(Modifier.height(16.dp))

        ProfileInfoText(
            title = stringResource(R.string.lastName),
            text = profile.lastName,
        )
        Spacer(Modifier.height(16.dp))

        ProfileInfoText(
            title = stringResource(R.string.age),
            text = profile.age,
        )
        Spacer(Modifier.height(60.dp))

        LogoutButton(onClick = onLogoutClicked)
    }
}

@Composable
fun TitleText(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.navigate_profile_title),
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
fun ProfileInfoText(
    title: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.White,
                shape = MaterialTheme.shapes.large,
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = "$title:",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun LogoutButton(
    onClick: OnClick,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(text = stringResource(R.string.logout))
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    RecipeTheme(true) {
        ProfileUi(
            state = ProfileState(
                profile = UserInfo(
                    accessToken = "",
                    username = "user_name",
                    firstName = "First Name",
                    lastName = "Last Name",
                    age = "23",
                ),
                eventSink = {},
            ),
        )
    }
}
