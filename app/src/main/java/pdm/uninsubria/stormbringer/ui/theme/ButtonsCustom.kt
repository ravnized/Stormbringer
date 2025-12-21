package pdm.uninsubria.stormbringer.ui.theme

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.Character


@Composable
fun ButtonInfoCharacter(onClick: () -> Unit = {}, character: Character = Character(name = "Gandalf")){
    Box(
        contentAlignment = Alignment.Center,
        propagateMinConstraints = true,
        modifier = Modifier
            .background(
                color = stormbringer_surface_dark,
                shape = RoundedCornerShape(16.dp)
            ).height(100.dp),
        content = {
            Button(
                enabled = true, shape = RoundedCornerShape(16.dp), colors = ButtonColors(
                    containerColor = stormbringer_surface_dark,
                    contentColor = stormbringer_background_dark,
                    disabledContainerColor = white_20,
                    disabledContentColor = white_20
                ),

                onClick = {
                    onClick()
                }, modifier = Modifier.fillMaxWidth().fillMaxHeight(), content = {
                    //row with a image of the character on the left and info on the right
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        //get image from firebase?
                        Text(text = character.name, color = white_70)
                    }
                })
        })
}


@Composable
fun ButtonActionPrimary(onClick: () -> Unit = {}, id: Int = R.string.login_button){
    Box(
        modifier = Modifier
            .innerShadow(
                shape = RoundedCornerShape(16.dp), shadow = Shadow(
                    color = glow_subtle,
                    spread = 1.dp,
                    radius = 16.dp,
                )
            )
            .dropShadow(
                shape = RoundedCornerShape(16.dp), shadow = Shadow(
                    color = glow_subtle,
                    spread = 1.dp,
                    radius = 16.dp,
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

                    onClick()

                }, modifier = Modifier.padding(), content = {
                    Text(
                        text = stringResource(id),
                        style = MaterialTheme.typography.headlineSmall,
                        color = stormbringer_surface_dark,
                        modifier = Modifier.padding(16.dp)
                    )
                })
        })
}