package com.sergiom.domain.usecase

import com.sergiom.data.model.ShopItemDataBase
import com.sergiom.data.repository.ShopRepository
import javax.inject.Inject

interface DeleteItemUseCase {
    suspend operator fun invoke(shopItem: ShopItemDataBase)
}

class DeleteItemUseCaseImpl @Inject constructor(
    private val repository: ShopRepository
): DeleteItemUseCase {
    override suspend fun invoke(shopItem: ShopItemDataBase): Unit =
        repository.deleteItem(shopItem)
}