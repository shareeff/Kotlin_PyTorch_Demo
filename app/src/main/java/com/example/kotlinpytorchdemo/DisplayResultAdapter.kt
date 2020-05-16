package com.example.kotlinpytorchdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinpytorchdemo.databinding.DisplayResultBinding

class DisplayResultAdapter() : ListAdapter<ClassificationResult, DisplayResultAdapter.ClassificationResultViewHolder>(DiffCallback) {

    class ClassificationResultViewHolder(private var binding: DisplayResultBinding):
            RecyclerView.ViewHolder(binding.root){
        fun bind(result: ClassificationResult){
            binding.result = result
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()

        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<ClassificationResult>() {
        override fun areItemsTheSame(oldItem: ClassificationResult, newItem: ClassificationResult): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ClassificationResult, newItem: ClassificationResult): Boolean {
            return oldItem.topNClassName == newItem.topNClassName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ClassificationResultViewHolder {
        return ClassificationResultViewHolder(DisplayResultBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ClassificationResultViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)
    }


}