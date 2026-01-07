package pdm.uninsubria.stormbringer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.tools.UserPreferences
import pdm.uninsubria.stormbringer.ui.fragments.CharacterManageFragment
import pdm.uninsubria.stormbringer.ui.fragments.InitialFragment
import pdm.uninsubria.stormbringer.ui.fragments.PartyManagerFragment

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("MainActivity", "Provo a caricare il Fragment")


        if (savedInstanceState == null) {
            lifecycleScope.launch {
                val user = UserPreferences(this@MainActivity)
                val userLogged = user.getPreferencesBoolean("logged")
                Log.i("MainActivity", "Valore di userLogged: $userLogged")
                val transaction =
                    supportFragmentManager.beginTransaction().setReorderingAllowed(true)
                if (userLogged) {
                    //home diversa tra player e gm
                    Log.i("MainActivity", "Utente loggato, carico HomeFragment")
                    if (user.getPreferencesString("player_mode") == "PLAYER") {
                        transaction.add(R.id.fragment_container, CharacterManageFragment())
                    } else {
                        transaction.add(R.id.fragment_container, PartyManagerFragment())
                    }
                } else {
                    Log.i("MainActivity", "Carico InitialFragment")

                    transaction.add(R.id.fragment_container, InitialFragment())
                }
                transaction.commit()
            }
        }
    }
}