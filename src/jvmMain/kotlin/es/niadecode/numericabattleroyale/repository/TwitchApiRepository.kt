package es.niadecode.numericabattleroyale.repository

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TwitchApiRepository(
    private val externalScope: CoroutineScope,
) {
    private val twitchValidateUrl = "https://id.twitch.tv/oauth2/validate"
    private val twitchBanUrl = "https://api.twitch.tv/helix/moderation/bans"
    private val twitchAuthUrl = "https://id.twitch.tv/oauth2/authorize"
    private val twitchVipUrl = "https://api.twitch.tv/helix/channels/vips"
    private val twitchSettingsUrl = "https://api.twitch.tv/helix/chat/settings"

    val twitchRedirectHost = "http://localhost:"

    var client: HttpClient? = null

    init {
        externalScope.launch {
            client = HttpClient(CIO){

            }

        }
    }

    fun close() {
        client?.close()
    }

    fun ban(list: List<String>) {
        externalScope.launch(Dispatchers.IO) {
            client?.post(twitchBanUrl) {
                url {
                    parameters.append("broadcaster_id", "TODO")
                    parameters.append("moderator_id", "TODO")
                }
                setBody(list)
            }
        }
    }

}
