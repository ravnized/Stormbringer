package pdm.uninsubria.stormbringer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.tools.UserPreferences
import pdm.uninsubria.stormbringer.ui.fragments.CharacterManageFragment
import pdm.uninsubria.stormbringer.ui.fragments.InitialFragment

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //get context
        Log.i("MainActivity", "Provo a caricare il Fragment")
        if (savedInstanceState == null) {
            lifecycleScope.launch {


                //get userLogged from
                val user = UserPreferences(this@MainActivity)
                val userLogged = user.getPreferencesBoolean("logged")
                Log.i("MainActivity", "Valore di userLogged: $userLogged")
                val transaction =
                    supportFragmentManager.beginTransaction().setReorderingAllowed(true)
                if (userLogged) {
                    // Logica per andare alla Home
                    Log.i("MainActivity", "Utente loggato, carico HomeFragment")
                    transaction.add(R.id.fragment_container, CharacterManageFragment())
                } else {
                    Log.i("MainActivity", "Carico InitialFragment")

                    // 3. Eseguiamo la transazione SOLO se è il primo avvio

                    transaction.add(R.id.fragment_container, InitialFragment())
                }
                transaction.commit()
            }
        }
    }
}