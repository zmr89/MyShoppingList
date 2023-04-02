package com.example.myshoppinglist.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppinglist.R
import com.example.myshoppinglist.data.AppDatabase
import com.example.myshoppinglist.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    lateinit var mainViewModel: MainViewModel
    lateinit var adapterShopList: ShopListAdapter
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val db = AppDatabase.getInstance(application)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.shopListLD.observe(this, Observer {
            adapterShopList.submitList(it)
        })

        binding.floatingActionButtonAdd.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentAdd(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newShopItemFragmentAdd())
            }
        }
    }

    private fun setupRecyclerView() {
        adapterShopList = ShopListAdapter()
        with(binding.rvShopList) {
            adapter = adapterShopList
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }

        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(binding.rvShopList)
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val shopItem = adapterShopList.currentList[position]
                mainViewModel.deleteShopItem(shopItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupClickListener() {
        adapterShopList.onShopItemClickListener = {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentEdit(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newShopItemFragmentEdit(it.id))
            }
        }
    }

    private fun setupLongClickListener() {
        adapterShopList.onShopItemLongClickListener = {
            mainViewModel.editShopItem(it)
        }
    }

    private fun isOnePaneMode(): Boolean {
        return binding.mainFragmentContainer == null
    }

    private fun launchFragment(fragment: ShopItemFragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction().replace(
            R.id.mainFragmentContainer,
            fragment
        ).addToBackStack(null).commit()
    }

    override fun onEditingFinished() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_LONG).show()
        supportFragmentManager.popBackStack()
    }
}
