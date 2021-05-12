package com.example.workoutplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutplanner.databinding.FragmentWorkoutsBinding

class Workouts : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentWorkoutsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_workouts, container, false)
        binding.addWorkoutButton.setOnClickListener{
            Toast.makeText(context,"Create Workout",Toast.LENGTH_SHORT).show()
        }
        val adapter = WorkoutRecyclerViewAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        populateCards(adapter)
        return binding.root
    }


    // Testing purposes
    private fun populateCards(adapter: WorkoutRecyclerViewAdapter){
        val workoutList: MutableList<Workout> = mutableListOf()
        for(i in 1..30){
            workoutList.add(Workout("Name $i",i,"https://i.pinimg.com/originals/96/20/08/962008bd0eb249e4d575363114cec835.jpg"))
        }
        adapter.setData(workoutList.toList())
    }
}