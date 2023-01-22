package com.example.myshoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.domain.ShopItem
import com.example.myshoppinglist.domain.ShopListRepository
import kotlin.random.Random

object ShopListRepositoryImpl : ShopListRepository {

    private val liveDataShopItem = MutableLiveData<List<ShopItem>>()
    private val listShopItem = sortedSetOf(Comparator<ShopItem> { t, t2 -> t.id.compareTo(t2.id) })
    private var shopId = 0

    init {
        for (i in 0..10) {
            val item = ShopItem("Name $i", i, Random.nextBoolean())
            addShopItem(item)
        }
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return liveDataShopItem
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return listShopItem.find { it.id == shopItemId }
            ?: throw Exception("Not find element with id $shopItemId")
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = shopId++
        }
        listShopItem.add(shopItem)
        updateShopList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        listShopItem.remove(shopItem)
        updateShopList()
    }

    override fun editShopItem(shopItem: ShopItem) {
//Мой вариант попробовать потом:
//        val oldElement = getShopItem(shopItem.id)
//        listDB.remove(oldElement)
//        listDB.add(shopItem)

        val oldElement = getShopItem(shopItem.id)
        listShopItem.remove(oldElement)
        addShopItem(shopItem)
    }

    private fun updateShopList() {
        liveDataShopItem.value = listShopItem.toList()
    }
}