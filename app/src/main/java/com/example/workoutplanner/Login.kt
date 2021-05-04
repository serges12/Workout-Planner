package com.example.workoutplanner

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.workoutplanner.databinding.FragmentHistoryBinding
import com.example.workoutplanner.databinding.FragmentLoginBinding


class Login : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.goToLogin.setOnClickListener{view :View ->
            Navigation.findNavController(view).navigate(R.id.action_login_to_register)
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