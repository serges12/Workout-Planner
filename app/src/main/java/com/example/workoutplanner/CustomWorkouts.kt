package com.example.workoutplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.workoutplanner.databinding.FragmentCustomWorkoutsBinding
import com.example.workoutplanner.databinding.FragmentWorkoutsBinding

class CustomWorkouts : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCustomWorkoutsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_custom_workouts, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Custom Workouts"
        return binding.root
    }

}