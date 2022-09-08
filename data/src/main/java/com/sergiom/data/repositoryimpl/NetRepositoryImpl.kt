package com.sergiom.data.repositoryimpl

import com.sergiom.data.extensions.errorsHandle
import com.sergiom.data.mapper.Mapper
import com.sergiom.data.model.ShopDataModel
import com.sergiom.data.net.RestClient
import com.sergiom.data.net.response.ShopDataEntity
import com.sergiom.data.repository.NetRepository
import com.sergiom.data.utils.Either
import com.sergiom.data.utils.eitherFailure
import com.sergiom.data.utils.eitherSuccess
import javax.inject.Inject

class NetRepositoryImpl @Inject constructor(
    private val restClient: RestClient,
    private val mapper: @JvmSuppressWildcards Mapper<ShopDataEntity, ShopDataModel>
): NetRepository {

    override suspend fun getCabiShopData(): Either<ShopDataModel, String> {
        try {
            val result = restClient.getRemoteCaller().getProducts()
            result.let {
                return eitherSuccess(mapper.map(it.errorsHandle()))
            }
        } catch (e: Exception) {
            return eitherFailure(e.toString())
        }
    }

}