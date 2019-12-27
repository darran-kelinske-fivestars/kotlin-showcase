package platform

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

var currentContext: String = "start-up"

inline fun <T> inContext(contextName: String, f: ()->T): T {
    val contextBefore = "before $contextName"
    currentContext = contextBefore
    val result: T
    try {
        result = f()
    } finally {
        val contextDuring = "during $contextName"
        currentContext = if (currentContext != contextBefore) "$contextDuring, $currentContext" else contextDuring
    }
    currentContext = "after $contextName"
    return result
}

expect fun handleError(throwable: Throwable)

inline fun handlingErrors(contextName: String, f: ()->Any?) {
    return inContext(contextName) {
        try {
            f()
        } catch (e: Throwable) {
            handleError(e)
        }
    }
}

fun <T> async(f: suspend () -> T): Deferred<T> {
    return GlobalScope.async { f() }
}

fun <T> asyncHandlingErrors(contextName: String, f: suspend () -> T): Deferred<T> {
    return async {
        inContext(contextName) {
            try {
                f()
            } catch (e: Throwable) {
                handleError(e)
                throw e
            }
        }
    }
}

fun launchHandlingErrors(contextName: String, f: suspend () -> Unit) {
    GlobalScope.launch {
        inContext(contextName) {
            try {
                f()
            } catch (e: Throwable) {
                handleError(e)
            }
        }
    }
}