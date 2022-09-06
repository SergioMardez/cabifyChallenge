package com.sergiom.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class ShopItemDataBase(
    @PrimaryKey(autoGenerate = true)
    val keyId: Int = 0,
    val code: String,
    val name: String,
    val price: Double
)
