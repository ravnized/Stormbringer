package pdm.uninsubria.stormbringer.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pdm.uninsubria.stormbringer.R

val inputStyling = TextFieldColors(
    unfocusedContainerColor = stormbringer_surface_dark,
    focusedContainerColor = stormbringer_input_bg,
    focusedTextColor = white_70,
    unfocusedTextColor = white_40,
    disabledTextColor = white_20,
    errorTextColor = Pink40,
    disabledContainerColor = white_20,
    errorContainerColor = Pink40,
    cursorColor = glow_active,
    errorCursorColor = Pink40,
    textSelectionColors = TextSelectionColors(stormbringer_primary_dark, glow_active),
    focusedIndicatorColor = stormbringer_primary,
    unfocusedIndicatorColor = stormbringer_primary_dark,
    disabledIndicatorColor = white_05,
    errorIndicatorColor = Pink40,
    focusedLeadingIconColor = white_60,
    unfocusedLeadingIconColor = white_40,
    disabledLeadingIconColor = white_20,
    errorLeadingIconColor = Pink40,
    focusedTrailingIconColor = white_60,
    unfocusedTrailingIconColor =white_40,
    disabledTrailingIconColor = white_20,
    errorTrailingIconColor = Pink40,
    focusedLabelColor = stormbringer_primary,
    unfocusedLabelColor = stormbringer_surface_dark,
    disabledLabelColor = white_20,
    errorLabelColor = Pink40,
    focusedPlaceholderColor = stormbringer_primary,
    unfocusedPlaceholderColor = stormbringer_surface_dark,
    disabledPlaceholderColor = white_20,
    errorPlaceholderColor = Pink40,
    focusedSupportingTextColor = white_60,
    unfocusedSupportingTextColor = white_40,
    disabledSupportingTextColor = white_20,
    errorSupportingTextColor = Pink40,
    focusedPrefixColor = white_60,
    unfocusedPrefixColor = white_40,
    disabledPrefixColor = white_20,
    errorPrefixColor = Pink40,
    focusedSuffixColor = white_60,
    unfocusedSuffixColor = white_40,
    disabledSuffixColor = white_20,
    errorSuffixColor = Pink40
)


@Composable
fun InputEmail(state: TextFieldState = rememberTextFieldState(initialText = "")){


    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,

    ){
        Text(
            stringResource(R.string.email_key),
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 0.dp),
        )



            TextField (

                placeholder = {
                    Text(
                        stringResource(R.string.email_hint),
                        color = white_20
                    )
                },
                lineLimits = TextFieldLineLimits.SingleLine,
                state = state,
                leadingIcon = @Composable {
                        Icon(
                            painter = painterResource(id = R.drawable.mail_24px),
                            contentDescription = "leadingIcon",
                        )
                },
                shape = RoundedCornerShape(16.dp),
                colors = inputStyling,
                modifier = Modifier.padding().fillMaxWidth()
            )



    }

}





@Composable
fun InputPassword(state: TextFieldState = rememberTextFieldState(initialText = "")){


    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            stringResource(R.string.password_key),
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 0.dp),
        )


            var passwordHidden by rememberSaveable { mutableStateOf(true) }
            SecureTextField(
                state = state,
                placeholder = { Text(stringResource(R.string.password_hint), color = white_20) },
                textObfuscationMode =
                    if (passwordHidden) TextObfuscationMode.RevealLastTyped
                    else TextObfuscationMode.Visible,



                leadingIcon = @Composable {

                        Icon(
                            painter = painterResource(id = R.drawable.lock_24px),
                            contentDescription = "leadingIcon",
                        )

                },
                trailingIcon = @Composable {


                        IconButton(onClick = { passwordHidden = !passwordHidden }) {
                            val visibilityIcon =
                                if (passwordHidden) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.visibility_24px),
                                        contentDescription = "leadingIcon",
                                    )
                                }else{
                                    Icon(
                                        painter = painterResource(id = R.drawable.visibility_off_24px),
                                        contentDescription = "leadingIcon",
                                    )
                                }
                            visibilityIcon
                        }

                },
                shape = RoundedCornerShape(16.dp),
                colors = inputStyling,
                modifier = Modifier.padding().fillMaxWidth()

            )


    }

}

@Composable
fun InputGeneral(state: TextFieldState = rememberTextFieldState(initialText = ""), label: Int, hint: Int){
    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,

        ){
        Text(
            stringResource(label),
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 0.dp),
        )



        TextField (

            placeholder = {
                Text(
                    stringResource(hint),
                    color = white_20
                )
            },
            lineLimits = TextFieldLineLimits.SingleLine,
            state = state,
            shape = RoundedCornerShape(16.dp),
            colors = inputStyling,
            modifier = Modifier.padding().fillMaxWidth()
        )



    }
}

