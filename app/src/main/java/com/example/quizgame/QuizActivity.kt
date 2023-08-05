package com.example.quizgame

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.example.quizgame.databinding.ActivityQuizBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuizActivity : AppCompatActivity() {

    lateinit var quizBinding : ActivityQuizBinding
    lateinit var timer : CountDownTimer

    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference.child("quistions")
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val scoreRef = database.reference

    private val totalTime = 25000L

    var question = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""
    var correctAnswer = ""
    var questionCount = 0
    var questionNumber = 1
    var userAnswer = ""
    var userCorrect = 0
    var userWrong = 0
    var timerContinue = false
    var leftTime = totalTime



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = ActivityQuizBinding.inflate(layoutInflater)
        val view = quizBinding.root
        setContentView(view)

        gameLogic()

        quizBinding.buttonNext.setOnClickListener {
            if(questionNumber == questionCount+1)
                sendScore()

            gameLogic()
            resetTimer()
            updateCountDownText()
        }
        quizBinding.buttonFinish.setOnClickListener {
            sendScore()
        }

        quizBinding.textViewA.setOnClickListener {
            userAnswer = "a"

            pauseTimer()

            if(correctAnswer == userAnswer){
                quizBinding.textViewA.setBackgroundColor(Color.GREEN)
                userCorrect++

                quizBinding.textViewCorrect.text = userCorrect.toString()
            }
            else{
                quizBinding.textViewA.setBackgroundColor(Color.RED)
                userWrong++

                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOfOptions()
        }
        quizBinding.textViewB.setOnClickListener {
            userAnswer = "b"

            pauseTimer()

            if(correctAnswer == userAnswer){
                quizBinding.textViewB.setBackgroundColor(Color.GREEN)
                userCorrect++

                quizBinding.textViewCorrect.text = userCorrect.toString()
            }
            else{
                quizBinding.textViewB.setBackgroundColor(Color.RED)
                userWrong++

                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOfOptions()
        }
        quizBinding.textViewC.setOnClickListener {
            userAnswer = "c"

            pauseTimer()

            if(correctAnswer == userAnswer){
                quizBinding.textViewC.setBackgroundColor(Color.GREEN)
                userCorrect++

                quizBinding.textViewCorrect.text = userCorrect.toString()
            }
            else{
                quizBinding.textViewC.setBackgroundColor(Color.RED)
                userWrong++

                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOfOptions()
        }
        quizBinding.textViewD.setOnClickListener {
            userAnswer = "d"

            pauseTimer()

            if(correctAnswer == userAnswer){
                quizBinding.textViewD.setBackgroundColor(Color.GREEN)
                userCorrect++

                quizBinding.textViewCorrect.text = userCorrect.toString()
            }
            else{
                quizBinding.textViewD.setBackgroundColor(Color.RED)
                userWrong++

                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

            disableClickableOfOptions()
        }

    }

        fun gameLogic(){
            restoreOptions()

            databaseReference.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    questionCount = snapshot.childrenCount.toInt()

                    if(questionNumber <= questionCount){
                        question = snapshot.child("" + questionNumber).child("q").value.toString()
                        answerA = snapshot.child("" + questionNumber).child("a").value.toString()
                        answerB = snapshot.child("" + questionNumber).child("b").value.toString()
                        answerC = snapshot.child("" + questionNumber).child("c").value.toString()
                        answerD = snapshot.child("" + questionNumber).child("d").value.toString()
                        correctAnswer = snapshot.child("" + questionNumber).child("answer").value.toString()

                        quizBinding.textViewQuestion.text = question
                        quizBinding.textViewA.text = answerA
                        quizBinding.textViewB.text = answerB
                        quizBinding.textViewC.text = answerC
                        quizBinding.textViewD.text = answerD

                        quizBinding.progressBarQuiz.visibility = View.INVISIBLE
                        quizBinding.linearLayoutInfo.visibility = View.VISIBLE
                        quizBinding.linearLayoutQuestion.visibility = View.VISIBLE
                        quizBinding.linearLayoutButtons.visibility = View.VISIBLE

                        startTimer()
                    }
                    else{
                        Toast.makeText(this@QuizActivity, "You answered all the question", Toast.LENGTH_LONG).show()
                    }

                    questionNumber++
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@QuizActivity, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }


    fun findAnswer(){
        when(correctAnswer){
            "a" -> quizBinding.textViewA.setBackgroundColor(Color.GREEN)
            "b" -> quizBinding.textViewB.setBackgroundColor(Color.GREEN)
            "c" -> quizBinding.textViewC.setBackgroundColor(Color.GREEN)
            "d" -> quizBinding.textViewD.setBackgroundColor(Color.GREEN)
        }
    }

    fun disableClickableOfOptions(){
        quizBinding.textViewA.isClickable = false
        quizBinding.textViewB.isClickable = false
        quizBinding.textViewC.isClickable = false
        quizBinding.textViewD.isClickable = false
    }

    fun restoreOptions(){
        quizBinding.textViewA.setBackgroundColor(Color.WHITE)
        quizBinding.textViewB.setBackgroundColor(Color.WHITE)
        quizBinding.textViewC.setBackgroundColor(Color.WHITE)
        quizBinding.textViewD.setBackgroundColor(Color.WHITE)

        quizBinding.textViewA.isClickable = true
        quizBinding.textViewB.isClickable = true
        quizBinding.textViewC.isClickable = true
        quizBinding.textViewD.isClickable = true
    }

    private fun startTimer(){
        timer = object : CountDownTimer(leftTime, 1000){
            override fun onTick(millisUntilFinished: Long) {
                leftTime = millisUntilFinished

                updateCountDownText()
            }

            override fun onFinish() {
                quizBinding.textViewQuestion.text = "Sorry, time is up! Continue with the next question"
                timerContinue = false
                pauseTimer()
                disableClickableOfOptions()
            }
        }.start()

        timerContinue = true
    }

    fun updateCountDownText(){
        val remainingTime : Int = (leftTime/1000).toInt()

        quizBinding.textViewTime.text = remainingTime.toString()
    }

    fun pauseTimer(){
        timer.cancel()
        timerContinue = false
    }

    fun resetTimer(){
        pauseTimer()
        leftTime = totalTime

        updateCountDownText()
    }

    fun sendScore(){
        user?.let {
            val userUID = it.uid

            scoreRef.child("scores").child(userUID).child("correct").setValue(userCorrect)
            scoreRef.child("scores").child(userUID).child("wrong").setValue(userWrong).addOnSuccessListener {

                val intent = Intent(this@QuizActivity, ResultActivity::class.java)
                startActivity(intent)

                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this@QuizActivity, MainActivity::class.java)
        startActivity(intent)
    }
}