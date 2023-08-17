package es.niadecode.numericabattleroyale.repository

import java.util.prefs.Preferences

class SettingsRepository(
    val preferences: Preferences
) {

    private val tokenKey = "token"
    private val channelKey = "channel"
    private val clientIdKey = "clientId"

    fun setToken(token: String) {
        preferences.put(tokenKey, token)
    }

    fun setChannel(channel: String) {
        preferences.put(channelKey, channel)
    }

    fun setClientID(clientId: String) {
        preferences.put(clientIdKey, clientId)
    }

    fun getToken(): String {
        return preferences.get(tokenKey, "")
    }

    fun getChannel(): String {
        return preferences.get(channelKey, "")
    }

    fun getClientID(): String {
        return preferences.get(clientIdKey, "")
    }

//    fun getToken(): Flow<String> {
//        return dataStore.data.map {
//            it[tokenKey].orEmpty()
//        }
//    }
//
//    fun getChannel(): Flow<String> {
//        return dataStore.data.map {
//            it[channelKey].orEmpty()
//        }
//    }
//
//    fun getTokenAlt(): String {
//        return runBlocking { dataStore.data.map { it[tokenKey].orEmpty() }.first() }
//    }

}