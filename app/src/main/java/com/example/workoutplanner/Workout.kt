package com.example.workoutplanner

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutplanner.databinding.FragmentWorkoutBinding
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Workout : Fragment() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Log.i("test","creatednjn gergerk ger fekr")
        val binding: FragmentWorkoutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout, container, false)
        val workout: WorkoutModel = WorkoutArgs.fromBundle(requireArguments()).workoutModel
        val startingDay: Int = WorkoutArgs.fromBundle(requireArguments()).startingDay
        val adapter = DailyExercisesRecylerViewAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        //temp list for current workout
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
        var day = Calendar.getInstance().time.day
        var today = Calendar.getInstance().time
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

        if(workout.Day1Exercises != null && workout.Day2Exercises !=null&& workout.Day3Exercises !=null&& workout.Day4Exercises !=null&& workout.Day5Exercises !=null&& workout.Day6Exercises !=null&& workout.Day7Exercises !=null) {
            for (exerciseID in workout.Day1Exercises!!) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days1.add(it.toObject(ExerciseModel::class.java)!!)
                        if(temp==0)
                            showDayWorkout(Days1, adapter, binding)
                    }
            }
            for (exerciseID in workout.Day2Exercises!!) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days2.add(it.toObject(ExerciseModel::class.java)!!)
                        if(temp==1)
                            showDayWorkout(Days2, adapter, binding)
                    }
            }
            for (exerciseID in workout.Day3Exercises!!) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days3.add(it.toObject(ExerciseModel::class.java)!!)
                        if(temp==2)
                            showDayWorkout(Days3, adapter, binding)
                    }
            }
            for (exerciseID in workout.Day4Exercises!!) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days4.add(it.toObject(ExerciseModel::class.java)!!)
                        if(temp==3)
                            showDayWorkout(Days4, adapter, binding)
                    }
            }
            for (exerciseID in workout.Day5Exercises!!) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days5.add(it.toObject(ExerciseModel::class.java)!!)
                        if(temp==4)
                            showDayWorkout(Days5, adapter, binding)
                    }
            }
            for (exerciseID in workout.Day6Exercises!!) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days6.add(it.toObject(ExerciseModel::class.java)!!)
                        if(temp==5)
                            showDayWorkout(Days6, adapter, binding)
                    }
            }
            for (exerciseID in workout.Day7Exercises!!) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days7.add(it.toObject(ExerciseModel::class.java)!!)
                        if(temp==6)
                            showDayWorkout(Days7, adapter, binding)
                    }
            }
        }

        //set current workout name
        binding.WorkoutNameText.text = workoutName

        adapter.onItemClick = {
            view?.findNavController()?.navigate(WorkoutDirections.actionWorkoutToExercise(it))
        }


        binding.chipGroup.setOnCheckedChangeListener{group, checkedId ->
            val exercisesList: MutableList<ExerciseModel> = when(checkedId){
                R.id.chip1->Days1
                R.id.chip2->Days2
                R.id.chip3->Days3
                R.id.chip4->Days4
                R.id.chip5->Days5
                R.id.chip6->Days6
                R.id.chip7->Days7
                else-> Days1
            }
           showDayWorkout(exercisesList, adapter, binding)
        }
        return binding.root
    }

    private fun showDayWorkout(exercisesList: MutableList<ExerciseModel>, adapter: DailyExercisesRecylerViewAdapter, binding: FragmentWorkoutBinding){
//        val chip = binding.chipGroup.getChildAt(1) as Chip
//        chip.isChecked = true
//        Toast.makeText(context, chip.text.toString(), Toast.LENGTH_SHORT).show()
        adapter.setData(exercisesList.toList())
        binding.restDayText.isVisible = exercisesList.size == 0
    }


//    override fun onPause() {
//        Log.i("test", "is paused")
//        super.onPause()
//    }
//
//    override fun onResume() {
//        Log.i("test", "On resume")
//        super.onResume()
//    }
}