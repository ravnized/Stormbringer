package pdm.uninsubria.stormbringer.tools

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {
    companion object {
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val PASSWORD_KEY = stringPreferencesKey("user_password")
        val USER_LEVEL_KEY = intPreferencesKey("user_level")
        val UNIQUE_ID_KEY = intPreferencesKey("unique_id")

        val LOGGED_KEY = booleanPreferencesKey("logged")
    }


    val userName: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[USER_NAME_KEY] ?: "" }

    val password: Flow<String> = context.dataStore.data
        .map {  preferences -> preferences[PASSWORD_KEY] ?: "" }

    val user_level: Flow<Int> = context.dataStore.data
        .map {  preferences -> preferences[USER_LEVEL_KEY] ?: 0 }

    val unique_id: Flow<Int> = context.dataStore.data
        .map {  preferences -> preferences[UNIQUE_ID_KEY] ?: 0 }

    val logged: Flow<Boolean> = context.dataStore.data
        .map {  preferences -> preferences[LOGGED_KEY] ?: false }

    suspend fun savePreferencesBoolean(value: Boolean, key: String){
        val dataStoreKey = booleanPreferencesKey(key)
        context.dataStore.edit {
            preferences -> preferences[dataStoreKey] = value
        }
    }


    suspend fun savePreferencesString(value: String, key: String){
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit {
            preferences -> preferences[dataStoreKey] = value
        }
    }

    suspend fun savePreferencesInt(value: Int, key: String) {
        val dataStoreKey = intPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }




}