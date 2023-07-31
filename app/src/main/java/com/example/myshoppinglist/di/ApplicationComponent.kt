package com.example.myshoppinglist.di

import android.app.Application
import com.example.myshoppinglist.presentation.MainActivity
import com.example.myshoppinglist.presentation.ShopItemFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Component.Factory

@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(shopItemFragment: ShopItemFragment)

    @Factory
    interface ApplicationComponentFactory {
        fun create(@BindsInstance application: Application) : ApplicationComponent
    }

}