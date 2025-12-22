package pdm.uninsubria.stormbringer.ui.theme

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.UserAction
import pdm.uninsubria.stormbringer.tools.UserPreferences
import pdm.uninsubria.stormbringer.ui.activity.ProfileDialog
import pdm.uninsubria.stormbringer.ui.fragments.CharacterEditFragment
import pdm.uninsubria.stormbringer.ui.fragments.CharacterManageFragment
import pdm.uninsubria.stormbringer.ui.fragments.InitialFragment
import pdm.uninsubria.stormbringer.ui.fragments.PartyManagerFragment

@Preview
@Composable
fun SelectorMode() {
    val context = LocalContext.current
    var modeSelected by remember { mutableStateOf("PLAYER") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ButtonSelectorMode(
            onClick = {
                scope.launch {
                    modeSelected = "PLAYER"
                    Log.i("UI", "Modalità selezionata: $modeSelected")
                    UserPreferences(context).savePreferencesString(
                        key = "player_mode", value = modeSelected
                    )
                }
            },
            text = stringResource(R.string.player_mode_title),
            description = stringResource(R.string.player_mode_desc),
            icon = R.drawable.swords_24px,
            isSelected = modeSelected == "PLAYER"
        )
        ButtonSelectorMode(
            onClick = {
                scope.launch {
                    modeSelected = "GM"
                    Log.i("UI", "Modalità selezionata: $modeSelected")
                    UserPreferences(context).savePreferencesString(
                        key = "player_mode", value = modeSelected
                    )
                }
            },
            text = stringResource(R.string.master_mode_title),
            description = stringResource(R.string.master_mode_desc),
            icon = R.drawable.sports_esports_24px,
            isSelected = modeSelected == "GM"
        )
    }
}


@Composable
fun NavigationBarSection(
    headLine: String,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
    currentTab: Int,
    gameMode: String = "PLAYER"
) {
    val context = LocalContext.current
    val activity = context as? androidx.fragment.app.FragmentActivity
    var showProfileDialog by remember { mutableStateOf(false) }
    val auth = remember { FirebaseAuth.getInstance() }
    val scope = rememberCoroutineScope()
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
                        text = headLine,
                        style = MaterialTheme.typography.headlineMedium,
                        color = stormbringer_primary,
                    )

                    IconButton(onClick = {
                        showProfileDialog = true
                    }) {
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
                containerColor = stormbringer_surface_dark
            ) {

                if (gameMode == "PLAYER") {
                    NavigationBarItem(
                        selected = currentTab == 0, onClick = {
                        if (currentTab != 0) {
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.setReorderingAllowed(true)
                                ?.replace(R.id.fragment_container, CharacterManageFragment())
                                ?.addToBackStack(null)?.commit()
                        }
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
                        selected = currentTab == 1,
                        onClick = {
                            if (currentTab != 1) {
                                activity?.supportFragmentManager?.beginTransaction()
                                    ?.setReorderingAllowed(true)
                                    ?.replace(R.id.fragment_container, CharacterEditFragment())
                                    ?.addToBackStack(null)?.commit()
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.brush_24px),
                                contentDescription = "Manage Heroes"
                            )
                        },
                        label = { Text("Manage Heroes") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = stormbringer_primary,
                            indicatorColor = stormbringer_primary.copy(alpha = 0.2f)
                        )
                    )

                    NavigationBarItem(
                        selected = currentTab == 2, onClick = {
                        if (currentTab != 2) {
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.setReorderingAllowed(true)
                                ?.replace(R.id.fragment_container, PartyManagerFragment())
                                ?.addToBackStack(null)?.commit()
                        }
                    }, icon = {
                        Icon(
                            painter = painterResource(R.drawable.sailing_24px),
                            contentDescription = "Party"
                        )
                    }, label = { Text("Party") }, colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = stormbringer_primary,
                        indicatorColor = stormbringer_primary.copy(alpha = 0.2f)
                    )
                    )
                } else if (gameMode == "GM") {
                    NavigationBarItem(
                        selected = currentTab == 0,
                        onClick = {
                            if (currentTab != 0) {
                                activity?.supportFragmentManager?.beginTransaction()
                                    ?.setReorderingAllowed(true)
                                    ?.replace(R.id.fragment_container, PartyManagerFragment())
                                    ?.addToBackStack(null)?.commit()
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.sailing_24px),
                                contentDescription = "Party Manager"
                            )
                        },
                        label = { Text(stringResource(R.string.party_title)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = stormbringer_primary,
                            indicatorColor = stormbringer_primary.copy(alpha = 0.2f)
                        )
                    )
                }


            }
        }, floatingActionButton = {
            floatingActionButton()
        }

    ) { innerPadding ->

        content(innerPadding)


    }


    if (showProfileDialog) {
        ProfileDialog(
            email = auth.currentUser?.email ?: "Ospite",
            onDismiss = { showProfileDialog = false },
            onLogout = {
                showProfileDialog = false

                val userAction = UserAction(context)


                scope.launch {
                    val result = userAction.logoutUser()
                    if (result) {
                        Log.i("ProfileDialog", "Logout successful")

                        activity?.supportFragmentManager?.popBackStack(
                            null, FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.fragment_container, InitialFragment())?.commit()
                    } else {
                        Log.e("ProfileDialog", "Logout failed")
                    }
                }
            })
    }
}

@Composable
fun HeaderLogo(id: Int) {
    Box(

        modifier = Modifier
            .innerShadow(
                shape = RoundedCornerShape(16.dp), shadow = Shadow(
                    color = glow_subtle,
                    spread = 1.dp,
                    radius = 16.dp,
                    //offset = DpOffset(x=0.dp,y=1.5.dp)
                )
            )
            .dropShadow(
                shape = RoundedCornerShape(16.dp), shadow = Shadow(
                    color = glow_subtle,
                    spread = 1.dp,
                    radius = 16.dp,
                    //offset = DpOffset(x=0.dp,1.5.dp)
                )
            ), contentAlignment = Alignment.Center, propagateMinConstraints = true, content = {
            Image(
                painter = painterResource(id = R.mipmap.stormbringer_logo_foreground),
                contentDescription = "Stormbringer Logo",
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        stormbringer_surface_dark
                    )
            )
        })

    Text(
        text = stringResource(id = R.string.app_name),
        style = MaterialTheme.typography.headlineLarge,
        color = white_100,
        modifier = Modifier.padding(16.dp)

    )
    Spacer(modifier = Modifier.padding(8.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        HorizontalDivider(
            modifier = Modifier.weight(1f), thickness = 1.dp, color = white_20
        )


        Text(
            text = stringResource(id = id),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )


        HorizontalDivider(
            modifier = Modifier.weight(1f), thickness = 1.dp, color = white_20
        )
    }
}