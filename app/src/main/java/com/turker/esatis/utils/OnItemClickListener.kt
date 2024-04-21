package com.turker.esatis.utils

import com.turker.esatis.data.models.ProductModel

interface OnItemClickListener {
    fun onItemClick(position: Int, product: ProductModel)
}