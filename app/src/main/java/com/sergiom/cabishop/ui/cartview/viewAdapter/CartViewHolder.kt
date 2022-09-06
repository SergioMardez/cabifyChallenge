package com.sergiom.cabishop.ui.cartview.viewAdapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sergiom.cabishop.databinding.CartRecyclerLayoutBinding
import com.sergiom.data.model.CartAdapterItem
import com.sergiom.data.model.ShopItemDataBase

class CartViewHolder(private val itemBinding: CartRecyclerLayoutBinding):
    RecyclerView.ViewHolder(itemBinding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(item: CartAdapterItem, listener: CartViewAdapter.DeleteFromCartListener) {
        itemBinding.itemName.text = item.name
        setDiscountPrice(item)
        itemBinding.deleteItem.setOnClickListener {
            listener.onDeleteClicked(ShopItemDataBase(
                keyId = item.keyId,
                code = item.code,
                name = item.name,
                price = item.price
            ))
        }
    }

    private fun setDiscountPrice(item: CartAdapterItem) {
        if (item.hasDiscount) {
            itemBinding.totalAmount.visibility = View.GONE
            itemBinding.discountAmount.visibility = View.VISIBLE
            itemBinding.discountAmount.text = String.format("${item.shopDiscount?.text} - Price: %.2f€", item.priceDiscount)
        } else {
            itemBinding.discountAmount.visibility = View.GONE
            itemBinding.totalAmount.visibility = View.VISIBLE
            itemBinding.totalAmount.text = String.format("Price: %.2f€", item.priceDiscount)
        }
    }
}