package es.niadecode.numericabattleroyale.repository

import java.util.prefs.Preferences

class SettingsRepository(
    val preferences: Preferences
) {

    private val tokenKey = "token"
    private val channelKey = "channel"
    private val clientIdKey = "clientId"
    private val timeoutKey = "timeout"
    private val timeoutMultiplierKey = "timeoutMultiplier"
    private val vipKey = "vip"
    private val moderatorImmunityKey = "mods"
    private val maxParticipationsKey = "maxParticipations"

    fun setToken(token: String) {
        preferences.put(tokenKey, token)
    }

    fun setChannel(channel: String) {
        preferences.put(channelKey, channel)
    }

    fun setClientID(clientId: String) {
        preferences.put(clientIdKey, clientId)
    }

    fun setBan(hasBan: Boolean) {
        preferences.put(timeoutKey, hasBan.toString())
    }

    fun setBanMultiplier(multiplier: Int) {
        preferences.put(timeoutMultiplierKey, multiplier.toString())
    }

    fun setVip(hasVip: Boolean) {
        preferences.put(vipKey, hasVip.toString())
    }

    fun setMaxParticipation(max: Int) {
        preferences.put(maxParticipationsKey, max.toString())
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

    fun getBan(): Boolean {
        return preferences.get(timeoutKey, "").toBoolean()
    }

    fun getBanMultiplier(): Int {
        return preferences.get(timeoutMultiplierKey, "").toInt()
    }

    fun getVip(): Boolean {
        return preferences.get(vipKey, "").toBoolean()
    }

    fun getModsImmunity(): Boolean {
        return preferences.get(moderatorImmunityKey, "").toBoolean()
    }

    fun getmaxParticipation(): Int {
        return preferences.get(maxParticipationsKey, "").toInt()
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