package pdm.uninsubria.stormbringer.ui.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.UserAction
import pdm.uninsubria.stormbringer.ui.fragments.CharacterManageFragment
import pdm.uninsubria.stormbringer.ui.theme.ButtonActionPrimary
import pdm.uninsubria.stormbringer.ui.theme.SelectorMode
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_primary
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_surface_dark

@Composable
fun StormbringerGuestActivity() {
    val context = LocalContext.current
    val activity = context as? androidx.fragment.app.FragmentActivity
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            SelectorMode()
            ButtonActionPrimary(onClick = {
                isLoading = true
                scope.launch {
                    UserAction(context).loginAsGuest()
                    activity?.supportFragmentManager?.popBackStack(
                        null, FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )

                    activity?.supportFragmentManager?.beginTransaction()?.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)?.setReorderingAllowed(true)
                        ?.replace(
                            R.id.fragment_container, CharacterManageFragment()
                        )?.commit()
                    isLoading = false
                }



            }, id = R.string.guest_continue_button)


            if (isLoading) {
                Dialog(onDismissRequest = { /* Disable dismiss on outside touch */ }) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = stormbringer_surface_dark),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(0.3f)
                            .padding(16.dp)
                    )

                    {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = stormbringer_primary)
                        }

                    }
                }

            }
        }
    }


}