package com.sergiom.cabishop.ui.shopview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sergiom.cabishop.R
import com.sergiom.cabishop.databinding.FragmentShopBinding
import com.sergiom.cabishop.ui.cartview.CartFragment
import com.sergiom.cabishop.ui.shopview.viewAdapter.ShopViewAdapter
import com.sergiom.cabishop.utils.autoCleared
import com.sergiom.data.model.ShopDiscountModel
import com.sergiom.data.model.ShopModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [ShopFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ShopFragment : Fragment(), ShopViewAdapter.AddToCartListener {

    private var binding: FragmentShopBinding by autoCleared()
    private val viewModel: ShopViewModel by viewModels()

    private lateinit var adapter: ShopViewAdapter
    private var promotions: ShopDiscountModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setUpCartButton()
        setupRecyclerView()
    }

    private fun setListeners() {
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.cabiImage.isVisible = it
        }

        viewModel.shopData.observe(viewLifecycleOwner) {
            it?.let {
                adapter.setItems(it, promotions)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            binding.errText.visibility = View.VISIBLE
            Toast.makeText(context, "ERROR: $it", Toast.LENGTH_LONG).show()
        }

        viewModel.cart.observe(viewLifecycleOwner) {
            binding.buttonCart.setNumOfItems(it.size)
        }

        viewModel.discounts.observe(viewLifecycleOwner) {
            promotions = it
        }
    }

    private fun setupRecyclerView() {
        adapter = ShopViewAdapter(this)
        binding.apply {
            shopRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            shopRecycler.adapter = adapter
        }
    }

    private fun setUpCartButton() {
        binding.buttonCart.setOnClickListener {
            val fragment = CartFragment.newInstance()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.addToBackStack("cartDetail")
            transaction.replace(R.id.container, fragment)
            transaction.commit()
        }
    }

    override fun onItemClicked(item: ShopModel) {
        viewModel.addItemToCart(item)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance() = ShopFragment()
    }
}