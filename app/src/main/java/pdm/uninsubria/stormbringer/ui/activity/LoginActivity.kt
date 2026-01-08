package pdm.uninsubria.stormbringer.ui.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.UserAction
import pdm.uninsubria.stormbringer.tools.UserPreferences
import pdm.uninsubria.stormbringer.ui.fragments.CharacterManageFragment
import pdm.uninsubria.stormbringer.ui.fragments.PartyManagerFragment
import pdm.uninsubria.stormbringer.ui.theme.AlertDialogRegister
import pdm.uninsubria.stormbringer.ui.theme.ButtonActionPrimary
import pdm.uninsubria.stormbringer.ui.theme.HeaderLogo
import pdm.uninsubria.stormbringer.ui.theme.InputEmail
import pdm.uninsubria.stormbringer.ui.theme.InputPassword
import pdm.uninsubria.stormbringer.ui.theme.SelectorMode
import pdm.uninsubria.stormbringer.ui.theme.ValidationError
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_primary
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_surface_dark

@Composable
fun StormbringerLogin() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        color = MaterialTheme.colorScheme.background,
    ) {
        val context = LocalContext.current
        val userAction = remember { UserAction(context) }
        val valueEmail = rememberTextFieldState(initialText = "")
        val valuePassword = rememberTextFieldState(initialText = "")
        val scope = rememberCoroutineScope()
        var showDialog by remember { mutableStateOf(false) }
        var titleAlert by remember { mutableStateOf("") }
        var messageAlert by remember { mutableStateOf("") }
        var isEmailTouched by remember { mutableStateOf(false) }
        var isPasswordTouched by remember { mutableStateOf(false) }
        val isEmailValid = remember(valueEmail.text) { isEmailValid(valueEmail.text.toString()) }
        val isPasswordValid =
            remember(valuePassword.text) { isPasswordValid(valuePassword.text.toString()) }
        val activity = context as? androidx.fragment.app.FragmentActivity
        var isLoading by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly

        ) {


            HeaderLogo(R.string.login_title)







            InputEmail(
                state = valueEmail,
                onFocusChanged = { isFocused -> if (!isFocused) isEmailTouched = true })

            ValidationError(
                text = stringResource(R.string.invalid_email_format),
                isVisible = isEmailTouched && !isEmailValid && valueEmail.text.isNotEmpty()
            )


            InputPassword(
                state = valuePassword,
                onFocusChanged = { isFocused -> if (!isFocused) isPasswordTouched = true })
            ValidationError(
                text = stringResource(R.string.invalid_password_format),
                isVisible = isPasswordTouched && !isPasswordValid && valuePassword.text.isNotEmpty()
            )


            Spacer(modifier = Modifier.padding(16.dp))
            val txtErrorTitle = stringResource(R.string.error_title_generic)
            val txtErrorMsg = stringResource(R.string.login_message_failure)
            ButtonActionPrimary(conditionEnable = isEmailValid && isPasswordValid, onClick = {
                isLoading = true
                scope.launch {
                    val savedMode = UserPreferences(context).getPreferencesString("player_mode")
                    val success = userAction.loginUser(
                        email = valueEmail.text.toString(), pass = valuePassword.text.toString()
                    )


                    if (success) {
                        isLoading = false
                        activity?.supportFragmentManager?.popBackStack(
                            null, FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )


                        if (savedMode == "GM") {
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)?.setReorderingAllowed(true)?.replace(
                                    R.id.fragment_container, PartyManagerFragment()
                                )?.commit()

                        } else {
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)?.setReorderingAllowed(true)?.replace(
                                    R.id.fragment_container, CharacterManageFragment()
                                )?.commit()
                        }

                    } else {
                        isLoading = false
                        titleAlert = txtErrorTitle
                        messageAlert = txtErrorMsg

                        showDialog = true
                    }


                }
            }, id = R.string.login_button)


            SelectorMode()
            if (isLoading) {
                Dialog(onDismissRequest = { /* Disable dismiss on outside touch */ }) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = stormbringer_surface_dark),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(0.3f)
                            .padding(16.dp)
                    )

                    {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = stormbringer_primary)
                        }

                    }
                }

            }

            if (showDialog) {
                AlertDialogRegister(
                    alertTitle = titleAlert,
                    alertMessage = messageAlert,
                    onDismiss = { showDialog = false } //reset
                )
            }
        }
    }
}