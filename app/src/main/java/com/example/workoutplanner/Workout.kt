package com.example.workoutplanner

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplanner.databinding.FragmentWorkoutBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class Workout : Fragment() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var binding: FragmentWorkoutBinding
    var exercisesAdapter: DailyExercisesRecylerViewAdapter? = null
    private lateinit var workout: WorkoutModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout, container, false)
        workout= WorkoutArgs.fromBundle(requireArguments()).workoutModel

        val workoutID: String = WorkoutArgs.fromBundle(requireArguments()).workoutID
        val allowModifications: Boolean = WorkoutArgs.fromBundle(requireArguments()).allowModifications
        var startingDay: Int = WorkoutArgs.fromBundle(requireArguments()).startingDay
        var day: Int = WorkoutArgs.fromBundle(requireArguments()).currentDay
        var query: Query = db.collection("exercises").whereIn("name", mutableListOf("placeholder"))

        var firestoreRecyclerOptions: FirestoreRecyclerOptions<ExerciseModel> = FirestoreRecyclerOptions.Builder<ExerciseModel>()
                .setQuery(query, ExerciseModel::class.java)
                .build()

        exercisesAdapter = DailyExercisesRecylerViewAdapter(firestoreRecyclerOptions)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = exercisesAdapter

        //set current workout name
        binding.WorkoutNameText.text = workout.name

        //store in database the starting day and generate names based on that starting day
        //set starting day
        var temp = startingDay
        val daysOfTheWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday","Sunday")
        for(i in 0..6) {
            val v: Chip = binding.chipGroup.getChildAt(i) as Chip
            v.text = daysOfTheWeek[(temp-1)%7]
            //set today's day as checked
            if((day-1)%7==(temp-1)%7) {
                v.isChecked = true
            }
            temp++
        }
        //to get today's day number based on the starting day
        temp = if(day-startingDay>=0)
            day - startingDay
        else
            7 + (day - startingDay)

        updateRecycler(temp+1)

        exercisesAdapter!!.onItemClick = {
            view?.findNavController()?.navigate(WorkoutDirections.actionWorkoutToExercise(it))
        }

        //if we're allowing adding/deleting exercises
//        if(allowModifications){
//            binding.addExerciseButton.visibility = View.VISIBLE
//
//            binding.addExerciseButton.setOnClickListener{view:View->
//
//            }
//            exercisesAdapter!!.onLongItemClick = {exercise->
//                db.collection("exercises").whereEqualTo("name", exercise.name).get()
//                        .addOnSuccessListener { snapshot->
//                            val exerciseID = snapshot.documents[0].id
//
//                            val checkedChipID = binding.chipGroup.checkedChipId
//                            val arrayName = "day"+((checkedChipID - R.id.chip1)+1).toString()+"Exercises"
//
//
//                            db.collection("workouts").document(workoutID).update(arrayName, FieldValue.arrayRemove(exerciseID))
//                                    .addOnSuccessListener {
//                                        workoutDays[checkedChipID - R.id.chip1].remove(exercise)
//                                        showDayWorkout(workoutDays[checkedChipID - R.id.chip1], exercisesAdapter, binding)
//                                        Toast.makeText(context, "Exercise Removed.", Toast.LENGTH_SHORT).show()
//                                    }
//                                    .addOnFailureListener{
//                                        Toast.makeText(context, "Error: "+it.message, Toast.LENGTH_SHORT).show()
//                                    }
//                        }
//                        .addOnFailureListener{
//                            Toast.makeText(context, "Error: "+it.message, Toast.LENGTH_SHORT).show()
//                        }
//            }
//
//        }

        binding.chipGroup.setOnCheckedChangeListener{group, checkedId ->
            when(checkedId){
                R.id.chip1->updateRecycler(1)
                R.id.chip2->updateRecycler(2)
                R.id.chip3->updateRecycler(3)
                R.id.chip4->updateRecycler(4)
                R.id.chip5->updateRecycler(5)
                R.id.chip6->updateRecycler(6)
                R.id.chip7->updateRecycler(7)
                else->updateRecycler(1)
            }
        }

        return binding.root
    }

    private fun updateRecycler(dayNumber: Int){
        var listToQuery = when(dayNumber){
            1->workout.day1Exercises!!.toMutableList()
            2->workout.day2Exercises!!.toMutableList()
            3->workout.day3Exercises!!.toMutableList()
            4->workout.day4Exercises!!.toMutableList()
            5->workout.day5Exercises!!.toMutableList()
            6->workout.day6Exercises!!.toMutableList()
            7->workout.day7Exercises!!.toMutableList()
            else->workout.day1Exercises!!.toMutableList()
        }
        if (listToQuery.isEmpty()){
            listToQuery.add("placeholder") //we have to do this because firebase doesnt handle querying empty lists
            //if list is empty, that means no exercises for the day, so we show the REST DAY text
            binding.restDayText.visibility = View.VISIBLE
        }
        else{
            //list is not empty
            binding.restDayText.visibility = View.INVISIBLE
        }
        var query: Query = db.collection("exercises").whereIn("name", listToQuery)
        var firestoreRecyclerOptions: FirestoreRecyclerOptions<ExerciseModel> = FirestoreRecyclerOptions.Builder<ExerciseModel>()
                .setQuery(query, ExerciseModel::class.java)
                .build()
        exercisesAdapter!!.updateOptions(firestoreRecyclerOptions)
    }

    override fun onStart() {
        super.onStart()
        exercisesAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        exercisesAdapter!!.stopListening()
    }

}