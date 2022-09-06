package com.sergiom.cabishop.ui.shopview.viewAdapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sergiom.cabishop.R
import com.sergiom.cabishop.databinding.RecyclerLayoutBinding
import com.sergiom.data.model.ShopDiscountModel
import com.sergiom.data.model.ShopModel

class ShopViewHolder(private val itemBinding: RecyclerLayoutBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(item: ShopModel, discounts: ShopDiscountModel?, listener: ShopViewAdapter.AddToCartListener) {
        setImage(item.code)
        itemBinding.item.text = item.name
        itemBinding.price.text = String.format("Price: %.2f€", item.price)
        itemBinding.buttonAdd.setOnClickListener {
            listener.onItemClicked(item)
        }
        setDiscountView(item.code, discounts)
    }

    private fun setImage(code: String) {
        when(code) {
            "VOUCHER" -> itemBinding.image.setImageResource(R.drawable.cabify_boucher)
            "TSHIRT" -> itemBinding.image.setImageResource(R.drawable.cabify_tshirt)
            "MUG" -> itemBinding.image.setImageResource(R.drawable.cabify_mug)
        }
    }

    private fun setDiscountView(itemCode: String, discounts: ShopDiscountModel?) {
        discounts?.discounts?.forEach {
            if (itemCode == it.itemCode) {
                itemBinding.promotion.visibility = View.VISIBLE
                itemBinding.promotion.binding.descriptionText.text = it.text
            }
        }
    }

}