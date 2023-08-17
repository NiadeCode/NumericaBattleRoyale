package es.niadecode.numericabattleroyale.model.twitchapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*{
  "client_id": "wbmytr93xzw8zbg0p1izqyzzc5mbiz",
  "login": "twitchdev",
  "scopes": [
    "channel:read:subscriptions"
  ],
  "user_id": "141981764",
  "expires_in": 5520838
}*/

@Serializable
data class ValidateResponse(
    @SerialName("client_id") val clientId: String,
    val login: String,
    @SerialName("user_id") val userId: String,
    @SerialName("expires_in") val expiresIn: Int,
)