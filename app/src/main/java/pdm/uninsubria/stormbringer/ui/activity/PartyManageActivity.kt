package pdm.uninsubria.stormbringer.ui.activity

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.Character
import pdm.uninsubria.stormbringer.tools.Party
import pdm.uninsubria.stormbringer.tools.UserPreferences
import pdm.uninsubria.stormbringer.tools.getCharacterById
import pdm.uninsubria.stormbringer.tools.loadPartyInfo
import pdm.uninsubria.stormbringer.tools.uploadCharacterImage
import pdm.uninsubria.stormbringer.ui.theme.ButtonActionPrimary
import pdm.uninsubria.stormbringer.ui.theme.CustomBottomSheet
import pdm.uninsubria.stormbringer.ui.theme.ImageSourceOptionDialog
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_primary

@Composable
fun PartyManageActivity() {
    val context = LocalContext.current
    val db = remember { FirebaseFirestore.getInstance() }
    var partyId by remember { mutableStateOf("") }
    var contentLoaded by remember { mutableStateOf(false) }
    var partyInfo by remember { mutableStateOf(Party()) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var list_members by remember { mutableStateOf(listOf<Character>()) }

    LaunchedEffect(Unit) {
        partyId = UserPreferences(context).getPreferencesString("current_party_id")
        Log.i("PartyManageActivity", "Loaded Party ID: $partyId")
        partyInfo = loadPartyInfo(db = db, partyId = partyId) ?: Party()
        Log.i("PartyManageActivity", "Loaded Party Info: $partyInfo")
        partyInfo.members.forEach { memberId ->
            list_members += getCharacterById(db = db, characterId = memberId) ?: Character()
            Log.i("PartyManageActivity", "Member ID: $memberId")
        }
        contentLoaded = true
    }
    val message = "${stringResource(R.string.whatsapp_text)}: *${partyInfo.id}*"
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = androidx.compose.material3.MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            if (!contentLoaded) {
                CircularProgressIndicator(color = stormbringer_primary)
            } else {

                Text(
                    text = "Party: ${partyInfo.name}",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text("Code: ${partyInfo.id}", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.bodyMedium)
                ButtonActionPrimary(
                    id = R.string.send_code_to_whatsapp, conditionEnable = true, onClick = {


                        val whatsappIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            setPackage("com.whatsapp")
                            putExtra(Intent.EXTRA_TEXT, message)
                        }

                        try {
                            context.startActivity(whatsappIntent)
                            showBottomSheet = false
                        } catch (e: Exception) {
                            //no whatsapp
                            showBottomSheet = true

                        }
                    })

                if(list_members.isEmpty()){
                    Text(
                        text = stringResource(R.string.no_party_members),
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }else{
                    Text(
                        text = stringResource(R.string.party_members_list),
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    list_members.forEach {
                            member ->
                        Text(
                            text = "- ${member.name} (Lv.${member.level} ${member.characterClass})",
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }



            }





        }
    }
    CustomBottomSheet(isVisible = showBottomSheet, onDismiss = {
        showBottomSheet = false
    }, content = {
        Text(text = stringResource(R.string.whatsapp_not_installed), modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium)
    })

}