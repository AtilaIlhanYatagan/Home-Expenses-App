package com.atila.home.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.atila.home.Adapters.ViewPagerAdapter
import com.atila.home.R
import com.atila.home.ViewModel.UserViewModel
import com.atila.home.databinding.FragmentHolderBinding


class HolderFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()

    private var _binding: FragmentHolderBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHolderBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.checkCurrentUser()

        setUpTabs()

        binding.imageButton.setOnClickListener() {
            val action = HolderFragmentDirections.actionHolderFragmentToReceiptAddingFragment()
            findNavController().navigate(action)
        }

        val navController = findNavController()
        userViewModel.userLiveData.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                Toast.makeText(requireContext(), "Welcoome", Toast.LENGTH_LONG).show()
            } else {
                navController.navigate(R.id.logInFragment)
            }
        })
    }

    // tab layout --> https://www.youtube.com/watch?v=ZxK7GomWRP8
    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(HomePaymentFragment(), "Ev Giderleri")
        adapter.addFragment(ApprovalFragment(), "Onay Bekleyenler")
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}