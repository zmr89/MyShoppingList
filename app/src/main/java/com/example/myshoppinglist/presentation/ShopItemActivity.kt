package com.example.myshoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myshoppinglist.R
import com.example.myshoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {

    lateinit var shopItemViewModel: ShopItemViewModel
    lateinit var tilName: TextInputLayout
    lateinit var etName: TextInputEditText
    lateinit var tilCount: TextInputLayout
    lateinit var etCount: TextInputEditText
    lateinit var buttonSave: Button
    private var screenMode = UNKNOWN_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        parseIntent()
        initViews()
        shopItemViewModel = ViewModelProvider(this).get(ShopItemViewModel::class.java)
        observeViewModel()
        setListeners()
        launchRightMode()
    }

    private fun launchRightMode() {
        when (screenMode) {
            EXTRA_MODE_ADD -> launchAddMode()
            EXTRA_MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            val name = etName.text?.toString()
            val count = etCount.text?.toString()
            shopItemViewModel.addShopItem(name, count)
        }
    }

    private fun launchEditMode() {
        shopItemViewModel.getShopItem(shopItemId)
        buttonSave.setOnClickListener {
            val name = etName.text?.toString()
            val count = etCount.text?.toString()
            shopItemViewModel.editShopItem(name, count)
        }
    }

    companion object {
        const val EXTRA_MODE_SCREEN = "mode_screen"
        const val EXTRA_MODE_ADD = "mode_add"
        const val EXTRA_MODE_EDIT = "mode_edit"
        const val EXTRA_SHOP_ITEM_ID = "shop_item_id"
        const val UNKNOWN_MODE = ""

        fun newIntentAdd(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE_SCREEN, EXTRA_MODE_ADD)
            return intent
        }

        fun newIntentEdit(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE_SCREEN, EXTRA_MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }

    private fun initViews() {
        tilName = findViewById(R.id.tilName)
        etName = findViewById(R.id.etName)
        tilCount = findViewById(R.id.tilCount)
        etCount = findViewById(R.id.etCount)
        buttonSave = findViewById(R.id.buttonSave)
    }

    private fun observeViewModel() {
        shopItemViewModel.shouldCloseScreen.observe(this, Observer {
            finish()
        })

        shopItemViewModel.shopItem.observe(this, Observer {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        })

        shopItemViewModel.errorInputName.observe(this, Observer {
            val message = if (it) {
                getString(R.string.invalid_name)
            } else {
                null
            }
            tilName.error = message
        })

        shopItemViewModel.errorInputCount.observe(this, Observer {
            val message = if (it) {
                getString(R.string.invalid_count)
            } else {
                null
            }
            tilCount.error = message
        })
    }

    private fun setListeners() {
        etName.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                shopItemViewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        etCount.addTextChangedListener(object  : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                shopItemViewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }


    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_MODE_SCREEN)) {
            throw RuntimeException("Screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_MODE_SCREEN)
        if (mode != EXTRA_MODE_ADD && mode != EXTRA_MODE_EDIT) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == EXTRA_MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("ShopItem id is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

}