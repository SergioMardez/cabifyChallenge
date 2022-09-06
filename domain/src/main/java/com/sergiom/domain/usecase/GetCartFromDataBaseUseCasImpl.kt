package com.sergiom.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.sergiom.data.model.ShopItemDataBase
import com.sergiom.data.repository.ShopRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface GetCartFromDataBaseUseCase {
    fun invoke(): LiveData<List<ShopItemDataBase>>
}

class GetCartFromDataBaseUseCaseImpl @Inject constructor(
    private val repository: ShopRepository
): GetCartFromDataBaseUseCase {
    override fun invoke(): LiveData<List<ShopItemDataBase>> =
        repository.getShopItemsFromDataBase()
}