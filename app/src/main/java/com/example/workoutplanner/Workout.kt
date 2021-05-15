package com.example.workoutplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.workoutplanner.databinding.FragmentWorkoutBinding
import com.google.firebase.firestore.FirebaseFirestore

class Workout : Fragment() {
    private lateinit var binding: FragmentWorkoutBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout,container, false)





        return binding.root
    }

}