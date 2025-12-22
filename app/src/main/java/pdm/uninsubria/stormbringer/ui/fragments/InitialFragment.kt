package pdm.uninsubria.stormbringer.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.ui.activity.StormbringerIntial
import pdm.uninsubria.stormbringer.ui.theme.StormbringerTheme

class InitialFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Log.i("Inside InitialFragment", "setContent")
                StormbringerTheme {
                    StormbringerIntial(

                        onLoginClick = {
                            navigateToLogin()
                        }, onRegisterClick = {
                            navigateToRegister()
                        },
                        onGuestClick = {
                            navigateToGuest()
                        }


                    )
                }

            }
        }
    }

    private fun navigateToLogin() {
        parentFragmentManager.beginTransaction().setReorderingAllowed(true)
            .replace(R.id.fragment_container, LoginFragment()).addToBackStack(null).commit()
    }

    private fun navigateToRegister() {


        parentFragmentManager.beginTransaction().setReorderingAllowed(true)
            .replace(R.id.fragment_container, RegisterFragment()).addToBackStack(null).commit()
    }

    private fun navigateToGuest() {
        parentFragmentManager.beginTransaction().setReorderingAllowed(true)
            .replace(R.id.fragment_container, GuestFragment()).addToBackStack(null).commit()
    }
}