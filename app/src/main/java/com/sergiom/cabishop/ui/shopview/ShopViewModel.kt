package com.sergiom.cabishop.ui.shopview

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergiom.data.model.ShopDataModel
import com.sergiom.data.model.ShopDiscountModel
import com.sergiom.data.model.ShopItemDataBase
import com.sergiom.data.model.ShopModel
import com.sergiom.domain.states.DataState
import com.sergiom.domain.usecase.GetCartFromDataBaseUseCase
import com.sergiom.domain.usecase.GetDiscountPromotionsUseCase
import com.sergiom.domain.usecase.GetShopDataUseCase
import com.sergiom.domain.usecase.SaveCartItemDataBaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val getShopDataUseCase: GetShopDataUseCase,
    private val saveCartItemToDataUseCase: SaveCartItemDataBaseUseCase,
    private val getCartDataBaseUseCase: GetCartFromDataBaseUseCase,
    private val getDiscountPromotionsUseCase: GetDiscountPromotionsUseCase
): ViewModel() {

    private val _shopData = MutableLiveData<ShopDataModel?>()
    val shopData: LiveData<ShopDataModel?> get() = _shopData

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _cart = getCartDataBaseUseCase.invoke()
    val cart: LiveData<List<ShopItemDataBase>> get() = _cart

    private val _discounts = MutableLiveData<ShopDiscountModel?>()
    val discounts: LiveData<ShopDiscountModel?> get() = _discounts

    init {
        //Para poder ver la imagen de cabify a modo de splash. Aunque se usa como loader en realidad
        getDiscounts()
        Handler(Looper.getMainLooper()).postDelayed({
            getShopData()
        }, 1000)
    }

    private fun getShopData() {
        viewModelScope.launch {
            when (val result = getShopDataUseCase.invoke()) {
                is DataState.Success -> {
                    _shopData.value = result.data
                    _loading.value = false
                }
                is DataState.Error -> {
                    _loading.value = false
                    _error.value = result.error.toString()
                }
            }
        }
    }

    private fun getDiscounts() {
        viewModelScope.launch {
            when (val result = getDiscountPromotionsUseCase.invoke()) {
                is DataState.Success -> {
                    _discounts.value = result.data
                }
                is DataState.Error -> {
                    _error.value = result.error.toString()
                }
            }
        }
    }

    fun addItemToCart(item: ShopModel) {
        viewModelScope.launch {
            saveCartItemToDataUseCase.invoke(item)
        }
    }

}