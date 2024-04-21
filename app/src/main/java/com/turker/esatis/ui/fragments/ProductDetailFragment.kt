package com.turker.esatis.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.turker.esatis.App
import com.turker.esatis.R
import com.turker.esatis.data.models.ProductModel
import com.turker.esatis.databinding.FragmentProductDetailBinding

private const val ARG_PRODUCT = "productData"

class ProductDetailFragment : Fragment() {
    lateinit var product: ProductModel

    private lateinit var binding: FragmentProductDetailBinding

    private val updatedProducts = ArrayList<ProductModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        var productData: ProductModel? = null
        arguments?.let {
            productData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                it.getSerializable(ARG_PRODUCT, ProductModel::class.java)
            else
                it.getSerializable(ARG_PRODUCT) as ProductModel?

            if (productData != null)
                product = productData as ProductModel
            else
                return
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        Glide.with(binding.root.context)
            .load(product.imageUrl)
            .placeholder(R.drawable.default_product_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.ivProduct)

        binding.tvProductDescription.text = product.description
        binding.tvProductName.text = product.name
        binding.tvProductPrice.text = product.priceText
        binding.tvProductCount.text = product.basketCount.toString()

        binding.ivCountIncrease.setOnClickListener {
            if (!updatedProducts.contains(product))
                updatedProducts.add(product)

            product.basketCount++

            binding.tvProductCount.text = product.basketCount.toString()

            App.addToBasket(product)

            if (product.basketCount > 0)
                binding.ivProductDecrease.isEnabled = true
        }

        binding.ivProductDecrease.setOnClickListener {
            if (product.basketCount > 0) {
                if (!updatedProducts.contains(product))
                    updatedProducts.add(product)

                product.basketCount--

                binding.tvProductCount.text = product.basketCount.toString()

                App.removeFromBasket(product)
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.tvProductCount.text = product.basketCount.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        App._updatedProducts.value = updatedProducts
    }

    companion object {
        @JvmStatic
        fun newInstance(productData: ProductModel) =
            ProductDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PRODUCT, productData)
                }
            }
    }
}