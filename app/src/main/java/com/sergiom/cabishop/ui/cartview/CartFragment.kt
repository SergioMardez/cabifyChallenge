package com.sergiom.cabishop.ui.cartview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sergiom.cabishop.R
import com.sergiom.cabishop.databinding.FragmentCartBinding
import com.sergiom.cabishop.ui.cartview.viewAdapter.CartViewAdapter
import com.sergiom.cabishop.utils.autoCleared
import com.sergiom.data.model.ShopDiscountModel
import com.sergiom.data.model.ShopItemDataBase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CartFragment : Fragment(), CartViewAdapter.DeleteFromCartListener {

    private var binding: FragmentCartBinding by autoCleared()
    private val viewModel: CartViewModel by viewModels()

    private var discounts: ShopDiscountModel? = null
    private lateinit var adapter: CartViewAdapter
    private var shopFinish = false
    private var handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setupRecyclerView()
        setButtons()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun setButtons() {
        binding.backButton.setOnClickListener {
            goToPreviousFragment()
        }
        binding.payButton.setOnClickListener {
            viewModel.finishOrder()
        }
    }

    private fun setListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest {
                    it.cart.observe(viewLifecycleOwner) { cart ->
                        if (cart.isEmpty() && shopFinish.not()) setCartView(true)
                        discounts?.let { discounts ->
                            adapter.setItems(cart, discounts)
                        }
                    }
                    it.discounts?.let { sale ->
                        discounts = sale
                    }
                    it.loading.let { isVisible ->
                        (if (isVisible) View.VISIBLE else View.GONE).also { visibility ->
                            binding.loader.visibility = visibility
                        }
                    }
                    it.error?.let { error ->
                        Toast.makeText(context, "ERROR: $error", Toast.LENGTH_LONG).show()
                    }
                    it.finishView.let { finished ->
                        if (finished) {
                            shopFinish = true
                            setCartView(false)
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = CartViewAdapter(this)
        binding.apply {
            cartRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            cartRecycler.adapter = adapter
        }
    }

    override fun onDeleteClicked(item: ShopItemDataBase) {
        viewModel.deleteItem(item)
    }

    override fun setTotalAmount(amount: Double) {
        binding.totalAmount.text = String.format("Total amount: %.2f€", amount)
    }

    private fun setCartView(isEmpty: Boolean) {
        if (isEmpty) {
            binding.thanksText.text = getString(R.string.cart_empty_text)
        } else {
            binding.thanksText.text = getString(R.string.cart_view_end_text)
            handler.apply {
                postDelayed({
                    goToPreviousFragment()
                }, 1000)
            }
        }
        binding.mainView.visibility = View.GONE
        binding.thanksText.visibility = View.VISIBLE
    }

    private fun goToPreviousFragment() {
        parentFragmentManager.popBackStack()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters
         */
        @JvmStatic
        fun newInstance() = CartFragment()
    }

}