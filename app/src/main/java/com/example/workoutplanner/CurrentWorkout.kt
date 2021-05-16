package com.example.workoutplanner

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginBottom
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutplanner.databinding.FragmentCurrentWorkoutBinding
import com.example.workoutplanner.databinding.FragmentHistoryBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.api.Distribution
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_current_workout.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import java.util.*

class CurrentWorkout : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCurrentWorkoutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_current_workout, container, false)

        val db = FirebaseFirestore.getInstance()
        var currentWorkoutID: String
        var startingDay: String

        db.collection("users").document(FirebaseAuth.getInstance().uid!!).get()
            .addOnSuccessListener {
                currentWorkoutID= it.data!!.get("currentWorkout").toString() //here we should get the current workout ID
                startingDay= it.data!!.get("startingDay").toString()//here we store the starting day

                //
                // ADD CODE HERE
                //



                //when we get userid, we set onclick listener
                binding.buttonGoToCurrentWorkout.setOnClickListener {view: View->
                    db.collection("workouts").document(currentWorkoutID)
                        .get()
                        .addOnSuccessListener {
                            view.findNavController()?.navigate(
                                CurrentWorkoutDirections.actionCurrentWorkoutToWorkout(
                                    it.toObject(WorkoutModel::class.java)!!,
                                    startingDay
                                )
                            )
                        }
                }
            }
            .addOnFailureListener{
                Toast.makeText(context, "Error: "+ it.message.toString(), Toast.LENGTH_SHORT).show()
            }


        return binding.root
    }

}