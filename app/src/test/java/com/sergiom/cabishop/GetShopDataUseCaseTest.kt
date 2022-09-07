package com.sergiom.cabishop

import com.google.gson.Gson
import com.sergiom.data.extensions.errorsHandle
import com.sergiom.data.mapper.Mapper
import com.sergiom.data.model.ShopDataModel
import com.sergiom.data.net.Api
import com.sergiom.data.net.response.ShopDataEntity
import com.sergiom.data.repository.NetRepository
import com.sergiom.domain.states.DataState
import com.sergiom.domain.usecase.GetShopDataUseCase
import com.sergiom.domain.usecase.GetShopDataUseCaseImpl
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.lang.Exception

@RunWith(MockitoJUnitRunner::class)
class GetShopDataUseCaseTest {
    @Mock
    lateinit var mockNetRepo: NetRepository

    lateinit var useCaseToTest: GetShopDataUseCase

    lateinit var mockShopItems: ShopDataModel

    @Mock
    lateinit var api: Api

    @Mock
    lateinit var mockResponse: Response<ShopDataEntity>

    @Mock
    lateinit var mapper: Mapper<ShopDataEntity, ShopDataModel>

    @Before
    fun setup() {
        mockShopItems = Gson().fromJson(products, ShopDataModel::class.java)
        useCaseToTest = GetShopDataUseCaseImpl(mockNetRepo)
    }

    @Test
    fun `get data and success`()  {
        runBlocking {
            Mockito.`when`(mockNetRepo.getCabiShopData()).thenReturn(mockShopItems)
            assert(useCaseToTest() is DataState.Success)
            when (val result = useCaseToTest.invoke()) {
                is DataState.Success -> {
                    assert(result.data.products.isNotEmpty())
                    assert(result.data.products.size == 3)
                    assert(result.data.products.first().name == "Cabify Voucher")
                }
                else -> {}
            }
        }
    }

    @Test
    fun `get data and fail`()  {
        runBlocking {
            Mockito.`when`(mockResponse.isSuccessful).thenReturn(false)
            Mockito.`when`(api.getProducts()).thenReturn(mockResponse)
            Mockito.`when`(mockNetRepo.getCabiShopData()).thenAnswer {
                runBlocking {
                   return@runBlocking mapper.map(api.getProducts().errorsHandle())
                }
            }
            assert(useCaseToTest() is DataState.Error)
            when (val result = useCaseToTest.invoke()) {
                is DataState.Error -> {
                    assert(result.error.toString() == "java.lang.Exception")
                }
                else -> {}
            }
        }
    }

}