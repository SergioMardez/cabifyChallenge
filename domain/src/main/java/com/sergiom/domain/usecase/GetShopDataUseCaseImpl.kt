package com.sergiom.domain.usecase

import com.sergiom.data.model.ShopDataModel
import com.sergiom.data.repository.NetRepository
import com.sergiom.data.utils.*
import javax.inject.Inject

interface GetShopDataUseCase {
    suspend operator fun invoke(): Either<ShopDataModel, String>
}

class GetShopDataUseCaseImpl @Inject constructor(
    private val repository: NetRepository
): GetShopDataUseCase {
    override suspend fun invoke(): Either<ShopDataModel, String> {
        val result = repository.getCabiShopData()
        result.onSuccess {
            if(it.products.isEmpty()) return eitherFailure("Empty error")
            return eitherSuccess(it)
        }.onFailure {
            return eitherFailure(it)
        }
        return eitherFailure("Undefined Error")
    }
}

