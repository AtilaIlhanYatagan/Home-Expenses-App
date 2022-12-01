package com.atila.home.View

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import com.atila.home.ViewModel.HomeDetailViewModel
import com.atila.home.databinding.FragmentHomeDetailBinding

class HomeDetailFragment : Fragment() {

    private lateinit var viewModel: HomeDetailViewModel
    private var _binding: FragmentHomeDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeCode: String

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
        viewModel.setHomeDocLiveData()

        binding.homeCodeCopyButton.setOnClickListener {
            copyToClipBoard(homeCode)
        }
    }

    override fun onStart() {
        super.onStart()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.homeDocLiveData.observe(viewLifecycleOwner) { homeDoc ->
            homeDoc.let {
                //ui update
                binding.homeNameTextView.text = homeDoc.get("homeName") as String
                binding.homeCodeTextView.text = homeDoc.id
                //set the homeCode for copy purposes
                homeCode = homeDoc.id
            }
        }
    }

    private fun copyToClipBoard(homeCode: String) {
        val myClipboard =
            getSystemService(requireContext(), ClipboardManager::class.java) as ClipboardManager
        val clip = ClipData.newPlainText("Copied", homeCode)
        myClipboard.setPrimaryClip(clip)
        Toast.makeText(requireActivity(), "Panoya Kopyalandı", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}