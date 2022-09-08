package com.sergiom.data.repository

import com.sergiom.data.model.ShopDataModel
import com.sergiom.data.utils.Either

interface NetRepository {
    suspend fun getCabiShopData(): Either<ShopDataModel, String>
}