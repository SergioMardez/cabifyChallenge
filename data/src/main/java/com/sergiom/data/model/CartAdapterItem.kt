package com.sergiom.data.model

data class CartAdapterItem(
    val keyId: Int,
    val code: String,
    val name: String,
    val price: Double,
    val priceDiscount: Double,
    val hasDiscount: Boolean,
    val shopDiscount: ShopDiscount?
)
