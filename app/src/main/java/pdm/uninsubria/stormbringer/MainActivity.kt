package pdm.uninsubria.stormbringer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.tools.UserAction
import pdm.uninsubria.stormbringer.tools.UserPreferences
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
                    userLogged = UserPreferences(context).getPreferencesBoolean("logged")
                }

                if (userLogged){
                    LoginActivity()
                }else{
                    StormbringerRegister()
                }
            }





        }
    }
}