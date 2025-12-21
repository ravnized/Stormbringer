package pdm.uninsubria.stormbringer.ui.activity

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.Character
import pdm.uninsubria.stormbringer.tools.getAllCharacters
import pdm.uninsubria.stormbringer.ui.fragments.CharacterCreationFragment
import pdm.uninsubria.stormbringer.ui.theme.ButtonActionPrimary
import pdm.uninsubria.stormbringer.ui.theme.ButtonInfoCharacter
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_background_dark
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_primary

@Composable
fun StormbringerCharacterManage() {

    val db = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    val context = LocalContext.current
    val activity = context as? androidx.fragment.app.FragmentActivity
    Surface(
        modifier = Modifier.fillMaxSize(), color = stormbringer_background_dark
    ) {
        Column(
            modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.character_manage_title),
                style = MaterialTheme.typography.headlineMedium,
                color = stormbringer_primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            var characters by remember { mutableStateOf<List<Character>>(emptyList()) }

            LaunchedEffect(key1 = auth.uid) {
                val uid = auth.uid
                if (uid != null) {
                    val retrieved = getAllCharacters(db, uid)
                    characters = retrieved
                    Log.i("CharacterManageActivity", "Characters retrieved: $characters")
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),

                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 16.dp, top = 16.dp)
            ) {
                items(characters) { character ->
                    ButtonInfoCharacter(
                        character = character,
                        onClick = {

                        }
                    )
                }
            }



            //button create new character
            ButtonActionPrimary(
                id = R.string.character_manage_create_button, onClick = {
                    Log.i("CharacterManageActivity", "Create new character button clicked")
                    activity?.supportFragmentManager?.beginTransaction()?.setReorderingAllowed(true)
                        ?.replace(R.id.fragment_container, CharacterCreationFragment())?.commit()
                })

        }
    }
}