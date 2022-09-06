package com.sergiom.data.repository

import com.sergiom.data.model.ShopDataModel
import com.sergiom.data.net.response.ShopDataEntity
import retrofit2.Response

interface NetRepository {

    suspend fun getCabiShopData(): ShopDataModel
}