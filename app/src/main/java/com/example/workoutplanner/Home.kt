package com.example.workoutplanner

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.workoutplanner.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.random.Random

class Home : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Home"


        //Setting Alarm
        binding.setAlarmButton.setOnClickListener{
//            Toast.makeText(context, binding.hourText.text.toString()+":"+binding.minuteText.text.toString(), Toast.LENGTH_SHORT).show()
            (activity as MainActivity).SetTime(binding.hourText.text.toString().toInt(),binding.minuteText.text.toString().toInt())
        }


        //set text and button as invisible
        binding.noCurrentWorkoutText.visibility = View.INVISIBLE
        binding.innerLayout.visibility = View.INVISIBLE

        val db = FirebaseFirestore.getInstance()
        var currentWorkoutID: String
        var startingDay: Int

        //Quotes Time
        var motivationalQuotes  = MotivationalQuotes()
        //Get random quote
        var index = Random.nextInt(motivationalQuotes.quotes.size)
        binding.quoteText.text = "\"" + motivationalQuotes.quotes[index][0] + "\""
        binding.quoteWriterText.text = "~ " + motivationalQuotes.quotes[index][1]

        db.collection("users").document(FirebaseAuth.getInstance().uid!!).get()
            .addOnSuccessListener {
                if(it.data != null) {
                    currentWorkoutID = it.data!!.get("currentWorkout").toString() //here we should get the current workout ID
                    startingDay = (it.data!!.get("startingDay") as Long).toInt()//here we store the starting day

                    db.collection("workouts").document(currentWorkoutID)
                            .get()
                            .addOnSuccessListener {
                                if(it.data != null) {
                                    binding.innerLayout.isVisible = true
                                    binding.workoutNameText.text =
                                        it.toObject(WorkoutModel::class.java)?.name
                                    //when we get userid, we set onclick listener
                                    binding.buttonGoToCurrentWorkout.setOnClickListener { view: View ->
                                        binding.buttonGoToCurrentWorkout.isClickable = false
                                        view.findNavController().navigate(
                                            HomeDirections.actionCurrentWorkoutToWorkout(
                                                it.toObject(WorkoutModel::class.java)!!,
                                                startingDay,
                                                false,
                                                currentWorkoutID,
                                                Calendar.getInstance().time.day
                                            )
                                        )
                                    }
                                }
                                else{
                                    binding.noCurrentWorkoutText.isVisible = true
                                    binding.buttonGoToCurrentWorkout.setOnClickListener {
                                        Toast.makeText(context, "Error: Select a current workout first!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                                .addOnFailureListener{
                                    binding.buttonGoToCurrentWorkout.isClickable = true
                                    Toast.makeText(context, "Error loading current workout.", Toast.LENGTH_SHORT).show()
                                }
                }
                else{
                    binding.noCurrentWorkoutText.isVisible = true
                    binding.buttonGoToCurrentWorkout.setOnClickListener {
                        Toast.makeText(context, "Error: Select a current workout first!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener{
                Toast.makeText(context, "Error: "+ it.message.toString(), Toast.LENGTH_SHORT).show()
            }


        return binding.root
    }



}