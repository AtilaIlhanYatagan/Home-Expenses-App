package com.atila.home.View

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.atila.home.Util.setSafeOnClickListener
import com.atila.home.ViewModel.ReceiptDetailViewModel
import com.atila.home.databinding.FragmentReceiptDetailBinding
import org.threeten.bp.format.DateTimeFormatter

class ReceiptDetailFragment : Fragment() {
    //viewModel declaration
    private lateinit var viewModel: ReceiptDetailViewModel

    //ViewBinding declarations
    private var _binding: FragmentReceiptDetailBinding? = null
    private val binding get() = _binding!!

    //PokemonListFragment Arg
    private var receiptIdFromListFragment = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentReceiptDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //getting the arguments
        arguments?.let {
            receiptIdFromListFragment = ReceiptDetailFragmentArgs.fromBundle(it).receiptId
        }

        binding.approveButton.setSafeOnClickListener {
            viewModel.approveButton(receiptIdFromListFragment)
        }

        viewModel = ViewModelProvider(this)[ReceiptDetailViewModel::class.java]
        viewModel.refreshData(receiptIdFromListFragment)

    }

    override fun onStart() {
        super.onStart()
        // shared element transition
        binding.cardView.transitionName = receiptIdFromListFragment

        observeLiveData()

    }

    private fun observeLiveData() {
        viewModel.receiptLiveData.observe(viewLifecycleOwner) { receipt ->

            binding.amount.text = receipt.amount.toString()
            binding.description.text = receipt.description
            binding.type.text = receipt.type
            binding.addedUser.text = receipt.addedUserId
            binding.date.text = receipt.receiptDate.format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy / HH:mm")
            )

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}