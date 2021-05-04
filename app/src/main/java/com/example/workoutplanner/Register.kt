package com.example.workoutplanner

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.workoutplanner.databinding.FragmentLoginBinding
import com.example.workoutplanner.databinding.FragmentRegisterBinding

class Register : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRegisterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.goToLogin.setOnClickListener{view :View ->
            Navigation.findNavController(view).navigate(R.id.action_register_to_login)
        }
        binding.loginButton.setOnClickListener(){
            //check credentials
            //if all good start mainactivity
            val intent = Intent(activity,MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        return binding.root
    }

}