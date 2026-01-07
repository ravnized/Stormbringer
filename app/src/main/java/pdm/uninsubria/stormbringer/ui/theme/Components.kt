package pdm.uninsubria.stormbringer.ui.theme

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import pdm.uninsubria.stormbringer.R
import pdm.uninsubria.stormbringer.tools.uploadCharacterImage

@Preview
@Composable
fun ControlButton(
    currentValue: Int = 10,
    maxValue: Int = 10,
    onChange: (Int) -> Unit = {},
    visibility: Boolean = true,
    text: String = ""
) {

    val valueColor = when {
        currentValue > maxValue * 0.5 -> stormbringer_primary
        currentValue > maxValue * 0.2 -> glow_active
        else -> white_10
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ){
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = white_70,
            modifier = Modifier.padding(start = 4.dp),
            textAlign = TextAlign.Center
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .size(150.dp)
                .padding(2.dp)
                .aspectRatio(1F)
                .clip(RoundedCornerShape(16.dp))
                .background(stormbringer_surface_dark)
        ) {


            SquareActionButton(
                icon = R.drawable.remove_24px,
                color = glow_active,
                onClick = {
                    if (currentValue > 0) onChange(currentValue - 1)
                },
                visibility = visibility
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .width(50.dp)
            ) {
                Text(
                    text = "$currentValue",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = valueColor
                )
            }

            SquareActionButton(
                icon = R.drawable.add_24px,
                color = stormbringer_primary,
                onClick = {
                    onChange(currentValue + 1)
                },
                visibility = visibility
            )
        }
    }

}

@Composable
fun SquareActionButton(
    icon: Int,
    color: Color,
    onClick: () -> Unit,
    visibility: Boolean = true
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(30.dp)
            .alpha(if(visibility) 1f else 0f)
            .background(stormbringer_surface_dark)
            .clickable { onClick() }
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ExperienceBar(currentXp: Int = 100, level: Int = 0) {
    val maxXp = level * 1000f

    val progress = (currentXp / maxXp).coerceIn(0f, 1f)

    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.8F),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "LVL $level",
                color = stormbringer_primary,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "$currentXp / ${maxXp.toInt()} XP",
                color = white_70,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth(0.8F)
                .height(12.dp)
                .width(1.dp)
                .clip(RoundedCornerShape(6.dp)),
            color = stormbringer_primary,
            trackColor = white_30
        )
    }
}

@Preview
@Composable
fun CustomBottomSheet(
    isVisible: Boolean = true, onDismiss: () -> Unit = {}, content: @Composable () -> Unit = {}
) {
    if (isVisible) {
        BackHandler(onBack = onDismiss)
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300)),
        modifier = Modifier.zIndex(1f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                ), contentAlignment = Alignment.BottomCenter
        ) {

        }
    }


    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it }, animationSpec = tween(300)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it }, animationSpec = tween(300)
        ),
        modifier = Modifier
            .zIndex(2f)
            .fillMaxSize(),

        ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = stormbringer_surface_dark,
                shadowElevation = 8.dp
            ) {
                content()
            }
        }
    }
}


@Composable
fun CustomProfileImageCircle(
    data: Any?,
    borderColor: Color = Color(0xFFD4AF37),
    onShow: () -> Unit = {},
    isEditable: Boolean = true
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(110.dp)

    ) {

        Box(
            modifier = Modifier
                .size(100.dp)

                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    spotColor = Color.Black
                )
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(2.dp, borderColor, CircleShape)
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data)
                    .crossfade(true)
                    .networkCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )


        }
        if(isEditable) {

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-4).dp, y = (-4).dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .size(32.dp)
                    .clickable(
                        onClick = {
                            onShow()
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit_24px),
                    contentDescription = "Change Image",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }


}



@Composable
fun ImageSourceOptionDialog(
    onDismiss: () -> Unit,
    onGenerateClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        title = {
            Text(
                text = stringResource(R.string.select_modify_avatar),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                SourceOptionItem(
                    icon = painterResource(R.drawable.wand_shine_24px),
                    title = stringResource(R.string.generate_avatar_ai),
                    subtitle = stringResource(R.string.generate_avatar_ai_subtitle),
                    onClick = onGenerateClick
                )


                SourceOptionItem(
                    icon = painterResource(id = R.drawable.cloud_upload_24px),
                    title = stringResource(R.string.upload_from_gallery),
                    subtitle = stringResource(R.string.upload_from_gallery_subtitle),
                    onClick = onGalleryClick
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel).uppercase())
            }
        }
    )
}


@Composable
private fun SourceOptionItem(
    icon: Painter,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun BiographyText(
    bio: String,
    onBioChange: (String) -> Unit,
    isEditable: Boolean = true
) {

    Text(stringResource(R.string.bio_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))

    TextField(
        value = bio,
        onValueChange = { onBioChange(it) },
        enabled = isEditable,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        placeholder = { Text(text = stringResource( R.string.bio_placeholder)) },
        maxLines = 6
    )
}

