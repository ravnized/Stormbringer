package pdm.uninsubria.stormbringer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.ui.fragments.InitialFragment

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("MainActivity", "Provo a caricare il Fragment")

        lifecycleScope.launch {

            val userLogged = false

            if (userLogged) {
                // Logica per andare alla Home
            } else {
                Log.i("MainActivity", "Carico InitialFragment")

                // 3. Eseguiamo la transazione SOLO se è il primo avvio
                if (savedInstanceState == null) {
                    supportFragmentManager.beginTransaction().setReorderingAllowed(true)
                        .add(R.id.fragment_container, InitialFragment()).commit()
                }
            }
        }
    }
}