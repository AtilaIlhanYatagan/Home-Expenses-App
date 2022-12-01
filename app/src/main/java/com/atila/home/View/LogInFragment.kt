package com.atila.home.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.atila.home.Util.hideKeyboard
import com.atila.home.ViewModel.UserViewModel
import com.atila.home.databinding.FragmentLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/*
* Login Logic
* 1) backPress on this fragment causes app to finish (prevent going back on login).
* 2) Home screen is the main screen. if no user -> login fragment will be pushed to the stack.
* 3) Login fragment will be popped when the user signs in.
* 4) If logout is detected in the main activity, all backstack will be cleared and home screen will be pushed.
     (pushing home screen with no users will push this fragment from home fragment).
*/

class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                activity?.finish()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        binding.registerButton.setOnClickListener {
            val action = LogInFragmentDirections.actionLogInFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        binding.logInButton.setOnClickListener {
            auth.signInWithEmailAndPassword(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            ).addOnSuccessListener {
                // user signed in successfully
                // set the current user and pop the login fragment
                userViewModel.setCurrentUser()
                hideKeyboard()
                findNavController().popBackStack()

            }.addOnFailureListener { exception ->
                // failed to login
                // show the error message
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
