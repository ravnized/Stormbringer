package pdm.uninsubria.stormbringer.ui.activity

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.Character
import pdm.uninsubria.stormbringer.tools.UserAction
import pdm.uninsubria.stormbringer.tools.createCharacter
import pdm.uninsubria.stormbringer.ui.fragments.CharacterManageFragment
import pdm.uninsubria.stormbringer.ui.theme.ButtonActionPrimary
import pdm.uninsubria.stormbringer.ui.theme.ControlButton
import pdm.uninsubria.stormbringer.ui.theme.InputGeneral
import pdm.uninsubria.stormbringer.ui.theme.glow_active
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_background_dark

@Composable
fun StormbringerCharacterCreation() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .imePadding().verticalScroll(rememberScrollState()), color = stormbringer_background_dark
    ) {
        val db = remember { FirebaseFirestore.getInstance() }
        remember { FirebaseAuth.getInstance() }
        val context = LocalContext.current
        val activity = context as? androidx.fragment.app.FragmentActivity
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val nameState = rememberTextFieldState()
            val classState = rememberTextFieldState()
            var hp by remember { mutableIntStateOf(10) }
            var mp by remember { mutableIntStateOf(10) }


            InputGeneral(
                state = nameState,

                label = R.string.chracter_name_key, hint = R.string.chracter_name_hint
            )
            InputGeneral(
                state = classState,
                label = R.string.chracter_class_key,
                hint = R.string.chracter_class_hint
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text(
                        text = stringResource(R.string.hp_label),
                        color = glow_active,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ControlButton(currentValue = hp, maxValue = 20, onChange = { value ->
                        if (value <= 20) {
                            hp = value
                        }
                    })
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.mp_label),
                        color = glow_active,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ControlButton(currentValue = mp, maxValue = 20, onChange = { value ->
                        if (value <= 20) {
                            mp = value
                        }
                    })
                }


            }

            stringResource(R.string.character_creation_success)
            val scope = rememberCoroutineScope()
            ButtonActionPrimary(
                onClick = {
                    //create character object
                    var character = Character(
                        name = nameState.text as String,
                        characterClass = classState.text as String,
                        hp = hp,
                        mp = mp
                    )
                    val userAction = UserAction(context)
                    val uid = userAction.uid

                    if (uid != null) {
                        // launch coroutine but don't return the Job from the lambda
                        scope.launch {
                            val success = createCharacter(db, uid, character)
                            if (success) {
                                //return to previous activity
                                Log.i("CharacterCreation", "Character created successfully")
                            } else {
                                Log.e("CharacterCreation", "Error creating character")
                            }
                            //goes back to previous activity
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.setReorderingAllowed(true)
                                ?.replace(R.id.fragment_container, CharacterManageFragment())
                                ?.commit()

                        }
                    }

                })


        }
    }
}