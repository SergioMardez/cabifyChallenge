package com.sergiom.data.model

data class ShopDataModel(
    val products: List<ShopModel> = listOf()
)

data class ShopModel(
    val code: String,
    val name: String,
    val price: Double
)