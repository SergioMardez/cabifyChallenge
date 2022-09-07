package com.sergiom.cabishop

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sergiom.cabishop.ui.cartview.CartViewModel
import com.sergiom.data.model.ShopDiscountModel
import com.sergiom.data.model.ShopItemDataBase
import com.sergiom.data.repository.ShopRepository
import com.sergiom.domain.states.DataState
import com.sergiom.domain.usecase.DeleteCartUseCase
import com.sergiom.domain.usecase.DeleteItemUseCase
import com.sergiom.domain.usecase.GetCartFromDataBaseUseCase
import com.sergiom.domain.usecase.GetDiscountPromotionsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CartViewModelTest {

    @Mock
    lateinit var getCartUseCase: GetCartFromDataBaseUseCase

    @Mock
    lateinit var getDiscountUseCase: GetDiscountPromotionsUseCase

    @Mock
    lateinit var deleteCartUseCase: DeleteCartUseCase

    @Mock
    lateinit var deleteItemUseCase: DeleteItemUseCase

    lateinit var shopRepository: ShopRepository

    lateinit var cartViewModel: CartViewModel

    lateinit var mockCartData: List<ShopItemDataBase>
    lateinit var mockDiscountData: ShopDiscountModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    val dispatcher = UnconfinedTestDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        mockCartData = Gson().fromJson(dbProducts, Array<ShopItemDataBase>::class.java).toList()
        cartViewModel = CartViewModel(
            getDiscountPromotionsUseCase = getDiscountUseCase,
            getCartDataBaseUseCase = getCartUseCase,
            deleteCartUseCase = deleteCartUseCase,
            deleteItemUseCase = deleteItemUseCase
        )
        mockDiscountData = Gson().fromJson(discounts, ShopDiscountModel::class.java)
        shopRepository = mock(ShopRepository::class.java)
    }

    @Test
    fun `get data and success`()  {
        runBlocking {
            Mockito.`when`(getDiscountUseCase.invoke()).thenReturn(
                DataState.Success(mockDiscountData)
            )
            val response = getDiscountUseCase.invoke()
            assert(response is DataState.Success)
            when (response) {
                is DataState.Success -> {
                    assert(response.data.discounts.isNotEmpty())
                    assert(response.data.discounts.size == 2)
                    assert(response.data.discounts.first().itemCode == "VOUCHER")
                }
                else -> {}
            }
        }
    }

    @Test
    fun `get cart data and success`()  {
        runBlocking {
            val liveData = MutableLiveData<List<ShopItemDataBase>>()
            liveData.value = mockCartData
            Mockito.`when`(shopRepository.getShopItemsFromDataBase()).thenAnswer {
                liveData
            }
            Mockito.`when`(getCartUseCase.invoke()).thenAnswer {
                shopRepository.getShopItemsFromDataBase()
            }
            val response = getCartUseCase.invoke()
            assert(response.value.isNullOrEmpty().not())
            assert(response.value!!.first().code == "VOUCHER")
        }
    }

}