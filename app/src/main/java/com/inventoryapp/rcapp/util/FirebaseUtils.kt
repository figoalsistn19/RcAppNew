package com.inventoryapp.rcapp.util

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


suspend fun <T> Task<T>.await(): T {
    // fast path
    if (isComplete) {
        val e = exception
        return if (e == null) {
            if (isCanceled) {
                throw CancellationException("Task $this was cancelled normally.")
            } else {
                result
            }
        } else {
            throw e
        }
    }

    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            val e = exception
            if (e == null) {
                if (isCanceled) cont.cancel() else cont.resume(result)
            } else {
                cont.resumeWithException(e)
            }
        }
    }
}

object FirebaseCoroutines {
    suspend fun <T> awaitTask(task: Task<T>): Resource<T> {
        return suspendCancellableCoroutine { cont ->
            cont.invokeOnCancellation {
                task.isCanceled
            }

            task.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    cont.resume(Resource.Success(task.result!!))
                } else {
                    cont.resume(Resource.Failure(task.exception!!))
                }
            }
        }
    }
}