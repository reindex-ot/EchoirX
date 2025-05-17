package app.echoirx.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.echoirx.R

@Composable
fun PreviewButton(
    onPreviewClick: () -> Unit,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        onClick = onPreviewClick,
        modifier = modifier,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = if (isPlaying)
                MaterialTheme.colorScheme.tertiaryContainer
            else
                MaterialTheme.colorScheme.primaryContainer,
            contentColor = if (isPlaying)
                MaterialTheme.colorScheme.onTertiaryContainer
            else
                MaterialTheme.colorScheme.onPrimaryContainer
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Icon(
            imageVector = if (isPlaying)
                Icons.Rounded.Stop
            else
                Icons.Rounded.PlayArrow,
            contentDescription = stringResource(
                if (isPlaying) R.string.cd_stop_preview
                else R.string.cd_play_preview
            ),
            modifier = Modifier.size(16.dp)
        )
    }
}