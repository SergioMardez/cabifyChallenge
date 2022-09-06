package com.sergiom.data.model

data class ShopDiscountModel (
    val discounts: List<ShopDiscount>
)

data class ShopDiscount(
    val itemCode: String,
    val numOfItems: Int,
    val price: Double,
    val type: DiscountType,
    val text: String
)

enum class DiscountType {
    TWOFORONE, ORMORE
}