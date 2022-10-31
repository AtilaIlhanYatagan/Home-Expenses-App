package com.atila.home.View

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.atila.home.Util.animateViewFromBottomToTop
import com.atila.home.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.threeten.bp.ZoneId
import java.security.Timestamp
import java.time.Duration
import java.time.OffsetDateTime
import java.util.*

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firestore = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore

        binding.createHouse.setOnClickListener {
            hideButtons()
            showCreateHouseRegistration()
        }

        binding.joinHouse.setOnClickListener {
            hideButtons()
            showJoinHouseRegistration()
        }

        binding.signUpWithJoinHomeButton.setOnClickListener {
            //TODO empty controls
            if (TextUtils.isEmpty(binding.emailEditText.text))
                binding.emailEditText.error = " asdasd"
/*
            auth.createUserWithEmailAndPassword(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            ).addOnSuccessListener {
                // user created successfully
                val user = auth.currentUser
                user.
            }.addOnFailureListener { exception ->
                // failed to create user
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }*/
        }

        binding.signUpWithHomeCreationButton.setOnClickListener {

            auth.createUserWithEmailAndPassword(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            ).addOnSuccessListener {
                // user created successfully
            }.addOnFailureListener { exception ->
                // failed to create user
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }

            val uid = Firebase.auth.currentUser?.uid
            val userName = binding.userNameEditText.text.toString().trim()
            val homeName = binding.homeNameEditText.text.toString().trim()
            val homeKey = "asd"

            val docData = hashMapOf(
                "homeName" to homeName,
                "dateExample" to com.google.firebase.Timestamp.now(),
                "userIdList" to arrayListOf(uid),
            )

            db.collection("homes").add(docData).addOnSuccessListener { documentReference ->
                // docData.get(key = homeName) -> key olabilir
                Toast.makeText(requireContext(), homeKey, Toast.LENGTH_LONG).show()
            }
                .addOnFailureListener { e ->

                }
            val city = hashMapOf(
                "name" to "Los Angeles",
                "state" to "CA",
                "country" to "USA"
            )
            // receipt ekleme querysi
            val homeref = db.collection("homes")
            homeref.whereArrayContains("userIdList", uid!!).get().addOnSuccessListener {
                homeref.document(it.documents[0].id).collection("Receipts").add(city)
            }


        }
    }


    private fun hideButtons() {
        binding.joinHouse.visibility = View.GONE
        binding.createHouse.visibility = View.GONE
        binding.newHomeTextView.visibility = View.GONE
        binding.joinHouseTextView.visibility = View.GONE
    }

    private fun showCreateHouseRegistration() {
        binding.emailContainer.visibility = View.VISIBLE
        binding.passwordContainer.visibility = View.VISIBLE
        binding.userNameContainer.visibility = View.VISIBLE
        binding.homeNameContainer.visibility = View.VISIBLE
        binding.signUpWithHomeCreationButton.visibility = View.VISIBLE

        animateViewFromBottomToTop(binding.emailContainer)
        animateViewFromBottomToTop(binding.passwordContainer)
        animateViewFromBottomToTop(binding.userNameContainer)
        animateViewFromBottomToTop(binding.homeNameContainer)
        animateViewFromBottomToTop(binding.signUpWithHomeCreationButton)
    }

    private fun showJoinHouseRegistration() {
        binding.emailContainer.visibility = View.VISIBLE
        binding.passwordContainer.visibility = View.VISIBLE
        binding.userNameContainer.visibility = View.VISIBLE
        binding.homeCodeContainer.visibility = View.VISIBLE
        binding.signUpWithJoinHomeButton.visibility = View.VISIBLE

        animateViewFromBottomToTop(binding.emailContainer)
        animateViewFromBottomToTop(binding.passwordContainer)
        animateViewFromBottomToTop(binding.userNameContainer)
        animateViewFromBottomToTop(binding.homeCodeContainer)
        animateViewFromBottomToTop(binding.signUpWithJoinHomeButton)
    }
}