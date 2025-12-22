package pdm.uninsubria.stormbringer.ui.activity

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.Character
import pdm.uninsubria.stormbringer.tools.UserAction
import pdm.uninsubria.stormbringer.tools.UserPreferences
import pdm.uninsubria.stormbringer.tools.getAllCharacters
import pdm.uninsubria.stormbringer.ui.fragments.CharacterCreationFragment
import pdm.uninsubria.stormbringer.ui.fragments.CharacterEditFragment
import pdm.uninsubria.stormbringer.ui.fragments.CharacterManageFragment
import pdm.uninsubria.stormbringer.ui.fragments.InitialFragment
import pdm.uninsubria.stormbringer.ui.theme.ButtonInfoCharacter
import pdm.uninsubria.stormbringer.ui.theme.NavigationBarSection
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_background_dark
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_primary
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_surface_dark


@Composable
fun StormbringerCharacterManage() {

    val db = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    val context = LocalContext.current
    val activity = context as? androidx.fragment.app.FragmentActivity
    var showProfileDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    var characters by remember { mutableStateOf<List<Character>>(emptyList()) }
    var selectedCharacterId by remember { mutableStateOf("") }
    val userPrefs = UserPreferences(context)
    LaunchedEffect(key1 = auth.uid) {
        val uid = auth.uid
        if (uid != null) {
            val retrieved = getAllCharacters(db, uid)
            characters = retrieved
            val savedId = userPrefs.getPreferencesString("character_id") ?: ""
            selectedCharacterId = savedId
            Log.i("CharacterManageActivity", "Characters retrieved: $characters")
            Log.i("CharacterManageActivity", "Selected Character ID: $selectedCharacterId")
        }else{
            val json = userPrefs.getPreferencesString("guest_characters_list") ?: "[]"
            Log.i("CharacterManageActivity", "Guest Characters JSON: $json")
            val type = object : TypeToken<List<Character>>() {}.type
            characters = Gson().fromJson(json, type) ?: emptyList()
            Log.i("CharacterManageActivity", "Guest Characters retrieved: $characters")
            val savedId = userPrefs.getPreferencesString("character_id") ?: ""
            selectedCharacterId = savedId
        }
    }

    NavigationBarSection(headLine = stringResource(R.string.character_manage_title), currentTab = 0 ,floatingActionButton = {
        FloatingActionButton(
            onClick = {
                activity?.supportFragmentManager?.beginTransaction()?.setReorderingAllowed(true)
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
    },content =  { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

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
                        isSelected = isSelected, // Passiamo il booleano calcolato
                        onClick = {
                            val newId = if (isSelected) "" else character.id
                            selectedCharacterId = newId
                            scope.launch {
                                val userPrefs = UserPreferences(context)
                                userPrefs.savePreferencesString(key = "character_id", value = newId )
                                Log.i("Selection", "Salvato ID: $newId")
                            }
                        })
                }
            }
            if (characters.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_characters_message),
                    style = MaterialTheme.typography.bodyMedium,
                    color = stormbringer_primary,
                )
            }

        }
    })
}