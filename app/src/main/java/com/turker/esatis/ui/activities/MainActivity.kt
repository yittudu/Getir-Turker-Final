package com.turker.esatis.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.turker.esatis.App
import com.turker.esatis.data.models.ProductModel
import com.turker.esatis.databinding.ActivityMainBinding
import com.turker.esatis.ui.fragments.ListProductFragment
import com.turker.esatis.ui.fragments.ProductDetailFragment
import com.turker.esatis.utils.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listProductFragment: ListProductFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listProductFragment = ListProductFragment()

        listProductFragment.onItemClickListener = this

        supportFragmentManager.addOnBackStackChangedListener {
            checkLastFragment()
        }

        addFragment(listProductFragment);

        binding.cvBasket.setOnClickListener {
            if (!App.basket.value.isNullOrEmpty())
                startActivity(Intent(this, BasketActivity::class.java))
        }

        binding.ivClose.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.tvTitle.setOnClickListener {
            checkLastFragment()
        }
    }

    override fun onResume() {
        super.onResume()

        App.total.observe(this) {
            binding.tvTotal.text = "₺${DecimalFormat("0.00").format(it)}"
        }
    }

    private fun addFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(binding.flFragment.id, fragment, fragment.javaClass.name)
        transaction.commit()
    }

    private fun addFragment(fragment: Fragment, backStackTag: String) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(binding.flFragment.id, fragment, fragment.javaClass.name)
        transaction.addToBackStack(backStackTag)
        transaction.commit()
    }

    override fun onItemClick(
        position: Int, product: ProductModel
    ) {
        addFragment(ProductDetailFragment.newInstance(product), product.id)
    }

    private fun checkLastFragment() {
        val fragmentManager = supportFragmentManager
        val lastFragment = fragmentManager.fragments.last()
        when (lastFragment) {
            is ListProductFragment -> {
                binding.ivClose.visibility = View.GONE
                binding.tvTitle.text = "Ürünler"
            }

            is ProductDetailFragment -> {
                binding.tvTitle.text = "Ürün Detayı"
            }

            else -> {}
        }

        if (lastFragment != null && lastFragment !is ListProductFragment) {
            binding.ivClose.visibility = View.VISIBLE
            return
        }
        binding.ivClose.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()

        App.clearBasket()
    }
}