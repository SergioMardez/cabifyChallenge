package com.sergiom.cabishop.ui.cartview.viewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sergiom.cabishop.databinding.CartRecyclerLayoutBinding
import com.sergiom.cabishop.utils.setDiscount
import com.sergiom.data.model.CartAdapterItem
import com.sergiom.data.model.ShopDiscountModel
import com.sergiom.data.model.ShopItemDataBase

class CartViewAdapter(private val listener: DeleteFromCartListener): RecyclerView.Adapter<CartViewHolder>() {

    private val items = ArrayList<CartAdapterItem>()

    interface DeleteFromCartListener {
        fun onDeleteClicked(item: ShopItemDataBase)
        fun setTotalAmount(amount: Double)
    }

    fun setItems(items: List<ShopItemDataBase>, discounts: ShopDiscountModel) {
        this.items.clear()
        this.items.addAll(items.setDiscount(discounts))
        setTotalPrice()
        notifyDataSetChanged()
    }

    private fun setTotalPrice() {
        var amount = 0.0
        for (item in items) {
            amount += item.priceDiscount
        }
        listener.setTotalAmount(amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding: CartRecyclerLayoutBinding = CartRecyclerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    override fun getItemCount(): Int = items.size
}