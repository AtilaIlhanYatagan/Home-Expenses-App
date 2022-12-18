package com.atila.home.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.atila.home.Adapters.ReceiptAdapter
import com.atila.home.Model.Receipt
import com.atila.home.ViewModel.ApprovalViewModel
import com.atila.home.ViewModel.HomePaymentViewModel
import com.atila.home.databinding.FragmentApprovalBinding

class ApprovalFragment : Fragment() {

    private lateinit var viewModel: ApprovalViewModel
    private var _binding: FragmentApprovalBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentApprovalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        observeLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewApproval.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewApproval.adapter = receiptAdapter
        viewModel = ViewModelProvider(this)[ApprovalViewModel::class.java]

        viewModel.refreshDataFromFirebase()
    }

    private fun observeLiveData() {

        viewModel.receiptsLiveData.observe(viewLifecycleOwner) { receipts ->
            receipts.let {
                receiptAdapter.submitList(receipts.toMutableList())
            }
        }

        viewModel.progressBarLiveData.observe(viewLifecycleOwner) { progress ->
            if (progress == true) {
                binding.progressBar.visibility = View.VISIBLE
            }
            if (progress == false) {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun onDeleteNote(it: Receipt) {
        // remove the receipt from list
        val newList = receiptAdapter.currentList.toMutableList().apply { remove(it) }
        receiptAdapter.submitList(newList)

        //remove the receipt from room
        viewModel.removeFromList(it)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}