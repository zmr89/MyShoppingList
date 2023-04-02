package com.example.myshoppinglist.data

import com.example.myshoppinglist.domain.ShopItem

class ShopListMapper {

    fun mapShopItemToShopItemDbModel(shopItem: ShopItem): ShopItemDbModel {
        return ShopItemDbModel(shopItem.id, shopItem.name, shopItem.count, shopItem.enabled)
    }

    fun mapShopItemDbModelToShopItem(shopItemDbModel: ShopItemDbModel): ShopItem {
        return ShopItem(shopItemDbModel.name, shopItemDbModel.count,
            shopItemDbModel.enabled, shopItemDbModel.id)
    }

    fun mapListShopItemDbModelToShopItem(list: List<ShopItemDbModel>): List<ShopItem> {
        return list.map {
            mapShopItemDbModelToShopItem(it)
        }
    }

}