package com.example.biststockwallet.ui.addstock

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.biststockwallet.R
import com.example.biststockwallet.databinding.FragmentAddStockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddStockFragment : Fragment(R.layout.fragment_add_stock) {


    private val viewModel: AddStockViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddStockBinding.bind(view)

        binding.apply {

            buttonStockInsert.setOnClickListener {
                viewModel.onSaveClick()
            }

        }

    }

}