package com.turker.esatis.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.turker.esatis.App
import com.turker.esatis.data.models.ProductModel
import com.turker.esatis.databinding.RowProductBinding
import com.turker.esatis.ui.viewholders.RowProductViewHolder

class BasketProductRVAdapter(context: Context) :
    ProductRVAdapter<RowProductViewHolder>(context) {

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowProductViewHolder {
        return RowProductViewHolder(
            RowProductBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RowProductViewHolder, position: Int) {
        val product: ProductModel = getItem(position)

        holder.binding.ivCountIncrease.setOnClickListener {
            onItemIncreaseListener?.onItemClick(position, product)

            product.basketCount++

            holder.binding.tvProductCount.text = product.basketCount.toString()

            App.addToBasket(product)
        }

        holder.binding.ivProductDecrease.setOnClickListener {
            onItemDecreaseListener?.onItemClick(position, product)

            product.basketCount--

            holder.binding.tvProductCount.text = product.basketCount.toString()

            App.removeFromBasket(product)

            if (product.basketCount == 0) {
                submitList(App.basket.value?.let { it1 -> ArrayList(it1) })
            }
        }

        holder.binding.ivProduct.setOnClickListener {
            onItemClickListener?.onItemClick(
                position,
                product
            )
        }

        holder.setData(product);
    }
}