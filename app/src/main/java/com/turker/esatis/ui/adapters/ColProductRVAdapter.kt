package com.turker.esatis.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.turker.esatis.App
import com.turker.esatis.data.models.ProductModel
import com.turker.esatis.databinding.ColProductBinding
import com.turker.esatis.ui.viewholders.ColProductViewHolder

class ColProductRVAdapter(context: Context) : ProductRVAdapter<ColProductViewHolder>(context) {

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColProductViewHolder {
        return ColProductViewHolder(
            ColProductBinding.inflate(
                inflater, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ColProductViewHolder, position: Int) {
        val product: ProductModel = getItem(position)

        holder.binding.ivCountIncrease.setOnClickListener {
            onItemIncreaseListener?.onItemClick(position, product)

            product.basketCount++

            holder.binding.tvProductCount.text = product.basketCount.toString()
            holder.binding.tvProductCount.visibility = View.VISIBLE
            holder.binding.ivProductDecrease.visibility = View.VISIBLE

            App.addToBasket(product)
        }

        holder.binding.ivProductDecrease.setOnClickListener {
            onItemDecreaseListener?.onItemClick(position, product)

            product.basketCount--

            holder.binding.tvProductCount.text = product.basketCount.toString()

            App.removeFromBasket(product)

            if (product.basketCount == 0) {
                holder.binding.tvProductCount.visibility = View.GONE
                holder.binding.ivProductDecrease.visibility = View.GONE
            }
        }

        holder.binding.ivProduct.setOnClickListener {
            onItemClickListener?.onItemClick(
                position, product
            )
        }

        holder.setData(product);
    }
}