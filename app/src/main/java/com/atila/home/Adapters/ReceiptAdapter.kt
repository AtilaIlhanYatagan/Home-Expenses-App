package com.atila.home.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.atila.home.Model.Receipt
import com.atila.home.databinding.ItemReceiptBinding

class ReceiptAdapter(private val onReceiptDeleteClick: (Receipt) -> Unit) :
    ListAdapter<Receipt, ReceiptAdapter.ReceiptViewHolder>(ReceiptDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val binding = ItemReceiptBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReceiptViewHolder(binding, onReceiptDeleteClick)
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        val receipt = currentList[position]
        holder.bind(receipt)
    }

    class ReceiptViewHolder(
        private val binding: ItemReceiptBinding,
        private val onReceiptDeleteClick: (Receipt) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(receipt: Receipt) {
            binding.itemAmountText.text = receipt.amount.toString()
            binding.itemDescriptionText.text = receipt.description
            binding.deleteReceiptButton.setOnClickListener { onReceiptDeleteClick(receipt) }
        }
    }

}

object ReceiptDiffCallback : DiffUtil.ItemCallback<Receipt>() {

    override fun areItemsTheSame(oldItem: Receipt, newItem: Receipt): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Receipt, newItem: Receipt): Boolean {
        return oldItem == newItem
    }
}

