package com.atila.home.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.atila.home.Model.Receipt
import com.atila.home.R
import com.atila.home.Util.setSafeOnClickListener
import com.atila.home.databinding.ItemReceiptBinding

class ReceiptAdapter(
    private val onReceiptDeleteClick: (Receipt) -> Unit,
    private val onItemClick: (Receipt, CardView) -> Unit
) :
    ListAdapter<Receipt, ReceiptAdapter.ReceiptViewHolder>(ReceiptDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val binding = ItemReceiptBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReceiptViewHolder(binding, onReceiptDeleteClick)
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {

        with(holder) {
            val receipt = currentList[position]
            holder.bind(receipt)

            this.itemView.animation =
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_view_animation)

        }

    }

    inner class ReceiptViewHolder(
        private val binding: ItemReceiptBinding,
        private val onReceiptDeleteClick: (Receipt) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(receipt: Receipt) {
            binding.itemAmountText.text = receipt.amount.toString()
            binding.itemTypeText.text = receipt.type
            binding.deleteReceiptButton.setOnClickListener { onReceiptDeleteClick(receipt) }

            // for shared element transition
            binding.cardView1.transitionName = receipt.id //-> unique transition name
            // click listener function is in the util/safeclicklistener
            binding.container.setSafeOnClickListener {
                //onItemClick(receipt, binding.itemAmountText)
                onItemClick(receipt, binding.cardView1)

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
}
