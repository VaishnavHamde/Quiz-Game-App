package com.example.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quizgame.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var forgotBinding : ActivityForgotPasswordBinding

    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = forgotBinding.root
        setContentView(view)

        forgotBinding.buttonForgot.setOnClickListener {
            val email = forgotBinding.editTextForgot.text.toString()

            if(email == "")
                Toast.makeText(this@ForgotPasswordActivity, "Please enter the email", Toast.LENGTH_LONG).show()
            else
                auth.sendPasswordResetEmail(email).addOnCompleteListener {task ->
                    if(task.isSuccessful){
                        Toast.makeText(this@ForgotPasswordActivity, "We sent a password reset mail to your email address", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    else{
                        Toast.makeText(this@ForgotPasswordActivity, task.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}