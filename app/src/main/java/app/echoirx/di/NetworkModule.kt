package app.echoirx.di

import app.echoirx.BuildConfig
import app.echoirx.data.remote.api.ApiService
import app.echoirx.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.Dns
import okhttp3.OkHttpClient
import java.net.InetAddress
import java.time.Duration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    class CloudflareDns : Dns {
        override fun lookup(hostname: String): List<InetAddress> {
            return try {
                InetAddress.getAllByName(hostname).toList()
            } catch (_: Exception) {
                Dns.SYSTEM.lookup(hostname)
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .dns(CloudflareDns())
        .connectTimeout(Duration.ofSeconds(20))
        .readTimeout(Duration.ofMinutes(1))
        .writeTimeout(Duration.ofMinutes(1))
        .retryOnConnectionFailure(true)
        .build()

    @Provides
    @Singleton
    fun provideKtorClient(okHttpClient: OkHttpClient): HttpClient = HttpClient(OkHttp) {
        engine {
            preconfigured = okHttpClient
        }

        install(HttpTimeout) {
            requestTimeoutMillis = Duration.ofMinutes(2).toMillis()
            connectTimeoutMillis = Duration.ofSeconds(20).toMillis()
            socketTimeoutMillis = Duration.ofMinutes(1).toMillis()
        }

        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 3)
            retryOnException(maxRetries = 3, retryOnTimeout = true)
            exponentialDelay()
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        defaultRequest {
            headers.append("X-App-Version", BuildConfig.VERSION_NAME)
        }
    }

    @Provides
    @Singleton
    fun provideApiService(
        client: HttpClient,
        settingsRepository: SettingsRepository
    ): ApiService = ApiService(client, settingsRepository)
}