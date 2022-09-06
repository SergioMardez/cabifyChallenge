package com.sergiom.data.mapper

interface Mapper<T, R> {
    fun map(input: T): R
}