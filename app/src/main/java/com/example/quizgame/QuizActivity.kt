package com.example.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.quizgame.databinding.ActivityQuizBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuizActivity : AppCompatActivity() {

    lateinit var quizBinding : ActivityQuizBinding

    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference.child("quistions")

    var question = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""
    var correctAnswer = ""
    var questionCount = 0
    var questionNumber = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = ActivityQuizBinding.inflate(layoutInflater)
        val view = quizBinding.root
        setContentView(view)

        gameLogic()

        quizBinding.buttonNext.setOnClickListener {
            gameLogic()
        }
        quizBinding.buttonFinish.setOnClickListener {

        }

        quizBinding.textViewA.setOnClickListener {

        }
        quizBinding.textViewB.setOnClickListener {

        }
        quizBinding.textViewC.setOnClickListener {

        }
        quizBinding.textViewD.setOnClickListener {

        }

    }

        fun gameLogic(){
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

}