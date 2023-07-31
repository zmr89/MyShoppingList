package com.example.myshoppinglist.di

import android.app.Application
import com.example.myshoppinglist.data.AppDatabase
import com.example.myshoppinglist.data.ShopListDao
import com.example.myshoppinglist.data.ShopListRepositoryImpl
import com.example.myshoppinglist.domain.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    fun bindShopListRepositoryImpl(repositoryImpl: ShopListRepositoryImpl): ShopListRepository


    companion object {
        @Provides
        fun provideDao(
            application: Application
        ): ShopListDao {
            return AppDatabase.getInstance(application).shopListDao()
        }
    }


}