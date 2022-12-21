package com.atila.home.View

import androidx.compose.runtime.getValue
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import com.atila.home.Model.User
import com.atila.home.ViewModel.HomeDetailViewModel
import com.atila.home.databinding.FragmentHomeDetailBinding


class HomeDetailFragment : Fragment() {

    private lateinit var viewModel: HomeDetailViewModel
    private var _binding: FragmentHomeDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // In Compose world
                MaterialTheme {
                    val userList by viewModel.userListLiveData.observeAsState(arrayListOf())
                    UserList(userList)
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeDetailViewModel::class.java]
        viewModel.setHomeDocLiveData()
        viewModel.setUserListLiveData()

        binding.homeCodeCopyButton.setOnClickListener {
            copyToClipBoard(binding.homeCodeTextView.text.toString())
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

    //users: List<String> = List(size = 10) { "$it" }
    @Composable
    fun UserList(userList: List<User>) {
        LazyColumn(modifier = androidx.compose.ui.Modifier.padding(vertical = 4.dp)) {

            items(items = userList) { user ->
                ListItem(name = user)
            }
        }
    }

    @Composable
    fun ListItem(name: User) {
        Surface(
            color = MaterialTheme.colors.primary,
            modifier = androidx.compose.ui.Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            Column(
                modifier = androidx.compose.ui.Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = name.userName)
                    Text(text = "asd")
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MaterialTheme {
            val userList by viewModel.userListLiveData.observeAsState()
            UserList(userList!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}