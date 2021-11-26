package com.example.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.quiz.databinding.ActivityTestBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    //private val quizeService by lazy { createService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //fetchQuizeData()

        val question = intent.getStringExtra("question")
        Log.d("intent", "getIntent")
        binding.testText.text = question
    }

    /*private fun fetchQuizeData() {
        quizeService.getQuizeProperties().enqueue(
            object: Callback<List<Quize>> {
                override fun onFailure(call: Call<List<Quize>>, t: Throwable) {

                }

                override fun onResponse(call: Call<List<Quize>>, response: Response<List<Quize>>) {
                    val quizeDatas = response.body()
                    binding.testText.text = quizeDatas!![0].choices[0].toString()

                }
            }
        )
    }*/


}