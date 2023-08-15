package es.niadecode.numericabattleroyale.repository

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TwitchLoginRepository(
    private val externalScope: CoroutineScope,
) {
    var server: NettyApplicationEngine? = null

    fun startServer() {
        externalScope.launch(Dispatchers.IO) {
            server = embeddedServer(Netty, port = 8080) {
                routing {
                    get("/") {
                        call.respondText("Hello, world!")
                    }
                }
            }.start(wait = true)
        }
    }

    fun stopServer() {
        server?.stop()
    }
}