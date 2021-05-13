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
        val exerciseID = ExerciseArgs.fromBundle(requireArguments()).exerciseID
        db.collection("exercises").document(exerciseID)
            .get()
            .addOnSuccessListener {
                val exerciseRef = it.toObject(ExerciseModel::class.java)
                (activity as AppCompatActivity).supportActionBar?.title = exerciseRef?.name
                binding.exerciseDescriptionText.setText(exerciseRef?.description)
                //here the paramter "it" is an ExerciseModel Objects, and you can access its attributes
                //see ExerciseModel.kt to check available attributes (name, description, videoLink, imageLink, bodyPart)
                binding.bodyPartText.text = exerciseRef?.bodyPart.toString()
                binding.imageView.load(exerciseRef?.imageLink)
                binding.exerciseDescriptionText.movementMethod = ScrollingMovementMethod()
                //do work here
            }
        return binding.root
    }

}