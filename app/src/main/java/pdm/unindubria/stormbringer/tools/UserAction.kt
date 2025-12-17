package pdm.unindubria.stormbringer.tools

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class UserAction {
    private val auth = Firebase.auth
    companion object {
        private var instance: UserAction? = null
    }
    val user = auth.currentUser
    val email = user?.email
    val uid = user?.uid


    fun registerUser(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Utente registrato correttamente
                    val user = auth.currentUser
                    println("Successo: ${user?.email}")
                } else {
                    // Gestione errore (es. email già esistente)
                    println("Errore: ${task.exception?.message}")
                }
            }
    }

    fun loginUser(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login effettuato correttamente
                    val user = auth.currentUser
                    println("Successo: ${user?.email}")
                } else {
                    // Gestione errore (es. email non registrata)
                    println("Errore: ${task.exception?.message}")
                }
            }
    }

}