package com.atila.home.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.atila.home.Adapters.ReceiptAdapter
import com.atila.home.Model.Receipt
import com.atila.home.ViewModel.HomePaymentViewModel
import com.atila.home.databinding.FragmentHomePaymentBinding


class HomePaymentFragment : Fragment() {

    private lateinit var viewModel: HomePaymentViewModel
    private var _binding: FragmentHomePaymentBinding? = null
    private val binding get() = _binding!!

    //This field is used for updating the total spending text without making a room call
    var totalSpendingText = 0

    private val receiptAdapter = ReceiptAdapter(onReceiptDeleteClick = {
        onDeleteNote(it)
    }, onItemClick = { receipt, cardView: CardView ->
        val extras = FragmentNavigatorExtras(
            cardView to receipt.id
        )
        val action = HolderFragmentDirections.actionHolderFragmentToReceiptDetailFragment(
            receipt.id
        )
        findNavController().navigate(action, extras)
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomePaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        observeLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewHome.layoutManager = LinearLayoutManager(context)

        viewModel = ViewModelProvider(this)[HomePaymentViewModel::class.java]

        viewModel.refreshData()

        initRecyclerView()

    }

    private fun initRecyclerView() {
        binding.recyclerViewHome.adapter = receiptAdapter
    }

    private fun observeLiveData() {
        viewModel.receiptsLiveData.observe(viewLifecycleOwner) { receipts ->
            receipts.let {
                receiptAdapter.submitList(receipts.toMutableList())
            }
        }
        viewModel.totalSpendingLiveData.observe(viewLifecycleOwner) { totalSpending ->
            totalSpending.let {
                if (totalSpending == null) {
                    binding.totalSpendingText.text = "Toplam Harcama : 0"
                } else if (totalSpending != 0) {
                    totalSpendingText = totalSpending
                    binding.totalSpendingText.text = "Toplam Harcama : $totalSpending"
                }
            }
        }
    }

    private fun onDeleteNote(it: Receipt) {
        // remove the receipt from list
        val newList = receiptAdapter.currentList.toMutableList().apply { remove(it) }
        receiptAdapter.submitList(newList)

        //remove the receipt from room
        viewModel.removeFromList(it)

        //update the UI
        totalSpendingText -= it.amount
        binding.totalSpendingText.text = "Toplam Harcama : $totalSpendingText"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}