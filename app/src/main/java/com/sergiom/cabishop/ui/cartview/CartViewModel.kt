package com.sergiom.cabishop.ui.cartview

import androidx.lifecycle.*
import com.sergiom.data.model.ShopDiscountModel
import com.sergiom.data.model.ShopItemDataBase
import com.sergiom.data.utils.onFailure
import com.sergiom.data.utils.onSuccess
import com.sergiom.domain.usecase.DeleteCartUseCase
import com.sergiom.domain.usecase.DeleteItemUseCase
import com.sergiom.domain.usecase.GetCartFromDataBaseUseCase
import com.sergiom.domain.usecase.GetDiscountPromotionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getDiscountPromotionsUseCase: GetDiscountPromotionsUseCase,
    private val getCartDataBaseUseCase: GetCartFromDataBaseUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val deleteItemUseCase: DeleteItemUseCase
): ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State(cart = getCartDataBaseUseCase.invoke()))
    val state: StateFlow<State> get() = _state.asStateFlow()

    init {
        getDiscounts()
    }

    fun getDiscounts() {
        viewModelScope.launch {
            val result = getDiscountPromotionsUseCase.invoke()
            result.onSuccess { discounts ->
                _state.update {
                    it.copy(loading = false, discounts = discounts)
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(loading = false, error = error)
                }
            }
        }
    }

    fun deleteItem(item: ShopItemDataBase) {
        viewModelScope.launch {
            loadingState(true)
            deleteItemUseCase.invoke(item)
            loadingState(false)
        }
    }

    fun finishOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingState(true)
            deleteCartUseCase.invoke()
            _state.update {
                it.copy(loading = false, finishView = true)
            }
        }
    }

    private fun loadingState(loading: Boolean) {
        _state.update {
            it.copy(loading = loading)
        }
    }

    data class State(
        var loading: Boolean = true,
        var cart: LiveData<List<ShopItemDataBase>>,
        var discounts: ShopDiscountModel? = null,
        var error: String? = null,
        var finishView: Boolean = false
    )
}