package com.sergiom.cabishop

import com.google.gson.Gson
import com.sergiom.data.extensions.errorsHandle
import com.sergiom.data.mapper.Mapper
import com.sergiom.data.model.ShopDataModel
import com.sergiom.data.net.Api
import com.sergiom.data.net.response.ShopDataEntity
import com.sergiom.data.repository.NetRepository
import com.sergiom.data.utils.Either
import com.sergiom.data.utils.eitherSuccess
import com.sergiom.data.utils.onFailure
import com.sergiom.data.utils.onSuccess
import com.sergiom.domain.usecase.GetShopDataUseCase
import com.sergiom.domain.usecase.GetShopDataUseCaseImpl
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

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

    @Before
    fun setup() {
        mockShopItems = Gson().fromJson(products, ShopDataModel::class.java)
        useCaseToTest = GetShopDataUseCaseImpl(mockNetRepo)
    }

    @Test
    fun `get net data and success`()  {
        runBlocking {
            Mockito.`when`(mockNetRepo.getCabiShopData()).thenReturn(eitherSuccess(mockShopItems))
            assert(useCaseToTest() is Either.Success)
            useCaseToTest.invoke().onSuccess {
                assert(it.products.isNotEmpty())
                assert(it.products.size == 3)
                assert(it.products.first().name == "Cabify Voucher")
            }
        }
    }

    //This last test is like the one in DataSourceTest `get data and fail`, just made it in a
    // different way. It has unnecessary stubbungs, but to test a bit more this function
    @Test
    fun `get data and fail`()  {
        runBlocking {
            Mockito.`when`(mockResponse.isSuccessful).thenReturn(false)
            Mockito.`when`(api.getProducts()).thenReturn(mockResponse)
            assert(useCaseToTest() is Either.Failure)
            useCaseToTest.invoke().onFailure {
                assert(it.isNotEmpty())
            }
        }
    }

}