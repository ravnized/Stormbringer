package pdm.uninsubria.stormbringer.ui.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.Party
import pdm.uninsubria.stormbringer.tools.loadPartyInfoByUser
import pdm.uninsubria.stormbringer.ui.fragments.CharacterEditFragment
import pdm.uninsubria.stormbringer.ui.fragments.CharacterManageFragment
import pdm.uninsubria.stormbringer.ui.fragments.PartyManagerFragment
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_background_dark
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_primary
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_surface_dark

@Composable
fun StormbringerPartyActivity() {
    val db = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    var party = remember { Party() }
    val context = LocalContext.current
    val activity = context as? androidx.fragment.app.FragmentActivity

    var selectedItem by remember { mutableIntStateOf(2) }

    LaunchedEffect(Unit) {
        if(loadPartyInfoByUser(db, auth.currentUser!!.uid) != null) {
            party = loadPartyInfoByUser(db, auth.currentUser!!.uid)!!
        }else{
            party = Party()
        }
    }


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

                NavigationBarItem(
                    selected = selectedItem == 2, onClick = {
                        selectedItem = 2
                        activity?.supportFragmentManager?.beginTransaction()?.setReorderingAllowed(true)
                            ?.replace(R.id.fragment_container, PartyManagerFragment())
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

        }

    ) {

        //get role from the userPreferences


    }
}


fun gameMasterScreen(){
    //implement the game master screen
}

fun playerScreen(){
    //implement the player screen
}


