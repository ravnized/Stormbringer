package pdm.uninsubria.stormbringer.ui.activity

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import pdm.uninsubria.stormbringer.tools.createParty
import pdm.uninsubria.stormbringer.tools.deleteParty
import pdm.uninsubria.stormbringer.tools.getCharacterById
import pdm.uninsubria.stormbringer.tools.loadMultiplePartyInfoByGM
import pdm.uninsubria.stormbringer.tools.loadPartyInfoByCharacter
import pdm.uninsubria.stormbringer.tools.removeMemberFromParty
import pdm.uninsubria.stormbringer.ui.theme.AlertDialogRegister
import pdm.uninsubria.stormbringer.ui.theme.ButtonActionPrimary
import pdm.uninsubria.stormbringer.ui.theme.ButtonInfoCharacter
import pdm.uninsubria.stormbringer.ui.theme.ButtonInfoParty
import pdm.uninsubria.stormbringer.ui.theme.CustomBottomSheet
import pdm.uninsubria.stormbringer.ui.theme.DiceRollDialog
import pdm.uninsubria.stormbringer.ui.theme.InputGeneral
import pdm.uninsubria.stormbringer.ui.theme.NavigationBarSection
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_background_dark
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_primary

@Composable
fun StormbringerPartyActivity() {
    val context = LocalContext.current
    val db = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    val scope = rememberCoroutineScope()
    val userPreferences = remember { UserPreferences(context) }
    var party by remember { mutableStateOf(Party()) }
    var mode by remember { mutableStateOf("") }

    context as? FragmentActivity

    var showTextSelect by remember { mutableStateOf(false) }
    var parties: List<Party> by remember { mutableStateOf(emptyList<Party>()) }
    var showSheet by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var charactersInParty by remember { mutableStateOf<List<Character>>(emptyList()) }
    var selectedPartyId by remember { mutableStateOf("") }
    var showEdit by remember { mutableStateOf(false) }
    var showRoll by remember { mutableStateOf(false) }
    var selectedSidesRoll by remember { mutableIntStateOf(6) }
    var showSides by remember { mutableStateOf(false) }
    var characterId by remember { mutableStateOf("") }
    val userUid = auth.currentUser?.uid ?: ""
    fun loadData() {
        scope.launch {
            characterId = userPreferences.getPreferencesString("character_id")
            val savedMode = userPreferences.getPreferencesString("player_mode")
            if (characterId.isNotEmpty() && savedMode != "GM") {
                val loadedParty = loadPartyInfoByCharacter(db = db, characterId = characterId)
                if (loadedParty != null) {
                    party = loadedParty
                    Log.i("PartyActivity", "Loaded party for character $characterId: ${party.id}")

                    if (savedMode.isNotEmpty()) {
                        mode = savedMode
                    }

                    charactersInParty = party.members.mapNotNull { memberId ->
                        getCharacterById(db = db, characterId = memberId)
                    }

                    showTextSelect = false
                } else {
                    Log.e("PartyActivity", "No party found for character $characterId")
                    party = Party()
                    isLoading = false
                    return@launch
                }

            } else if (savedMode == "GM") {

                parties = loadMultiplePartyInfoByGM(db = db, userUid = userUid)
                Log.i("PartyActivity", "Loaded parties for GM: ${parties.size}")
                mode = "GM"
                selectedPartyId = userPreferences.getPreferencesString("current_party_id")
                showTextSelect = false
            } else {
                Log.e("PartyActivity", "No character ID or user is not GM")
                showTextSelect = true
            }

            isLoading = false
        }

    }

    LaunchedEffect(Unit) {
        loadData()
    }


    NavigationBarSection(headLine = stringResource(R.string.party_title), floatingActionButton = {
        if (mode == "GM") {
            FloatingActionButton(
                onClick = {
                    //edit party
                    showEdit = !showEdit
                },
                containerColor = stormbringer_primary,
                contentColor = stormbringer_background_dark,
                shape = CircleShape
            ) {

                if (showEdit) {
                    Icon(
                        painter = painterResource(R.drawable.check_24px),
                        contentDescription = "confirm"
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.edit_24px), contentDescription = "edit"
                    )
                }


            }
        } else {
            FloatingActionButton(
                onClick = {
                    //edit party
                    showSides = true
                },
                containerColor = stormbringer_primary,
                contentColor = stormbringer_background_dark,
                shape = CircleShape
            ) {

                Icon(
                    painter = painterResource(R.drawable.casino_24px), contentDescription = "roll"
                )


            }
        }
    }, currentTab = if (mode == "GM") 0 else 2, content = { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //is loading per evitate caricamenti strani
            if (isLoading) {
                CircularProgressIndicator(color = stormbringer_primary)
            } else {
                if (mode == "GM") {

                    GameMasterScreen(parties = parties, editEnable = showEdit, loadData = {
                        loadData()
                    })
                    ButtonActionPrimary(
                        onClick = { showSheet = true },
                        id = R.string.add_party_button,
                        conditionEnable = true
                    )
                } else {
                    if (showTextSelect) {
                        Text(
                            text = stringResource(R.string.no_heroes_selected),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        PlayerScreen(
                            partyId = party.id,
                            partyInfo = party,
                            characters = charactersInParty,
                            onAddPartyClick = {
                                isLoading = true
                                loadData()
                            },
                            onRemovePartyClick = {
                                isLoading = true
                                scope.launch {
                                    removeMemberFromParty(
                                        db = db,
                                        partyId = party.id,
                                        memberId = characterId
                                    )
                                    loadData()
                                }
                            })
                    }

                }
            }
        }
    })



    CustomBottomSheet(
        isVisible = showSides, onDismiss = { showSides = false }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .imePadding()
        ) {
            Text(
                text = stringResource(R.string.select_dice_sides),
                style = MaterialTheme.typography.headlineSmall,
                color = stormbringer_primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val sidesOptions = listOf(4, 6, 8, 10, 12, 20, 100)

            sidesOptions.forEach { sides ->
                Button(
                    onClick = {
                        selectedSidesRoll = sides
                        showSides = false
                        showRoll = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = stormbringer_primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text("D$sides", color = Color.Black)
                }
            }
        }
    }


    if (showRoll) {
        DiceRollDialog(
            sides = selectedSidesRoll,
            onDismiss = { showRoll = false }
        )
    }



    CustomBottomSheet(
        isVisible = showSheet, onDismiss = { showSheet = false }) {
        // Contenuto del form
        CreatePartyForm(onCancel = { showSheet = false }, onClick = { name ->
            showSheet = false
            //create new party

            scope.launch {
                val string = createParty(
                    db = db, partyName = name, userUid = auth.currentUser?.uid ?: ""
                )
                if (string != null) {
                    Log.i("PartyActivity", "Nuovo party creato con ID: $string")
                    //reload parties
                    parties = loadMultiplePartyInfoByGM(
                        db = db, userUid = auth.currentUser?.uid ?: ""
                    )
                } else {
                    Log.e("PartyActivity", "Errore creazione party")
                }

            }
        })
    }


}

@Composable
fun GameMasterScreen(parties: List<Party>, editEnable: Boolean = false, loadData: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Log.i("PartyActivity", "Rendering GameMasterScreen with ${parties.size} parties")
    var currentPartyId by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        currentPartyId = UserPreferences(context).getPreferencesString("current_party_id")
    }


    LazyColumn(
        modifier = Modifier,

        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 16.dp, top = 16.dp)
    ) {
        items(parties) { party ->
            val isSelected = (party.id == currentPartyId)
            ButtonInfoParty(
                party = party, onClick = {
                    scope.launch {
                        UserPreferences(context).savePreferencesString(
                            key = "current_party_id",
                            value = if (isSelected) "" else party.id
                        )
                        currentPartyId = if (isSelected) "" else party.id

                    }

                }, isSelected = isSelected, editEnable = editEnable, onClickDelete = {
                    scope.launch {
                        deleteParty(
                            db = FirebaseFirestore.getInstance(),
                            partyId = party.id
                        )
                        loadData()
                    }
                })
        }
    }


}


@Composable
fun CreatePartyForm(onCancel: () -> Unit, onClick: (String) -> Unit) {
    var partyName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .imePadding()
    ) {
        Text(
            text = "Nuovo Party",
            style = MaterialTheme.typography.headlineSmall,
            color = stormbringer_primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = partyName,
            onValueChange = { partyName = it },
            label = { Text("Nome del gruppo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = stormbringer_primary,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = stormbringer_primary,
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) {
                Text("Annulla", color = Color.Gray)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { if (partyName.isNotBlank()) onClick(partyName) },
                colors = ButtonDefaults.buttonColors(containerColor = stormbringer_primary),
                enabled = partyName.isNotBlank()
            ) {
                Text("Crea", color = Color.Black)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun PlayerScreen(
    partyId: String = "",
    partyInfo: Party = Party(),
    characters: List<Character> = emptyList(),
    onAddPartyClick: () -> Unit,
    onRemovePartyClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val partycode = rememberTextFieldState(initialText = "")
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
                enabled = partycode.text.trim().isNotEmpty(),
                onClick = {
                    //join party action
                    scope.launch {
                        val result = addNewMemberToParty(
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
                        onAddPartyClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
            ) {
                Text(
                    text = stringResource(R.string.join_party_button),
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            if (showDialog) {
                AlertDialogRegister(
                    alertTitle = stringResource(R.string.join_party_failed_title),
                    alertMessage = stringResource(R.string.join_party_failed_message),
                    onDismiss = { showDialog = false } //reset
                )
            }

        }
    } else {

        var selectedCharacterId by remember { mutableStateOf("") }



        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Name: " + partyInfo.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "${stringResource(R.string.party_code_label)}: $partyId",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Members: ${partyInfo.members.size}",
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
                        character = character, isSelected = isSelected, onClick = {})
                }
            }

            ButtonActionPrimary(
                onClick = onRemovePartyClick,
                id = R.string.leave_party_button,
                conditionEnable = true,
                iconID = R.drawable.logout_24px
            )
        }
    }
}


