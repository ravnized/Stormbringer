package pdm.unindubria.stormbringer


import androidx.compose.ui.graphics.Color
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SecureTextField

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.shadow.Shadow

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pdm.unindubria.stormbringer.ui.theme.Pink40
import pdm.unindubria.stormbringer.ui.theme.StormbringerTheme
import pdm.unindubria.stormbringer.ui.theme.glow_active
import pdm.unindubria.stormbringer.ui.theme.glow_subtle
import pdm.unindubria.stormbringer.ui.theme.stormbringer_input_bg
import pdm.unindubria.stormbringer.ui.theme.stormbringer_primary
import pdm.unindubria.stormbringer.ui.theme.stormbringer_primary_dark
import pdm.unindubria.stormbringer.ui.theme.stormbringer_surface_dark
import pdm.unindubria.stormbringer.ui.theme.white_05
import pdm.unindubria.stormbringer.ui.theme.white_100
import pdm.unindubria.stormbringer.ui.theme.white_20
import pdm.unindubria.stormbringer.ui.theme.white_40
import pdm.unindubria.stormbringer.ui.theme.white_60
import pdm.unindubria.stormbringer.ui.theme.white_70

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StormbringerTheme {
                StormbringerHome()
            }
        }
    }
}

@Composable
fun StormbringerHome(){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            Box(

                modifier = Modifier
                    .innerShadow(
                        shape = RoundedCornerShape(16.dp),
                        shadow = Shadow(
                            color = glow_subtle,
                            spread = 1.dp,
                            radius = 16.dp,
                            //offset = DpOffset(x=0.dp,y=1.5.dp)
                        )
                    )
                    .dropShadow(
                        shape = RoundedCornerShape(16.dp),
                        shadow = Shadow(
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
                }
            )

            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                color = white_100,
                modifier = Modifier.padding(16.dp)

            )
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
            InputRender(
                hint = "mage@stormbringer.rpg", text = "Email address",
                tint = white_20, leadingIcon = R.drawable.mail_24px, colors = inputStyling
            )

            InputRender(type = "Passowrd", hint = "******", text = "Password",
                tint = white_20, leadingIcon = R.drawable.lock_24px, trailingIcon = R.drawable.visibility_24px, trailingIcon_filled = R.drawable.visibility_off_24px, colors = inputStyling
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun InputRender(type: String = "Email", hint: String = "mage@stormbringer.rpg", text: String = "Email address", tint: Color = white_100, leadingIcon: Int ?= null, trailingIcon: Int ?= null, trailingIcon_filled: Int ?= null, colors: TextFieldColors ?= null){


    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 0.dp),
            color = tint
        )

        if(type == "Email"){
            var value =rememberTextFieldState(initialText = "")
            TextField (

                placeholder = {
                    Text(
                        hint,
                        color = white_20
                    )
                },
                lineLimits = TextFieldLineLimits.SingleLine,
                state = value,
                leadingIcon = @Composable {
                    if(leadingIcon != null)
                        Icon(
                            painter = painterResource(id = leadingIcon),
                            contentDescription = "leadingIcon",
                            tint = tint)

                },
                trailingIcon = @Composable {
                    if(trailingIcon != null)
                        Icon(
                            painter = painterResource(id = trailingIcon),
                            contentDescription = "trailingIcon",
                            tint = tint)
                },
                shape = RoundedCornerShape(16.dp),
                colors = colors!!,
                modifier = Modifier.padding()
            )
        }else {
            val value: TextFieldState = rememberTextFieldState(initialText = "")
            var passwordHidden by rememberSaveable { mutableStateOf(true) }
            SecureTextField(
                state = value,
                placeholder = { Text(hint, color = white_20) },
                textObfuscationMode =
                    if (passwordHidden) TextObfuscationMode.RevealLastTyped
                    else TextObfuscationMode.Visible,



                leadingIcon = @Composable {
                    if(leadingIcon != null)
                        Icon(
                            painter = painterResource(id = leadingIcon),
                            contentDescription = "leadingIcon",
                            tint = tint)

                },
                trailingIcon = @Composable {
                    if(trailingIcon != null && trailingIcon_filled != null)

                            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                                val visibilityIcon =
                                    if (passwordHidden) {
                                        Icon(
                                            painter = painterResource(id = trailingIcon),
                                            contentDescription = "leadingIcon",
                                            tint = tint)
                                    }else{
                                        Icon(
                                            painter = painterResource(id = trailingIcon_filled),
                                            contentDescription = "leadingIcon",
                                            tint = tint)
                                    }
                                visibilityIcon
                            }

                },
                shape = RoundedCornerShape(16.dp),
                colors = colors!!,
                modifier = Modifier.padding()

            )
        }

    }



}


