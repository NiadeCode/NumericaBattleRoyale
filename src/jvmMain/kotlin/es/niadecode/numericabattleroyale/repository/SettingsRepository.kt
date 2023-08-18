package es.niadecode.numericabattleroyale.repository

import java.util.prefs.Preferences

class SettingsRepository(
    val preferences: Preferences
) {

    private val tokenKey = "token"
    private val channelKey = "channel"
    private val clientIdKey = "clientId"
    private val userIdKey = "userId"
    private val timeoutKey = "timeout"
    private val timeoutMultiplierKey = "timeoutMultiplier"
    private val vipKey = "vip"
    private val vipUserKey = "vipUser"
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

    fun setUserId(userId: String) {
        preferences.put(userIdKey, userId)
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

    fun setMod(mod: Boolean) {
        preferences.put(moderatorImmunityKey, mod.toString())
    }

    fun setMaxParticipation(max: Int) {
        preferences.put(maxParticipationsKey, max.toString())
    }

    fun setUserVip(userVip: String) {
        preferences.put(vipUserKey, userVip)
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

    fun getUserId(): String {
        return preferences.get(userIdKey, "")
    }

    fun getBan(): Boolean {
        return preferences.getBoolean(timeoutKey, true)
    }

    fun getBanMultiplier(): Int {
        return preferences.getInt(timeoutMultiplierKey, 2)
    }

    fun getVip(): Boolean {
        return preferences.getBoolean(vipKey, true)
    }

    fun getModsImmunity(): Boolean {
        return preferences.getBoolean(moderatorImmunityKey, false)
    }

    fun getmaxParticipation(): Int {
        return preferences.getInt(maxParticipationsKey, 20)
    }

    fun getVipUser(): String {
        return preferences.get(vipUserKey, "")
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