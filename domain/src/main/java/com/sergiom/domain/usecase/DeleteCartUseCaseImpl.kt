package com.sergiom.domain.usecase

import com.sergiom.data.repository.ShopRepository
import javax.inject.Inject

interface DeleteCartUseCase {
    suspend operator fun invoke()
}

class DeleteCartUseCaseImpl@Inject constructor(
    private val repository: ShopRepository
): DeleteCartUseCase {
    override suspend fun invoke() {
        repository.deleteAllCart()
    }
}