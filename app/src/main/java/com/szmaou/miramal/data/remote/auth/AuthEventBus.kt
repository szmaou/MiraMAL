package com.szmaou.miramal.data.remote.auth

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

object AuthEventBus {
    private val _events = Channel<String>(Channel.BUFFERED)
    val events: Flow<String> = _events.receiveAsFlow()

    suspend fun send(code: String) {
        _events.send(code)
    }
}
