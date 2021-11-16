package com.example.biststockwallet.ui.stocks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.biststockwallet.R
import com.example.biststockwallet.databinding.FragmentStocksBinding
import com.example.biststockwallet.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_stocks.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StockFragment : Fragment(R.layout.fragment_stocks) {

    private val viewModel: StocksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentStocksBinding.bind(view)

        binding.fabAddStock.setOnClickListener {
            viewModel.onAddNewStockClick()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.stocksEvent.collect { event ->
                when (event) {
                    is StocksViewModel.StocksEvent.NavigateToAddStockScreen -> {
                        val action = StockFragmentDirections.actionStockFragmentToAddStockFragment()
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }
    }
}