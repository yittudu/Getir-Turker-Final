package com.turker.esatis.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.turker.esatis.App
import com.turker.esatis.data.NetworkService
import com.turker.esatis.data.models.ProductModel
import com.turker.esatis.databinding.FragmentListProductBinding
import com.turker.esatis.ui.adapters.ColProductRVAdapter
import com.turker.esatis.ui.dialogs.LoadingDialog
import com.turker.esatis.ui.viewmodels.ListProductViewModel
import com.turker.esatis.utils.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListProductFragment : Fragment(), OnItemClickListener/*, CoroutineScope*/ {

    private lateinit var recommendedProductRVAdapter: ColProductRVAdapter
    private lateinit var allProductRVAdapter: ColProductRVAdapter

    private lateinit var binding: FragmentListProductBinding

    var onItemClickListener: OnItemClickListener? = null

    @Inject
    lateinit var networkService: NetworkService

    private lateinit var loadingDialog: LoadingDialog

    private val viewModel: ListProductViewModel by viewModels {
        ListProductViewModelFactory(networkService)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListProductBinding.inflate(inflater, container, false)

        loadingDialog = LoadingDialog(requireContext())

        recommendedProductRVAdapter = ColProductRVAdapter(requireContext())
        binding.rvRecommendedProducts.adapter = recommendedProductRVAdapter
        recommendedProductRVAdapter.onItemClickListener = this

        allProductRVAdapter = ColProductRVAdapter(requireContext())
        binding.rvAllProducts.adapter = allProductRVAdapter
        allProductRVAdapter.onItemClickListener = this

        viewModel.spinner.observe(viewLifecycleOwner) { b ->
            if (b && !loadingDialog.isShowing)
                loadingDialog.show()
            else if (!b)
                loadingDialog.dismiss()
        }

        subscribeUi()

        App.updatedProducts.observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                lifecycleScope.launch {
                    it.forEach { product ->
                        if (allProductRVAdapter.currentList.contains(product))
                            allProductRVAdapter.notifyItemChanged(
                                allProductRVAdapter.currentList.indexOf(
                                    product
                                )
                            )
                        else if (recommendedProductRVAdapter.currentList.contains(product))
                            recommendedProductRVAdapter.notifyItemChanged(
                                recommendedProductRVAdapter.currentList.indexOf(
                                    product
                                )
                            )
                    }
                }
            }
        }

        return binding.root
    }

    private fun subscribeUi() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            allProductRVAdapter.submitList(products.toList())
        }

        viewModel.suggestedProducts.observe(viewLifecycleOwner) { products ->
            recommendedProductRVAdapter.submitList(products.toList())
            binding.rvRecommendedProducts.visibility = VISIBLE

            App.suggestedProducts.clear()
            App.suggestedProducts.addAll(ArrayList(products))
        }
    }

    override fun onItemClick(
        position: Int, product: ProductModel
    ) {
        onItemClickListener?.onItemClick(position, product)
    }
}

class ListProductViewModelFactory(
    private val networkService: NetworkService
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ListProductViewModel(networkService) as T
}
