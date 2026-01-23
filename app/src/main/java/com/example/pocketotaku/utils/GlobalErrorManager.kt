package com.example.pocketotaku.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class GlobalErrorManager {
    private val _errorChannel = Channel<Unit>(Channel.BUFFERED)
    val errors = _errorChannel.receiveAsFlow()

    suspend fun triggerError() {
        _errorChannel.send(Unit)
    }
}
