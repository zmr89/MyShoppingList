package com.example.myshoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.data.ShopListRepositoryImpl
import com.example.myshoppinglist.domain.DeleteShopItemUseCase
import com.example.myshoppinglist.domain.EditShopItemUseCase
import com.example.myshoppinglist.domain.GetShopListUseCase
import com.example.myshoppinglist.domain.ShopItem
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)
    private val getShopListUseCase = GetShopListUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    val shopListLD = getShopListUseCase.getShopList()


    fun deleteShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
        }
    }

    fun editShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            val copy = shopItem.copy(enabled = !shopItem.enabled)
            editShopItemUseCase.editShopItem(copy)
        }
    }

}