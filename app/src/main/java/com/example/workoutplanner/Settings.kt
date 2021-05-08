package com.example.workoutplanner

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.workoutplanner.databinding.FragmentSettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Settings : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.logOutButton.setOnClickListener(){
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder
                .setTitle("Log Out?")
                .setMessage("You are about to be logged out. Are you sure you want to proceed?")
                .setNegativeButton("No"){
                    dialogInterface, i ->
                        dialogInterface.cancel()
                }
                .setPositiveButton("Yes"){
                    dialogInterface, i ->
                    //Handle Logout here

                    
                    val intent = Intent(activity,LoginRegisterActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            val alert = dialogBuilder.create()
            alert.show()
        }
        return binding.root
    }

}