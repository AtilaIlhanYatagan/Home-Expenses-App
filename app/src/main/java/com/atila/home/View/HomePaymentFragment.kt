package com.atila.home.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.atila.home.Adapters.ReceiptAdapter
import com.atila.home.Model.Receipt
import com.atila.home.ViewModel.HomePaymentViewModel
import com.atila.home.databinding.FragmentHomePaymentBinding


class HomePaymentFragment : Fragment() {

    private lateinit var viewModel: HomePaymentViewModel
    private val receiptAdapter = ReceiptAdapter(onReceiptDeleteClick = {
        onDeleteNote(it)
    })

    private fun onDeleteNote(it: Receipt) {
        val newList = receiptAdapter.currentList.toMutableList().apply { remove(it) }
        receiptAdapter.submitList(newList)
        viewModel.removeFromList(it)
    }

    private var _binding: FragmentHomePaymentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        println("observe çağırıldı")
        viewModel.receiptsLiveData.observe(viewLifecycleOwner, Observer { receipts ->
            receipts.let {
                receiptAdapter.submitList(receipts.toMutableList())
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}