package es.niadecode.numericabattleroyale.model.config

data class ConfigState(
    val timeout: Boolean,
    val timeoutMultiplier: Int,
    val vip: Boolean,
    val modImmunity: Boolean,
    val isConnected: Boolean,
    val maxParticipations: Int,
)
