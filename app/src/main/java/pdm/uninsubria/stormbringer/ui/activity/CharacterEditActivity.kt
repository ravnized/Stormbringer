package pdm.uninsubria.stormbringer.ui.activity

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.Character
import pdm.uninsubria.stormbringer.tools.UserPreferences
import pdm.uninsubria.stormbringer.tools.changeCharInfo
import pdm.uninsubria.stormbringer.tools.generateCharacterImage
import pdm.uninsubria.stormbringer.tools.getCharacterById
import pdm.uninsubria.stormbringer.tools.uploadCharacterImage
import pdm.uninsubria.stormbringer.ui.theme.BiographyText
import pdm.uninsubria.stormbringer.ui.theme.ControlButton
import pdm.uninsubria.stormbringer.ui.theme.CustomProfileImageCircle
import pdm.uninsubria.stormbringer.ui.theme.ExperienceBar
import pdm.uninsubria.stormbringer.ui.theme.ImageSourceOptionDialog
import pdm.uninsubria.stormbringer.ui.theme.NavigationBarSection
import pdm.uninsubria.stormbringer.ui.theme.StatControl
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_background_dark
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_primary
import pdm.uninsubria.stormbringer.ui.theme.stormbringer_surface_dark
import pdm.uninsubria.stormbringer.ui.theme.white_20
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
    val userUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var isLoading by remember { mutableStateOf(true) }

    fun loadAllData() {
        scope.launch {
            isLoading = true

            val characterId = userPreferences.getPreferencesString("character_id")

            try {
                if (characterId.isNotEmpty() && userUid.isNotEmpty()) {
                    val fetchedChar =
                        getCharacterById(db = db, characterId = characterId, userUid = userUid)
                    character = fetchedChar

                }
            } catch (e: Exception) {
                Log.e("LoadData", "Errore caricamento dati: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateField(field: String, value: Any, newCharState: Character) {
        character = newCharState
        scope.launch {
            changeCharInfo(
                db = db,
                userUid = userUid,
                characterId = character?.id ?: "",
                field = field,
                value = value
            )
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
            if (!isLoading && character != null) {
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
                            painter = painterResource(R.drawable.edit_24px),
                            contentDescription = "Edit"
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
            }


        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = stormbringer_primary)
                    }
                } else {
                    val currentChar = character
                    if (currentChar == null) {
                        Text(
                            text = stringResource(R.string.no_heroes_selected),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    } else {


                        val galleryLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.GetContent()
                        ) { uri: Uri? ->
                            uri?.let {
                                scope.launch {
                                    isLoading = true
                                    val success = uploadCharacterImage(
                                        storage = FirebaseStorage.getInstance(),
                                        db = db,
                                        userUid = userUid,
                                        characterId = currentChar.id,
                                        imageUri = uri
                                    )
                                    if (success) {
                                        loadAllData()
                                    }
                                    isLoading = false
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
                                        if (currentChar.bio.isEmpty()) {
                                            //fail the generation if no bio is present
                                            Toast.makeText(
                                                context,
                                                "Please add a biography before generating an image.",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            isLoading = false
                                            return@launch
                                        }
                                        generateCharacterImage(db, userUid, currentChar)
                                        loadAllData()

                                    }
                                })
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(bottom = 80.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(modifier = Modifier.padding(top = 24.dp)) {
                                CustomProfileImageCircle(
                                    data = currentChar.image,
                                    borderColor = stormbringer_primary,
                                    onShow = { if (visibilityMod) showSourceDialog = true },
                                    isEditable = visibilityMod,
                                )
                            }

                            Text(
                                text = currentChar.name,
                                style = MaterialTheme.typography.headlineMedium,
                                color = stormbringer_primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 16.dp)
                            )

                            //show if is dead
                            if (currentChar.isDead()) {
                                Text(
                                    text = "DEAD",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }


                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Lvl. ${currentChar.getLevel()}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = white_70,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = " | ${currentChar.characterClass}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = white_70
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))


                            ExperienceBar(currentXp = {
                                currentChar.xp
                            }, level = {
                                currentChar.getLevel()
                            })


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 32.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {


                                Button(
                                    onClick = {
                                        val newXp = (currentChar.xp - 100).coerceAtLeast(0)
                                        updateField(
                                            "xp", newXp, character!!.copy(xp = newXp)
                                        )
                                    },
                                    enabled = visibilityMod,
                                    shape = CircleShape,
                                ) {
                                    Text(
                                        text = "-100 xp",
                                        color = if (visibilityMod) stormbringer_surface_dark else stormbringer_surface_dark
                                    )
                                }

                                Button(
                                    onClick = {
                                        val newXp = (currentChar.xp - 10).coerceAtLeast(0)
                                        updateField(
                                            "xp", newXp, character!!.copy(xp = newXp)
                                        )
                                    },
                                    enabled = visibilityMod,
                                    shape = CircleShape,
                                ) {
                                    Text(
                                        text = "-10 xp",
                                        color = if (visibilityMod) stormbringer_surface_dark else stormbringer_surface_dark
                                    )
                                }


                            }


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 32.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = {
                                        val newXp = currentChar.xp + 100
                                        updateField(
                                            "xp", newXp, character!!.copy(xp = newXp)
                                        )
                                    },
                                    enabled = visibilityMod,
                                    shape = CircleShape,
                                ) {
                                    Text(
                                        text = "+100 xp",
                                        color = if (visibilityMod) stormbringer_surface_dark else stormbringer_surface_dark
                                    )
                                }

                                Button(
                                    onClick = {
                                        val newXp = currentChar.xp + 10
                                        updateField(
                                            "xp", newXp, character!!.copy(xp = newXp)
                                        )
                                    },
                                    enabled = visibilityMod,
                                    shape = CircleShape,
                                ) {
                                    Text(
                                        text = "+10 xp",
                                        color = if (visibilityMod) stormbringer_surface_dark else stormbringer_surface_dark
                                    )
                                }


                            }



                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Attributes",
                                style = MaterialTheme.typography.titleMedium,
                                color = white_70,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(start = 16.dp, bottom = 8.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ControlButton(
                                    text = "HP",
                                    currentValue = currentChar.hp,
                                    maxValue = 100,
                                    visibility = visibilityMod,
                                    onChange = {
                                        updateField(
                                            "hp",
                                            it,
                                            newCharState = character!!.copy(hp = it)
                                        )
                                    })
                                ControlButton(
                                    text = "MP",
                                    currentValue = currentChar.mp,
                                    maxValue = 100,
                                    visibility = visibilityMod,
                                    onChange = {
                                        updateField(
                                            "mp",
                                            it,
                                            newCharState = character!!.copy(mp = it)
                                        )
                                    })
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    StatControl(
                                        "STR",
                                        currentChar.strength,
                                        visibilityMod
                                    ) {
                                        updateField(
                                            "strength",
                                            it,
                                            character!!.copy(strength = it)
                                        )
                                    }
                                    StatControl(
                                        "DEX",
                                        currentChar.dexterity,
                                        visibilityMod
                                    ) {
                                        updateField(
                                            "dexterity",
                                            it,
                                            character!!.copy(dexterity = it)
                                        )
                                    }
                                    StatControl(
                                        "INT",
                                        currentChar.intelligence,
                                        visibilityMod
                                    ) {
                                        updateField(
                                            "intelligence",
                                            it,
                                            character!!.copy(intelligence = it)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    StatControl(
                                        "WIS",
                                        currentChar.wisdom,
                                        visibilityMod
                                    ) { updateField("wisdom", it, character!!.copy(wisdom = it)) }
                                    StatControl(
                                        "CHA",
                                        currentChar.charisma,
                                        visibilityMod
                                    ) {
                                        updateField(
                                            "charisma",
                                            it,
                                            character!!.copy(charisma = it)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                                BiographyText(
                                    bio = currentChar.bio,
                                    isEditable = visibilityMod,
                                    onBioChange = {
                                        updateField(
                                            "bio",
                                            it,
                                            character!!.copy(bio = it)
                                        )
                                    })
                            }
                        }


                    }


                }
            }
        })


}

