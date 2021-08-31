package com.example.quiz

import com.opencsv.bean.CsvBindByName

data class QuizeInfo(
    @CsvBindByName(column = "問題文", required = true)
    val quize: String = "",
    @CsvBindByName(column = "画像ファイル", required = true)
    val image: String = "",
    @CsvBindByName(column = "選択肢1", required = true)
    val correct: String = "",
    @CsvBindByName(column = "選択肢2", required = true)
    val an_1: String = "",
    @CsvBindByName(column = "選択肢3", required = true)
    val an_2: String = "",
    @CsvBindByName(column = "選択肢4", required = true)
    val an_3: String = ""
)
