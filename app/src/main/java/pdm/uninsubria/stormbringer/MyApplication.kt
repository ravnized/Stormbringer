package pdm.uninsubria.stormbringer

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.initialize
import pdm.uninsubria.stormbringer.BuildConfig

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Inizializza Firebase manualmente qui
        Firebase.initialize(context = this)

        // Scegli il provider in base al tipo di build
        val appCheckProvider = if (BuildConfig.DEBUG) {
            // In DEBUG usa questo per vedere il token nel Logcat
            DebugAppCheckProviderFactory.getInstance()
        } else {
            // In RELEASE usa Play Integrity (quello per il Play Store)
            PlayIntegrityAppCheckProviderFactory.getInstance()
        }

        Firebase.appCheck.installAppCheckProviderFactory(appCheckProvider)
    }
}