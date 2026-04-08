package com.url.androidcore.core.utils

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


fun <T> CoroutineScope.launchData(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    onStart: suspend () -> Unit = {},
    onSuccess: suspend (T) -> Unit = {},
    onError: suspend (Throwable) -> Unit = {},
    onCompletion: suspend () -> Unit = {},
    block: suspend () -> T
) {
    launch(dispatcher) {
        onStart()

        val result = runCatching { block() }

        result.fold(
            onSuccess = { onSuccess(it) },
            onFailure = {
                if (it !is CancellationException) {
                    onError(it)
                }
            }
        )

        onCompletion()
    }
}

