package com.example.myshoppinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun getShopList(): LiveData<List<ShopItem>>

    suspend fun getShopItem(shopItemId: Int): ShopItem

    suspend fun addShopItem(shopItem: ShopItem)

    suspend fun deleteShopItem(shopItem: ShopItem)

    suspend fun editShopItem(shopItem: ShopItem)

}