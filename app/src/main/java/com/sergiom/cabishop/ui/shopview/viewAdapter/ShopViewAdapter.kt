package com.sergiom.cabishop.ui.shopview.viewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sergiom.cabishop.databinding.RecyclerLayoutBinding
import com.sergiom.data.model.ShopDataModel
import com.sergiom.data.model.ShopDiscountModel
import com.sergiom.data.model.ShopModel

class ShopViewAdapter(private val listener: AddToCartListener): RecyclerView.Adapter<ShopViewHolder>() {

    private val items = ArrayList<ShopModel>()
    private var promotions: ShopDiscountModel? = null

    interface AddToCartListener {
        fun onItemClicked(item: ShopModel)
    }

    fun setItems(items: ShopDataModel, promotions: ShopDiscountModel?) {
        this.items.clear()
        this.items.addAll(items.products)
        this.promotions = promotions
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val binding: RecyclerLayoutBinding = RecyclerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.bind(items[position], promotions, listener)
    }

    override fun getItemCount(): Int = items.size
}