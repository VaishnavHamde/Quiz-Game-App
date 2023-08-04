package com.example.quizgame

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.quizgame.databinding.ActivityLogInBinding
import com.example.quizgame.databinding.ActivityWelcomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.security.auth.callback.Callback

class LogInActivity : AppCompatActivity() {

    lateinit var logInBinding : ActivityLogInBinding
    lateinit var googleSignInClient : GoogleSignInClient
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logInBinding = ActivityLogInBinding.inflate(layoutInflater)
        val view = logInBinding.root
        setContentView(view)

        val textOfGoogleButton = logInBinding.buttonGoogleSignIn.getChildAt(0) as TextView
        textOfGoogleButton.text = "Continue with Google"
        textOfGoogleButton.setTextColor(Color.BLACK)
        textOfGoogleButton.textSize = 18F

        registerActivityForGoogleSignIn()

        logInBinding.buttonSignIn.setOnClickListener {
            val userEmail = logInBinding.editTextEmail.text.toString()
            val userPassword = logInBinding.editTextLogInPassword.text.toString()

            if(userEmail == "" || userPassword == "")
                Toast.makeText(this@LogInActivity, "Please enter the username and password", Toast.LENGTH_LONG).show()
            else
                signInUser(userEmail, userPassword)
        }
        logInBinding.buttonGoogleSignIn.setOnClickListener{
            signInGoogle()
        }
        logInBinding.textViewSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        logInBinding.textViewForgotPassword.setOnClickListener {
            val intent = Intent(this@LogInActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()

        val user = auth.currentUser

        if(user != null){
            Toast.makeText(this@LogInActivity, "Welcome to Quiz Game", Toast.LENGTH_LONG).show()
            val intent = Intent(this@LogInActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerActivityForGoogleSignIn(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {result ->
            val resultCode =  result.resultCode
            val data = result.data

            if(resultCode == RESULT_OK && data != null){
                val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                firebaseSignInWithGoogle(task)
            }
        })
    }

    fun signInUser(email : String, password : String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(this@LogInActivity, "Welcome to Quiz Game", Toast.LENGTH_LONG).show()
                val intent = Intent(this@LogInActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
                Toast.makeText(this@LogInActivity,task.exception?.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }

    private fun signInGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("526671553549-5tue2tev6mambe75nnl57gtv7phl0ppg.apps.googleusercontent.com")
            .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this@LogInActivity, gso)

        signIn()
    }

    private fun signIn(){
        val signInIntent : Intent = googleSignInClient.signInIntent

        activityResultLauncher.launch(signInIntent)
    }

    private fun firebaseSignInWithGoogle(task : Task<GoogleSignInAccount>){
        try{
            val account : GoogleSignInAccount = task.getResult(ApiException::class.java)
            Toast.makeText(this@LogInActivity, "Welcome to Quiz Game", Toast.LENGTH_LONG).show()

            val intent = Intent(this@LogInActivity, MainActivity::class.java)
            startActivity(intent)

            finish()

            firebaseGoogleAccount(account)
        }
        catch(e : ApiException){
            Toast.makeText(this@LogInActivity, e.localizedMessage?.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun firebaseGoogleAccount(account : GoogleSignInAccount){
        val authCredential = GoogleAuthProvider.getCredential(account.idToken, null)

        auth.signInWithCredential(authCredential)
    }
}