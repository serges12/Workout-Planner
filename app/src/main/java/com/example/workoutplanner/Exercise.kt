package com.example.workoutplanner

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.load
import com.example.workoutplanner.databinding.FragmentExerciseBinding
import com.google.firebase.firestore.FirebaseFirestore


class Exercise : Fragment() {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentExerciseBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_exercise,container,false)
        val exercise: ExerciseModel = ExerciseArgs.fromBundle(requireArguments()).exercise
        //now we have access to the exercise
        //see ExerciseModel.kt to check available attributes (name, description, videoLink, imageLink, bodyPart)

        (activity as AppCompatActivity).supportActionBar?.title = exercise?.name
        binding.exerciseDescriptionText.setText(exercise?.description)

        binding.bodyPartText.text = exercise?.bodyPart.toString()
        binding.imageView.load(exercise?.imageLink)
        binding.exerciseDescriptionText.movementMethod = ScrollingMovementMethod()

        return binding.root
    }

}