package com.sergiom.data.net.response

data class ShopDataEntity (
    val products: List<ShopEntity>?
)

data class ShopEntity(
    val code: String?,
    val name: String?,
    val price: Double?
)