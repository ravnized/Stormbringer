package pdm.uninsubria.stormbringer.ui.activity

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.zIndex
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
import pdm.uninsubria.stormbringer.tools.getCharacterById
import pdm.uninsubria.stormbringer.tools.loadMultiplePartyInfoByGM
import pdm.uninsubria.stormbringer.tools.loadPartyInfo
import pdm.uninsubria.stormbringer.tools.loadPartyInfoByCharacter
import pdm.uninsubria.stormbringer.ui.fragments.PartyManageFragment
import pdm.uninsubria.stormbringer.ui.theme.AlertDialogRegister
import pdm.uninsubria.stormbringer.ui.theme.ButtonInfoCharacter
import pdm.uninsubria.stormbringer.ui.theme.ButtonInfoParty
import pdm.uninsubria.stormbringer.ui.theme.InputGeneral
import pdm.uninsubria.stormbringer.ui.theme.NavigationBarSection
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_background_dark
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_primary
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_surface_dark

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
    LaunchedEffect(Unit) {
        val characterId = userPreferences.getPreferencesString("character_id")
        val savedMode = userPreferences.getPreferencesString("player_mode")
        if (characterId.isNotEmpty() && savedMode != "GM") {
            val loadedParty = loadPartyInfoByCharacter(db = db, characterId = characterId)
            if (loadedParty != null) {
                party = loadedParty
            }


            if (savedMode.isNotEmpty()) {
                mode = savedMode
            }
            showTextSelect = false
        } else if (savedMode == "GM") {
            val userUid = auth.currentUser?.uid ?: ""
            parties = loadMultiplePartyInfoByGM(db = db, userUid = userUid)
            Log.i("PartyActivity", "Loaded parties for GM: ${parties.size}")
            mode = "GM"
            showTextSelect = false
        } else {
            Log.e("PartyActivity", "No character ID or user is not GM")
            showTextSelect = true
        }

        isLoading = false
    }


    NavigationBarSection(headLine = stringResource(R.string.party_title), floatingActionButton = {
        FloatingActionButton(
            onClick = {
                //add new party
                showSheet = true
            },
            containerColor = stormbringer_primary,
            contentColor = stormbringer_background_dark,
            shape = CircleShape
        ) {
            Icon(
                painter = painterResource(R.drawable.add_24px), contentDescription = "add"
            )
        }
    }, currentTab = if (mode == "GM") 0 else 2, gameMode = mode, content = { innerPadding ->
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
                    GameMasterScreen(parties = parties)
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
                        playerScreen(partyId = party.id)
                    }

                }
            }
        }
    })
    CustomBottomSheet(
        isVisible = showSheet, onDismiss = { showSheet = false }) {
        // Contenuto del form
        CreatePartyForm(onCancel = { showSheet = false }, onCreate = { name ->
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
fun GameMasterScreen(parties: List<Party>) {
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val scope = rememberCoroutineScope()
    Log.i("PartyActivity", "Rendering GameMasterScreen with ${parties.size} parties")
    LazyColumn(
        modifier = Modifier.fillMaxSize(),

        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 16.dp, top = 16.dp)
    ) {
        items(parties) { party ->
            ButtonInfoParty(
                party = party, onClick = {
                    //open party details
                    scope.launch {
                        UserPreferences(context).savePreferencesString(
                            key = "current_party_id",
                            value = party.id
                        )


                        activity?.supportFragmentManager?.beginTransaction()
                            ?.setReorderingAllowed(true)?.replace(
                                R.id.fragment_container, PartyManageFragment()
                            )?.commit()
                    }

                })
        }
    }


}

@Composable
fun CustomBottomSheet(
    isVisible: Boolean, onDismiss: () -> Unit, content: @Composable () -> Unit
) {
    if (isVisible) {
        BackHandler(onBack = onDismiss)
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300)),
        modifier = Modifier.zIndex(1f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                ), contentAlignment = Alignment.BottomCenter
        ) {

        }
    }


    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it }, animationSpec = tween(300)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it }, animationSpec = tween(300)
        ),
        modifier = Modifier
            .zIndex(2f)
            .fillMaxSize(),

        ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = stormbringer_surface_dark,
                shadowElevation = 8.dp
            ) {
                content()
            }
        }
    }
}


@Composable
fun CreatePartyForm(onCancel: () -> Unit, onCreate: (String) -> Unit) {
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
                onClick = { if (partyName.isNotBlank()) onCreate(partyName) },
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
                enabled = partycode.text.trim().isNotEmpty(),
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
        var characters: List<Character> = remember { mutableListOf<Character>() }
        var scope = rememberCoroutineScope()
        var party: Party = remember { Party() }
        var selectedCharacterId by remember { mutableStateOf("") }
        scope.launch {
            party = loadPartyInfo(db = FirebaseFirestore.getInstance(), partyId = partyId)!!
            //load characters
            characters = party.members.mapNotNull { memberId ->
                val character = getCharacterById(
                    db = FirebaseFirestore.getInstance(), characterId = memberId
                )
                character
            }

            selectedCharacterId = UserPreferences(context).getPreferencesString("character_id")
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
                        character = character, isSelected = isSelected, onClick = {})
                }
            }
        }
    }
}


