package pdm.uninsubria.stormbringer.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pdm.uninsubria.stormbringer.R
@Preview
@Composable
fun ControlButton(
    currentValue: Int = 10,
    maxValue: Int = 10,
    onChange: (Int) -> Unit = {},
    visibility: Boolean = true
) {

    val valueColor = when {
        currentValue > maxValue * 0.5 -> stormbringer_primary
        currentValue > maxValue * 0.2 -> glow_active
        else -> white_10
    }

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