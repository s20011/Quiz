package com.example.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.quiz.databinding.ActivityQuizeBinding
import com.opencsv.CSVIterator
import com.opencsv.CSVReader
import java.io.BufferedReader
import java.io.InputStreamReader

class QuizeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizeBinding

    var quizeCount = 0 // 何問目かの値を持っている
    var countTime = 0 // かかった時間
    var correct = 0 //正解した問題数
    val quizeList: MutableList<Array<String>> = mutableListOf()
    var n: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mode = intent.getIntExtra("MODE", 11)
        binding.timerText.text = "${mode.toString()}"

        n = if(mode == 11){
            11L
        }else 6L


        val fileReader = BufferedReader(InputStreamReader(assets.open("s20011.csv")))
        val csvIter = CSVIterator(CSVReader(fileReader))
        for(row in csvIter){
            if(row == null) break

            row.toString().split(",").toTypedArray()
            quizeList.add(row)
        }
        quizeList.removeAt(0)

        gameStart(quizeList)

        binding.s1.setOnClickListener {
            val answer = binding.s1.text.toString()
            Alert(quizeCount,answer,quizeList)
            quizeCount++

        }
        binding.s2.setOnClickListener {
            val answer = binding.s2.text.toString()
            Alert(quizeCount,answer,quizeList)
            quizeCount++
        }
        binding.s3.setOnClickListener {
            val answer = binding.s3.text.toString()
            Alert(quizeCount,answer,quizeList)
            quizeCount++
        }
        binding.s4.setOnClickListener {
            val answer = binding.s4.text.toString()
            Alert(quizeCount,answer,quizeList)
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
            var time: Int
            if(quizeCount == 9){
                time = 10 - binding.timerText.text.toString().toInt()
                countTime += time
                AlertDialog.Builder(this@QuizeActivity)
                    .setTitle("ゲーム終了")
                    .setMessage("＊＊＊＊＊＊＊＊＊＊")
                    .setPositiveButton("結果画面", { dialog, which ->
                        resActivity()
                    })
                    .show()
            }else{
                time = 10 - binding.timerText.text.toString().toInt()
                countTime += time
                quizeCount++
                Log.d("mode=", "${n}")
                AlertDialog.Builder(this@QuizeActivity)
                    .setTitle("結果")
                    .setMessage("不正解　✕")
                    .setPositiveButton("次へ", { dialog, which ->
                        gameStart(quizeList)
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
            var time: Int
            if(quizeCount == 9){
                time = 10 - binding.timerText.text.toString().toInt()
                countTime += time
                AlertDialog.Builder(this@QuizeActivity)
                    .setTitle("ゲーム終了")
                    .setMessage("＊＊＊＊＊＊＊＊＊＊")
                    .setPositiveButton("結果画面", { dialog, which ->
                        resActivity()
                    })
                    .show()
            }else{
                time = 10 - binding.timerText.text.toString().toInt()
                countTime += time
                quizeCount++
                Log.d("mode=", "${n}")
                AlertDialog.Builder(this@QuizeActivity)
                    .setTitle("結果")
                    .setMessage("不正解　✕")
                    .setPositiveButton("次へ", { dialog, which ->
                        gameStart(quizeList)
                    })
                    .show()
            }
        }
    }

    //クイズをスタートする
    fun gameStart(quize: MutableList<Array<String>>){
        binding.quize.text = "${quizeCount + 1}問目：　${quize[quizeCount][0]}"
        var choices: MutableList<String> = mutableListOf()
        for(i in 2..5){
            choices.add(quize[quizeCount][i])
        }

        if(quize[quizeCount][1] == ""){
            binding.quizeImage.setImageResource(R.drawable.quize)
        }

        choices.shuffle()
        binding.s1.text = choices[0]
        binding.s2.text = choices[1]
        binding.s3.text = choices[2]
        binding.s4.text = choices[3]


        timerStart(n)

        //val timer = MyCountDownTimer(11 * 1000, 100)
        //timer.start()

    }


    //ダイアログを表示する
    fun Alert(count:Int, Answer:String,quize: MutableList<Array<String>>){
        timerCansel(n) //タイマーを止める

        val time = 10 - binding.timerText.text.toString().toInt() // かかった時間
        countTime += time
        if(count == 9){
            AlertDialog.Builder(this)
                .setTitle("ゲーム終了")
                .setMessage("＊＊＊＊＊＊＊＊＊＊")
                .setPositiveButton("結果画面", { dialog, which ->
                    resActivity()
                })
                .show()
        }else if(Answer == quize[quizeCount][2]){
            correct++
            AlertDialog.Builder(this)
                .setTitle("結果")
                .setMessage("正解　◎")
                .setPositiveButton("次へ", { dialog, which ->
                    gameStart(quize)
                })
                .show()

        }else {
            AlertDialog.Builder(this)
                .setTitle("結果")
                .setMessage("不正解　✕")
                .setPositiveButton("次へ", { dialog, which ->
                    gameStart(quize)
                })
                .show()
        }
    }

    fun resActivity(){
        val intent = Intent(this@QuizeActivity,ResultActivity::class.java)
        intent.putExtra("COUNT_TIME",countTime)
        intent.putExtra("CORRECT",correct)
        intent.putExtra("MODE",n)
        startActivity(intent)

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

}

