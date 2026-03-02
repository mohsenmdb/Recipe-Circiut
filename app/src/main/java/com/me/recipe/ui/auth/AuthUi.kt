package com.me.recipe.ui.auth

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.R
import com.me.recipe.ui.component.util.DefaultSnackbar
import com.me.recipe.ui.component.util.MessageEffect
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent

@CircuitInject(AuthScreen::class, SingletonComponent::class)
@Composable
internal fun AuthUi(
    state: AuthState,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val containerColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.background,
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow),
    )
    MessageEffect(
        snackbarHostState = snackbarHostState,
        message = state.message,
        onClearMessage = { state.eventSink(AuthEvent.ClearMessage) },
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
        Content(state, modifier.padding(padding))
    }
}

@Composable
private fun Content(state: AuthState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        TitleText(text = stringResource(if (state.isLoginMode) R.string.login else R.string.register))
        Spacer(modifier = Modifier.height(16.dp))
        Inputs(state)
        Spacer(modifier = Modifier.height(24.dp))
        Buttons(
            isLoading = state.isLoading,
            isLoginMode = state.isLoginMode,
            isSubmitButtonEnable = state.isSubmitButtonEnable,
            onSubmitClicked = { state.eventSink(AuthEvent.OnSubmitClicked) },
            onSwitchModeClicked = { state.eventSink(AuthEvent.OnSwitchModeClicked) },
        )
    }
}

@Composable
private fun ColumnScope.Inputs(state: AuthState) {
    TextInput(
        label = stringResource(R.string.username),
        username = state.username,
        onUsernameChange = { state.eventSink(AuthEvent.OnUsernameChange(it)) },
    )
    Spacer(modifier = Modifier.height(8.dp))

    if (!state.isLoginMode) {
        TextInput(
            label = stringResource(R.string.firstName),
            username = state.firstName,
            onUsernameChange = { state.eventSink(AuthEvent.OnFirstNameChange(it)) },
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextInput(
            label = stringResource(R.string.lastName),
            username = state.lastName,
            onUsernameChange = { state.eventSink(AuthEvent.OnLastNameChange(it)) },
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextInput(
            label = stringResource(R.string.age),
            username = state.age,
            onUsernameChange = { state.eventSink(AuthEvent.OnAgeChange(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Spacer(modifier = Modifier.height(8.dp))
    }

    PasswordInput(
        password = state.password,
        onPasswordChange = {
            state.eventSink(AuthEvent.OnPasswordChange(it))
        },
    )

    if (!state.isLoginMode) {
        Spacer(modifier = Modifier.height(8.dp))
        RetryPasswordInput(
            password = state.retryPassword,
            passwordError = state.hasPasswordError,
            onPasswordChange = {
                state.eventSink(AuthEvent.OnRetryPasswordChange(it))
            },
        )

        if (state.hasPasswordError) PasswordErrorText()
    }
}

@Composable
private fun ColumnScope.Buttons(
    isLoginMode: Boolean,
    isLoading: Boolean,
    onSubmitClicked: OnClick,
    onSwitchModeClicked: OnClick,
    isSubmitButtonEnable: Boolean,
) {
    SubmitButton(
        text = stringResource(if (isLoginMode) R.string.login else R.string.register),
        isLoading = isLoading,
        isEnable = isSubmitButtonEnable,
        onClick = onSubmitClicked,
    )
    Spacer(modifier = Modifier.height(8.dp))

    SwitchModeButton(
        text = stringResource(if (isLoginMode) R.string.do_not_have_account else R.string.already_have_account),
        onClick = onSwitchModeClicked,
    )
}

@Composable
private fun TitleText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun TextInput(
    label: String,
    username: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onUsernameChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = username,
        onValueChange = { onUsernameChange(it) },
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun PasswordInput(password: String, onPasswordChange: (String) -> Unit) {
    OutlinedTextField(
        value = password,
        onValueChange = {
            onPasswordChange(it)
        },
        label = { Text("Password") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun RetryPasswordInput(
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: Boolean,
) {
    OutlinedTextField(
        value = password,
        onValueChange = {
            onPasswordChange(it)
        },
        label = { Text("Retry Password") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
        isError = passwordError,
    )
}

@Composable
private fun PasswordErrorText() {
    Text(
        text = "Passwords do not match",
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
    )
}

@Composable
private fun SubmitButton(
    text: String,
    isLoading: Boolean,
    isEnable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = isEnable,
        modifier = modifier.fillMaxWidth(),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp,
            )
        } else {
            Text(text = text)
        }
    }
}

@Composable
private fun SwitchModeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(text = text)
    }
}

@Preview
@Composable
private fun AuthScreenPreview() {
    RecipeTheme(true) {
        AuthUi(
            state = AuthState(eventSink = {}),
        )
    }
}
