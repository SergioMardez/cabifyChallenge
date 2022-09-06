package com.sergiom.domain.usecase

import com.sergiom.data.model.ShopModel
import com.sergiom.data.repository.ShopRepository
import javax.inject.Inject

interface SaveCartItemDataBaseUseCase {
    suspend operator fun invoke(shopItem: ShopModel)
}

class SaveCartItemDataBaseUseCaseImpl @Inject constructor(
    private val repository: ShopRepository
): SaveCartItemDataBaseUseCase {
    override suspend fun invoke(shopItem: ShopModel): Unit =
        repository.saveShopItemToDatabase(shopItem)
}

