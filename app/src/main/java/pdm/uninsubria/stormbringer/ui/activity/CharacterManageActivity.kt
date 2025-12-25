package pdm.uninsubria.stormbringer.ui.activity

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.Character
import pdm.uninsubria.stormbringer.tools.UserPreferences
import pdm.uninsubria.stormbringer.tools.getAllCharacters
import pdm.uninsubria.stormbringer.ui.fragments.CharacterCreationFragment
import pdm.uninsubria.stormbringer.ui.theme.ButtonInfoCharacter
import pdm.uninsubria.stormbringer.ui.theme.NavigationBarSection
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_background_dark
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_primary


@Composable
fun StormbringerCharacterManage() {

    val db = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    val context = LocalContext.current
    val activity = context as? androidx.fragment.app.FragmentActivity
    val scope = rememberCoroutineScope()
    var characters by remember { mutableStateOf<List<Character>>(emptyList()) }
    var selectedCharacterId by remember { mutableStateOf("") }
    val userPrefs = UserPreferences(context)
    var mode by remember { mutableStateOf("") }
    LaunchedEffect(key1 = auth.uid) {
        val uid = auth.uid
        mode = userPrefs.getPreferencesString("player_mode")
        Log.i("CharacterManageActivity", "Player mode: $mode")
        if (uid != null) {
            val retrieved = getAllCharacters(db, uid)
            characters = retrieved
            val savedId = userPrefs.getPreferencesString("character_id") ?: ""
            selectedCharacterId = savedId
            Log.i("CharacterManageActivity", "Characters retrieved: $characters")
            Log.i("CharacterManageActivity", "Selected Character ID: $selectedCharacterId")
        }
    }

    NavigationBarSection(
        headLine = stringResource(R.string.character_manage_title),
        currentTab = 0,
        floatingActionButton = {

            if (mode != "GM") {
                FloatingActionButton(
                    onClick = {
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.setReorderingAllowed(true)
                            ?.replace(R.id.fragment_container, CharacterCreationFragment())
                            ?.addToBackStack(null)?.commit()
                    },
                    containerColor = stormbringer_primary,
                    contentColor = stormbringer_background_dark,
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_24px), contentDescription = "Add"
                    )
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (characters.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier,

                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(characters) { character ->
                            val isSelected = (character.id == selectedCharacterId)
                            ButtonInfoCharacter(
                                character = character, isSelected = isSelected, onClick = {
                                    val newId = if (isSelected) "" else character.id
                                    selectedCharacterId = newId
                                    scope.launch {
                                        val userPrefs = UserPreferences(context)
                                        userPrefs.savePreferencesString(
                                            key = "character_id", value = newId
                                        )
                                        Log.i("Selection", "Salvato ID: $newId")
                                    }
                                })
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(
                                16.dp
                            )
                    ) {
                        Text(
                            text = stringResource(R.string.no_characters_message),
                            style = MaterialTheme.typography.bodyMedium,
                            color = stormbringer_primary,
                        )
                    }

                }

            }
        })
}