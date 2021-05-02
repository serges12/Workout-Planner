package com.example.workoutplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.workoutplanner.databinding.FragmentWorkoutsBinding

class Workouts : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentWorkoutsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_workouts, container, false)
        binding.addWorkoutButton.setOnClickListener{
            Toast.makeText(context,"Create Workout",Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

}