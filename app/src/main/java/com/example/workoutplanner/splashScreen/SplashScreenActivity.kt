package com.example.workoutplanner.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.workoutplanner.MainActivity
import com.example.workoutplanner.R
import com.example.workoutplanner.authentication.LoginRegisterActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        this.supportActionBar?.hide()
            Handler().postDelayed({
                val intent = Intent(this, LoginRegisterActivity::class.java)
                startActivity(intent)
//                overridePendingTransition(0, R.anim.slide_up)
            this?.finish()
            },2000
            )
    }
}