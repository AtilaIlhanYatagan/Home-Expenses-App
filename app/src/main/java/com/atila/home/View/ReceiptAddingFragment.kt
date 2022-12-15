package com.atila.home.View

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.atila.home.Model.Receipt
import com.atila.home.R
import com.atila.home.Util.hideKeyboard
import com.atila.home.ViewModel.HomePaymentViewModel
import com.atila.home.databinding.FragmentReceiptAddingBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class ReceiptAddingFragment : Fragment() {

    private var _binding: FragmentReceiptAddingBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomePaymentViewModel

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = Firebase.firestore
        firebaseAuth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentReceiptAddingBinding.inflate(inflater, container, false)

        //dropdown menu --> https://www.youtube.com/watch?v=741l_fPKL3Y
        val receiptTypes = resources.getStringArray(R.array.receiptTypes)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.receipt_dropdown_item, receiptTypes)
        binding.receiptTypeEditText.setAdapter(arrayAdapter)

        receiptDescriptionFocusListener()
        receiptAmountFocusListener()
        receiptTypesListener()

        binding.submitButton.setOnClickListener {
            submitForm()
            hideKeyboard()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomePaymentViewModel::class.java]

    }

    private fun submitForm() {
        binding.amountContainer.helperText = validAmount()
        binding.descriptionContainer.helperText = validDescription()
        binding.receiptTypeContainer.helperText = validType()

        val validAmount = binding.amountContainer.helperText == null
        val validDescription = binding.descriptionContainer.helperText == null
        val validType = binding.receiptTypeContainer.helperText == null

        if (validAmount && validDescription && validType) {
            resetForm()
        } else {
            invalidForm()
        }
    }

    private fun invalidForm() {
        var message = ""
        if (binding.receiptTypeContainer.helperText != null)
            message += "\n\nHarcama Türü :  " + binding.receiptTypeContainer.helperText
        if (binding.descriptionContainer.helperText != null)
            message += "\n\nAçıklama :  " + binding.descriptionContainer.helperText
        if (binding.amountContainer.helperText != null)
            message += "\n\nTutar :  " + binding.amountContainer.helperText

        AlertDialog.Builder(activity)
            .setTitle("Ekleme Başarısız").setMessage(message)
            .setPositiveButton("Onay") { _, _ ->
                //do nothing
            }.show()
    }

    private fun resetForm() {
        // adding the receipt to the list before resetting the screen.
        val receipt = Receipt(
            id = "asd",
            amount = binding.amountEditText.text.toString().toInt(),
            description = binding.descriptionEditText.text.toString(),
            type = binding.receiptTypeEditText.text.toString(),
            receiptDate = OffsetDateTime.now(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy / HH:mm"))
                .toString(),
            addedUserId = firebaseAuth.currentUser?.uid.toString()
        )
        viewModel.addReceiptToDatabase(receipt)

        // receipt ekleme querysi
        val db = Firebase.firestore
        val uid = Firebase.auth.currentUser?.uid
        val homeRef = db.collection("homes")

        homeRef.whereArrayContains("userIdList", uid!!).get().addOnSuccessListener {
            // it refers to the single document here
            homeRef.document(it.documents[0].id).collection("receipts").add(receipt)
                .addOnSuccessListener { documentReference ->
                    // receipt added successfully , update the fields here
                    documentReference.update("id", documentReference.id).addOnSuccessListener {
                        Snackbar.make(
                            binding.coordinatorLayout,
                            "Ekleme Başarılı",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        e.localizedMessage?.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
        /*
        // creating the message
        var message = ""
        message += "Açıklama : " + binding.descriptionEditText.text
        message += "\nTutar : " + binding.amountEditText.text
        message += "\nHarcama Türü : " + binding.receiptTypeEditText.text

        //showing the related information to the user
        AlertDialog.Builder(context)
            .setTitle("Ekleme Başarılı").setMessage(message)
            .setPositiveButton("Onay") { _, _ ->
                binding.descriptionEditText.text = null
                binding.amountEditText.text = null
                binding.receiptTypeEditText.text = null

                binding.amountContainer.helperText = "Gerekli"
                binding.descriptionContainer.helperText = "Gerekli"
                binding.receiptTypeContainer.helperText = "Seçiniz"
            }.show()*/
    }

    private fun receiptTypesListener() {
        binding.receiptTypeEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.descriptionContainer.helperText = validType()
            }
        }
    }

    private fun validType(): String? {
        val descriptionText = binding.receiptTypeEditText.text.toString()
        if (descriptionText.isNullOrEmpty())
            return "Seçiniz"
        return null
    }

    private fun receiptDescriptionFocusListener() {
        binding.descriptionEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.descriptionContainer.helperText = validDescription()
            }
        }
    }

    private fun validDescription(): String? {
        val descriptionText = binding.descriptionEditText.text.toString().trim()
        if (descriptionText.isNullOrEmpty())
            return "Gerekli"
        return null
    }

    private fun receiptAmountFocusListener() {
        binding.descriptionEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.amountContainer.helperText = validAmount()
            }
        }
    }

    private fun validAmount(): String? {
        val descriptionText = binding.amountEditText.text.toString().trim()
        if (descriptionText.isNullOrEmpty())
            return "Gerekli"
        return null
    }

}