package com.atila.home.View

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.atila.home.Model.Receipt
import com.atila.home.R
import com.atila.home.ViewModel.HomePaymentViewModel
import com.atila.home.databinding.FragmentReceiptAddingBinding


class ReceiptAddingFragment : Fragment() {

    private var _binding: FragmentReceiptAddingBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomePaymentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReceiptAddingBinding.inflate(inflater, container, false)

        //dropdown menu --> https://www.youtube.com/watch?v=741l_fPKL3Y
        val receiptTypes = resources.getStringArray(R.array.receiptTypes)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.receipt_dropdown_item, receiptTypes)
        binding.receiptTypeEditText.setAdapter(arrayAdapter)

        receiptDescriptionFocusListener()
        receiptAmountFocusListener()
        //receiptTypesListener()
        binding.submitButton.setOnClickListener { submitForm() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomePaymentViewModel::class.java]
    }

    private fun submitForm() {
        binding.amountContainer.helperText = validAmount()
        binding.descriptionContainer.helperText = validDescription()

        val validAmount = binding.amountContainer.helperText == null
        val validDescription = binding.descriptionContainer.helperText == null

        if (validAmount && validDescription) {
            resetForm()
        } else {
            invalidForm()
        }
    }

    private fun invalidForm() {
        var message = ""

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
        val receipt = Receipt(
            amount = binding.amountEditText.text.toString().toInt(),
            description = binding.descriptionEditText.text.toString(),
            type = "asddasd"
        )
        viewModel.addReceiptToList(receipt)


        var message = ""
        message += "Açıklama : " + binding.descriptionEditText.text
        message += "\nTutar : " + binding.amountEditText.text

        AlertDialog.Builder(context)
            .setTitle("Ekleme Başarılı").setMessage(message)
            .setPositiveButton("Onay") { _, _ ->
                binding.descriptionEditText.text = null
                binding.amountEditText.text = null

                binding.amountContainer.helperText = "Gerekli"
                binding.descriptionContainer.helperText = "Gerekli"
            }.show()

    }

    private fun receiptTypesListener() {
        binding.receiptTypeEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.descriptionContainer.helperText = validType()
            }
        }
    }

    private fun validType(): String? {
        val descriptionText = binding.descriptionEditText.text.toString().trim()
        if (descriptionText == "Harcama Türü")
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