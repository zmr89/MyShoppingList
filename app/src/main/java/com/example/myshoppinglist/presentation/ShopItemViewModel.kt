package com.example.myshoppinglist.presentation

import android.app.Application
import androidx.lifecycle.*
import com.example.myshoppinglist.data.ShopListRepositoryImpl
import com.example.myshoppinglist.domain.*
import kotlinx.coroutines.launch

class ShopItemViewModel(application: Application) : AndroidViewModel(application) {

    private val shopListRepository = ShopListRepositoryImpl(application)
    private val addShopItemUseCase = AddShopItemUseCase(shopListRepository)
    private val editShopItemUseCase = EditShopItemUseCase(shopListRepository)
    private val getShopItemUseCase = GetShopItemUseCase(shopListRepository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean> = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen : LiveData<Unit>
    get() = _shouldCloseScreen


    fun addShopItem(inputName: String?, inputCount: String?) {
            val name = parseName(inputName)
            val count = parseCount(inputCount)
            val validated = isValidatedFields(name, count)
            if (validated) {
                viewModelScope.launch {
                val shopItem = ShopItem(name, count, true)
                addShopItemUseCase.addShopItem(shopItem)
                finishWork()
            }
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
            val name = parseName(inputName)
            val count = parseCount(inputCount)
            val validated = isValidatedFields(name, count)
        if (validated) {
        viewModelScope.launch {
                val shopItemFromMLD = _shopItem.value
                shopItemFromMLD?.let {
                    val shopItemCopy = it.copy(name = name, count = count)
                    editShopItemUseCase.editShopItem(shopItemCopy)
                    finishWork()
                }
            }
        }
    }

    fun getShopItem(shopItemId: Int) {
        viewModelScope.launch {
            _shopItem.value = getShopItemUseCase.getShopItem(shopItemId)
        }
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun isValidatedFields(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }
}