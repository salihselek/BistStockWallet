package com.example.biststockwallet.ui.stocks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biststockwallet.R
import com.example.biststockwallet.databinding.FragmentStocksBinding
import com.example.biststockwallet.model.Result
import com.example.biststockwallet.model.Stock
import com.example.biststockwallet.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_stocks.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StockFragment : Fragment(R.layout.fragment_stocks) {

    private val viewModel: StocksViewModel by viewModels()

    lateinit var stocksAdapter: StocksAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentStocksBinding.bind(view)

        binding.fabAddStock.setOnClickListener {
            viewModel.onAddNewStockClick()
        }

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val stock = stocksAdapter.stocks[viewHolder.adapterPosition]
                viewModel.onStockSwipped(stock)
            }

        }).attachToRecyclerView(recycler_view_stocks)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.stocksEvent.collect { event ->
                when (event) {
                    is StocksViewModel.StocksEvent.NavigateToAddStockScreen -> {
                        val action = StockFragmentDirections.actionStockFragmentToAddStockFragment()
                        findNavController().navigate(action)
                    }
                    is StocksViewModel.StocksEvent.ShowStockSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is StocksViewModel.StocksEvent.ShowStockDeleteMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }

        setFragmentResultListener("add_request") { _, bundle ->
            val result = bundle.getInt("add_result")
            viewModel.onAddResult(result)
        }

        viewModel.stocks.observe(viewLifecycleOwner) {
            initRecyclerView(it)
        }

    }

    fun initRecyclerView(stocks: List<Stock>) {
        recycler_view_stocks.layoutManager = LinearLayoutManager(recycler_view_stocks.context)
        stocksAdapter = StocksAdapter(stocks)
        recycler_view_stocks.adapter = stocksAdapter
        stocksAdapter.notifyDataSetChanged()
    }

}