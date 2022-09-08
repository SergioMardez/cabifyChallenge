package com.sergiom.cabishop.ui.shopview

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergiom.data.model.ShopDataModel
import com.sergiom.data.model.ShopDiscountModel
import com.sergiom.data.model.ShopItemDataBase
import com.sergiom.data.model.ShopModel
import com.sergiom.data.utils.onFailure
import com.sergiom.data.utils.onSuccess
import com.sergiom.domain.usecase.GetCartFromDataBaseUseCase
import com.sergiom.domain.usecase.GetDiscountPromotionsUseCase
import com.sergiom.domain.usecase.GetShopDataUseCase
import com.sergiom.domain.usecase.SaveCartItemDataBaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val getShopDataUseCase: GetShopDataUseCase,
    private val saveCartItemToDataUseCase: SaveCartItemDataBaseUseCase,
    private val getCartDataBaseUseCase: GetCartFromDataBaseUseCase,
    private val getDiscountPromotionsUseCase: GetDiscountPromotionsUseCase
): ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State(cart = getCartDataBaseUseCase.invoke()))
    val state: StateFlow<State> get() = _state.asStateFlow()

    init {
        getDiscounts()
        Handler(Looper.getMainLooper()).postDelayed({
            getShopData()
        }, 1000) //To see cabify image like a splash
    }

    private fun getShopData() {
        viewModelScope.launch {
            val result = getShopDataUseCase.invoke()
            result.onSuccess { items ->
                _state.update {
                    it.copy(loading = false, shopItem = items)
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(loading = false, error = error)
                }
            }
        }
    }

    private fun getDiscounts() {
        viewModelScope.launch {
            val result = getDiscountPromotionsUseCase.invoke()
            result.onSuccess { items ->
                _state.update {
                    it.copy(discounts = items)
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(loading= false, error = error)
                }
            }
        }
    }

    fun addItemToCart(item: ShopModel) {
        viewModelScope.launch {
            saveCartItemToDataUseCase.invoke(item)
        }
    }

    data class State(
        var loading: Boolean = true,
        var cart: LiveData<List<ShopItemDataBase>>,
        var shopItem: ShopDataModel? = null,
        var discounts: ShopDiscountModel? = null,
        var error: String? = null
    )

}