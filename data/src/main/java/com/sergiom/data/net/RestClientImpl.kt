package com.sergiom.data.net

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

interface RestClient{
    fun getRemoteCaller(): Api
}

class RestClientImpl: RestClient {
    private val api: Api
    private val apiUrl = "https://gist.githubusercontent.com/"
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    init {
        api = retrofit.create(Api::class.java)
    }

    override fun getRemoteCaller(): Api = api
}