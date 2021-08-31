package com.example.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quiz.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val correct = intent.getIntExtra("CORRECT", 0)
        val countTime = intent.getIntExtra("COUNT_TIME", 0)

        val rank = if (correct == 10) {
            "S"
        } else if (8 == correct || correct == 9) {
            "A"
        } else if (5 <= correct && correct <= 7) {
            "B"
        } else if (1 <= correct && correct <= 4) {
            "C"
        } else "D"

        binding.correct.text = "正解した問題数：10問中${correct.toString()}正解"
        binding.time.text = "かかった時間：${countTime.toString()}秒"
        binding.rank.text = "RANK: ${rank}"

    }




}