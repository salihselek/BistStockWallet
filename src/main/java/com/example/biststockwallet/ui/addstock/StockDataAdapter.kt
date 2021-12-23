package com.example.biststockwallet.ui.addstock

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.biststockwallet.databinding.ItemStockDataBinding
import java.util.*
import kotlin.collections.ArrayList

class StockDataAdapter(
    private val listener: OnItemClickListeneer,
    private var stockDataList: ArrayList<String>
) :
    RecyclerView.Adapter<StockDataAdapter.StockDataViewHolder>(), Filterable {

    var stockDataFilterList = ArrayList<String>()

    init {
        stockDataFilterList = stockDataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockDataViewHolder {
        val binding =
            ItemStockDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockDataViewHolder, position: Int) {
        holder.bind(stockDataFilterList[position])
    }

    override fun getItemCount() = stockDataFilterList.count()

    inner class StockDataViewHolder(private val binding: ItemStockDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(stockDataFilterList[position])
                    }
                }
            }
        }

        fun bind(code: String) {
            binding.apply {
                textViewStockCodeName.text = code
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty())
                    stockDataFilterList = stockDataList;
                else {
                    val resultList = ArrayList<String>()
                    for (row in stockDataList) {
                        if (row.toLowerCase().contains(charSearch.toLowerCase()))
                            resultList.add(row)
                    }
                    stockDataFilterList = resultList
                }

                val filterResult = FilterResults()
                filterResult.values = stockDataFilterList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                stockDataFilterList = results?.values as ArrayList<String>
                notifyDataSetChanged()
            }
        }
    }

    interface OnItemClickListeneer {
        fun onItemClick(stockName: String)
    }

}