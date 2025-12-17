package pdm.uninsubria.stormbringer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.tools.UserAction
import pdm.uninsubria.stormbringer.tools.UserPreferences
import pdm.uninsubria.stormbringer.ui.fragments.InitialFragment
import pdm.uninsubria.stormbringer.ui.theme.StormbringerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StormbringerTheme {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()

                var userLogged = remember { false }

                scope.launch {
                    //userLogged = UserPreferences(context).getPreferencesBoolean("logged")
                }

                if (userLogged){
                    //vai alla home
                }else{
                    Log.i("MainActivity", "InitialFragment")
                    InitialFragment()
                }
            }





        }
    }
}