package com.sergiom.domain.states

sealed class DataState<out T> {
    class Success<out T>(val data: T) : DataState<T>()
    class Error(val error: Any?) : DataState<Nothing>()
}