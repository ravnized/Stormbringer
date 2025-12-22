package pdm.uninsubria.stormbringer.ui.activity

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.Character
import pdm.uninsubria.stormbringer.tools.Party
import pdm.uninsubria.stormbringer.tools.UserPreferences
import pdm.uninsubria.stormbringer.tools.addNewMemberToParty
import pdm.uninsubria.stormbringer.tools.getCharacterById
import pdm.uninsubria.stormbringer.tools.loadPartyInfo
import pdm.uninsubria.stormbringer.tools.loadPartyInfoByCharacter
import pdm.uninsubria.stormbringer.tools.loadPartyInfoByUser
import pdm.uninsubria.stormbringer.ui.theme.AlertDialogRegister
import pdm.uninsubria.stormbringer.ui.theme.ButtonInfoCharacter
import pdm.uninsubria.stormbringer.ui.theme.InputGeneral
import pdm.uninsubria.stormbringer.ui.theme.NavigationBarSection

@Composable
fun StormbringerPartyActivity() {
    val db = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    var party by remember { mutableStateOf(Party()) }
    var mode by remember { mutableStateOf("") }
    val context = LocalContext.current
    context as? FragmentActivity
    val userPreferences = remember { UserPreferences(context) }
    var showTextSelect = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        val characterId = userPreferences.getPreferencesString("character_id")
        val savedMode = userPreferences.getPreferencesString("player_mode")
        if (characterId.isNotEmpty() && savedMode != "GM") {
            val loadedParty = loadPartyInfoByCharacter(db= db, characterId = characterId)
            if (loadedParty != null) {
                party = loadedParty
            }


            if (savedMode.isNotEmpty()) {
                mode = savedMode
            }
            showTextSelect.value = false
        }else if(savedMode=="GM") {
            val userUid = auth.currentUser?.uid ?: ""
            val loadedParty = loadPartyInfoByUser(db = db, userUid = userUid)
            if (loadedParty != null) {
                party = loadedParty
            }

            mode = "GM"
            showTextSelect.value = false
        }else {
            Log.e("PartyActivity", "No character ID or user is not GM")
            showTextSelect.value = true
        }


    }


    NavigationBarSection(
        headLine = stringResource(R.string.party_title),
        floatingActionButton = {},
        currentTab = 2,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (mode == "GM") {

                } else {
                    if(showTextSelect.value) {
                        Text(
                            text = stringResource(R.string.no_heroes_selected),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }else{
                        playerScreen(partyId = party.id)
                    }

                }
            }
        })
}


fun gameMasterScreen() {
    //implement the game master screen
}

@Composable
fun playerScreen(partyId: String = "") {
    val scope = rememberCoroutineScope()
    var partycode = rememberTextFieldState(initialText = "")
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    //implement the player screen

    if (partyId.isEmpty()) {
        //join party screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.join_party_title),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            InputGeneral(
                state = partycode,
                label = R.string.party_code_label,
                hint = R.string.party_code_hint
            )



            Button(
                enabled = if(partycode.text.trim().isNotEmpty()) true else false,
                onClick = {
                    //join party action
                    scope.launch {
                        var result = addNewMemberToParty(
                            db = FirebaseFirestore.getInstance(),
                            partyId = partycode.text.trim() as String,
                            newMemberId = UserPreferences(context).getPreferencesString("character_id")
                        )

                        if (result) {
                            //successfully joined party
                            showDialog = false
                        } else {
                            //failed to join party
                            showDialog = true
                        }
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
            ) {
                Text(text = stringResource(R.string.join_party_button), modifier = Modifier.height(20.dp).fillMaxWidth(), textAlign = TextAlign.Center)
            }

            if (showDialog) {
                AlertDialogRegister(
                    alertTitle = stringResource(R.string.join_party_failed_title),
                    alertMessage = stringResource(R.string.join_party_failed_message),
                    onDismiss = { showDialog = false } //reset
                )
            }

        }
    }else{
        var characters: List<Character> = remember { mutableListOf<Character>() }
        var scope = rememberCoroutineScope()
        var party: Party = remember { Party() }
        var selectedCharacterId by remember { mutableStateOf("") }
        scope.launch {
            party = loadPartyInfo(db = FirebaseFirestore.getInstance(), partyId = partyId)!!
            //load characters
            characters = party.members.mapNotNull { memberId ->
                val character = getCharacterById(
                    db = FirebaseFirestore.getInstance(),
                    characterId = memberId
                )
                character
            }

            selectedCharacterId = UserPreferences(context).getPreferencesString("character_id") ?: ""
        }
        //party info screen and list of members
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.party_info_title),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "${stringResource(R.string.party_code_label)}: $partyId",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),

                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 16.dp, top = 16.dp)
            ) {
                items(characters) { character ->
                    val isSelected = (character.id == selectedCharacterId)
                    ButtonInfoCharacter(
                        character = character,
                        isSelected = isSelected,
                        onClick = {
                        })
                }
            }
        }
    }
}


