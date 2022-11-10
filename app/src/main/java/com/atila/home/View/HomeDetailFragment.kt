package com.atila.home.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.atila.home.ViewModel.HomeDetailViewModel
import com.atila.home.databinding.FragmentHomeDetailBinding

class HomeDetailFragment : Fragment() {

    private lateinit var viewModel: HomeDetailViewModel
    private var _binding: FragmentHomeDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeDetailViewModel::class.java]
        viewModel.setHomeCodeLiveData()
    }

    override fun onStart() {
        super.onStart()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.homeDocLiveData.observe(viewLifecycleOwner) { homeDoc ->
            homeDoc.let {
                binding.homeNameTextView.text = homeDoc.get("homeName") as String
                binding.homeCodeTextView.text = homeDoc.id
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}