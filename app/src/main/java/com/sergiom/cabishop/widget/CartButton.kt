package com.sergiom.cabishop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.sergiom.cabishop.databinding.CartButtonViewBinding

class CartButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {

    var binding: CartButtonViewBinding = CartButtonViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setNumOfItems(items: Int) {
        binding.numItems.visibility = if (items == 0) GONE else VISIBLE
        binding.numItems.text = items.toString()
    }

}