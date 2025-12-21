package pdm.uninsubria.stormbringer.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import pdm.uninsubria.stormbringer.ui.activity.StormbringerCharacterEditActivity
import pdm.uninsubria.stormbringer.ui.activity.StormbringerCharacterManage
import pdm.uninsubria.stormbringer.ui.theme.StormbringerTheme

class CharacterEditFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Log.i("Inside Character Manage Fragment", "setContent")
                StormbringerTheme {
                    StormbringerCharacterEditActivity()
                }

            }
        }
    }
}