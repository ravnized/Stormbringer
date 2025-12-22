package pdm.uninsubria.stormbringer.tools

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class Party(
    val id: String = "",
    var name: String = "",
    var members: List<String> = emptyList(),
    var description: String = "",
    val gameMaster:String = "",
    val createdAt: Long = System.currentTimeMillis()
){
    companion object {
        const val MAX_MEMBERS = 6
    }


}

suspend fun createParty(db: FirebaseFirestore,userUid: String, partyName: String): String? {
    return try {
        val userRef = db.collection("users").document(userUid)

        val userSnapshot = userRef.get().await()
        val currentPartyId = userSnapshot.getString("partyId")

        if (!currentPartyId.isNullOrEmpty()) {
            Log.e("PartyManager", "L'utente è già in un party ($currentPartyId)")
            return null
        }

        val newPartyRef = db.collection("parties").document()

        val newParty = Party(
            id = newPartyRef.id,
            name = partyName,
            gameMaster = userUid
        )

        val batch = db.batch()

        batch.set(newPartyRef, newParty)

        batch.update(userRef, "partyId", newPartyRef.id)

        batch.commit().await()

        Log.i("PartyManager", "Party creato con ID: ${newPartyRef.id}")
        newPartyRef.id

    } catch (e: Exception) {
        Log.e("PartyManager", "Errore creazione party: ${e.message}")
        null
    }
}

suspend fun addNewMemberToParty(db: FirebaseFirestore, partyId: String, newMemberId: String): Boolean {
    return try {
        val partyRef = db.collection("parties").document(partyId)
        val partySnapshot = partyRef.get().await()

        if (!partySnapshot.exists()) {
            Log.e("PartyManager", "Party con ID $partyId non trovato")
            return false
        }

        val party = partySnapshot.toObject(Party::class.java) ?: return false

        if (party.members.size >= Party.MAX_MEMBERS) {
            Log.e("PartyManager", "Il party ha già il numero massimo di membri")
            return false
        }

        if (party.members.contains(newMemberId)) {
            Log.e("PartyManager", "L'utente è già membro del party")
            return false
        }

        val updatedMembers = party.members.toMutableList().apply { add(newMemberId) }

        partyRef.update("members", updatedMembers).await()

        Log.i("PartyManager", "Nuovo membro aggiunto al party $partyId")
        true

    } catch (e: Exception) {
        Log.e("PartyManager", "Errore aggiunta membro al party: ${e.message}")
        false
    }
}

suspend fun loadPartyInfo(db: FirebaseFirestore, partyId: String): Party? {
    return try {
        val partyRef = db.collection("parties").document(partyId)
        val partySnapshot = partyRef.get().await()

        if (!partySnapshot.exists()) {
            Log.e("PartyManager", "Party con ID $partyId non trovato")
            return null
        }

        val party = partySnapshot.toObject(Party::class.java)

        Log.i("PartyManager", "Party info caricata per ID $partyId")
        party

    } catch (e: Exception) {
        Log.e("PartyManager", "Errore caricamento info party: ${e.message}")
        null
    }
}

suspend fun loadPartyInfoByUser(db: FirebaseFirestore, userUid: String): Party? {
    return try {
        val userRef = db.collection("users").document(userUid)
        val userSnapshot = userRef.get().await()

        val partyId = userSnapshot.getString("partyId")
        if (partyId.isNullOrEmpty()) {
            Log.e("PartyManager", "L'utente non è in nessun party")
            return null
        }

        loadPartyInfo(db, partyId)

    } catch (e: Exception) {
        Log.e("PartyManager", "Errore caricamento info party per utente: ${e.message}")
        null
    }
}

suspend fun loadPartyInfoByCharacter(db: FirebaseFirestore, characterId: String): Party? {
    return try {
        val usersSnapshot = db.collection("users").get().await()

        for (userDoc in usersSnapshot.documents) {
            val charDoc = userDoc.reference.collection("characters")
                .document(characterId).get().await()

            if (charDoc.exists()) {
                val partyId = userDoc.getString("partyId")
                if (!partyId.isNullOrEmpty()) {
                    return loadPartyInfo(db, partyId)
                } else {
                    Log.e("PartyManager", "Il personaggio non è in nessun party")
                    return null
                }
            }
        }

        Log.e("PartyManager", "Personaggio con ID $characterId non trovato")
        null

    } catch (e: Exception) {
        Log.e("PartyManager", "Errore caricamento info party per personaggio: ${e.message}")
        null
    }
}
