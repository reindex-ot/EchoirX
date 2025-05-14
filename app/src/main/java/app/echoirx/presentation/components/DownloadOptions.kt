package app.echoirx.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonDefaults.LeadingButton
import androidx.compose.material3.SplitButtonDefaults.TrailingButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.echoirx.R
import app.echoirx.domain.model.QualityConfig

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DownloadOptions(
    formats: List<String>?,
    modes: List<String>?,
    onOptionSelected: (QualityConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    val downloadOptions = buildList {
        if (!formats.isNullOrEmpty() && !modes.isNullOrEmpty()) {
            val hasDolbyAtmos = "DOLBY_ATMOS" in modes && "DOLBY_ATMOS" in formats
            val hasStereo = "STEREO" in modes

            if (hasDolbyAtmos) {
                add(QualityConfig.DolbyAtmosAC3)
                add(QualityConfig.DolbyAtmosAC4)
            }

            if (hasStereo && "HIRES_LOSSLESS" in formats) {
                add(QualityConfig.HiRes)
            }

            if (hasStereo) {
                if ("LOSSLESS" in formats) {
                    add(QualityConfig.Lossless)
                }
                add(QualityConfig.AAC320)
                add(QualityConfig.AAC96)
            }
        }
    }

    var expanded by remember { mutableStateOf(false) }
    var selectedQuality by remember {
        mutableStateOf(
            downloadOptions.firstOrNull() ?: QualityConfig.AAC320
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            LeadingButton(
                onClick = { onOptionSelected(selectedQuality) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.FileDownload,
                    modifier = Modifier.size(SplitButtonDefaults.LeadingIconSize),
                    contentDescription = null,
                )

                Spacer(
                    modifier = Modifier.size(size = ButtonDefaults.IconSpacing)
                )

                Text(
                    text = stringResource(
                        R.string.action_download_with_quality,
                        stringResource(selectedQuality.label)
                    ),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(
                modifier = Modifier.width(3.dp)
            )

            Box {
                TrailingButton(
                    checked = expanded,
                    onCheckedChange = { expanded = it }
                ) {
                    val rotation: Float by animateFloatAsState(
                        targetValue = if (expanded) 180f else 0f,
                        label = "Trailing Icon Rotation"
                    )
                    Icon(
                        Icons.Filled.KeyboardArrowDown,
                        modifier = Modifier
                            .size(SplitButtonDefaults.TrailingIconSize)
                            .graphicsLayer { rotationZ = rotation },
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    shape = MaterialTheme.shapes.medium
                ) {
                    downloadOptions.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(option.label),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                selectedQuality = option
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Text(
                text = stringResource(selectedQuality.summary),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
        }
    }
}