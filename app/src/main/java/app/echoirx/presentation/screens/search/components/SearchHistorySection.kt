package app.echoirx.presentation.screens.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.echoirx.R
import app.echoirx.domain.model.SearchHistoryItem
import app.echoirx.presentation.components.EmptyStateMessage

@Composable
fun SearchHistorySection(
    searchHistory: List<SearchHistoryItem>,
    onHistoryItemClick: (SearchHistoryItem) -> Unit,
    onDeleteHistoryItem: (SearchHistoryItem) -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (searchHistory.isEmpty()) {
            EmptyStateMessage(
                title = stringResource(R.string.msg_no_search_history),
                description = stringResource(R.string.msg_no_search_history_desc),
                painter = androidx.compose.ui.res.painterResource(R.drawable.ic_search)
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.title_recent_searches),
                    style = MaterialTheme.typography.titleMedium
                )

                TextButton(
                    onClick = onClearHistory
                ) {
                    Text(
                        text = stringResource(R.string.action_clear_all),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            LazyColumn {
                itemsIndexed(
                    items = searchHistory,
                    key = { index, item -> "search_history_${item.id}_$index" }
                ) { index, item ->
                    SearchHistoryItem(
                        item = item,
                        onClick = { onHistoryItemClick(item) },
                        onDelete = { onDeleteHistoryItem(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchSuggestionsSection(
    suggestions: List<SearchHistoryItem>,
    onSuggestionClick: (SearchHistoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    if (suggestions.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.title_suggestions),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyColumn {
            itemsIndexed(
                items = suggestions,
                key = { index, item -> "suggestion_${item.id}_$index" }
            ) { index, item ->
                SearchSuggestionItem(
                    item = item,
                    onClick = { onSuggestionClick(item) }
                )
            }
        }
    }
}