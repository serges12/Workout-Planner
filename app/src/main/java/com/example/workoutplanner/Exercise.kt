package com.example.workoutplanner

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.load
import com.example.workoutplanner.databinding.FragmentExerciseBinding
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.google.firebase.firestore.FirebaseFirestore


class Exercise : Fragment() {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var binding : FragmentExerciseBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_exercise,container,false)
        val exercise: ExerciseModel = ExerciseArgs.fromBundle(requireArguments()).exercise
        //now we have access to the exercise
        //see ExerciseModel.kt to check available attributes (name, description, videoLink, imageLink, bodyPart)
        (activity as AppCompatActivity).supportActionBar?.title = exercise?.name
        binding.exerciseDescriptionText.setText(exercise?.description)
        binding.bodyPartText.text = exercise?.bodyPart.toString()
        binding.imageView.load(exercise?.imageLink)
        binding.exerciseDescriptionText.movementMethod = ScrollingMovementMethod()

        val youtubePlayerFragment = YouTubePlayerFragment()
        youtubePlayerFragment.initialize(getString(R.string.api_key),object: YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
                p1?.cueVideo(URLtoID(exercise?.videoLink.toString()))
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(context, "Error Occured!", Toast.LENGTH_SHORT).show()
            }

        })
        (activity as AppCompatActivity).fragmentManager.beginTransaction().replace(R.id.youtube_player, youtubePlayerFragment).commit()
        return binding.root
    }

    // function to extract the video id from the URL
    private fun URLtoID(url:String): String{
        var id = url.substring(url.lastIndexOf("=")+1)
        return id
    }

}