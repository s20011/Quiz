package com.example.quiz

data class Quize(
    val id: Int,
    val question: String,
    val answers: Int,
    val choices: List<String>
)
