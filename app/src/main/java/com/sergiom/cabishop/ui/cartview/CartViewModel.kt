package com.sergiom.cabishop.ui.cartview

import androidx.lifecycle.*
import com.sergiom.data.model.ShopDiscountModel
import com.sergiom.data.model.ShopItemDataBase
import com.sergiom.domain.states.DataState
import com.sergiom.domain.usecase.DeleteCartUseCase
import com.sergiom.domain.usecase.DeleteItemUseCase
import com.sergiom.domain.usecase.GetCartFromDataBaseUseCase
import com.sergiom.domain.usecase.GetDiscountPromotionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getDiscountPromotionsUseCase: GetDiscountPromotionsUseCase,
    private val getCartDataBaseUseCase: GetCartFromDataBaseUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val deleteItemUseCase: DeleteItemUseCase
): ViewModel() {

    private val _discounts = MutableLiveData<ShopDiscountModel?>()
    val discounts: LiveData<ShopDiscountModel?> get() = _discounts

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _cart = getCartDataBaseUseCase.invoke()
    val cart: LiveData<List<ShopItemDataBase>> get() = _cart

    private val _finishView = MutableLiveData<Boolean>()
    val finishView: LiveData<Boolean> get() = _finishView

    init {
        getDiscounts()
    }

    private fun getDiscounts() {
        viewModelScope.launch {
            when (val result = getDiscountPromotionsUseCase.invoke()) {
                is DataState.Success -> {
                    _discounts.value = result.data
                }
                is DataState.Error -> _error.value = result.error.toString()
                else -> {}
            }
        }
        _loading.value = false
    }

    fun deleteItem(item: ShopItemDataBase) {
        _loading.value = true
        viewModelScope.launch {
            deleteItemUseCase.invoke(item)
        }
        _loading.value = false
    }

    fun finishOrder() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            deleteCartUseCase.invoke()
        }
        _loading.value = false
        _finishView.value = true
    }
}