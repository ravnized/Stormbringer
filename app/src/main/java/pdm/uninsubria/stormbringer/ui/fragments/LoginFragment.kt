package pdm.uninsubria.stormbringer.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import pdm.uninsubria.stormbringer.ui.activity.StormbringerIntial
import pdm.uninsubria.stormbringer.ui.activity.StormbringerLogin
import pdm.uninsubria.stormbringer.ui.theme.StormbringerTheme

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Log.i("Inside LoginFragment", "setContent")
                StormbringerTheme {
                    StormbringerLogin()
                }

            }
        }
    }
}