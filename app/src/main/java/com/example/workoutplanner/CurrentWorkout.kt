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
import kotlinx.android.synthetic.main.fragment_current_workout.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import java.util.*

class CurrentWorkout : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCurrentWorkoutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_current_workout, container, false)

        //temp list for current workout
        val currentWorkoutName = "Push Pull Legs 3Days"
        var Days1: List<ExerciseModel> =
                listOf(
            ExerciseModel("ExerciseName", "ExerciseDescription", "ExerciseBodyPart", "https://www.youtube.com/watch?v=Ptrhz2zW--o", "http://www.mandysam.com/img/random.jpg"),
            ExerciseModel("ExerciseName2", "ExerciseDescription", "ExerciseBodyPart", "https://www.youtube.com/watch?v=Ptrhz2zW--o", "http://www.mandysam.com/img/random.jpg"),
            ExerciseModel("ExerciseName", "ExerciseDescription", "ExerciseBodyPart", "https://www.youtube.com/watch?v=Ptrhz2zW--o", "http://www.mandysam.com/img/random.jpg"),
            ExerciseModel("ExerciseName", "ExerciseDescription", "ExerciseBodyPart", "https://www.youtube.com/watch?v=Ptrhz2zW--o", "http://www.mandysam.com/img/random.jpg"),
            ExerciseModel("ExerciseName", "ExerciseDescription", "ExerciseBodyPart", "https://www.youtube.com/watch?v=Ptrhz2zW--o", "http://www.mandysam.com/img/random.jpg"),
            ExerciseModel("ExerciseName", "ExerciseDescription", "ExerciseBodyPart", "https://www.youtube.com/watch?v=Ptrhz2zW--o", "http://www.mandysam.com/img/random.jpg")
        )
        var Days2: List<ExerciseModel> =
                listOf(
                        ExerciseModel("ExerciseName234", "ExerciseDescription", "ExerciseBodyPart", "https://www.youtube.com/watch?v=Ptrhz2zW--o", "http://www.mandysam.com/img/random.jpg"),
                        ExerciseModel("ExerciseName2", "ExerciseDescription", "ExerciseBodyPart", "https://www.youtube.com/watch?v=Ptrhz2zW--o", "http://www.mandysam.com/img/random.jpg"),
                        ExerciseModel("ExerciseName2345", "ExerciseDescription", "ExerciseBodyPart", "https://www.youtube.com/watch?v=Ptrhz2zW--o", "http://www.mandysam.com/img/random.jpg"),
                        ExerciseModel("ExerciseName", "ExerciseDescription", "ExerciseBodyPart", "https://www.youtube.com/watch?v=Ptrhz2zW--o", "http://www.mandysam.com/img/random.jpg"),
                        ExerciseModel("ExerciseName", "ExerciseDescription", "ExerciseBodyPart", "https://www.youtube.com/watch?v=Ptrhz2zW--o", "http://www.mandysam.com/img/random.jpg"),
                        ExerciseModel("ExerciseName", "ExerciseDescription", "ExerciseBodyPart", "https://www.youtube.com/watch?v=Ptrhz2zW--o", "http://www.mandysam.com/img/random.jpg")
                )
        //set current workout name
        binding.currentWorkoutNameText.text = currentWorkoutName

        //store in database the starting day and generate names based on that starting day
        var now = Calendar.DAY_OF_WEEK
        var temp = now
        val daysOfTheWeek = listOf("Sunday","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
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
        val adapter = DailyExercisesRecylerViewAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        val exercisesList: MutableList<ExerciseModel> = mutableListOf()
        for(i in Days1){
            exercisesList.add(i)
        }
        adapter.setData(exercisesList.toList())

        adapter.onItemClick = {
            view?.findNavController()?.navigate(CurrentWorkoutDirections.actionCurrentWorkoutToExercise(it))
        }


        binding.chipGroup.setOnCheckedChangeListener{group, checkedId ->
            val v: Chip = group.getChildAt(0) as Chip
//            val currentChip: Chip = group.getChildAt(checkedId-v.id) as Chip
            Toast.makeText(context, checkedId.toString(), Toast.LENGTH_SHORT).show()
            val exercisesList: MutableList<ExerciseModel> = mutableListOf()
            if (checkedId==binding.chip10.id || checkedId==binding.chip5.id){
                for(i in Days2){
                    exercisesList.add(i)
                }
            }
            else{
                for(i in Days1){
                    exercisesList.add(i)
                }
            }
            adapter.setData(exercisesList.toList())
        }

        return binding.root
    }

}