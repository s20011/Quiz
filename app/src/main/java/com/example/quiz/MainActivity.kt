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
    private val quizeService by lazy { createService() }
    private val helper = DatabaseHelper(this@MainActivity)
    private lateinit var quize: Array<Quize>
    private val testId = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchQuizeData()

        val db = helper.writableDatabase
        val sql = "SELECT * FROM quize WHERE _id = ${testId}"
        val cursor = db.rawQuery(sql, null)
        var question = ""
        while (cursor.moveToNext()) {
            val index = cursor.getColumnIndex("question")
            question = cursor.getString(index)
        }
        cursor.close()

        binding.startBt.setOnClickListener {
            quizeActivity(11)
        }

        binding.second5.setOnClickListener {
            quizeActivity(6)
        }

        binding.test.setOnClickListener {
            Log.d("testBotton", "testBottonPush")
            val testActivity = Intent(this@MainActivity, TestActivity::class.java)
            testActivity.putExtra("question", question)
            startActivity(testActivity)
            finish()
        }
    }

    override fun onDestroy() {
        helper.close()
        super.onDestroy()
    }

    fun quizeActivity(select: Int){
        val mode = select
        val intent = Intent(this@MainActivity, QuizeActivity::class.java)
        intent.putExtra("MODE",mode)
        intent.putExtra(QuizeActivity.KEY_STATE, quize)
        startActivity(intent)
        finish()
    }

    private fun fetchQuizeData() {
        quizeService.getQuizeProperties().enqueue(
            object: Callback<List<Quize>> {
                override fun onFailure(call: Call<List<Quize>>, t: Throwable) {

                }

                override fun onResponse(call: Call<List<Quize>>, response: Response<List<Quize>>) {
                    insertSql(response)

                }
            }
        )
    }

    fun insertSql(response: Response<List<Quize>>){
        val quizeDates = response.body()

        val insert = """INSERT INTO quize (
            _id, question, answers, 
            choicei, choiceii, choiceiii, choiceiv, choicev, choicevi
            ) VALUES (?,?,?,?,?,?,?,?,?)""".trimIndent()
        val stmt = helper.writableDatabase.compileStatement(insert)
        for (i in 0..quizeDates!!.size - 1){
            stmt.let {
                it.bindLong(1, (i + 1).toLong())
                it.bindLong(3, quizeDates[i].answers.toLong())
                it.bindString(2, quizeDates[i].question.toString())
                it.bindString(4, quizeDates[i].choices[0].toString())
                it.bindString(5, quizeDates[i].choices[1].toString())
                it.bindString(6, quizeDates[i].choices[2].toString())
                it.bindString(7, quizeDates[i].choices[3].toString())
                it.bindString(8, quizeDates[i].choices[4].toString())
                it.bindString(9, quizeDates[i].choices[5].toString())

                it.executeInsert()
            }
        }

    }


}