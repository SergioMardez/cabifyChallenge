package com.sergiom.domain.usecase

import com.sergiom.data.model.DiscountType
import com.sergiom.data.model.ShopDiscount
import com.sergiom.data.model.ShopDiscountModel
import com.sergiom.data.utils.*

interface GetDiscountPromotionsUseCase {
    suspend operator fun invoke(): Either<ShopDiscountModel, String>
}

class GetDiscountPromotionsUseCaseImpl: GetDiscountPromotionsUseCase {
    override suspend fun invoke(): Either<ShopDiscountModel, String> {
        return eitherSuccess(getDiscounts())
    }
}

private fun getDiscounts(): ShopDiscountModel = ShopDiscountModel(
    discounts = listOf(
        ShopDiscount(itemCode = "VOUCHER", numOfItems = 2, price = 0.0,
            type = DiscountType.TWOFORONE, text = "2 x 1"),
        ShopDiscount(itemCode = "TSHIRT", numOfItems = 3, price = 19.00,
            type = DiscountType.ORMORE, text = "Buying 3 or more")
    )
)

