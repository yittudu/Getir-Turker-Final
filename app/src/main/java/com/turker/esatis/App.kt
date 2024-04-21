package com.turker.esatis

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.turker.esatis.data.models.ProductModel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        val suggestedProducts = ArrayList<ProductModel>()
        val _total = MutableLiveData<Float>()
        val total: LiveData<Float> = _total
        val _basket = MutableLiveData<ArrayList<ProductModel>>()
        val basket: LiveData<ArrayList<ProductModel>> = _basket
        val _updatedProducts = MutableLiveData<List<ProductModel>>()
        val updatedProducts: LiveData<List<ProductModel>> = _updatedProducts

        fun addToBasket(productModel: ProductModel) {
            if (_basket.value == null)
                _basket.value = ArrayList<ProductModel>()

            val value = basket.value ?: return

            if (!value.contains(productModel)) {
                value.add(productModel)
                _basket.value = basket.value
            }

            updateTotal()
        }

        fun removeFromBasket(productModel: ProductModel) {
            if (_basket.value == null)
                _basket.value = ArrayList<ProductModel>()

            val value = basket.value ?: return

            if (value.contains(productModel)) {
                if (value[value.indexOf(productModel)].basketCount == 0)
                    value.remove(productModel)
                _basket.value = value

                updateTotal()
            }
        }

        private fun updateTotal() {
            val value = basket.value ?: return

            var ttl = 0F

            value.forEach {
                ttl += it.price * it.basketCount
            }

            _total.value = ttl
        }

        fun clearBasket() {
            _basket.value = ArrayList<ProductModel>()
            _total.value = 0F
        }
    }
}