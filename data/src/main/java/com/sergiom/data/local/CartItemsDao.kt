package com.sergiom.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sergiom.data.model.ShopItemDataBase

@Dao
interface CartItemsDao {

    @Query("SELECT * FROM cart_items")
    fun getCart() : LiveData<List<ShopItemDataBase>>

    @Query("SELECT * FROM cart_items WHERE keyId = :key")
    fun getItem(key: Int): LiveData<ShopItemDataBase>

    @Query("DELETE FROM cart_items")
    fun deleteAllCart()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: ShopItemDataBase)

    @Delete
    suspend fun deleteItem(item: ShopItemDataBase)

}