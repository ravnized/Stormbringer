package pdm.uninsubria.stormbringer.ui.theme

import android.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.Character
import pdm.uninsubria.stormbringer.tools.Party
import pdm.uninsubria.stormbringer.tools.UserPreferences


@Composable
fun ButtonInfoCharacter(
    onClick: () -> Unit = {},
    character: Character = Character(name = "Gandalf", characterClass = "Wizard"), // Esempio dati
    isSelected: Boolean = false,
    showEdit: Boolean = false,
    onEdit : () -> Unit = {}
) {

    val borderStroke = if (isSelected) {
        BorderStroke(2.dp, glow_active)
    } else {
        BorderStroke(1.dp, white_20)
    }


    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = stormbringer_surface_dark,
        border = borderStroke,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(vertical = 4.dp),
        shadowElevation = if (isSelected) 8.dp else 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) glow_active else white_20,
                        shape = CircleShape
                    )
                    .padding(2.dp)
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(character.image)
                        .crossfade(true)
                        .networkCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = "Avatar of ${character.name}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Spacer(modifier = Modifier.width(16.dp))


            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = character.name,
                    color = if (isSelected) glow_active else white_70,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )


                Text(
                    text = character.characterClass,
                    color = white_70,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp
                )
            }
            if(showEdit){
                // Edit icon on the right and on click edit character
                Button(
                    onClick = {
                        onEdit()
                    },
                    colors = ButtonColors(
                        containerColor = stormbringer_surface_dark,
                        contentColor = white_50,
                        disabledContentColor = stormbringer_background_dark,
                        disabledContainerColor = stormbringer_surface_dark
                    ),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .padding(0.dp)
                        .size(24.dp),
                    enabled = true
                ) {
                    Icon(
                        painter = painterResource(R.drawable.remove_24px),
                        contentDescription = "Remove Character",
                        tint = white_50,
                        modifier = Modifier.size(24.dp)
                    )

                }

            }else if (isSelected) {
                Icon(
                    painter = painterResource(R.drawable.check_circle_24px),
                    contentDescription = "Selected",
                    tint = glow_active,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ButtonInfoParty(
    onClick: () -> Unit = {},
    party: Party = Party(name = "Fellowship"),
    isSelected: Boolean = false,
    editEnable : Boolean = false,
    onClickDelete: () -> Unit = {},
) {

    val borderStroke = if (isSelected) {
        BorderStroke(2.dp, glow_active)
    } else {
        BorderStroke(1.dp, white_20)
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = stormbringer_surface_dark,
        border = borderStroke,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(vertical = 4.dp, horizontal = 8.dp),
        shadowElevation = if (isSelected) 8.dp else 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {


            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = party.name,
                    color = if (isSelected) glow_active else white_70,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )

                Text(
                    text = "${party.members.size} members",
                    color = white_50,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp
                )
            }
            if(editEnable){
                // Delete icon on the right and on click delete party
                Button(
                    onClick = {
                        onClickDelete()
                    },
                    colors = ButtonColors(
                        containerColor = stormbringer_surface_dark,
                        contentColor = white_50,
                        disabledContentColor = stormbringer_background_dark,
                        disabledContainerColor = stormbringer_surface_dark
                    ),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .padding(0.dp)
                        .size(24.dp),
                    enabled = true
                ) {


                    Icon(
                        painter = painterResource(R.drawable.delete_24px),
                        contentDescription = "Delete Party",
                        tint = white_50,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }else if (isSelected) {
                Icon(
                    painter = painterResource(R.drawable.check_circle_24px),
                    contentDescription = "Selected",
                    tint = glow_active,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun ButtonActionPrimary(
    onClick: () -> Unit = {},
    id: Int = R.string.login_button,
    conditionEnable: Boolean = true,
    iconID: Int? = null
) {
    Box(
        modifier = Modifier
            .innerShadow(
                shape = RoundedCornerShape(16.dp), shadow = Shadow(
                    color = glow_subtle,
                    spread = 1.dp,
                    radius = 16.dp,
                )
            )
            .dropShadow(
                shape = RoundedCornerShape(16.dp), shadow = Shadow(
                    color = glow_subtle,
                    spread = 1.dp,
                    radius = 16.dp,
                )
            ), contentAlignment = Alignment.Center, propagateMinConstraints = true, content = {
            Button(
                enabled = conditionEnable, shape = RoundedCornerShape(16.dp), colors = ButtonColors(
                    containerColor = stormbringer_primary,
                    contentColor = stormbringer_background_dark,
                    disabledContainerColor = white_20,
                    disabledContentColor = white_20
                ),

                onClick = {

                    onClick()

                }, modifier = Modifier.padding(), content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        if(iconID != null){
                            Icon(
                                painter = painterResource(id = iconID),
                                contentDescription = "Icon",
                                tint = stormbringer_background_dark,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        Text(
                            text = stringResource(id),
                            style = MaterialTheme.typography.headlineSmall,
                            color = stormbringer_surface_dark,

                            modifier = Modifier.padding(16.dp)
                        )
                    }

                })
        })
}

@Preview
@Composable
fun ButtonSelectorMode(
    onClick: () -> Unit = {},
    text: String = "Adventure Mode",
    description: String = "Embark on an epic journey filled with quests and challenges.",
    icon: Int = R.drawable.sailing_24px,
    isSelected: Boolean = false
) {
    val glowModifier = if (isSelected) {
        Modifier
            .innerShadow(
                shape = RoundedCornerShape(16.dp), shadow = Shadow(
                    color = glow_subtle,
                    spread = 1.dp,
                    radius = 16.dp,
                )
            )
            .dropShadow(
                shape = RoundedCornerShape(16.dp), shadow = Shadow(
                    color = glow_subtle,
                    spread = 1.dp,
                    radius = 16.dp,
                )
            )
    } else {
        Modifier
    }

    val backgroundColor = if (isSelected) {
        stormbringer_primary.copy(alpha = 0.3f)
    } else {
        stormbringer_surface_dark
    }



    Box(

        propagateMinConstraints = true, modifier = Modifier
            .then(glowModifier)
            .background(
                color = stormbringer_surface_dark, shape = RoundedCornerShape(16.dp)
            )
            .height(75.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
        content = {
            Button(
                enabled = true, shape = RoundedCornerShape(16.dp),

                colors = ButtonColors(
                    containerColor = backgroundColor,
                    contentColor = backgroundColor,
                    disabledContainerColor = white_20,
                    disabledContentColor = white_20
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                onClick = {
                    onClick()
                }, modifier = Modifier.fillMaxWidth(), content = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = "Mode Icon",
                            tint = if (isSelected) stormbringer_primary else white_70,
                            modifier = Modifier.size(50.dp)
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text(
                                text = text,
                                style = MaterialTheme.typography.headlineSmall,
                                color = white_70
                            )
                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = white_50,
                                maxLines = 1,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                })
        })
}