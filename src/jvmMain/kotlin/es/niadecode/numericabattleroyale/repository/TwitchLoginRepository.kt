package es.niadecode.numericabattleroyale.repository

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


private const val loginSuccessMessage = "<b>Done!</b><br>You can close this window now!"
private const val loginSuccessMessage2 =
    "<html><head><meta http-equiv='cache-control' content='no-cache'><meta http-equiv='expires' content='0'> <meta http-equiv='pragma' content='no-cache'></head><body><script>var link = window.location.href; link = link.substr(0, link.indexOf(\"?\")); window.location.replace(link+\"ready\");</script></body></html>"
private const val loginFailMessage = "<b>Oops!</b><br>Something bad happened. Please restart de game and try again."
private const val magicScriptFragmentsToParams =
    "<html><head><meta http-equiv='cache-control' content='no-cache'><meta http-equiv='expires' content='0'> <meta http-equiv='pragma' content='no-cache'></head><body><script>var link = window.location.toString(); link = link.replace('#','?'); window.location.replace(link);</script></body></html>";


const val twitchAuthUrl = "https://id.twitch.tv/oauth2/authorize"

class TwitchLoginRepository(
    private val externalScope: CoroutineScope,
    private val settingsRepository: SettingsRepository
) {
    var server: NettyApplicationEngine? = null

    private val _tokenFlow = MutableSharedFlow<Boolean>()
    val tokenFlow: SharedFlow<Boolean> = _tokenFlow

    fun startServer(port: Int) {
        externalScope.launch(Dispatchers.IO) {
            server = embeddedServer(Netty, port) {
                routing {
                    get("/") {

                        if (call.request.queryParameters.contains("access_token")) {
                            call.respond(loginSuccessMessage2.toByteArray())
                            digestToken(call.request.queryParameters)
                        } else if (call.request.queryParameters.contains("error")) {
                            call.respond(loginFailMessage.toByteArray())
                        } else {
                            call.respond(magicScriptFragmentsToParams.toByteArray())
                        }
                    }
                    get("/ready") {
                        call.respond(loginSuccessMessage.toByteArray())
                    }
                }
            }.start(wait = true)
        }
    }

    private suspend fun digestToken(params: Parameters) {
        val authToken = params["access_token"].orEmpty()
        val state = params["state"]
        settingsRepository.setToken(authToken)

        //externalScope.launch {
            _tokenFlow.emit(true)
        //}
        //  SessionData.token = authToken.orEmpty()
        println("token = $authToken")
        println("state = $state")
    }

    fun stopServer() {
        server?.stop()
        server = null
    }
}