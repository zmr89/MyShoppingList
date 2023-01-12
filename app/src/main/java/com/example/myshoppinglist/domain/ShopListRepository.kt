package com.example.myshoppinglist.domain

interface ShopListRepository {

    fun getShopList(): List<ShopItem>

    fun getShopItem(shopItemId: Int): ShopItem

    fun addShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun editShopItem(shopItem: ShopItem)

}