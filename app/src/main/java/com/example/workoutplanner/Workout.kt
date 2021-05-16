package com.example.workoutplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutplanner.databinding.FragmentWorkoutBinding
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Workout : Fragment() {
    private lateinit var binding: FragmentWorkoutBinding
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentWorkoutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout, container, false)
        val workout: WorkoutModel = WorkoutArgs.fromBundle(requireArguments()).workoutModel
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

        if(workout.Day1Exercises != null && workout.Day2Exercises !=null&& workout.Day3Exercises !=null&& workout.Day4Exercises !=null&& workout.Day5Exercises !=null&& workout.Day6Exercises !=null&& workout.Day7Exercises !=null) {
            for (exerciseID in workout.Day1Exercises!!) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days1.add(it.toObject(ExerciseModel::class.java)!!)
                    }
            }
            for (exerciseID in workout.Day2Exercises!!) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days2.add(it.toObject(ExerciseModel::class.java)!!)
                    }
            }
            for (exerciseID in workout.Day3Exercises!!) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days3.add(it.toObject(ExerciseModel::class.java)!!)
                    }
            }
            for (exerciseID in workout.Day4Exercises!!) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days4.add(it.toObject(ExerciseModel::class.java)!!)
                    }
            }
            for (exerciseID in workout.Day5Exercises!!) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days5.add(it.toObject(ExerciseModel::class.java)!!)
                    }
            }
            for (exerciseID in workout.Day6Exercises!!) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days6.add(it.toObject(ExerciseModel::class.java)!!)
                    }
            }
            for (exerciseID in workout.Day7Exercises!!) {
                db.collection("exercises").document(exerciseID).get()
                    .addOnSuccessListener {
                        Days7.add(it.toObject(ExerciseModel::class.java)!!)
                    }
            }
        }

        //set current workout name
        binding.WorkoutNameText.text = workoutName



        //now the lists Days1, Days2... Days7 contain exercises for each day

        //store in database the starting day and generate names based on that starting day
        var now = Calendar.DAY_OF_WEEK
        var temp = now
        val daysOfTheWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday","Sunday")
        for(i in 0..6) {
            val v: Chip = binding.chipGroup.getChildAt(i) as Chip
            v.text = daysOfTheWeek[(temp-1)%7]
            //set today's day as checked
            if(daysOfTheWeek[(now-1)%7]==daysOfTheWeek[(temp-1)%7])
                v.isChecked = true
            temp++
        }


        //fill recycler view
        // should be done on each chip click
        adapter.setData(Days1.toList())

        adapter.onItemClick = {
            view?.findNavController()?.navigate(WorkoutDirections.actionWorkoutToExercise(it))
        }


        binding.chipGroup.setOnCheckedChangeListener{group, checkedId ->
            val v: Chip = group.getChildAt(0) as Chip
//            val currentChip: Chip = group.getChildAt(checkedId-v.id) as Chip
            Toast.makeText(context, checkedId.toString(), Toast.LENGTH_SHORT).show()
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
            adapter.setData(exercisesList.toList())
        }
        return binding.root
    }


}