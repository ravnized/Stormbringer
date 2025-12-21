package pdm.uninsubria.stormbringer.ui.activity

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.UserAction
import pdm.uninsubria.stormbringer.ui.fragments.CharacterManageFragment
import pdm.uninsubria.stormbringer.ui.theme.AlertDialogRegister
import pdm.uninsubria.stormbringer.ui.theme.InputEmail
import pdm.uninsubria.stormbringer.ui.theme.InputPassword
import pdm.uninsubria.stormbringer.ui.theme.glow_subtle
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_background_dark
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_primary
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_surface_dark
import pdm.uninsubria.stormbringer.ui.theme.white_100
import pdm.uninsubria.stormbringer.ui.theme.white_20

@Composable
fun StormbringerLogin() {
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
        val isPasswordValid = remember(valuePassword.text) { isPasswordValid(valuePassword.text.toString()) }
        val activity = context as? androidx.fragment.app.FragmentActivity
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {


            Box(

                modifier = Modifier
                    .innerShadow(
                        shape = RoundedCornerShape(16.dp), shadow = Shadow(
                            color = glow_subtle,
                            spread = 1.dp,
                            radius = 16.dp,
                            //offset = DpOffset(x=0.dp,y=1.5.dp)
                        )
                    )
                    .dropShadow(
                        shape = RoundedCornerShape(16.dp), shadow = Shadow(
                            color = glow_subtle,
                            spread = 1.dp,
                            radius = 16.dp,
                            //offset = DpOffset(x=0.dp,1.5.dp)
                        )
                    ),
                contentAlignment = Alignment.Center,
                propagateMinConstraints = true,
                content = {
                    Image(
                        painter = painterResource(id = R.mipmap.stormbringer_logo_foreground),
                        contentDescription = "Stormbringer Logo",
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                stormbringer_surface_dark
                            )
                    )
                })

            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                color = white_100,
                modifier = Modifier.padding(16.dp)

            )
            Spacer(modifier = Modifier.padding(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                HorizontalDivider(
                    modifier = Modifier.weight(1f), thickness = 1.dp, color = white_20
                )


                Text(
                    text = stringResource(R.string.login_title),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )


                HorizontalDivider(
                    modifier = Modifier.weight(1f), thickness = 1.dp, color = white_20
                )
            }







            InputEmail(
                state = valueEmail,
                onFocusChanged = { isFocused -> if (!isFocused) isEmailTouched = true }
            )
            // Mostra l'errore solo se il campo è stato toccato e non è valido
            if (isEmailTouched && !isEmailValid && valueEmail.text.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.invalid_email_format), // Crea questa stringa in strings.xml
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }


            InputPassword(
                state = valuePassword,
                onFocusChanged = { isFocused -> if (!isFocused) isPasswordTouched = true }
            )
            if (isPasswordTouched && !isPasswordValid && valuePassword.text.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.invalid_password_format), // Crea questa stringa in strings.xml
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }


            Spacer(modifier = Modifier.padding(16.dp))
            Box(

                modifier = Modifier
                    .innerShadow(
                        shape = RoundedCornerShape(16.dp), shadow = Shadow(
                            color = glow_subtle,
                            spread = 1.dp,
                            radius = 16.dp,
                            //offset = DpOffset(x=0.dp,y=1.5.dp)
                        )
                    )
                    .dropShadow(
                        shape = RoundedCornerShape(16.dp), shadow = Shadow(
                            color = glow_subtle,
                            spread = 1.dp,
                            radius = 16.dp,
                            //offset = DpOffset(x=0.dp,1.5.dp)
                        )
                    ),
                contentAlignment = Alignment.Center,
                propagateMinConstraints = true,
                content = {
                    Button(
                        enabled = isEmailValid && isPasswordValid, shape = RoundedCornerShape(16.dp), colors = ButtonColors(
                        containerColor = stormbringer_primary,
                        contentColor = stormbringer_background_dark,
                        disabledContainerColor = white_20,
                        disabledContentColor = white_20
                    ),

                        onClick = {
                            scope.launch {
                                val success = userAction.loginUser(
                                    email = valueEmail.text.toString(),
                                    pass = valuePassword.text.toString()
                                )
                                if (success) {
                                    Log.i("UI", "Navigazione verso il Vuoto...")

                                } else {
                                    Log.e("UI", "Registrazione fallita")
                                }
                                titleAlert = if (success) "Patto Sigillato" else "Rituale Fallito"
                                messageAlert = if (success) "Benvenuto nel Vuoto." else "Riprova."

                                if(success){

                                    activity?.supportFragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                                    activity?.supportFragmentManager?.beginTransaction()
                                        ?.setReorderingAllowed(true)
                                        ?.replace(R.id.fragment_container, CharacterManageFragment())
                                        ?.commit()
                                }


                                showDialog = !success

                            }


                        }, modifier = Modifier.padding(), content = {
                            Text(
                                text = stringResource(R.string.login_button),
                                style = MaterialTheme.typography.headlineSmall,
                                color = stormbringer_surface_dark,
                                modifier = Modifier.padding(16.dp)
                            )
                        })
                })
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