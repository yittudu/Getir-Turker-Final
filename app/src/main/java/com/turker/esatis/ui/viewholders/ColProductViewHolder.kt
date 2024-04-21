package com.turker.esatis.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.turker.esatis.data.models.ProductModel
import com.turker.esatis.databinding.ColProductBinding

class ColProductViewHolder(val binding: ColProductBinding) :
    ViewHolder(binding.root) {

    fun setData(product: ProductModel) {
        binding.apply {
            if (product.basketCount < 1) {
                tvProductCount.visibility = View.GONE
                ivProductDecrease.visibility = View.GONE
            } else {
                tvProductCount.visibility = View.VISIBLE
                ivProductDecrease.visibility = View.VISIBLE
                tvProductCount.text = product.basketCount.toString()
            }
            productData = product
            executePendingBindings()
        }
    }
}