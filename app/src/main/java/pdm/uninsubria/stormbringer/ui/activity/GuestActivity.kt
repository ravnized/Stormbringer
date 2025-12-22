package pdm.uninsubria.stormbringer.ui.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.ui.fragments.CharacterManageFragment
import pdm.uninsubria.stormbringer.ui.theme.ButtonActionPrimary
import pdm.uninsubria.stormbringer.ui.theme.SelectorMode

@Composable
fun StormbringerGuestActivity() {
    val context = LocalContext.current
    val activity = context as? androidx.fragment.app.FragmentActivity
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

                activity?.supportFragmentManager?.popBackStack(
                    null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )

                activity?.supportFragmentManager?.beginTransaction()
                    ?.setReorderingAllowed(true)?.replace(
                        R.id.fragment_container, CharacterManageFragment()
                    )?.commit()
            }, id = R.string.guest_continue_button)
        }
    }




}