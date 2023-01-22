package com.example.myshoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppinglist.R

class ShopListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvShopItemName = itemView.findViewById<TextView>(R.id.tvShopItemName)
    val tvCount = itemView.findViewById<TextView>(R.id.tvCount)
}