package es.niadecode.numericabattleroyale.util

import java.util.prefs.Preferences

var preferences: Preferences? = null
fun createPreferences(): Preferences =
        if (preferences != null) {
            preferences!!
        } else {
            preferences = Preferences.userRoot().node(dataStoreFileName)
            .also {
                preferences = it
            }
            preferences!!
        }

internal const val dataStoreFileName = "numerica_battle_royale"