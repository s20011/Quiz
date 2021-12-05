package com.example.quiz

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiz.databinding.ActivityQuizeBinding

class QuizeActivity : AppCompatActivity() {
    private val helper = DatabaseHelper(this@QuizeActivity)
    private val quiz = arrayListOf<Quize>()

    private lateinit var binding: ActivityQuizeBinding
    var quizeCount = 0 // 何問目かの値を持っている
    var countTime = 0 // かかった時間
    var correct = 0 //正解した問題数
    var n: Long = 0
    val select = mutableListOf<String>() //ユーザーが選んだ答え
    val answer = mutableListOf<String>() //問題の答え

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mode = intent.getIntExtra("MODE", 11)
        binding.timerText.text = "${mode}"

        n = if(mode == 11){
            11L
        }else 6L

        createQuizList(dbCount())

        gameStart(quiz)

        binding.nextbt.setOnClickListener {
            Log.d("QuizActivity", "nextButton")
            Alert(quizeCount, answer, select)
            Log.d("QuizActivity", "uerSelect = ${println(select)}")
            quizeCount++
        }

    }

    val timer = object : CountDownTimer(11 * 1000, 1000){
        override fun onTick(millisUntilFinished: Long) {
            val second = millisUntilFinished / 1000L
            binding.timerText.text = "%1d".format(second)
        }

        override fun onFinish() {
            binding.timerText.text = "0"
            if(quizeCount == 9){
                val time = 10 - binding.timerText.text.toString().toInt()
                countTime += time
                AlertDialog.Builder(this@QuizeActivity)
                    .setTitle("ゲーム終了")
                    .setMessage("＊＊＊＊＊＊＊＊＊＊")
                    .setPositiveButton("結果画面", { dialog, which ->
                        resActivity()
                    })
                    .show()
            }else{
                val time = 10 - binding.timerText.text.toString().toInt()
                countTime += time
                quizeCount++
                Log.d("mode=", "${n}")
                AlertDialog.Builder(this@QuizeActivity)
                    .setTitle("結果")
                    .setMessage("不正解　✕")
                    .setPositiveButton("次へ", { dialog, which ->
                        gameStart(quiz)
                    })
                    .show()
            }
        }
    }

    val timer_6s = object : CountDownTimer(6 * 1000, 1000){
        override fun onTick(millisUntilFinished: Long) {
            val second = millisUntilFinished / 1000L
            binding.timerText.text = "%1d".format(second)
        }

        override fun onFinish() {
            binding.timerText.text = "0"
            if(quizeCount == 9){
                val time = 5 - binding.timerText.text.toString().toInt()
                countTime += time
                AlertDialog.Builder(this@QuizeActivity)
                    .setTitle("ゲーム終了")
                    .setMessage("＊＊＊＊＊＊＊＊＊＊")
                    .setPositiveButton("結果画面", { dialog, which ->
                        resActivity()
                    })
                    .show()
            }else{
                val time = 5 - binding.timerText.text.toString().toInt()
                countTime += time
                quizeCount++
                Log.d("mode=", "${n}")
                AlertDialog.Builder(this@QuizeActivity)
                    .setTitle("結果")
                    .setMessage("不正解　✕")
                    .setPositiveButton("次へ", { dialog, which ->
                        gameStart(quiz)
                    })
                    .show()
            }
        }
    }

    //クイズをスタートする
    fun gameStart(quiz: ArrayList<Quize>){
        binding.quize.text = "${quizeCount + 1}問目：　${quiz[quizeCount].question}"
        select.clear()
        answer.clear()

        val choices: MutableList<String> = mutableListOf()
        for(i in quiz[quizeCount].choices){
            if(i != ""){
                choices.add(i)
            }
        }

        val answers = quiz[quizeCount].answers
        if(answers == 1){
            answer.add(choices[0])
        }else {
            answer.let {
                it.add(choices[0])
                it.add(choices[1])
            }

        }

        choices.shuffle()

        Log.d("QuizActivity", "question = ${quiz[quizeCount].question}")
        Log.d("QuizActivity", "answer = ${println(answer)}")
        Log.d("QuizActivity", "choices = ${println(choices)}")


        val adapter = RecyclerAdapter(choices)
        val manager = LinearLayoutManager(this@QuizeActivity)
        //RecyclerViewのクリック処理
        adapter.itemClickListener = object : RecyclerAdapter.OnItemClickListener{
            override fun onItemClick(holder: RecyclerAdapter.ViewHolder) {
                //val position = holder.bindingAdapterPosition
                val choice = holder.choiceView.text.toString()
                if(choice in select){
                    holder.row.setBackgroundColor(Color.WHITE)
                    select.remove(choice)
                    Log.d("QuzieActivity", "select = ${println(select)}")
                }else if(select.size == quiz[quizeCount].answers){
                    if(choice in select){
                        holder.row.setBackgroundColor(Color.WHITE)
                        select.remove(choice)
                        Log.d("QuzieActivity", "select = ${println(select)}")
                    }else {
                        Log.d("QuizeActivity", "msg = can not Select")
                        Log.d("QuzieActivity", "select = ${println(select)}")
                    }
                }else {
                    holder.row.setBackgroundColor(Color.RED)
                    select.add(choice)
                    Log.d("QuzieActivity", "select = ${println(select)}")
                }
            }
        }

        binding.lvMenu.let {
            it.adapter = adapter
            manager.orientation = LinearLayoutManager.VERTICAL
            it.layoutManager = manager
        }


        timerStart(n)
    }


    //ダイアログを表示する
    private fun Alert(count:Int,Answer:MutableList<String>,select: MutableList<String>){
        timerCansel(n) //タイマーを止める
        val limit = n.toInt() - 1


        var corNum = 0
        for(i in Answer){
            if(i in select){
                corNum++
            }
        }

        val time = limit - binding.timerText.text.toString().toInt() // かかった時間
        countTime += time
        if(count == 9){
            AlertDialog.Builder(this)
                .setTitle("ゲーム終了")
                .setMessage("＊＊＊＊＊＊＊＊＊＊")
                .setPositiveButton("結果画面", { _, _ ->
                    resActivity()
                })
                .show()

        }else if(corNum == Answer.size){
            AlertDialog.Builder(this@QuizeActivity)
                .setTitle("結果")
                .setMessage("正解　◎")
                .setPositiveButton("次へ", { dialog, which ->
                    gameStart(quiz)
                }).show()

        }else {
            AlertDialog.Builder(this@QuizeActivity)
                .setTitle("結果")
                .setMessage("不正解　✕")
                .setPositiveButton("次へ", { dialog, which ->
                    gameStart(quiz)
                }).show()
        }



    }

    fun resActivity(){
        val intent = Intent(this@QuizeActivity,ResultActivity::class.java)
        intent.putExtra("COUNT_TIME",countTime)
        intent.putExtra("CORRECT",correct)
        intent.putExtra("MODE",n)

        startActivity(intent)
        finish()

    }

    fun timerStart(n:Long){
        when(n){
            11L -> timer.start()
            6L -> timer_6s.start()
        }
    }

    fun timerCansel(n:Long){
        when(n){
            11L -> timer.cancel()
            6L -> timer_6s.cancel()
        }
    }

    private fun createQuizList(n:Int){
        val db = helper.writableDatabase
        val randomList = mutableListOf<Int>()

        while (randomList.size < 10) {
            val random = (1..n).random()
            randomList.add(random)
        }

        Log.d("QuizActivity", "randomList = ${println(randomList)}")

        for(i in randomList){
            val sqlSelect = "SELECT * FROM quiz WHERE _id = ${i}"
            val cursor = db.rawQuery(sqlSelect, null)
            while(cursor.moveToNext()){
                cursor.let {
                    quiz.add(
                        Quize(
                            id = it.getInt(it.getColumnIndex("_id")),
                            question = it.getString(it.getColumnIndex("question")),
                            answers = it.getInt(it.getColumnIndex("answers")),
                            choices = mutableListOf<String>(
                                it.getString(it.getColumnIndex("choicei")),
                                it.getString(it.getColumnIndex("choiceii")),
                                it.getString(it.getColumnIndex("choiceiii")),
                                it.getString(it.getColumnIndex("choiceiv")),
                                it.getString(it.getColumnIndex("choicev")),
                                it.getString(it.getColumnIndex("choicevi"))
                            )
                        )
                    )
                }
            }
            cursor.close()
        }

        Log.d("QuizActivity", "quiz = ${println(quiz)}")
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
            Log.d("QuizActivity", "COUNT = ${cursor.getInt(index)}")
            }
        }
        cursor.close()

        return count
    }



}

