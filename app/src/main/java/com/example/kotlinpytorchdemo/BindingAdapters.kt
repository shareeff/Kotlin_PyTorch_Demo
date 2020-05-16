package com.example.kotlinpytorchdemo

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView


@BindingAdapter("resultData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<ClassificationResult>?){
    val adapter = recyclerView.adapter as DisplayResultAdapter
    adapter.submitList(data)
}

@BindingAdapter("stringScore")
fun bindText(textView: TextView, score: Float?){
    textView.text = score.toString()
}