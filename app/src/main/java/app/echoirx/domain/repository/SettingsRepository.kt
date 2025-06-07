package app.echoirx.domain.repository

import app.echoirx.domain.model.FileNamingFormat

interface SettingsRepository {
    suspend fun getOutputDirectory(): String?
    suspend fun setOutputDirectory(uri: String?)
    suspend fun getFileNamingFormat(): FileNamingFormat
    suspend fun setFileNamingFormat(format: FileNamingFormat)
    suspend fun getServerUrl(): String
    suspend fun setServerUrl(url: String)
    suspend fun getSaveCoverArt(): Boolean
    suspend fun setSaveCoverArt(enabled: Boolean)
    suspend fun getSaveLyrics(): Boolean
    suspend fun setSaveLyrics(enabled: Boolean)
    suspend fun getIncludeTrackNumber(): Boolean
    suspend fun setIncludeTrackNumber(enabled: Boolean)
    suspend fun getUseCloudflareEns(): Boolean
    suspend fun setUseCloudflareEns(enabled: Boolean)
}