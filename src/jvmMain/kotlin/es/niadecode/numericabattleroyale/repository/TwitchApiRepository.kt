package es.niadecode.numericabattleroyale.repository

import es.niadecode.numericabattleroyale.model.twitchapi.BanRequest
import es.niadecode.numericabattleroyale.model.twitchapi.BanRequestBody
import es.niadecode.numericabattleroyale.model.twitchapi.ValidateResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class TwitchApiRepository(
    private val externalScope: CoroutineScope,
    private val settingsRepository: SettingsRepository,
) {
    private val twitchValidateUrl = "https://id.twitch.tv/oauth2/validate"
    private val twitchBanUrl = "https://api.twitch.tv/helix/moderation/bans"
    private val twitchVipUrl = "https://api.twitch.tv/helix/channels/vips"
    private val twitchSettingsUrl = "https://api.twitch.tv/helix/chat/settings"

    private var client: HttpClient? = null

    init {
        externalScope.launch {
            client = HttpClient(CIO) {

                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }
            }
        }
    }

    fun close() {
        client?.close()
    }

    suspend fun validate(): Flow<Boolean> {
        return flow {
            val token = settingsRepository.getToken()
            val call = client!!.get(twitchValidateUrl) {
                header("Authorization", "OAuth $token")
            }
            val validateResponse: ValidateResponse = call.body()
            if (call.status.isSuccess()) {
                settingsRepository.setClientID(validateResponse.clientId)
                settingsRepository.setChannel(validateResponse.login)
                emit(true)
            } else {
                emit(false)
            }
        }

    }

    //{"error":"Unauthorized","status":401,"message":"Missing scope: moderator:manage:banned_users"}
    // TERMINA LAS OPCIONES Y LUEGO SIGUES COÑO
    suspend fun ban(victimId: String, duration: Int) {
        if (settingsRepository.getBan()) {
            val token = settingsRepository.getToken()
            client?.post(twitchBanUrl) {
                header(HttpHeaders.Authorization, "Bearer $token")
                header("Client-Id", settingsRepository.getClientID())
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("broadcaster_id", settingsRepository.getClientID())
                    parameters.append("moderator_id", settingsRepository.getClientID())
                }
                    setBody(BanRequest(BanRequestBody(victimId, duration.coerceIn(1..1209599))))
            }
        }
    }
}
