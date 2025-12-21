package pdm.uninsubria.stormbringer.ui.activity

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.Character
import pdm.uninsubria.stormbringer.tools.UserPreferences
import pdm.uninsubria.stormbringer.tools.changeCharInfo
import pdm.uninsubria.stormbringer.tools.getCharacterById
import pdm.uninsubria.stormbringer.ui.fragments.CharacterEditFragment
import pdm.uninsubria.stormbringer.ui.fragments.CharacterManageFragment
import pdm.uninsubria.stormbringer.ui.theme.ControlButton
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_background_dark
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_primary
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_surface_dark
import pdm.uninsubria.stormbringer.ui.theme.white_30
import pdm.uninsubria.stormbringer.ui.theme.white_70

@Composable
fun StormbringerCharacterEditActivity() {
    val context = LocalContext.current
    val activity = context as? androidx.fragment.app.FragmentActivity
    var selectedItem by remember { mutableIntStateOf(1) }
    var character by remember { mutableStateOf<Character?>(null) }
    val scope = rememberCoroutineScope()
    val db = remember { FirebaseFirestore.getInstance() }
    val userPreferences = UserPreferences(context)

    LaunchedEffect(Unit) {

        val characterId = userPreferences.getPreferencesString("character_id")
        Log.i("CharacterEditActivity", "Character ID: $characterId")
        val userUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        if (characterId.isNotEmpty() && userUid.isNotEmpty()) {
            character = getCharacterById(db = db, characterId = characterId, userUid = userUid)
        } else {

            character = Character()
        }


    }
    var visibilityMod by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = stormbringer_background_dark,

        topBar = {
            Surface(
                color = stormbringer_background_dark,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.charcater_edit_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = stormbringer_primary,
                    )

                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.person_24px),
                            contentDescription = "Profile",
                            tint = stormbringer_primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }, bottomBar = {
            NavigationBar(
                containerColor = stormbringer_surface_dark // Colore sfondo barra
            ) {
                NavigationBarItem(
                    selected = selectedItem == 0, onClick = {
                    selectedItem = 0
                    activity?.supportFragmentManager?.beginTransaction()?.setReorderingAllowed(true)
                        ?.replace(R.id.fragment_container, CharacterManageFragment())
                        ?.addToBackStack(null)?.commit()
                }, icon = {
                    Icon(
                        painter = painterResource(R.drawable.domino_mask_24px),
                        contentDescription = "Heroes"
                    )
                }, label = { Text("Heroes") }, colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = stormbringer_primary,
                    indicatorColor = stormbringer_primary.copy(alpha = 0.2f)
                )
                )


                NavigationBarItem(
                    selected = selectedItem == 1, onClick = {
                    selectedItem = 1
                    activity?.supportFragmentManager?.beginTransaction()?.setReorderingAllowed(true)
                        ?.replace(R.id.fragment_container, CharacterEditFragment())
                        ?.addToBackStack(null)?.commit()
                }, icon = {
                    Icon(
                        painter = painterResource(R.drawable.brush_24px),
                        contentDescription = "Manage Heroes"
                    )
                }, label = { Text("Manage Heroes") }, colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = stormbringer_primary,
                    indicatorColor = stormbringer_primary.copy(alpha = 0.2f)
                )
                )
            }
        }, floatingActionButton = {
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
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (character == null) {

                CircularProgressIndicator(color = stormbringer_primary)
                Text("Caricamento eroe...", color = white_70)
            } else {
                Text(
                    text = character!!.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = stormbringer_primary,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "class: ${character!!.characterClass}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = white_70,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                ExperienceBar(character!!.xp, character!!.level)

                ControlButton(
                    currentValue = character!!.hp,
                    maxValue = 100,
                    onChange = { value ->
                        scope.launch {
                            character = character!!.copy(hp = value)
                            changeCharInfo(
                                db = db,
                                userUid = FirebaseAuth.getInstance().currentUser!!.uid,
                                characterId = character!!.id,
                                field = "hp",
                                value = value
                            )

                        }
                    }
                , visibility = visibilityMod
                )
                ControlButton(
                    currentValue = character!!.mp,
                    maxValue = 100,
                    onChange = { value ->
                        scope.launch {
                            character = character!!.copy(mp = value)
                            changeCharInfo(
                                db = db,
                                userUid = FirebaseAuth.getInstance().currentUser!!.uid,
                                characterId = character!!.id,
                                field = "mp",
                                value = value
                            )

                        }
                    }
                    , visibility = visibilityMod
                )
            }
        }
    }

}
@Preview
@Composable
fun ExperienceBar(currentXp: Int = 100, level: Int = 0) {
    val maxXp = level * 1000f

    val progress = (currentXp / maxXp).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.8F),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "LVL $level",
                color = stormbringer_primary,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "$currentXp / ${maxXp.toInt()} XP",
                color = white_70,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth(0.8F)
                .height(12.dp)
                .width(1.dp)
                .clip(RoundedCornerShape(6.dp)),
            color = stormbringer_primary,
            trackColor = white_30
        )
    }
}