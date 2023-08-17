package es.niadecode.numericabattleroyale.model.twitchapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class BanRequest(val data: BanRequestBody)

@Serializable
class BanRequestBody(
    @SerialName("user_id") val userId: String,
    val duration: Int,
    val reason: String = "",
)