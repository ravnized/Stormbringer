package pdm.uninsubria.stormbringer.tools

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.tasks.await

data class Character(
    val id: String = "",
    var name: String = "",
    var characterClass: String = "",
    var level: Int = 1,
    var isAlive: Boolean = true,
    var xp: Int = 0,
    var image: String = "",
    var hp: Int = 0,
    var mp: Int = 0,
    var strength: Int = 0,
    var dexterity: Int = 0,
    var intelligence: Int = 0,
    var wisdom: Int = 0,
    var charisma: Int = 0,
    var inventory: List<String> = emptyList(),
    var spells: List<String> = emptyList(),
    var background: String = "",
) {



    fun isDead() = !isAlive

    fun levelUp() {
        level++
    }

    fun levelDown() {
        if (level > 1) level--
    }

    fun increaseXp(amount: Int) {
        xp += amount
    }

    fun decreaseXp(amount: Int) {
        xp = (xp - amount).coerceAtLeast(0)
    }

    fun resetXp() {
        xp = 0
    }

}

suspend fun createCharacter(db: FirebaseFirestore, userUid: String, character: Character): Boolean {
    return try {

        val newCharRef =
            db.collection("users").document(userUid).collection("characters").document()


        val characterWithId = character.copy(id = newCharRef.id)


        newCharRef.set(characterWithId).await()





        Log.d("DB", "ID: ${newCharRef.id}")
        true
    } catch (e: Exception) {
        Log.e("DB", "Errore: ${e.message}")
        false
    }
}

suspend fun changeCharInfo(
    db: FirebaseFirestore, userUid: String, characterId: String, field: String, value: Any
): Boolean {
    return try {
        val charRef =
            db.collection("users").document(userUid).collection("characters").document(characterId)

        charRef.update(field, value).await()
        Log.d("DB", "$field = $value")
        true
    } catch (e: Exception) {
        Log.e("DB", "Errore: ${e.message}")
        false
    }
}

suspend fun deleteCharacter(db: FirebaseFirestore, userUid: String, characterId: String): Boolean {
    return try {
        val charRef =
            db.collection("users").document(userUid).collection("characters").document(characterId)

        charRef.delete().await()
        Log.d("DB", "elim")
        true
    } catch (e: Exception) {
        Log.e("DB", "Errore: ${e.message}")
        false
    }
}

suspend fun getAllCharacters(db: FirebaseFirestore, userUid: String): List<Character> {
    return try {
        val charCollection =
            db.collection("users").document(userUid).collection("characters").get().await()

        val characters = charCollection.documents.mapNotNull { it.toObject(Character::class.java) }

        Log.d("DB", "Fetched ${characters.size} characters")
        characters
    } catch (e: Exception) {
        Log.e("DB", "Errore: ${e.message}")
        emptyList()
    }
}

suspend fun getCharacterById(
    db: FirebaseFirestore, userUid: String, characterId: String
): Character? {
    return try {
        val charDoc =
            db.collection("users").document(userUid).collection("characters")
                .document(characterId).get().await()

        val character = charDoc.toObject(Character::class.java)

        Log.d("DB", "Fetched character: ${character?.name}")
        character
    } catch (e: Exception) {
        Log.e("DB", "Errore: ${e.message}")
        null
    }
}

suspend fun getCharacterById(
    db: FirebaseFirestore, characterId: String
): Character? {
    return try {
        val usersSnapshot = db.collection("users").get().await()

        for (userDoc in usersSnapshot.documents) {
            val charDoc = userDoc.reference.collection("characters")
                .document(characterId).get().await()

            if (charDoc.exists()) {
                val character = charDoc.toObject(Character::class.java)
                Log.d("DB", "Fetched character: ${character?.name}")
                return character
            }
        }

        Log.d("DB", "Character with ID $characterId not found")
        null
    } catch (e: Exception) {
        Log.e("DB", "Errore: ${e.message}")
        null
    }
}

suspend fun saveGuestCharacterToPrefs(context: Context, newCharacter: Character) {
    val userPrefs = UserPreferences(context)
    val gson = Gson()

    val existingJson = userPrefs.getPreferencesString("guest_characters_list") ?: "[]"

    val type = object : TypeToken<MutableList<Character>>() {}.type
    val characterList: MutableList<Character> = try {
        gson.fromJson(existingJson, type) ?: mutableListOf()
    } catch (e: Exception) {
        mutableListOf()
    }

    characterList.add(newCharacter)

    val newJson = gson.toJson(characterList)
    Log.i("CharacterCreation", "Saving guest characters list: $newJson")
    userPrefs.savePreferencesString(key = "guest_characters_list", value = newJson)
}