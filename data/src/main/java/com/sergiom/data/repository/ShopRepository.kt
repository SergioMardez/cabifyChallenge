package com.sergiom.data.repository

import com.sergiom.data.local.CartItemsDao
import com.sergiom.data.model.ShopItemDataBase
import com.sergiom.data.model.ShopModel
import com.sergiom.data.utils.performGetItem
import com.sergiom.data.utils.performGetItems
import javax.inject.Inject

class ShopRepository @Inject constructor(
    private val localDataSource: CartItemsDao
) {
    suspend fun saveShopItemToDatabase(shopItem: ShopModel) {
        localDataSource.insert(
            ShopItemDataBase(
                code = shopItem.code,
                name = shopItem.name,
                price = shopItem.price
            )
        )
    }

    fun getShopItemFromDataBase(keyId: Int) = performGetItem( databaseQuery = { localDataSource.getItem(keyId) })

    fun getShopItemsFromDataBase() = performGetItems( databaseQuery = { localDataSource.getCart() })

    fun deleteAllCart() = localDataSource.deleteAllCart()

    suspend fun deleteItem(itemDataBase: ShopItemDataBase) = localDataSource.deleteItem(itemDataBase)
}