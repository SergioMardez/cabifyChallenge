package com.sergiom.data.repository

import com.sergiom.data.model.ShopDataModel

interface NetRepository {
    suspend fun getCabiShopData(): ShopDataModel
}