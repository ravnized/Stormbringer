package pdm.uninsubria.stormbringer.tools

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import android.content.Context

class UserAction(private val context: Context) {
    private val auth = Firebase.auth

    val user get() = auth.currentUser
    val email get()= user?.email
    val uid get()= user?.uid


    suspend fun registerUser(email: String, pass: String): Boolean {

        return try{
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            UserPreferences(context).savePreferencesBoolean(true, "logged")
            Log.i("registerUser", "User registered successfully")
            true
        }catch (e: Exception) {
            Log.e("registerUser", e.message.toString())
            false
        }

    }

    suspend fun loginUser(email: String, pass: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            UserPreferences(context).savePreferencesBoolean(true, "logged")
            Log.i("loginUser", "Login successful")
            true
        } catch (e: Exception) {
            Log.e("loginUser", e.message.toString())
            false
        }
    }

}


