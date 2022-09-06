package com.sergiom.domain.usecase

import com.sergiom.data.model.ShopDataModel
import com.sergiom.data.repository.NetRepository
import com.sergiom.domain.states.DataState
import javax.inject.Inject

interface GetShopDataUseCase {
    suspend operator fun invoke(): DataState<ShopDataModel>
}

class GetShopDataUseCaseImpl @Inject constructor(
    private val repository: NetRepository
): GetShopDataUseCase {
    override suspend fun invoke(): DataState<ShopDataModel> =
        try {
            DataState.Success(repository.getCabiShopData())
        } catch (throwable: Throwable) {
            DataState.Error(throwable)
        }
}

