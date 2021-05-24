package com.example.workoutplanner

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import coil.load
import com.example.workoutplanner.databinding.FragmentExerciseBinding
import com.example.workoutplanner.models.ExerciseModel
import com.google.firebase.firestore.FirebaseFirestore
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener


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
        (activity as AppCompatActivity).supportActionBar?.title = exercise.name
        binding.exerciseDescriptionText.setText(exercise.description)
        binding.bodyPartText.text = exercise.bodyPart.toString()
//        binding.imageView.load(exercise.imageLink)
        binding.exerciseDescriptionText.movementMethod = ScrollingMovementMethod()


        val youTubePlayerView = binding.thirdPartyPlayerView
        lifecycle.addObserver(youTubePlayerView)
        //remove fullscreen button
        youTubePlayerView.getPlayerUiController().showFullscreenButton(false)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = URLtoID(exercise.videoLink!!)
                youTubePlayer.cueVideo(videoId, 0F)
            }
        })

        return binding.root
    }

    // function to extract the video id from the URL
    private fun URLtoID(url:String): String{
        var id = url.substring(url.lastIndexOf("=")+1)
        return id
    }

}