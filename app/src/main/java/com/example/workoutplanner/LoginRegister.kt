package com.example.workoutplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.workoutplanner.databinding.ActivityLoginRegisterBinding

class LoginRegister : AppCompatActivity() {
    private lateinit var binding: ActivityLoginRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_register)
    }

}