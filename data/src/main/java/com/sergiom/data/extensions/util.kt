package com.sergiom.data.extensions

import retrofit2.Response

fun <T> Response<T>.errorsHandle(): T =
    try {
        if (this.isSuccessful) {
            this.body()?.let {
                return it
            }
            throw Exception(this.message())
        } else {
            throw Exception(this.errorBody()?.toString())
        }
    } catch (e: Exception) {
        throw Exception(e.message)
    }