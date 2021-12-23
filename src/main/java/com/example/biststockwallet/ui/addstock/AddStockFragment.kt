package com.example.biststockwallet.ui.addstock

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.biststockwallet.R
import com.example.biststockwallet.databinding.FragmentAddStockBinding
import com.example.biststockwallet.model.Result
import com.example.biststockwallet.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_stock.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

/**
 *  seciç ypaınca klavye kapansın
 *
 *  enter ile ekle ye geçip ok ile klavye kapasın
 *
 * dto ya çevrilecek
 *
 *
 *
 */


@AndroidEntryPoint
class AddStockFragment : Fragment(R.layout.fragment_add_stock),
    StockDataAdapter.OnItemClickListeneer {

    private val viewModel: AddStockViewModel by viewModels()

    lateinit var adapter: StockDataAdapter

    private var stockList: ArrayList<String> = arrayListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddStockBinding.bind(view)

        binding.apply {

            buttonStockInsert.setOnClickListener {
                hideKeyboard()
                viewModel.onSaveClick()
            }

            searchViewStock.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.filter.filter(newText ?: "")
                    return false
                }
            })

            editTextPrice.addTextChangedListener {
                if (it.isNullOrEmpty())
                    viewModel.selectedStockPrice = Float.NaN
                else {
                    viewModel.selectedStockPrice = it.toString().toFloat()
                }
            }

            editTextAmount.addTextChangedListener {
                if (it.isNullOrEmpty())
                    viewModel.selectedStockAmount = 0
                else
                    viewModel.selectedStockAmount = it.toString().toInt()

            }
        }

        viewModel.stockCodes.observe(viewLifecycleOwner) {
            stockList = it
            initRecylerView()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addStockEvent.collect { event ->
                when (event) {
                    is AddStockViewModel.AddStockEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                    }
                    is AddStockViewModel.AddStockEvent.NavigateBackWithResult -> {
                        setFragmentResult(
                            "add_request",
                            bundleOf("add_result" to event.result)
                        )
                        // setFragmentResult()
                        findNavController().popBackStack()
                    }
                }
            }
        }.exhaustive
    }

    private fun initRecylerView() {
        recycler_view_stocks.layoutManager = LinearLayoutManager(recycler_view_stocks.context)
        adapter = StockDataAdapter(this, stockList)
        recycler_view_stocks.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(stockName: String) {
        viewModel.stockSelected(stockName)
        text_view_selected_stock.text = stockName
        hideKeyboard()
    }

    private fun saveStockListToSharedPreferences(stocks: ArrayList<String>) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.saveToSharedPreferences(stocks)
        }
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(if (currentFocus == null) View(this) else currentFocus!!)
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}