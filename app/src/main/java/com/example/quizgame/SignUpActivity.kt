package com.example.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.quizgame.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sign

class SignUpActivity : AppCompatActivity() {

    lateinit var signUpBinding : ActivitySignUpBinding

    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = signUpBinding.root
        setContentView(view)

        signUpBinding.buttonSignUp.setOnClickListener {
            val email = signUpBinding.editTextSignUpEmail.text.toString()
            val password = signUpBinding.editTextLogUpPassword.text.toString()

            if(email == "" || password == "")
                Toast.makeText(this@SignUpActivity, "Please enter the username and password", Toast.LENGTH_LONG).show()
            else
                signUpWithFirebase(email, password)
        }
    }

    fun signUpWithFirebase(email : String, password : String){
        signUpBinding.progressBarSignUp.visibility = View.VISIBLE
        signUpBinding.buttonSignUp.isClickable = false

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task ->
            if(task.isSuccessful){
                Toast.makeText(this@SignUpActivity, "Your account has been created", Toast.LENGTH_LONG).show()
                signUpBinding.progressBarSignUp.visibility = View.INVISIBLE
                signUpBinding.buttonSignUp.isClickable = true
                finish()
            }
            else{
                Toast.makeText(this@SignUpActivity, task.exception?.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}