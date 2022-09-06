package com.sergiom.cabishop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.sergiom.cabishop.databinding.PromotionLayoutBinding
import com.sergiom.data.model.ShopDiscount
import com.sergiom.data.model.ShopDiscountModel

class PromotionView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {

    var binding: PromotionLayoutBinding = PromotionLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    fun setPromotion(shopDiscount: ShopDiscount) {
        binding.descriptionText.text = shopDiscount.text
    }

}