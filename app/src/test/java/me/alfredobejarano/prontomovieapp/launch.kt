package me.alfredobejarano.prontomovieapp

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * launch
 */

fun launchTest(testBlock: suspend () -> Unit) {
    runBlocking {
        GlobalScope.launch { testBlock() }
    }
}