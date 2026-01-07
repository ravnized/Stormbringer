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

suspend fun deleteParty(db: FirebaseFirestore, partyId: String): Boolean {
    return try {
        db.collection("parties").document(partyId).delete().await()
        Log.i("PartyManager", "Party con ID $partyId eliminato con successo")
        true
    } catch (e: Exception) {
        Log.e("PartyManager", "Errore eliminazione party: ${e.message}")
        false
    }
}


suspend fun createParty(db: FirebaseFirestore,userUid: String, partyName: String): String? {
    return try {
        val newPartyRef = db.collection("parties").document()
        val newParty = Party(
            id = newPartyRef.id,
            name = partyName,
            listOf(),
            gameMaster = userUid
        )
        newPartyRef.set(newParty).await()

        Log.i("PartyManager", "Nuovo party creato con ID: ${newPartyRef.id}")
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
        val partySnapshot = db.collection("parties").document(partyId).get().await()

        if (partySnapshot.exists()) {
            val party = partySnapshot.toObject(Party::class.java)
            Log.i("PartyManager", "Info party caricate per ID $partyId")
            party
        } else {
            Log.e("PartyManager", "Party con ID $partyId non trovato")
            null
        }

    } catch (e: Exception) {
        Log.e("PartyManager", "Errore caricamento info party: ${e.message}")
        null
    }
}

suspend fun loadMultiplePartyInfoByGM(db: FirebaseFirestore, userUid: String): List<Party> {
    return try {
        val partiesSnapshot = db.collection("parties")
            .whereEqualTo("gameMaster", userUid)
            .get().await()

        val parties = partiesSnapshot.documents.mapNotNull { it.toObject(Party::class.java) }

        Log.i("PartyManager", "Caricate ${parties.size} party per GM $userUid")
        parties

    } catch (e: Exception) {
        Log.e("PartyManager", "Errore caricamento party per GM: ${e.message}")
        emptyList()
    }
}

suspend fun loadPartyInfoByCharacter(db: FirebaseFirestore, characterId: String): Party? {
    return try {
        val querySnapshot = db.collection("parties")
            .whereArrayContains("members", characterId)
            .limit(1)
            .get()
            .await()

        if (!querySnapshot.isEmpty) {
            val party = querySnapshot.documents.first().toObject(Party::class.java)
            Log.i("PartyManager", "Trovato party per personaggio $characterId")
            return party
        }

        Log.e("PartyManager", "Personaggio con ID $characterId non trovato")
        null

    } catch (e: Exception) {
        Log.e("PartyManager", "Errore caricamento info party per personaggio: ${e.message}")
        null
    }
}

suspend fun removeMemberFromParty(db: FirebaseFirestore, partyId: String, memberId: String): Boolean {
    return try {
        val partyRef = db.collection("parties").document(partyId)
        val partySnapshot = partyRef.get().await()

        if (!partySnapshot.exists()) {
            Log.e("PartyManager", "Party con ID $partyId non trovato")
            return false
        }

        val party = partySnapshot.toObject(Party::class.java) ?: return false

        if (!party.members.contains(memberId)) {
            Log.e("PartyManager", "L'utente non è membro del party")
            return false
        }

        val updatedMembers = party.members.toMutableList().apply { remove(memberId) }

        partyRef.update("members", updatedMembers).await()

        Log.i("PartyManager", "Membro rimosso dal party $partyId")
        true

    } catch (e: Exception) {
        Log.e("PartyManager", "Errore rimozione membro dal party: ${e.message}")
        false
    }
}
