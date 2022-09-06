package com.sergiom.cabishop.utils

import com.sergiom.data.model.*

fun List<ShopItemDataBase>.setDiscount(discount: ShopDiscountModel): List<CartAdapterItem> {
    val cartAdapterItems = mutableListOf<CartAdapterItem>()
    val cartDiscount =  mutableListOf<CartAdapterItem>()
    var hasDiscount = false

    forEach { shopItemDataBase ->
        discount.discounts.forEach { shopDiscount ->
            if (shopDiscount.itemCode == shopItemDataBase.code) {
                when (shopDiscount.type) {
                    DiscountType.TWOFORONE -> {
                        hasDiscount = true
                        cartAdapterItems.add(createCartAdapterList(
                            shopItem = shopItemDataBase,
                            shopDiscount = shopDiscount
                        ))
                    }
                    DiscountType.ORMORE -> {
                        hasDiscount = true
                        cartAdapterItems.add(createCartAdapterList(
                            shopItem = shopItemDataBase,
                            shopDiscount = shopDiscount
                        ))
                    }
                }
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
                when (shopDiscount.type) {
                    DiscountType.TWOFORONE -> {
                        numItemsOnDiscount++
                        if (numItemsOnDiscount == shopDiscount.numOfItems) {
                            cartDiscount.addAll(applyDiscount(cartAdapterItems, shopDiscount))
                            hasDiscount = true
                        }
                    }
                    DiscountType.ORMORE -> {
                        numItemsOnDiscount++
                        if (numItemsOnDiscount == shopDiscount.numOfItems) {
                            cartDiscount.addAll(applyDiscount(cartAdapterItems, shopDiscount))
                            hasDiscount = true
                        }
                    }
                }
            }
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

private fun applyDiscount(list: List<CartAdapterItem>, shopDiscount: ShopDiscount): List<CartAdapterItem> {
    val cartAdapterItem = mutableListOf<CartAdapterItem>()
    var itemsDiscount = 0

    list.forEach {
        when (shopDiscount.type) {
            DiscountType.TWOFORONE -> {
                if (shopDiscount.itemCode == it.code) {
                    itemsDiscount++
                    if (itemsDiscount == shopDiscount.numOfItems) {
                        itemsDiscount = 0
                        cartAdapterItem.add(addCartAdapterItem(it, shopDiscount, true))
                    } else {
                        cartAdapterItem.add(addCartAdapterItem(it, shopDiscount, false))
                    }
                } else {
                    cartAdapterItem.add(it)
                }
            }
            DiscountType.ORMORE -> {
                if (shopDiscount.itemCode == it.code) {
                    cartAdapterItem.add(addCartAdapterItem(it, shopDiscount, true))
                } else {
                    cartAdapterItem.add(it)
                }
            }
            else -> cartAdapterItem.add(it)
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