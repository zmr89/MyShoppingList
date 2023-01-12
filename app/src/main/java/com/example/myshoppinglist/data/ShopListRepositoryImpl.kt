package com.example.myshoppinglist.data

import com.example.myshoppinglist.domain.ShopItem
import com.example.myshoppinglist.domain.ShopListRepository

object ShopListRepositoryImpl : ShopListRepository {

    private val listDB = mutableListOf<ShopItem>()
    private var shopId = 0

    override fun getShopList(): List<ShopItem> {
        return listDB.toList()
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
       return listDB.find { it.id == shopItemId } ?: throw Exception("Not find element with id $shopItemId")
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = shopId++
        }
        listDB.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        listDB.remove(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
//Мой вариант попробовать потом:
//        val oldElement = getShopItem(shopItem.id)
//        listDB.remove(oldElement)
//        listDB.add(shopItem)

        val oldElement = getShopItem(shopItem.id)
        listDB.remove(oldElement)
        addShopItem(shopItem)
    }
}