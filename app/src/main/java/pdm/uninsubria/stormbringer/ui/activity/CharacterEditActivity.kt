package pdm.uninsubria.stormbringer.ui.activity

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.Character
import pdm.uninsubria.stormbringer.tools.UserPreferences
import pdm.uninsubria.stormbringer.tools.changeCharInfo
import pdm.uninsubria.stormbringer.tools.generateCharacterImage
import pdm.uninsubria.stormbringer.tools.getCharacterById
import pdm.uninsubria.stormbringer.tools.getImageFromUrl
import pdm.uninsubria.stormbringer.tools.uploadCharacterImage
import pdm.uninsubria.stormbringer.ui.theme.BiographyText
import pdm.uninsubria.stormbringer.ui.theme.ControlButton
import pdm.uninsubria.stormbringer.ui.theme.CustomProfileImageCircle
import pdm.uninsubria.stormbringer.ui.theme.ExperienceBar
import pdm.uninsubria.stormbringer.ui.theme.ImageSourceOptionDialog
import pdm.uninsubria.stormbringer.ui.theme.NavigationBarSection
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_background_dark
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_primary
import pdm.uninsubria.stormbringer.ui.theme.white_70

@Composable
fun StormbringerCharacterEditActivity() {
    val context = LocalContext.current
    context as? androidx.fragment.app.FragmentActivity
    var character by remember { mutableStateOf<Character?>(null) }
    val scope = rememberCoroutineScope()
    val db = remember { FirebaseFirestore.getInstance() }
    val userPreferences = UserPreferences(context)
    var showSourceDialog by remember { mutableStateOf(false) }
    var showAiInputForm by remember { mutableStateOf(false) }
    val userUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var doneUploading by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    fun loadAllData() {
        scope.launch {
            isLoading = true // 1. Attiva il loader

            val characterId = userPreferences.getPreferencesString("character_id")

            try {
                if (characterId.isNotEmpty() && userUid.isNotEmpty()) {
                    val fetchedChar = getCharacterById(db = db, characterId = characterId, userUid = userUid)
                    character = fetchedChar

                }
            } catch (e: Exception) {
                Log.e("LoadData", "Errore caricamento dati: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
    LaunchedEffect(Unit) {
        loadAllData()

    }
    var visibilityMod by remember { mutableStateOf(false) }

    NavigationBarSection(
        headLine = stringResource(R.string.charcater_edit_title),
        currentTab = 1,
        floatingActionButton = {
            if (!visibilityMod) {

                FloatingActionButton(
                    onClick = {
                        visibilityMod = true
                    },
                    containerColor = stormbringer_primary,
                    contentColor = stormbringer_background_dark,
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(R.drawable.edit_24px), contentDescription = "Edit"
                    )
                }
            } else {
                FloatingActionButton(
                    onClick = {
                        visibilityMod = false
                    },
                    containerColor = stormbringer_primary,
                    contentColor = stormbringer_background_dark,
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(R.drawable.check_24px),
                        contentDescription = "Save"
                    )
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                val currentChar = character
                if (currentChar == null) {
                    Text(stringResource(R.string.no_heroes_selected), color = white_70)
                } else {
                    if(isLoading){
                        CircularProgressIndicator(color = stormbringer_primary)
                    }else{
                        CustomProfileImageCircle(
                            data = currentChar.image,
                            borderColor = stormbringer_primary,
                            onShow = { showSourceDialog = true },
                            isEditable = true,
                        )
                        Text(
                            text = currentChar.name,
                            style = MaterialTheme.typography.headlineSmall,
                            color = stormbringer_primary,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "class: ${currentChar.characterClass}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = white_70,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        ExperienceBar(currentChar.xp, currentChar.level)

                        BiographyText(
                            bio = currentChar.bio,
                            isEditable = visibilityMod,
                            onBioChange = { newBio ->
                                scope.launch {
                                    character = currentChar.copy(bio = newBio)
                                    changeCharInfo(
                                        db = db,
                                        userUid = userUid,
                                        characterId = currentChar.id,
                                        field = "bio",
                                        value = newBio
                                    )

                                }
                            }
                        )




                        Row(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            ControlButton(
                                text = "HP",
                                currentValue = currentChar.hp, maxValue = 100, onChange = { value ->
                                    scope.launch {
                                        character = currentChar.copy(hp = value)
                                        changeCharInfo(
                                            db = db,
                                            userUid = userUid,
                                            characterId = currentChar.id,
                                            field = "hp",
                                            value = value
                                        )

                                    }
                                }, visibility = visibilityMod
                            )
                            ControlButton(
                                text = "MP",
                                currentValue = currentChar.mp, maxValue = 100, onChange = { value ->
                                    scope.launch {
                                        character = currentChar.copy(mp = value)
                                        changeCharInfo(
                                            db = db,
                                            userUid = userUid,
                                            characterId = currentChar.id,
                                            field = "mp",
                                            value = value
                                        )

                                    }
                                }, visibility = visibilityMod
                            )
                        }


                        val galleryLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.GetContent()
                        ) { uri: Uri? ->
                            uri?.let {
                                // Handle the selected image URI

                                scope.launch {
                                    isLoading = true
                                    val success = uploadCharacterImage(
                                        storage = FirebaseStorage.getInstance(), db = db, userUid = userUid,
                                        characterId = currentChar.id, imageUri = uri
                                    )

                                    if (success) {
                                        loadAllData()
                                    } else {
                                        isLoading = false
                                        Toast.makeText(context, "Errore caricamento", Toast.LENGTH_SHORT).show()
                                    }
                                }

                            }
                        }


                        if (showSourceDialog) {
                            ImageSourceOptionDialog(
                                onDismiss = { showSourceDialog = false },
                                onGalleryClick = {
                                    showSourceDialog = false
                                    galleryLauncher.launch("image/*")
                                },
                                onGenerateClick = {
                                    showSourceDialog = false
                                    scope.launch {
                                        isLoading = true
                                        val success = generateCharacterImage(db = db, userUid = userUid, character = currentChar)
                                        if (success) {
                                            loadAllData()
                                        } else {
                                            isLoading = false
                                            Toast.makeText(context, "Errore generazione immagine", Toast.LENGTH_SHORT).show()
                                        }

                                    }
                                }
                            )
                        }
                    }




                }
            }
        })


}

