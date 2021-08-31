package com.example.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startBt.setOnClickListener {
            quizeActivity(11)
        }

        binding.second5.setOnClickListener {
            quizeActivity(6)
        }
    }

    fun quizeActivity(select: Int){
        val mode = select
        val intent = Intent(this@MainActivity, QuizeActivity::class.java)
        intent.putExtra("MODE",mode)
        startActivity(intent)
    }
}