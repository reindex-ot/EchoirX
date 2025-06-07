package app.echoirx.presentation.components.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun PreferenceItem(
    title: String,
    subtitle: String? = null,
    icon: Any,
    onClick: (() -> Unit)? = null,
    iconTint: Color = LocalContentColor.current,
    position: PreferencePosition = PreferencePosition.Single,
    trailingContent: @Composable (() -> Unit)? = null
) {
    val shape = when (position) {
        PreferencePosition.Single -> MaterialTheme.shapes.large
        PreferencePosition.Top -> MaterialTheme.shapes.large.copy(
            bottomStart = MaterialTheme.shapes.small.bottomStart,
            bottomEnd = MaterialTheme.shapes.small.bottomEnd
        )

        PreferencePosition.Bottom -> MaterialTheme.shapes.large.copy(
            topStart = MaterialTheme.shapes.small.topStart,
            topEnd = MaterialTheme.shapes.small.topEnd
        )

        PreferencePosition.Middle -> MaterialTheme.shapes.small
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            ),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = shape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (icon) {
                is ImageVector -> Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )

                is Painter -> Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            trailingContent?.invoke()
        }
    }
}