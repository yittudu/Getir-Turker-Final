package com.turker.esatis.data.models

import java.io.Serializable

data class ProductModel(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Float,
    val priceText: String,
    val isSuggesting: Boolean,
    var basketCount: Int
) : Serializable