package com.example.workoutplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.workoutplanner.databinding.FragmentCurrentWorkoutBinding
import com.example.workoutplanner.databinding.FragmentHistoryBinding

class CurrentWorkout : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCurrentWorkoutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_current_workout, container, false)
        return binding.root
    }

}