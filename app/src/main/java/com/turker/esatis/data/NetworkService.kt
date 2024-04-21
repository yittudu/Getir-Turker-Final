package com.turker.esatis.data

import android.util.Log
import com.google.gson.JsonArray
import com.turker.esatis.data.models.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class NetworkService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://65c38b5339055e7482c12050.mockapi.io/api/")
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val productService = retrofit.create(ProductService::class.java)

    suspend fun allProducts(): List<ProductModel> = withContext(Dispatchers.IO) {
        val result = productService.getAllProducts()

        val jsonArray = result.get(0).asJsonObject.get("products").asJsonArray;


        val products = ArrayList<ProductModel>()

        for (i in 0 until jsonArray.size()) {
            val productObject = jsonArray.get(i).asJsonObject

            var description = ""
            var imageUrl = ""

            if (productObject.get("attribute") != null)
                description = productObject.get("attribute").asString
            else if (productObject.get("shortDescription") != null)
                description = productObject.get("shortDescription").asString

            if (productObject.get("imageURL") != null)
                imageUrl = productObject.get("imageURL").asString
            else if (productObject.get("thumbnailURL") != null)
                imageUrl = productObject.get("thumbnailURL").asString

            products.add(
                ProductModel(
                    productObject.get("id").asString,
                    productObject.get("name").asString,
                    description,
                    imageUrl,
                    productObject.get("price").asFloat,
                    productObject.get("priceText").asString,
                    false,
                    0
                )
            )
        }

        products
    }

    suspend fun suggestedProducts(): List<ProductModel> = withContext(Dispatchers.IO) {
        val result = productService.getSuggestedProducts()

        val jsonArray = result.get(0).asJsonObject.get("products").asJsonArray;


        val products = ArrayList<ProductModel>()

        for (i in 0 until jsonArray.size()) {
            val productObject = jsonArray.get(i).asJsonObject

            var description = ""
            var imageUrl = ""

            if (productObject.get("shortDescription") != null)
                description = productObject.get("shortDescription").asString

            if (productObject.get("imageURL") != null)
                imageUrl = productObject.get("imageURL").asString
            else if (productObject.get("squareThumbnailURL") != null)
                imageUrl = productObject.get("squareThumbnailURL").asString

            products.add(
                ProductModel(
                    productObject.get("id").asString,
                    productObject.get("name").asString,
                    description,
                    imageUrl,
                    productObject.get("price").asFloat,
                    productObject.get("priceText").asString,
                    true,
                    0
                )
            )
        }

        products
    }
}

interface ProductService {
    @GET("products")
    suspend fun getAllProducts(): JsonArray

    @GET("suggestedProducts")
    suspend fun getSuggestedProducts(): JsonArray
}