package com.sergiom.data.net

import com.sergiom.data.net.response.ShopDataEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface Api {
    @Headers( "Accept: application/json",
        "Content-type:application/json")
    @GET("palcalde/6c19259bd32dd6aafa327fa557859c2f/raw/ba51779474a150ee4367cda4f4ffacdcca479887/Products.json")
    suspend fun getProducts():Response<ShopDataEntity>
}