package com.sergiom.cabishop.injector

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sergiom.data.local.AppDatabase
import com.sergiom.data.local.CartItemsDao
import com.sergiom.data.mapper.Mapper
import com.sergiom.data.mapper.ShopDataMapper
import com.sergiom.data.mapper.ShopMapper
import com.sergiom.data.model.ShopDataModel
import com.sergiom.data.model.ShopModel
import com.sergiom.data.net.Api
import com.sergiom.data.net.RestClient
import com.sergiom.data.net.RestClientImpl
import com.sergiom.data.net.response.ShopDataEntity
import com.sergiom.data.net.response.ShopEntity
import com.sergiom.data.repository.NetRepository
import com.sergiom.data.repository.ShopRepository
import com.sergiom.data.repositoryimpl.NetRepositoryImpl
import com.sergiom.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideRemoteCaller(gson: Gson): Api = RestClientImpl().getRemoteCaller()

    @Provides
    fun bindsShopMapper(): Mapper<ShopEntity, ShopModel> = ShopMapper()

    @Provides
    fun bindsShopDataMapper(shopMapper: ShopMapper): Mapper<ShopDataEntity, ShopDataModel> = ShopDataMapper(shopMapper)

    @Provides
    fun provideRestClient(): RestClient = RestClientImpl()

    @Provides
    fun provideNetRepository(restClient: RestClient, mapper: ShopDataMapper): NetRepository = NetRepositoryImpl(restClient, mapper)

    @Provides
    fun providesGetShopDataUseCase(netRepository: NetRepository): GetShopDataUseCase = GetShopDataUseCaseImpl(netRepository)

    @Provides
    fun providesGetDiscountPromotionsUseCase(): GetDiscountPromotionsUseCase = GetDiscountPromotionsUseCaseImpl()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideEventsDao(db: AppDatabase) = db.eventsDao()

    @Singleton
    @Provides
    fun provideShopRepository(localDataSource: CartItemsDao) = ShopRepository(localDataSource)

    @Provides
    fun providesGetCartFromDataBaseUseCase(shopRepository: ShopRepository): GetCartFromDataBaseUseCase = GetCartFromDataBaseUseCaseImpl(shopRepository)

    @Provides
    fun providesSaveCartItemDataUseCase(shopRepository: ShopRepository): SaveCartItemDataBaseUseCase = SaveCartItemDataBaseUseCaseImpl(shopRepository)

    @Provides
    fun providesDeleteCartUseCase(shopRepository: ShopRepository): DeleteCartUseCase = DeleteCartUseCaseImpl(shopRepository)

    @Provides
    fun providesDeleteItemUseCase(shopRepository: ShopRepository): DeleteItemUseCase = DeleteItemUseCaseImpl(shopRepository)

}