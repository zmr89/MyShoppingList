package com.example.myshoppinglist.presentation

import android.content.Context
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
import com.example.myshoppinglist.databinding.FragmentShopItemBinding
import com.example.myshoppinglist.di.ApplicationComponent
import com.example.myshoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

class ShopItemFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    val shopItemViewModel: ShopItemViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShopItemViewModel::class.java)
    }
    val component: ApplicationComponent by lazy {
        (requireActivity().application as MyShopListApplication).component
    }

    private var screenMode = UNKNOWN_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw Exception("FragmentShopItemBinding = null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        shopItemViewModel = ViewModelProvider(this).get(ShopItemViewModel::class.java)
        observeViewModel()
        setListeners()
        launchRightMode()

        binding.shopItemViewModelBinding = shopItemViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchAddMode() {
        binding.buttonSave.setOnClickListener {
            val name = binding.etName.text?.toString()
            val count = binding.etCount.text?.toString()
            shopItemViewModel.addShopItem(name, count)
        }
    }

    private fun launchEditMode() {
        shopItemViewModel.getShopItem(shopItemId)
        binding.buttonSave.setOnClickListener {
            val name = binding.etName.text?.toString()
            val count = binding.etCount.text?.toString()
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

    private fun observeViewModel() {
        shopItemViewModel.shouldCloseScreen.observe(viewLifecycleOwner, Observer {
            onEditingFinishedListener.onEditingFinished()
        })
    }

    private fun setListeners() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                shopItemViewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.etCount.addTextChangedListener(object : TextWatcher {
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

    interface OnEditingFinishedListener {
        fun onEditingFinished()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}