package com.example.myshoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myshoppinglist.R
import com.example.myshoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment : Fragment() {

    lateinit var shopItemViewModel: ShopItemViewModel
    lateinit var tilName: TextInputLayout
    lateinit var etName: TextInputEditText
    lateinit var tilCount: TextInputLayout
    lateinit var etCount: TextInputEditText
    lateinit var buttonSave: Button
    private var screenMode = UNKNOWN_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("ShopItemFragment","onCreate")
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        shopItemViewModel = ViewModelProvider(this).get(ShopItemViewModel::class.java)
        observeViewModel()
        setListeners()
        launchRightMode()
    }


    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
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
        const val MODE_SCREEN = "mode_screen"
        const val MODE_ADD = "mode_add"
        const val MODE_EDIT = "mode_edit"
        const val SHOP_ITEM_ID = "shop_item_id"
        const val UNKNOWN_MODE = ""

        fun newShopItemFragmentAdd(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE_SCREEN, MODE_ADD)
                }
            }
        }

        fun newShopItemFragmentEdit(shopItemId: Int): ShopItemFragment {
            val fragment = ShopItemFragment()
            val args = Bundle()
            args.putString(MODE_SCREEN, MODE_EDIT)
            args.putInt(SHOP_ITEM_ID, shopItemId)
            fragment.arguments = args
            return fragment
        }
    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.tilName)
        etName = view.findViewById(R.id.etName)
        tilCount = view.findViewById(R.id.tilCount)
        etCount = view.findViewById(R.id.etCount)
        buttonSave = view.findViewById(R.id.buttonSave)
    }

    private fun observeViewModel() {
        shopItemViewModel.shouldCloseScreen.observe(viewLifecycleOwner, Observer {
            activity?.onBackPressed()
        })

        shopItemViewModel.shopItem.observe(viewLifecycleOwner, Observer {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        })

        shopItemViewModel.errorInputName.observe(viewLifecycleOwner, Observer {
            val message = if (it) {
                getString(R.string.invalid_name)
            } else {
                null
            }
            tilName.error = message
        })

        shopItemViewModel.errorInputCount.observe(viewLifecycleOwner, Observer {
            val message = if (it) {
                getString(R.string.invalid_count)
            } else {
                null
            }
            tilCount.error = message
        })
    }

    private fun setListeners() {
        etName.addTextChangedListener(object : TextWatcher {
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


    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(MODE_SCREEN)) {
            throw RuntimeException("Screen mode is absent")
        }
        val mode = args.getString(MODE_SCREEN)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("ShopItem id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }


}