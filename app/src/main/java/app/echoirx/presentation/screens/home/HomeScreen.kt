package app.echoirx.presentation.screens.home

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import app.echoirx.R
import app.echoirx.data.utils.extensions.openAudioFile
import app.echoirx.data.utils.extensions.showSnackbar
import app.echoirx.domain.model.Download
import app.echoirx.presentation.components.DownloadBottomSheet
import app.echoirx.presentation.components.EmptyStateMessage
import app.echoirx.presentation.screens.home.components.DownloadItem

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedDownload by remember { mutableStateOf<Download?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.error ?: stringResource(R.string.msg_unknown_error),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        state.activeDownloads.isEmpty() && state.downloadHistory.isEmpty() -> {
            EmptyStateMessage(
                title = stringResource(R.string.msg_downloads_empty),
                description = stringResource(R.string.msg_downloads_empty_desc),
                painter = painterResource(R.drawable.ic_download)
            )
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.activeDownloads.isNotEmpty()) {
                    item(key = "active_downloads_header") {
                        Text(
                            text = stringResource(R.string.title_active_downloads),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    itemsIndexed(
                        items = state.activeDownloads,
                        key = { index, download -> "active_${download.downloadId}_$index" }
                    ) { index, download ->
                        DownloadItem(
                            download = download
                            // Don't make active downloads clickable
                        )
                    }
                }

                if (state.downloadHistory.isNotEmpty()) {
                    item(key = "download_history_header") {
                        Text(
                            text = stringResource(R.string.title_download_history),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = if (state.activeDownloads.isNotEmpty()) 16.dp else 8.dp,
                                bottom = 8.dp
                            )
                        )
                    }

                    itemsIndexed(
                        items = state.downloadHistory,
                        key = { index, download -> "history_${download.downloadId}_$index" }
                    ) { index, download ->
                        DownloadItem(
                            download = download,
                            modifier = Modifier.clickable {
                                selectedDownload = download
                                showBottomSheet = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (showBottomSheet && selectedDownload != null) {
        DownloadBottomSheet(
            download = selectedDownload!!,
            onOpenFile = {
                selectedDownload?.filePath?.openAudioFile(
                    context,
                    snackbarHostState,
                    coroutineScope
                )
                showBottomSheet = false
            },
            onDeleteFile = {
                val success = viewModel.deleteFile(selectedDownload!!)
                if (success) {
                    snackbarHostState.showSnackbar(
                        scope = coroutineScope,
                        message = context.getString(R.string.msg_file_deleted)
                    )
                }
                showBottomSheet = false
            },
            onDeleteFromHistory = {
                val success = viewModel.deleteDownload(selectedDownload!!)
                if (success) {
                    snackbarHostState.showSnackbar(
                        scope = coroutineScope,
                        message = context.getString(R.string.msg_removed_from_history)
                    )
                }
                showBottomSheet = false
            },
            onShareFile = {
                viewModel.shareFile(selectedDownload!!)?.let { shareIntent ->
                    try {
                        context.startActivity(Intent.createChooser(shareIntent, null))
                    } catch (e: Exception) {
                        snackbarHostState.showSnackbar(
                            scope = coroutineScope,
                            message = e.message ?: context.getString(R.string.msg_unknown_error)
                        )
                    }
                }
                showBottomSheet = false
            },
            onRetryDownload = {
                val success = viewModel.retryDownload(selectedDownload!!)
                if (success) {
                    snackbarHostState.showSnackbar(
                        scope = coroutineScope,
                        message = context.getString(R.string.msg_download_restarted)
                    )
                } else {
                    snackbarHostState.showSnackbar(
                        scope = coroutineScope,
                        message = context.getString(R.string.msg_download_retry_failed)
                    )
                }
                showBottomSheet = false
            },
            onDismiss = {
                showBottomSheet = false
            }
        )
    }
}