package app.echoirx.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.echoirx.domain.model.FileNamingFormat
import app.echoirx.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : SettingsRepository {
    private object PreferencesKeys {
        val OUTPUT_DIRECTORY = stringPreferencesKey("output_directory")
        val FILE_NAMING_FORMAT = intPreferencesKey("file_naming_format")
        val REGION = stringPreferencesKey("region")
        val SERVER_URL = stringPreferencesKey("server_url")
        val SAVE_COVER_ART = booleanPreferencesKey("save_cover_art")
        val SAVE_LYRICS = booleanPreferencesKey("save_lyrics")
        val INCLUDE_TRACK_NUMBER = booleanPreferencesKey("include_track_number")
    }

    // Default server URL - use this as the fallback value
    private val DEFAULT_SERVER_URL = "https://example.com/api/echoir"

    override suspend fun getOutputDirectory(): String? {
        return context.dataStore.data.first()[PreferencesKeys.OUTPUT_DIRECTORY]
    }

    override suspend fun setOutputDirectory(uri: String?) {
        context.dataStore.edit { preferences ->
            if (uri == null) {
                preferences.remove(PreferencesKeys.OUTPUT_DIRECTORY)
            } else {
                preferences[PreferencesKeys.OUTPUT_DIRECTORY] = uri
            }
        }
    }

    override suspend fun getFileNamingFormat(): FileNamingFormat {
        val ordinal = context.dataStore.data.first()[PreferencesKeys.FILE_NAMING_FORMAT] ?: 0
        return FileNamingFormat.fromOrdinal(ordinal)
    }

    override suspend fun setFileNamingFormat(format: FileNamingFormat) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FILE_NAMING_FORMAT] = format.ordinal
        }
    }

    override suspend fun getRegion(): String {
        return context.dataStore.data.first()[PreferencesKeys.REGION] ?: "BR"
    }

    override suspend fun setRegion(region: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.REGION] = region
        }
    }

    override suspend fun getServerUrl(): String {
        return context.dataStore.data.first()[PreferencesKeys.SERVER_URL] ?: DEFAULT_SERVER_URL
    }

    override suspend fun setServerUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SERVER_URL] = url
        }
    }

    override suspend fun getSaveCoverArt(): Boolean {
        return context.dataStore.data.first()[PreferencesKeys.SAVE_COVER_ART] ?: false
    }

    override suspend fun setSaveCoverArt(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SAVE_COVER_ART] = enabled
        }
    }

    override suspend fun getSaveLyrics(): Boolean {
        return context.dataStore.data.first()[PreferencesKeys.SAVE_LYRICS] ?: false
    }

    override suspend fun setSaveLyrics(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SAVE_LYRICS] = enabled
        }
    }

    override suspend fun getIncludeTrackNumber(): Boolean {
        return context.dataStore.data.first()[PreferencesKeys.INCLUDE_TRACK_NUMBER] ?: false
    }

    override suspend fun setIncludeTrackNumber(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.INCLUDE_TRACK_NUMBER] = enabled
        }
    }
}