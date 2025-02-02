package com.example.mathgame

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mathgame.databinding.ActivityGameBinding
import com.example.mathgame.databinding.ActivityMainBinding
import java.util.Locale
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    lateinit var gameBinding: ActivityGameBinding

    var correctAns = 0
    var userScore = 0
    var userLife = 3

    // Các biến cho cơ chế đếm ngược (Count down timer)
    lateinit var cdt: CountDownTimer
    private var startTimerInMillis: Long = 61000
    var timeLeftInMillis: Long = startTimerInMillis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        gameBinding = ActivityGameBinding.inflate(layoutInflater)
        val view = gameBinding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar!!.title = intent.getStringExtra("title")

        gameContinue()

        gameBinding.btnOk.setOnClickListener {
            val inp = gameBinding.etAnswer.text.toString()

            if ( inp == "" ) {
                Toast.makeText(this@GameActivity, "Please enter your answer or click the next button !", Toast.LENGTH_SHORT).show()
            }
            else {

                pauseTimer()

                val userAnswer = inp.toInt()

                if ( userAnswer == correctAns ) {
                    userScore += 10
                    gameBinding.tvQuestion.text = "Correct !"
                    gameBinding.vlScore.text = userScore.toString()
                    waitNextQuestion()
                }
                else {
                    userLife--
                    gameBinding.tvQuestion.text = "Wrong answer !"
                    gameBinding.vlLife.text = userLife.toString()
                    if (userLife <= 0) {
                        val intent = Intent(this@GameActivity, ResultActivity::class.java)
                        intent.putExtra("score", userScore)
                        startActivity(intent)
                        finish()
                    }
                    waitNextQuestion()
                }
            }
        }

        gameBinding.btnNext.setOnClickListener {
            pauseTimer()
            resetTimer()
            gameBinding.etAnswer.setText("")

            if (userLife <= 0) {
                val intent = Intent(this@GameActivity, ResultActivity::class.java)
                intent.putExtra("score", userScore)
                startActivity(intent)
                finish()
            }
            else {
                gameContinue()
            }
        }
    }

    fun gameContinue() {
        gameBinding.tvWait.visibility = View.INVISIBLE

        val number1 = Random.nextInt(0, 100)
        val number2 = Random.nextInt(0, 100)

        correctAns = number1 + number2
        gameBinding.tvQuestion.text = "$number1 + $number2"

        startTimer()
    }

    fun startTimer() {
        cdt = object: CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateText()
            }

            override fun onFinish() {
                pauseTimer()
                resetTimer()
                updateText()

                userLife--
                gameBinding.vlLife.text = userLife.toString()
                gameBinding.tvQuestion.text = "Your time is up !"
            }

        }.start()
    }

    fun updateText() {
        val remainingTime: Int = (timeLeftInMillis /1000).toInt()
        gameBinding.vlTime.text = remainingTime.toString()
//        gameBinding.vlTime.text = String.format(Locale.getDefault(), "%02d", remainingTime)
    }

    fun pauseTimer() {
        cdt.cancel()
    }

    fun resetTimer() {
        timeLeftInMillis = startTimerInMillis
        updateText()
    }

    fun waitNextQuestion() {
        gameBinding.tvWait.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            pauseTimer()
            resetTimer()
            gameContinue()
            gameBinding.etAnswer.setText("")
        }, 2000)
    }

}