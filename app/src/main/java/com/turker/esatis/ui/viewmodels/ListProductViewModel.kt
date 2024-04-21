package com.turker.esatis.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turker.esatis.data.NetworkService
import com.turker.esatis.data.models.ProductModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListProductViewModel @Inject internal constructor(
    private val networkService: NetworkService
) : ViewModel() {
    private val _products = MutableLiveData<List<ProductModel>>()
    private val _suggestedProducts = MutableLiveData<List<ProductModel>>()
    val products: LiveData<List<ProductModel>> = _products
    val suggestedProducts: LiveData<List<ProductModel>> = _suggestedProducts

    val _spinner = MutableLiveData<Boolean>(false)
    val spinner: LiveData<Boolean>
        get() = _spinner

    init {
        tryUpdateData()
    }

    private fun tryUpdateData() {
        launchDataLoad {
            val allProducts = networkService.allProducts()
            val suggestedProducts = networkService.suggestedProducts()
            _products.value = allProducts;
            _suggestedProducts.value = suggestedProducts;
        }
    }


    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: Throwable) {
                error.printStackTrace()
            } finally {
                _spinner.value = false
            }
        }
    }
}