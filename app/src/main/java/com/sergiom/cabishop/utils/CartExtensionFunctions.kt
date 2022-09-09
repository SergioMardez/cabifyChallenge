package com.sergiom.cabishop.utils

import com.sergiom.data.model.*

fun List<ShopItemDataBase>.setDiscount(discount: ShopDiscountModel): List<CartAdapterItem> {
    val cartAdapterItems = mutableListOf<CartAdapterItem>()
    val cartDiscount =  mutableListOf<CartAdapterItem>()
    var hasDiscount = false

    forEach { shopItemDataBase ->
        discount.discounts.forEach { shopDiscount ->
            if (shopDiscount.itemCode == shopItemDataBase.code) {
                hasDiscount = true
                cartAdapterItems.add(createCartAdapterList(
                    shopItem = shopItemDataBase,
                    shopDiscount = shopDiscount
                ))
            }
        }
        if (hasDiscount.not()) {
            cartAdapterItems.add(createCartAdapterList(shopItem = shopItemDataBase))
        } else {
            hasDiscount = false
        }
    }

    discount.discounts.forEach { shopDiscount ->
        var numItemsOnDiscount = 0
        forEach { shopItemDataBase ->
            if (shopDiscount.itemCode == shopItemDataBase.code) {
                numItemsOnDiscount++
            }
        }
        if (numItemsOnDiscount >= shopDiscount.numOfItems) {
            cartDiscount.addAll(applyDiscount(cartAdapterItems, shopDiscount, numItemsOnDiscount))
            hasDiscount = true
        }
        if (hasDiscount) {
            cartAdapterItems.clear()
            cartAdapterItems.addAll(cartDiscount)
            cartDiscount.clear()
            hasDiscount = false
        }
    }

    return cartAdapterItems
}

private fun applyDiscount(list: List<CartAdapterItem>, shopDiscount: ShopDiscount, itemsOnDiscount: Int): List<CartAdapterItem> {
    val cartAdapterItem = mutableListOf<CartAdapterItem>()
    var itemsDiscount = (itemsOnDiscount * (shopDiscount.itemsToApply.toDouble()/100)).toInt()

    list.forEach {
        if (shopDiscount.itemCode == it.code) {
            if (itemsDiscount != 0) {
                itemsDiscount--
                cartAdapterItem.add(addCartAdapterItem(it, shopDiscount, true))
            } else {
                cartAdapterItem.add(addCartAdapterItem(it, shopDiscount, false))
            }
        } else {
            cartAdapterItem.add(it)
        }
    }

    return cartAdapterItem
}

private fun addCartAdapterItem(shopItem: CartAdapterItem, shopDiscount: ShopDiscount, isDiscount: Boolean): CartAdapterItem = CartAdapterItem(
    keyId = shopItem.keyId,
    code = shopItem.code,
    name = shopItem.name,
    price = shopItem.price,
    priceDiscount = if (isDiscount) shopDiscount.price else shopItem.price,
    hasDiscount = isDiscount,
    shopDiscount = shopDiscount
)

private fun createCartAdapterList(shopItem: ShopItemDataBase, shopDiscount: ShopDiscount? = null): CartAdapterItem = CartAdapterItem(
    keyId = shopItem.keyId,
    code = shopItem.code,
    name = shopItem.name,
    price = shopItem.price,
    priceDiscount = shopItem.price,
    hasDiscount = false,
    shopDiscount = shopDiscount
)