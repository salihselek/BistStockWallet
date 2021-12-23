package com.example.biststockwallet.ui.stocks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.biststockwallet.databinding.ItemStockBinding
import com.example.biststockwallet.databinding.ItemStockDataBinding
import com.example.biststockwallet.model.Stock
import com.example.biststockwallet.ui.addstock.StockDataAdapter

class StocksAdapter(var stocks: List<Stock>) :
    RecyclerView.Adapter<StocksAdapter.StockViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val currentItem = stocks[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = stocks.size

    inner class StockViewHolder(private val binding: ItemStockBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stock: Stock) {
            binding.apply {
                textViewStockName.text = stock.name
                textViewQuantity.text = stock.quantity.toString()
                textViewAverageCost.text = stock.averageCost.toString()
                textViewAmount.text = stock.price.toString()
            }
        }
    }
}