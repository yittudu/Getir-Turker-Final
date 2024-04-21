package com.turker.esatis.ui.viewholders

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.turker.esatis.data.models.ProductModel
import com.turker.esatis.databinding.RowProductBinding

class RowProductViewHolder(val binding: RowProductBinding) : ViewHolder(binding.root) {
    fun setData(product: ProductModel) {
        binding.apply {
            productData = product
            executePendingBindings()
        }
    }
}