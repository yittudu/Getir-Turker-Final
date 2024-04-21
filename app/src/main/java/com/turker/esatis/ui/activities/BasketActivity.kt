package com.turker.esatis.ui.activities

import android.os.Bundle
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.turker.esatis.App
import com.turker.esatis.data.NetworkService
import com.turker.esatis.data.models.ProductModel
import com.turker.esatis.databinding.ActivityBasketBinding
import com.turker.esatis.ui.adapters.BasketProductRVAdapter
import com.turker.esatis.ui.adapters.ColProductRVAdapter
import com.turker.esatis.ui.dialogs.CheckoutDialog
import com.turker.esatis.ui.dialogs.LoadingDialog
import com.turker.esatis.utils.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class BasketActivity : AppCompatActivity() {

    lateinit var binding: ActivityBasketBinding

    private lateinit var suggestedProductRVAdapter: ColProductRVAdapter
    private lateinit var basketProductRVAdapter: BasketProductRVAdapter

    private lateinit var loadingDialog: LoadingDialog

    private val updatedProducts = ArrayList<ProductModel>()

    private lateinit var checkoutDialog: CheckoutDialog

    @Inject
    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = LoadingDialog(this)
        checkoutDialog = CheckoutDialog(this)

        checkoutDialog.setOnClickListener {
            clearBasket()
        }

        binding.ivClose.setOnClickListener { finish() }
        binding.ivClearBasket.setOnClickListener {
            clearBasket()
        }

        suggestedProductRVAdapter = ColProductRVAdapter(this)
        binding.rvSuggestedProducts.adapter = suggestedProductRVAdapter

        basketProductRVAdapter = BasketProductRVAdapter(this)
        binding.rvBasketProducts.adapter = basketProductRVAdapter

        binding.tvPay.setOnClickListener {
            val total = App.total.value
            if (total != null) {
                checkoutDialog.total = total
                checkoutDialog.show()
            }
        }

        suggestedProductRVAdapter.onItemIncreaseListener = object : OnItemClickListener {
            override fun onItemClick(position: Int, product: ProductModel) {
                if (!updatedProducts.contains(product))
                    updatedProducts.add(product)

                val newList = ArrayList(suggestedProductRVAdapter.currentList)
                newList.remove(product)
                suggestedProductRVAdapter.submitList(newList)
                suggestedProductRVAdapter.notifyItemRangeChanged(
                    position,
                    suggestedProductRVAdapter.itemCount - position
                )

                if (newList.isEmpty())
                    binding.llSuggestedBody.visibility = GONE
            }
        }

        val addUpdatedProductsListener = object : OnItemClickListener {
            override fun onItemClick(position: Int, product: ProductModel) {
                if (!updatedProducts.contains(product))
                    updatedProducts.add(product)
            }
        }

        suggestedProductRVAdapter.onItemDecreaseListener = addUpdatedProductsListener
        basketProductRVAdapter.onItemIncreaseListener = addUpdatedProductsListener
        basketProductRVAdapter.onItemDecreaseListener = addUpdatedProductsListener

        App.basket.observe(this) {
            val newList = ArrayList(it)
            basketProductRVAdapter.submitList(newList)
        }

        launchDataLoad {
            val suggestedProducts = App.suggestedProducts.filter { it.basketCount == 0 }

            if (suggestedProducts.isEmpty()) {
                binding.llSuggestedBody.visibility = GONE
                return@launchDataLoad
            }

            suggestedProductRVAdapter.submitList(suggestedProducts)
        }

        App.total.observe(this) {
            binding.tvTotal.text = "₺${DecimalFormat("0.00").format(it)}"
        }
    }

    private fun clearBasket() {
        basketProductRVAdapter.currentList.forEach {
            it.basketCount = 0
            if (!updatedProducts.contains(it))
                updatedProducts.add(it)
        }

        App.clearBasket()
        finish()
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return lifecycleScope.launch {
            try {
                loadingDialog.show()
                block()
            } catch (error: Exception) {
                error.printStackTrace()
            } finally {
                loadingDialog.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        App._updatedProducts.value = updatedProducts
    }
}