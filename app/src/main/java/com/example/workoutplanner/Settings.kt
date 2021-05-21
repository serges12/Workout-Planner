package com.example.workoutplanner

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import com.example.workoutplanner.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlin.math.min

class Settings : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        val saveData = NotificationsSaveData((activity as MainActivity).applicationContext)
        binding.notificationTimePicker.text = String.format("%02d",saveData.getHour()) + ":" + String.format("%02d",saveData.getMinute())
        binding.notificationTimePicker.setOnClickListener{
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                binding.notificationTimePicker.text = hour.toString()+":"+minute.toString()
                (activity as MainActivity).SetTime(hour, minute)
            }
            val timePickerDialog = TimePickerDialog(context,timeSetListener,saveData.getHour(),saveData.getMinute(),true)
            timePickerDialog.show()
        }

        binding.logOutButton.setOnClickListener(){
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder
                .setTitle("Log Out?")
                .setMessage("You are about to be logged out. Are you sure you want to proceed?")
                .setNegativeButton("No"){
                    dialogInterface, _ ->
                        dialogInterface.cancel()
                }
                .setPositiveButton("Yes"){
                    _, _ ->
                    //Handle Logout here
                    FirebaseAuth.getInstance().signOut()
                    
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