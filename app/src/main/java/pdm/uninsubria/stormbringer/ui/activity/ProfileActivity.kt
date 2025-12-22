package pdm.uninsubria.stormbringer.ui.activity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.UserPreferences
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_primary
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_surface_dark
import pdm.uninsubria.stormbringer.ui.theme.white_40

@Composable
fun ProfileDialog(
    email: String,
    onDismiss: () -> Unit,
    onLogout: () -> Unit
) {
    var savedMode = ""
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        savedMode = UserPreferences(context).getPreferencesString("player_mode")
    }
    Dialog(onDismissRequest = { onDismiss() }) {

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = stormbringer_surface_dark),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)){
                    Icon(
                        painter = painterResource(id = R.drawable.close_24px),
                        contentDescription = "Close Icon",
                        tint = white_40
                    )
                }

                // Icona Profilo
                Icon(
                    painter = painterResource(id = R.drawable.person_24px),
                    contentDescription = "Profile",
                    modifier = Modifier.size(64.dp),
                    tint = stormbringer_primary
                )



                Spacer(modifier = Modifier.height(16.dp))

                // Email Utente
                Text(
                    text = "${stringResource(R.string.email_key)} :",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                //Ruolo
                Text(
                    text = stringResource(R.string.role_player),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = savedMode,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tasto Logout
                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id =R.drawable.logout_24px),
                        contentDescription = "Logout Icon",
                        tint = white_40
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Esci")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}