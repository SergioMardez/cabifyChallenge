package com.sergiom.data.mapper

import com.sergiom.data.model.ShopDataModel
import com.sergiom.data.model.ShopModel
import com.sergiom.data.net.response.ShopDataEntity
import com.sergiom.data.net.response.ShopEntity
import javax.inject.Inject

class ShopDataMapper @Inject constructor(
    private val mapper: @JvmSuppressWildcards Mapper<ShopEntity, ShopModel>
) : Mapper<ShopDataEntity, ShopDataModel> {
    override fun map(input: ShopDataEntity): ShopDataModel =
        ShopDataModel(
            products = input.products?.map { mapper.map(it) } ?: listOf()
        )
}

class ShopMapper() : Mapper<ShopEntity, ShopModel> {
    override fun map(input: ShopEntity): ShopModel =
        ShopModel(
            code = input.code ?: "",
            name = input.name ?: "",
            price = input.price ?: 0.0
        )
}