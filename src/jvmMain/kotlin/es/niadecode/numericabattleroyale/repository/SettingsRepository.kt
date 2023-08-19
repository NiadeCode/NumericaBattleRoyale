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
    private val vipUserIdKey = "vipUserId"
    private val vipUserNameKey = "vipUserName"
    private val moderatorImmunityKey = "mods"
    private val maxParticipationsKey = "maxParticipations"
    private val battleOnFailKey = "battleOnFail"

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
        preferences.putBoolean(timeoutKey, hasBan)
    }

    fun setBanMultiplier(multiplier: Int) {
        preferences.putInt(timeoutMultiplierKey, multiplier)
    }

    fun setVip(hasVip: Boolean) {
        preferences.putBoolean(vipKey, hasVip)
    }

    fun setMod(mod: Boolean) {
        preferences.putBoolean(moderatorImmunityKey, mod)
    }

    fun setMaxParticipation(max: Int) {
        preferences.putInt(maxParticipationsKey, max)
    }

    fun setBattleOnFail(battleOnFail: Boolean) {
        preferences.putBoolean(battleOnFailKey, battleOnFail)
    }

    fun setVipUserId(userVip: String) {
        preferences.put(vipUserIdKey, userVip)
    }

    fun setVipUserName(userVip: String) {
        preferences.put(vipUserNameKey, userVip)
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

    fun getVipUserId(): String {
        return preferences.get(vipUserIdKey, "")
    }

    fun getVipNameUser(): String {
        return preferences.get(vipUserNameKey, "")
    }

    fun getBattleOnFail(): Boolean {
        return preferences.getBoolean(battleOnFailKey, true)
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