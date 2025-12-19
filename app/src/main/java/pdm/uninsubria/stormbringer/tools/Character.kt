package pdm.uninsubria.stormbringer.tools

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class Character(
    val id: String = "",
    var name: String = "",
    var characterClass: String = "",
    var level: Int = 1,
    var isAlive: Boolean = true,
    var xp: Int = 0
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

