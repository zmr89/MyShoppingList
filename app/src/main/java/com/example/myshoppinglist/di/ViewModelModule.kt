package com.example.myshoppinglist.di

import androidx.lifecycle.ViewModel
import com.example.myshoppinglist.presentation.MainViewModel
import com.example.myshoppinglist.presentation.ShopItemViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(mainViewModel: MainViewModel) : ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(ShopItemViewModel::class)
    fun bindShopItemViewModel(shopItemViewModel: ShopItemViewModel): ViewModel

}