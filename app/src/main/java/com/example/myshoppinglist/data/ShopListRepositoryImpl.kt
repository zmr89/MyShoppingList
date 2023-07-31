package com.example.myshoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.myshoppinglist.domain.ShopItem
import com.example.myshoppinglist.domain.ShopListRepository
import javax.inject.Inject

class ShopListRepositoryImpl @Inject constructor(
    private val mapper: ShopListMapper,
    private val shopListDao: ShopListDao
) : ShopListRepository {

    override suspend fun addShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapShopItemToShopItemDbModel(shopItem))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapShopItemToShopItemDbModel(shopItem))
    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem {
        return mapper.mapShopItemDbModelToShopItem(shopListDao.getShopItem(shopItemId))
    }

    override fun getShopList(): LiveData<List<ShopItem>> = Transformations
        .map(shopListDao.getShopList(), {
            mapper.mapListShopItemDbModelToShopItem(it)
        })
}