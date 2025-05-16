package app.echoirx.presentation.screens.settings

import app.echoirx.domain.model.FileNamingFormat

data class SettingsState(
    val outputDirectory: String? = null,
    val fileNamingFormat: FileNamingFormat = FileNamingFormat.TITLE_ONLY,
    val region: String = "BR",
    val serverUrl: String = "https://example.com/api/echoir",
    val saveCoverArt: Boolean = false,
    val saveLyrics: Boolean = false,
    val includeTrackNumber: Boolean = false
)