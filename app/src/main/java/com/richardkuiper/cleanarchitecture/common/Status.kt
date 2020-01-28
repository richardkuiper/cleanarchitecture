package com.richardkuiper.cleanarchitecture.common

sealed class Status<out T : Any> {
    data class Success<out T : Any>(val data: T) : Status<T>()
    data class Error<out T : Any>(val cause: Throwable, val data: T? = null) : Status<T>()
    data class Loading<T : Any>(var data: T? = null) : Status<T>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Failure[failure=$cause]"
            is Loading -> "Loading[data=$data]"
        }
    }
}