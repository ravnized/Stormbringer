package pdm.uninsubria.stormbringer.ui.activity


import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.UserAction
import pdm.uninsubria.stormbringer.ui.fragments.CharacterManageFragment
import pdm.uninsubria.stormbringer.ui.theme.AlertDialogRegister
import pdm.uninsubria.stormbringer.ui.theme.ButtonActionPrimary
import pdm.uninsubria.stormbringer.ui.theme.HeaderLogo
import pdm.uninsubria.stormbringer.ui.theme.InputEmail
import pdm.uninsubria.stormbringer.ui.theme.InputPassword
import pdm.uninsubria.stormbringer.ui.theme.SelectorMode

private const val PASSWORD_VALIDATION_REGEX =
    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"

fun isEmailValid(email: String): Boolean {
    return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPasswordValid(password: String): Boolean {
    return PASSWORD_VALIDATION_REGEX.toRegex().matches(password)
}

@Composable
fun StormbringerRegister() {
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
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
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {


            HeaderLogo(R.string.register_title)

            InputEmail(
                valueEmail, onFocusChanged = { isFocused -> if (!isFocused) isEmailTouched = true })
            if (isEmailTouched && !isEmailValid && valueEmail.text.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.invalid_email_format),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
            InputPassword(
                state = valuePassword,
                onFocusChanged = { isFocused -> if (!isFocused) isPasswordTouched = true })
            if (isPasswordTouched && !isPasswordValid && valuePassword.text.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.invalid_password_format),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }



            Spacer(modifier = Modifier.padding(16.dp))

            ButtonActionPrimary(conditionEnable = isEmailValid && isPasswordValid, onClick = {
                scope.launch {
                    val success = userAction.registerUser(
                        email = valueEmail.text.toString(), pass = valuePassword.text.toString()
                    )
                    if (success) {
                        Log.i("UI", "Navigazione verso il Vuoto...")

                    } else {
                        Log.e("UI", "Registrazione fallita")
                    }
                    titleAlert = if (success) "Patto Sigillato" else "Rituale Fallito"
                    messageAlert = if (success) "Benvenuto nel Vuoto." else "Riprova."
                    showDialog = true
                    if (success) {
                        activity?.supportFragmentManager?.popBackStack(
                            null, FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )

                        activity?.supportFragmentManager?.beginTransaction()
                            ?.setReorderingAllowed(true)?.replace(
                                R.id.fragment_container, CharacterManageFragment()
                            )?.commit()
                    }
                }
            }, id = R.string.register_button)

            SelectorMode()
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

