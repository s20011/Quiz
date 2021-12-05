package com.example.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quiz.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val correct = intent.getIntExtra("CORRECT", 0) + 1
        val countTime = intent.getIntExtra("COUNT_TIME", 0)
        val mode = intent.getLongExtra("MODE",0)

        val rank = if (correct == 10) {
            binding.imageView2.setImageResource(R.drawable.s)
            "S"
        } else if (8 == correct || correct == 9) {
            binding.imageView2.setImageResource(R.drawable.a)
            "A"
        } else if (5 <= correct && correct <= 7) {
            binding.imageView2.setImageResource(R.drawable.b)
            "B"
        } else if (1 <= correct && correct <= 4) {
            binding.imageView2.setImageResource(R.drawable.c)
            "C"
        } else{
            binding.imageView2.setImageResource(R.drawable.d)
            "D"
        }

        val modemsg = when(mode){
            11L -> "ノーマルモード"
            6L -> "5秒モード"
            else -> ""
        }

        binding.correct.text = "10問中${correct}正解"
        binding.time.text = "${countTime}秒"
        binding.rank.text = rank
        binding.mode.text = modemsg

        binding.main.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }




}