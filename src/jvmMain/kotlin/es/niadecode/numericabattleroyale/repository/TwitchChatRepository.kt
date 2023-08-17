package es.niadecode.numericabattleroyale.repository

import com.gikk.twirk.TwirkBuilder
import com.gikk.twirk.events.TwirkListener
import com.gikk.twirk.types.twitchMessage.TwitchMessage
import com.gikk.twirk.types.users.TwitchUser
import es.niadecode.numericabattleroyale.model.numerica.GameParticipation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class TwitchChatRepository(
    private val externalScope: CoroutineScope,
    private val preferences: SettingsRepository
) {

    private val _participationFlow = MutableSharedFlow<GameParticipation>()
    val participationFlow: SharedFlow<GameParticipation> = _participationFlow


    private val twirk = TwirkBuilder(
        /* channel = */ preferences.getChannel(),
        /* nick = */ preferences.getChannel(),
        /* oauth = */ "oauth:${preferences.getToken()}"
    )
        //.setVerboseMode(true)
        .build().apply {
            connect()
            addIrcListener(object : TwirkListener {
                override fun onPrivMsg(sender: TwitchUser?, message: TwitchMessage?) {
                    //   println("${sender?.displayName}: ${message?.content}")
                    onTwitchMessageReceived(
                        sender?.displayName.orEmpty(),
                        message?.content.orEmpty(),
                        sender?.userID.toString(),
                        sender?.isMod ?: false
                    )
                }

            })
        }

    fun close() {
        twirk.close()
    }

    private fun onTwitchMessageReceived(userName: String, message: String, userId: String, mod: Boolean) {
        try {
            val number = Integer.parseInt(message)
            externalScope.launch {
                _participationFlow.emit(GameParticipation(userName, number, userId, mod))
            }

        } catch (e: NumberFormatException) {
            return
        }

    }


}
