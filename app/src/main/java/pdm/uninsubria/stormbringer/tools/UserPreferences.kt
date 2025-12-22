package pdm.uninsubria.stormbringer.tools

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {
    companion object {
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val PASSWORD_KEY = stringPreferencesKey("user_password")
        val USER_LEVEL_KEY = intPreferencesKey("user_level")
        val UNIQUE_ID_KEY = intPreferencesKey("unique_id")

        val LOGGED_KEY = booleanPreferencesKey("logged")

        val ALERT_KEY = booleanPreferencesKey("alert")

        val CHARACTER_ID_KEY = stringPreferencesKey("character_id")

        val PLAYER_MODE_KEY = stringPreferencesKey("player_mode")

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

    val alert: Flow<Boolean> = context.dataStore.data
        .map {  preferences -> preferences[ALERT_KEY] ?: false }

    val character_id: Flow<String> = context.dataStore.data
        .map {  preferences -> preferences[CHARACTER_ID_KEY] ?: "" }

    val player_mode: Flow<String> = context.dataStore.data
        .map {  preferences -> preferences[PLAYER_MODE_KEY] ?: "" }





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

    suspend fun getPreferencesString(key: String): String {
        val dataStoreKey = stringPreferencesKey(key)
        return context.dataStore.data.map { preferences ->
            preferences[dataStoreKey] ?: ""
        }.first()
    }

    suspend fun getPreferencesBoolean(key: String): Boolean{
        val dataStoreKey = booleanPreferencesKey(key)

        return context.dataStore.data.map {
            preferences -> preferences[dataStoreKey] ?: false
        }.first()

    }

    suspend fun getPreferencesInt(key: String): Int {
        val dataStoreKey = intPreferencesKey(key)
        return context.dataStore.data.map { preferences ->
            preferences[dataStoreKey] ?: 0
        }.first()
    }

    suspend fun getPreferencesCharacterId(): String {
        return context.dataStore.data.map { preferences ->
            preferences[CHARACTER_ID_KEY] ?: ""
        }.first()
    }


    suspend fun clearPreferences() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }




}