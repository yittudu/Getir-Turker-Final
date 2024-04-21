package com.turker.esatis.ui.adapters

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.turker.esatis.data.models.ProductModel
import com.turker.esatis.utils.OnItemClickListener

abstract class ProductRVAdapter<VH : RecyclerView.ViewHolder>(private val context: Context) :
    ListAdapter<ProductModel, VH>(ProductDiffCallback()) {

    var onItemClickListener: OnItemClickListener? = null
    var onItemIncreaseListener: OnItemClickListener? = null
    var onItemDecreaseListener: OnItemClickListener? = null
}

private class ProductDiffCallback : DiffUtil.ItemCallback<ProductModel>() {
    override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
        return oldItem == newItem
    }
}