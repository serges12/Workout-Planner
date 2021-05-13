package com.example.workoutplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutplanner.databinding.FragmentMusclesBinding

class Muscles : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentMusclesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_muscles, container, false)

        val adapter = MusclesRecyclerViewAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        adapter.onItemClick = {
            Toast.makeText(context, adapter.musclesList[adapter.pos].name.toString(), Toast.LENGTH_SHORT).show()

        }
        populateCards(adapter)
        return binding.root
    }


    // Testing purposes
    private fun populateCards(adapter: MusclesRecyclerViewAdapter){
        val muscleList: MutableList<Muscle> = mutableListOf(
                Muscle("Chest", R.drawable.chest),
                Muscle("Back", R.drawable.back),
                Muscle("Biceps", R.drawable.biceps),
                Muscle("Triceps", R.drawable.triceps),
                Muscle("Shoulders", R.drawable.shoulders),
                Muscle("Legs", R.drawable.legs),
                Muscle("Abs", R.drawable.abs)
        )
        adapter.setData(muscleList)
    }
}