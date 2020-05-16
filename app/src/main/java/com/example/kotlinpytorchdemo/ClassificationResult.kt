package com.example.kotlinpytorchdemo


data class ClassificationResult(
    val topNClassName: String,
    val topNScore: Float
)