package pdm.uninsubria.stormbringer.ui.activity

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.Character
import pdm.uninsubria.stormbringer.tools.UserPreferences
import pdm.uninsubria.stormbringer.tools.changeCharInfo
import pdm.uninsubria.stormbringer.tools.getCharacterById
import pdm.uninsubria.stormbringer.ui.theme.ControlButton
import pdm.uninsubria.stormbringer.ui.theme.ExperienceBar
import pdm.uninsubria.stormbringer.ui.theme.NavigationBarSection
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_background_dark
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_primary
import pdm.uninsubria.stormbringer.ui.theme.white_70

@Composable
fun StormbringerCharacterEditActivity() {
    val context = LocalContext.current
    context as? androidx.fragment.app.FragmentActivity
    var character by remember { mutableStateOf<Character?>(null) }
    val scope = rememberCoroutineScope()
    val db = remember { FirebaseFirestore.getInstance() }
    val userPreferences = UserPreferences(context)
    LaunchedEffect(Unit) {

        val characterId = userPreferences.getPreferencesString("character_id")
        Log.i("CharacterEditActivity", "Character ID: $characterId")
        val userUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val userPrefs = UserPreferences(context)
        var characters: List<Character> = emptyList()
        if (characterId.isNotEmpty() && userUid.isNotEmpty()) {
            character = getCharacterById(db = db, characterId = characterId, userUid = userUid)
        }
        if (userUid.isEmpty() && characterId.isNotEmpty()) {
            try {
                val json = userPrefs.getPreferencesString("guest_characters_list") ?: "[]"
                Log.i("CharacterManageActivity", "Guest Characters JSON: $json")
                val type = object : TypeToken<List<Character>>() {}.type
                characters = Gson().fromJson(json, type) ?: emptyList()
                Log.i("CharacterEditActivity", "Guest Characters retrieved: $characters")
                character = characters.find { character ->
                    character.id == characterId
                }
                if (character != null) {
                    Log.e("DEBUG_FIND", "SUCCESSO! Trovato: ${character?.name}")
                } else {
                    Log.e("DEBUG_FIND", "FALLITO. Nessuna corrispondenza trovata.")
                    character = null
                }
            } catch (e: Exception) {
                Log.e("DEBUG_FIND", "ERRORE: ${e.message}")
            }
        }

        if (characterId.isEmpty()) {
            Log.i("CharacterEditActivity", "No character ID found in preferences.")
            character = null
        }


    }
    var visibilityMod by remember { mutableStateOf(false) }

    NavigationBarSection(
        headLine = stringResource(R.string.charcater_edit_title),
        currentTab = 1,
        floatingActionButton = {
            if (!visibilityMod) {

                FloatingActionButton(
                    onClick = {
                        visibilityMod = true
                    },
                    containerColor = stormbringer_primary,
                    contentColor = stormbringer_background_dark,
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(R.drawable.edit_24px), contentDescription = "Edit"
                    )
                }
            } else {
                FloatingActionButton(
                    onClick = {
                        visibilityMod = false
                    },
                    containerColor = stormbringer_primary,
                    contentColor = stormbringer_background_dark,
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(R.drawable.check_24px),
                        contentDescription = "Save"
                    )
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                val currentChar = character
                if (currentChar == null) {
                    Text(stringResource(R.string.no_heroes_selected), color = white_70)
                } else {

                    Text(
                        text = currentChar.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = stormbringer_primary,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "class: ${currentChar.characterClass}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = white_70,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    ExperienceBar(currentChar.xp, currentChar.level)

                    Row(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        ControlButton(
                            currentValue = currentChar.hp, maxValue = 100, onChange = { value ->
                                scope.launch {
                                    character = currentChar.copy(hp = value)
                                    changeCharInfo(
                                        db = db,
                                        userUid = FirebaseAuth.getInstance().currentUser!!.uid,
                                        characterId = currentChar.id,
                                        field = "hp",
                                        value = value
                                    )

                                }
                            }, visibility = visibilityMod
                        )
                        ControlButton(
                            currentValue = currentChar.mp, maxValue = 100, onChange = { value ->
                                scope.launch {
                                    character = currentChar.copy(mp = value)
                                    changeCharInfo(
                                        db = db,
                                        userUid = FirebaseAuth.getInstance().currentUser!!.uid,
                                        characterId = currentChar.id,
                                        field = "mp",
                                        value = value
                                    )

                                }
                            }, visibility = visibilityMod
                        )
                    }
                }
            }
        })


}

