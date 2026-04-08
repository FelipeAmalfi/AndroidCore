package com.url.androidcore.core.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Executes two suspend blocks in parallel and returns their results as a Pair.
 * Both blocks start immediately and run concurrently.
 *
 * @param blockA First suspend block
 * @param blockB Second suspend block
 * @return A Pair containing results from both blocks
 * @throws The first exception that occurs, if any
 */
suspend inline fun <A, B> parallel(
    crossinline blockA: suspend () -> A,
    crossinline blockB: suspend () -> B
): Pair<A, B> = coroutineScope {
    val deferredA = async { blockA() }
    val deferredB = async { blockB() }
    Pair(deferredA.await(), deferredB.await())
}

