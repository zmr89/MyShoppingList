package com.example.myshoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myshoppinglist.R
import com.example.myshoppinglist.domain.ShopItem

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private var screenMode = UNKNOWN_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        parseIntent()
        if (savedInstanceState == null) {
            launchRightMode()
        }
    }

    private fun launchRightMode() {
        val fragment = when (screenMode) {
            MODE_ADD -> ShopItemFragment.newShopItemFragmentAdd()
            MODE_EDIT -> ShopItemFragment.newShopItemFragmentEdit(shopItemId)
            else -> throw RuntimeException("Screen mode is absent")
        }
        supportFragmentManager.beginTransaction().replace(R.id.shopItemContainer, fragment).commit()
    }

    companion object {
        const val MODE_SCREEN = "mode_screen"
        const val MODE_ADD = "mode_add"
        const val MODE_EDIT = "mode_edit"
        const val SHOP_ITEM_ID = "shop_item_id"
        const val UNKNOWN_MODE = ""

        fun newIntentAdd(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(MODE_SCREEN, MODE_ADD)
            return intent
        }

        fun newIntentEdit(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(MODE_SCREEN, MODE_EDIT)
            intent.putExtra(SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }

    private fun parseIntent() {
        if (!intent.hasExtra(MODE_SCREEN)) {
            throw RuntimeException("Screen mode is absent")
        }
        val mode = intent.getStringExtra(MODE_SCREEN)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(SHOP_ITEM_ID)) {
                throw RuntimeException("ShopItem id is absent")
            }
            shopItemId = intent.getIntExtra(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    override fun onEditingFinished() {
        finish()
    }

}