package pdm.uninsubria.stormbringer.ui.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import pdm.uninsubria.stormbringer.tools.Party
import pdm.uninsubria.stormbringer.tools.UserPreferences
import pdm.uninsubria.stormbringer.tools.loadPartyInfo

@Composable
fun PartyManageActivity() {
    val context = LocalContext.current
    val db = remember { FirebaseFirestore.getInstance() }
    var partyId by remember { mutableStateOf("") }
    var contentLoaded by remember { mutableStateOf(false) }
    var partyInfo by remember { mutableStateOf(Party()) }
    LaunchedEffect(Unit) {
        partyId = UserPreferences(context).getPreferencesString("party_id")
        val gameMaster = UserPreferences(context).getPreferencesString("unique_id")
        partyInfo = loadPartyInfo(db = db, partyId = partyId, gameMaster = gameMaster ) ?: Party()

        contentLoaded = true
    }



}