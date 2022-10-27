package com.atila.home.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.atila.home.Adapters.ViewPagerAdapter
import com.atila.home.databinding.FragmentHolderBinding


class HolderFragment : Fragment() {

    private var _binding: FragmentHolderBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHolderBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTabs()

        binding.imageButton.setOnClickListener() {
            val action = HolderFragmentDirections.actionHolderFragmentToReceiptAddingFragment()
            Navigation.findNavController(it).navigate(action)
        }
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