package com.sergiom.data.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

fun <T> performGetItems(databaseQuery: () -> LiveData<List<T>>): LiveData<List<T>> =
    liveData(Dispatchers.IO) {
        val source = databaseQuery.invoke()
        emitSource(source)
    }

fun <T> performGetItem(databaseQuery: () -> LiveData<T>): LiveData<T> =
    liveData(Dispatchers.IO) {
        val source = databaseQuery.invoke()
        emitSource(source)
    }