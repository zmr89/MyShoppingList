package com.example.myshoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppinglist.R

class ShopListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvShopItemName: TextView = itemView.findViewById(R.id.tvShopItemName)
    val tvCount: TextView = itemView.findViewById(R.id.tvCount)
}