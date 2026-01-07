package pdm.uninsubria.stormbringer.tools

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.ByteArrayOutputStream

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
    var bio: String = "",
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
            db.collection("users").document(userUid).collection("characters").document(characterId)
                .get().await()

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
    try {
        Log.i("DB", "Searching via CollectionGroup for ID: $characterId")

        val querySnapshot =
            db.collectionGroup("characters").whereEqualTo("id", characterId).limit(1).get().await()

        if (!querySnapshot.isEmpty) {

            val doc = querySnapshot.documents.first()
            val character = doc.toObject(Character::class.java)


            val charWithId = character?.copy(id = doc.id)

            Log.d("DB", "Found character: ${charWithId?.name}")
            return charWithId
        } else {
            Log.d("DB", "Character not found")
            return null
        }

    } catch (e: Exception) {
        Log.e("DB", "Errore ricerca: ${e.message}")

        return null
    }
}

suspend fun uploadCharacterImage(
    storage: FirebaseStorage, db: FirebaseFirestore ,userUid: String, characterId: String, imageUri: Uri
): Boolean {
    return try {
        val fileName = "user_upload_${System.currentTimeMillis()}.jpg"
        val storageRef = storage.reference
            .child("characters")
            .child(userUid)
            .child(characterId)
            .child(fileName)

        storageRef.putFile(imageUri).await()
        val downloadUrl = storageRef.downloadUrl.await()
        downloadUrl.toString()

        if(downloadUrl!=null){

            try {
                val charRef =
                    db.collection("users").document(userUid).collection("characters")
                        .document(characterId)

                charRef.update("image", downloadUrl.toString()).await()
                Log.d("DB", "Image uploaded and URL updated: $downloadUrl")
                true
            }catch (e: Exception) {
                Log.e("DB", "Errore aggiornamento URL immagine: ${e.message}")
                return false
            }

        }else{
            false
        }
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}


suspend fun uploadCharacterImageAI(
    storage: FirebaseStorage,
    db: FirebaseFirestore,
    userUid: String,
    characterId: String,
    bitmap: Bitmap
): Boolean {
    return try {

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
        val data = baos.toByteArray()
        val fileName = "ai_generated_${System.currentTimeMillis()}.jpg"
        val storageRef = storage.reference
            .child("characters")
            .child(userUid)
            .child(characterId)
            .child(fileName)
        storageRef.putBytes(data).await()

        val downloadUrl = storageRef.downloadUrl.await()

        if (downloadUrl != null) {
            try {
                val charRef = db.collection("users")
                    .document(userUid)
                    .collection("characters")
                    .document(characterId)

                charRef.update("image", downloadUrl.toString()).await()
                Log.d("DB", "AI Image uploaded and URL updated: $downloadUrl")
                true
            } catch (e: Exception) {
                Log.e("DB", "Errore aggiornamento DB: ${e.message}")
                false
            }
        } else {
            false
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("DB", "Errore upload Storage: ${e.message}")
        false
    }
}

suspend fun generateCharacterImage(
    db: FirebaseFirestore, userUid: String, character: Character
): Boolean {
    val ai = Firebase.ai(backend = GenerativeBackend.vertexAI())
    val model = ai.imagenModel("imagen-4.0-generate-001")
    val prompt = """
            A high quality, detailed digital art character portrait of a ${character.characterClass}.
            Character description: ${character.bio}.
            Fantasy style, epic lighting, ratio square, centered composition.
        """

    Log.i("DB", "Generating image with prompt: $prompt")
    val imageResponse = model.generateImages(prompt)
    val image = imageResponse.images.first()

    val bitmapImage = image.asBitmap()
    return try {
        val storage = FirebaseStorage.getInstance()
        //save image to storage and update character image url

        val success = uploadCharacterImageAI(storage =  storage, db = db, userUid = userUid, characterId = character.id, bitmap = bitmapImage)

        if (success) {
            Log.i("DB", "Prompt immagine aggiornato con successo")
        } else {
            Log.e("DB", "Errore nell'upload dell'immagine generata")
            throw error("Upload failed")
        }
        true
    } catch (e: Exception) {
        Log.e("DB", "Errore aggiornamento prompt immagine: ${e.message}")
        false
    }
}

suspend fun getImageFromUrl(imageUrl: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(imageUrl)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e("ImageFetch", "Errore server: ${response.code}")
                    return@withContext null
                }

                val inputStream = response.body?.byteStream()
                if (inputStream != null) {
                    BitmapFactory.decodeStream(inputStream)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ImageFetch", "Eccezione: ${e.message ?: "Errore sconosciuto (probabilmente Thread o SSL)"}")
            null
        }
    }
}

