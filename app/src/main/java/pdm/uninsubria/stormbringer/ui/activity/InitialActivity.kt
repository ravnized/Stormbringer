package pdm.uninsubria.stormbringer.ui.activity

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.ui.theme.glow_subtle
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_background_dark
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_primary
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_surface_dark
import pdm.uninsubria.stormbringer.ui.theme.white_100
import pdm.uninsubria.stormbringer.ui.theme.white_20

@Composable
fun StormbringerIntial(
    onLoginClick: () -> Unit,    // Callback per il login
    onRegisterClick: () -> Unit,  // Callback per la registrazione
    onGuestClick: () -> Unit  // Callback per accesso come ospite
){
    Surface(
        modifier = Modifier.fillMaxSize(), color = stormbringer_background_dark
    ){
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
                        enabled = true, shape = RoundedCornerShape(16.dp), colors = ButtonColors(
                            containerColor = stormbringer_primary,
                            contentColor = stormbringer_background_dark,
                            disabledContainerColor = white_20,
                            disabledContentColor = white_20
                        ),

                        onClick = {

                            Log.i("UI", "Click Login")
                            onLoginClick()



                        }, modifier = Modifier.padding().fillMaxWidth(), content = {
                            Text(
                                text = stringResource(R.string.login_title),
                                style = MaterialTheme.typography.headlineSmall,
                                color = stormbringer_surface_dark,
                                modifier = Modifier.padding(16.dp)
                            )
                        })
                })

            Spacer(modifier = Modifier.padding(8.dp))


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
                        enabled = true, shape = RoundedCornerShape(16.dp), colors = ButtonColors(
                            containerColor = stormbringer_primary,
                            contentColor = stormbringer_background_dark,
                            disabledContainerColor = white_20,
                            disabledContentColor = white_20
                        ),

                        onClick = {

                            Log.i("UI", "Click Register")
                            onRegisterClick()


                        }, modifier = Modifier.padding().fillMaxWidth(), content = {
                            Text(
                                text = stringResource(R.string.register_title),
                                style = MaterialTheme.typography.headlineSmall,
                                color = stormbringer_surface_dark,
                                modifier = Modifier.padding(16.dp)
                            )
                        })
                })

            Spacer(modifier = Modifier.padding(8.dp))


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
                        enabled = true, shape = RoundedCornerShape(16.dp), colors = ButtonColors(
                            containerColor = stormbringer_primary,
                            contentColor = stormbringer_background_dark,
                            disabledContainerColor = white_20,
                            disabledContentColor = white_20
                        ),

                        onClick = {

                            Log.i("UI", "Click Guest")
                            onGuestClick()


                        }, modifier = Modifier.padding().fillMaxWidth(), content = {
                            Text(
                                text = stringResource(R.string.guest_access_title),
                                style = MaterialTheme.typography.headlineSmall,
                                color = stormbringer_surface_dark,
                                modifier = Modifier.padding(16.dp)
                            )
                        })
                })
        }
    }
}