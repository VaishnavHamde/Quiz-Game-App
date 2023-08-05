package com.example.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.example.quizgame.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    lateinit var splashBinding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = splashBinding.root
        setContentView(view)

        val alphaAnimation = AnimationUtils.loadAnimation(this@WelcomeActivity, R.anim.splash_anim)
        splashBinding.textViewSplash.startAnimation(alphaAnimation)

        val handller = Handler(Looper.getMainLooper())

        handller.postDelayed(object : Runnable {
            override fun run() {
                val intent = Intent(this@WelcomeActivity, LogInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 1000)
    }
}