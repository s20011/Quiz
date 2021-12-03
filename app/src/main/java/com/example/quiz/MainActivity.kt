package com.example.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.quiz.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val quizService by lazy { createService() }
    private val helper = DatabaseHelper(this@MainActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("MainActivity", "onCreate")

        val count = dbCount()
        if (count == 0){
            Log.d("MainActivity", "First Fetch")
            fetchQuizData()
        }

        binding.startBt.setOnClickListener {
            quizActivity(11)
        }

        binding.second5.setOnClickListener {
            quizActivity(6)
        }

        binding.test.setOnClickListener {
            val testActivity = Intent(this@MainActivity, TestActivity::class.java)
            startActivity(testActivity)

        }


    }

    override fun onDestroy() {
        helper.close()
        super.onDestroy()
    }

    private fun quizActivity(select: Int){
        val intent = Intent(this@MainActivity, QuizeActivity::class.java)
        intent.putExtra("MODE",select)
        startActivity(intent)
        finish()
    }

    private fun fetchQuizData() {
        Log.d("MainActivity", "start fetchQuizData")
        quizService.getQuizProperties().enqueue(
            object: Callback<List<Quize>> {
                override fun onFailure(call: Call<List<Quize>>, t: Throwable) {
                    Log.d("MainActivity", "API = $t")
                }

                override fun onResponse(call: Call<List<Quize>>, response: Response<List<Quize>>) {
                    Log.d("MainActivity", "start onResponse")
                    insertSql(response)

                }
            }
        )
    }

    private fun insertSql(response: Response<List<Quize>>?){
        Log.d("MainActivity", "start insertSql")
        val quizDates = response!!.body()

        val db = helper.writableDatabase
        val sqlDelete = "DELETE FROM quiz"
        val stmtDel = db.compileStatement(sqlDelete)
        stmtDel.executeUpdateDelete()

        val insert = """INSERT INTO quiz (
            _id, question, answers, 
            choicei, choiceii, choiceiii, choiceiv, choicev, choicevi
            ) VALUES (?,?,?,?,?,?,?,?,?)""".trimIndent()
        val stmt = db.compileStatement(insert)

        for(i in 0..quizDates!!.size - 1){
            stmt.bindLong(1, (i + 1).toLong())
            stmt.bindString(2, quizDates[i].question)
            stmt.bindLong(3, quizDates[i].answers.toLong())
            val choices = quizDates[i].choices
            stmt.bindString(4, choices[0])
            stmt.bindString(5, choices[1])
            stmt.bindString(6, choices[2])
            stmt.bindString(7, choices[3])
            stmt.bindString(8, choices[4])
            stmt.bindString(9, choices[5])
            stmt.executeInsert()
        }

        Log.d("MainActivity", "Quiz = ${quizDates.size}")
    }

    private fun dbCount():Int {
        val db = helper.readableDatabase
        val sqlCount = "SELECT COUNT(_id) AS count FROM quiz"
        val cursor = db.rawQuery(sqlCount, null)
        var count = 0
        if(cursor.moveToFirst()) {
            cursor.let {
                val index = cursor.getColumnIndex("count")
                count = cursor.getInt(index)
                Log.d("MainActivity", "COUNT = ${cursor.getInt(index)}")
            }
        }
        cursor.close()

        return count
    }



}