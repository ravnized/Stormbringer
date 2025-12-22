package pdm.uninsubria.stormbringer.tools

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class UserAction(private val context: Context) {
    private val auth = Firebase.auth

    val user get() = auth.currentUser
    val email get() = user?.email
    val uid get() = user?.uid


    suspend fun registerUser(email: String, pass: String): Boolean {

        return try {
            auth.createUserWithEmailAndPassword(email, pass).await()
            UserPreferences(context).savePreferencesBoolean(true, "logged")
            Log.i("registerUser", "User registered successfully")
            true
        } catch (e: Exception) {
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

    suspend fun logoutUser(): Boolean {
        return try {
            auth.signOut()
            UserPreferences(context).clearPreferences()
            Log.i("logoutUser", "Logout successful")
            true
        } catch (e: Exception) {
            Log.e("logoutUser", e.message.toString())
            false
        }
    }

    suspend fun removeUser(): Boolean {
        return try {
            user?.delete()?.await()
            UserPreferences(context).savePreferencesBoolean(false, "logged")
            Log.i("removeUser", "User removed successfully")
            true
        } catch (e: Exception) {
            Log.e("removeUser", e.message.toString())
            false
        }
    }

    suspend fun loginAsGuest(): Boolean {
        return try {
            auth.signInAnonymously().await()
            UserPreferences(context).savePreferencesBoolean(true, "logged")
            Log.i("loginAsGuest", "Guest login successful")
            true
        } catch (e: Exception) {
            Log.e("loginAsGuest", e.message.toString())
            false
        }
    }





}


