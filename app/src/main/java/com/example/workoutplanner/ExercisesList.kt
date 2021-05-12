package com.example.workoutplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.workoutplanner.databinding.FragmentExercisesListBinding


class ExercisesList : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentExercisesListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercises_list, container, false)
//        Toast.makeText(context, arguments?.getString("muscle"), Toast.LENGTH_SHORT).show()
        return binding.root
    }

}