package pdm.uninsubria.stormbringer.tools

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserActionTest {

    private val uniqueEmail = "test${System.currentTimeMillis()}@test.com"
    private val password = "password123"

    fun loginUserTest(userAction: UserAction, appContext: Context) = runBlocking {
        val result = userAction.loginUser(uniqueEmail, password)
        assertTrue("Il login è fallito", result)
        val isLogged = UserPreferences(appContext).getPreferencesBoolean("logged")
        assertTrue("L'utente non è loggato", isLogged)
    }


    fun registerUserTest(userAction: UserAction, appContext: Context) = runBlocking {
        val result = userAction.registerUser(uniqueEmail, password)
        assertTrue("L'registrazione è fallita", result)
        val isLogged = UserPreferences(appContext).getPreferencesBoolean("logged")
        assertTrue("L'utente non è loggato", isLogged)
    }


    fun logoutUserTest(userAction: UserAction, appContext: Context) = runBlocking {
        val result = userAction.logoutUser()
        assertTrue("Il logout è fallito", result)
        val isLogged = UserPreferences(appContext).getPreferencesBoolean("logged")
        assertFalse("L'utente è ancora loggato", isLogged)
    }


    fun removeUserTest(userAction: UserAction, appContext: Context) = runBlocking {
        val result = userAction.removeUser()
        assertTrue("L'eliminazione è fallita", result)
        val isLogged = UserPreferences(appContext).getPreferencesBoolean("logged")
        assertFalse("L'utente è ancora loggato", isLogged)
    }


    @Test
    fun testAllUserActions() = runBlocking {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val userAction = UserAction(appContext)
        registerUserTest(userAction, appContext)
        logoutUserTest(userAction, appContext)
        loginUserTest(userAction, appContext)
        removeUserTest(userAction, appContext)
    }
}