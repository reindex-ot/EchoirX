package app.echoirx.presentation.screens.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.echoirx.R
import app.echoirx.domain.model.SearchResult
import app.echoirx.presentation.components.TrackCover
import java.util.Locale

@Composable
fun SearchResultItem(
    result: SearchResult,
    onClick: () -> Unit
) {
    val formatsDisplay = result.formats?.let { formats ->
        formats.mapTo(mutableSetOf()) {
            when (it) {
                "HIRES_LOSSLESS" -> stringResource(R.string.quality_label_hires)
                "LOSSLESS" -> stringResource(R.string.quality_label_cdq)
                "DOLBY_ATMOS" -> stringResource(R.string.label_dolby)
                "HIGH", "LOW" -> stringResource(R.string.label_aac)
                else -> stringResource(R.string.label_unknown)
            }
        }.apply {
            if (formats.any { it == "HIRES_LOSSLESS" || it == "LOSSLESS" }) {
                add(stringResource(R.string.label_aac))
            }
        }.joinToString(" / ").uppercase(Locale.getDefault())
    } ?: stringResource(R.string.label_unknown).uppercase(Locale.getDefault())

    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        overlineContent = {
            Text(
                text = formatsDisplay,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        headlineContent = {
            Text(
                text = result.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        },
        supportingContent = {
            Text(
                text = result.artists.joinToString(", "),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        },
        leadingContent = {
            TrackCover(
                url = result.cover,
                size = 60.dp
            )
        },
        trailingContent = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = result.duration,
                    style = MaterialTheme.typography.bodySmall
                )
                if (result.explicit) {
                    Icon(
                        painter = painterResource(R.drawable.ic_explicit),
                        contentDescription = stringResource(R.string.label_explicit),
                        modifier = Modifier.size(16.dp),
                    )
                }
                result.formats?.let { formats ->
                    if ("DOLBY_ATMOS" in formats) {
                        Icon(
                            painter = painterResource(R.drawable.ic_dolby),
                            contentDescription = stringResource(R.string.label_dolby_atmos),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    )
}