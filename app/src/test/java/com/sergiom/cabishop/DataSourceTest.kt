package com.sergiom.cabishop

import com.sergiom.data.extensions.errorsHandle
import com.sergiom.data.mapper.Mapper
import com.sergiom.data.model.ShopDataModel
import com.sergiom.data.net.Api
import com.sergiom.data.net.response.ShopDataEntity
import com.sergiom.data.repository.NetRepository
import com.sergiom.data.utils.eitherFailure
import com.sergiom.data.utils.eitherSuccess
import com.sergiom.data.utils.onFailure
import com.sergiom.data.utils.onSuccess
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import kotlin.Exception

@RunWith(MockitoJUnitRunner::class)
class DataSourceTest {

    @Mock
    lateinit var mockNetRepo: NetRepository

    @Mock
    lateinit var mockResponse: Response<ShopDataEntity>

    @Mock
    lateinit var mockShopDataEntity: ShopDataEntity

    @Mock
    lateinit var mapper: Mapper<ShopDataEntity, ShopDataModel>

    @Test
    fun `get data and success`() {
        runBlocking {
            Mockito.`when`(mockResponse.isSuccessful).thenReturn(true)
            Mockito.`when`(mockResponse.body()).thenReturn(mockShopDataEntity)
            Mockito.`when`(mockNetRepo.getCabiShopData()).thenAnswer {
                eitherSuccess(mapper.map(mockResponse.errorsHandle()))
            }
            mockNetRepo.getCabiShopData().onSuccess {
                assert(it == mapper.map(mockShopDataEntity))
            }
        }
    }

    @Test
    fun `get data and fail`() {
        runBlocking {
            mockNetRepo.getCabiShopData().onSuccess {
                assert(it.equals(null))
            }.onFailure {
                assert(it.isNotEmpty())
            }
        }
    }

}