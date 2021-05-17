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
import com.example.workoutplanner.databinding.FragmentWorkoutBinding
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Workout : Fragment() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var binding: FragmentWorkoutBinding
    var day = Calendar.getInstance().time.day
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout, container, false)
        val workout: WorkoutModel = WorkoutArgs.fromBundle(requireArguments()).workoutModel
        val workoutID: String = WorkoutArgs.fromBundle(requireArguments()).workoutID
        val allowModifications: Boolean = WorkoutArgs.fromBundle(requireArguments()).allowModifications
        val startingDay: Int = WorkoutArgs.fromBundle(requireArguments()).startingDay

        Log.i("test",binding.chipGroup.checkedChipId.toString())


        val adapter = DailyExercisesRecylerViewAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        val workoutName = workout.name
        var Days1: MutableList<ExerciseModel> = mutableListOf<ExerciseModel>()
        var Days2: MutableList<ExerciseModel> = mutableListOf<ExerciseModel>()
        var Days3: MutableList<ExerciseModel> = mutableListOf<ExerciseModel>()
        var Days4: MutableList<ExerciseModel> = mutableListOf<ExerciseModel>()
        var Days5: MutableList<ExerciseModel> = mutableListOf<ExerciseModel>()
        var Days6: MutableList<ExerciseModel> = mutableListOf<ExerciseModel>()
        var Days7: MutableList<ExerciseModel> = mutableListOf<ExerciseModel>()

        //now the lists Days1, Days2... Days7 contain exercises for each day
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

        if(workout.day1Exercises != null && workout.day2Exercises !=null&& workout.day3Exercises !=null&& workout.day4Exercises !=null&& workout.day5Exercises !=null&& workout.day6Exercises !=null&& workout.day7Exercises !=null) {
            for (exerciseID in workout.day1Exercises) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days1.add(it.toObject(ExerciseModel::class.java)!!)
                        if(temp==0)
                            showDayWorkout(Days1, adapter, binding)
                    }
            }
            for (exerciseID in workout.day2Exercises) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days2.add(it.toObject(ExerciseModel::class.java)!!)
                        if(temp==1)
                            showDayWorkout(Days2, adapter, binding)
                    }
            }
            for (exerciseID in workout.day3Exercises) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days3.add(it.toObject(ExerciseModel::class.java)!!)
                        if(temp==2)
                            showDayWorkout(Days3, adapter, binding)
                    }
            }
            for (exerciseID in workout.day4Exercises) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days4.add(it.toObject(ExerciseModel::class.java)!!)
                        if(temp==3)
                            showDayWorkout(Days4, adapter, binding)
                    }
            }
            for (exerciseID in workout.day5Exercises) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days5.add(it.toObject(ExerciseModel::class.java)!!)
                        if(temp==4)
                            showDayWorkout(Days5, adapter, binding)
                    }
            }
            for (exerciseID in workout.day6Exercises) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days6.add(it.toObject(ExerciseModel::class.java)!!)
                        if(temp==5)
                            showDayWorkout(Days6, adapter, binding)
                    }
            }
            for (exerciseID in workout.day7Exercises) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days7.add(it.toObject(ExerciseModel::class.java)!!)
                        if(temp==6)
                            showDayWorkout(Days7, adapter, binding)
                    }
            }
        }

        var workoutDays = listOf(Days1,Days2,Days3,Days4,Days5,Days6,Days7)

        //set current workout name
        binding.WorkoutNameText.text = workoutName

        adapter.onItemClick = {
            view?.findNavController()?.navigate(WorkoutDirections.actionWorkoutToExercise(it))
        }

        //if we're allowing adding/deleting exercises
        if(allowModifications){
            binding.addExerciseButton.visibility = View.VISIBLE

            binding.addExerciseButton.setOnClickListener{view:View->

            }
            adapter.onLongItemClick = {exercise->
                db.collection("exercises").whereEqualTo("name", exercise.name).get()
                        .addOnSuccessListener { snapshot->
                            val exerciseID = snapshot.documents[0].id

                            val checkedChipID = binding.chipGroup.checkedChipId
                            val arrayName = "day"+((checkedChipID - R.id.chip1)+1).toString()+"Exercises"


                            db.collection("workouts").document(workoutID).update(arrayName, FieldValue.arrayRemove(exerciseID))
                                    .addOnSuccessListener {
                                        workoutDays[checkedChipID - R.id.chip1].remove(exercise)
                                        showDayWorkout(workoutDays[checkedChipID - R.id.chip1], adapter, binding)
                                        Toast.makeText(context, "Exercise Removed.", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener{
                                        Toast.makeText(context, "Error: "+it.message, Toast.LENGTH_SHORT).show()
                                    }
                        }
                        .addOnFailureListener{
                            Toast.makeText(context, "Error: "+it.message, Toast.LENGTH_SHORT).show()
                        }
            }

        }

        binding.chipGroup.setOnCheckedChangeListener{group, checkedId ->
            val exercisesList: MutableList<ExerciseModel> = workoutDays[checkedId - R.id.chip1]
            showDayWorkout(exercisesList, adapter, binding)
        }

        Log.i("test",binding.chipGroup.checkedChipId.toString() + " " +binding.chip1.id.toString())

        return binding.root
    }

    private fun showDayWorkout(exercisesList: MutableList<ExerciseModel>, adapter: DailyExercisesRecylerViewAdapter, binding: FragmentWorkoutBinding){
//        val chip = binding.chipGroup.getChildAt(1) as Chip
//        chip.isChecked = true
//        Toast.makeText(context, binding.chipGroup.checkedChipId.toString(), Toast.LENGTH_SHORT).show()
        binding.restDayText.isVisible = exercisesList.size == 0

        adapter.setData(exercisesList.toList())
    }

    //to make sure the right chip is checked on resume
    override fun onPause() {
        val chip: Chip = binding.chipGroup.getChildAt(day-1) as Chip
        chip.isChecked = true
        super.onPause()
    }
//
//    override fun onResume() {
//        Log.i("test", "On resume")
//        super.onResume()
//    }
}