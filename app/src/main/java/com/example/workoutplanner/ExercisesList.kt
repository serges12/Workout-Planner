package com.example.workoutplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplanner.databinding.FragmentExercisesListBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class ExercisesList : Fragment() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var exerciseAdapter: ExercisesRecyclerViewAdapter? = null
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentExercisesListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercises_list, container, false)
        val muscleName = ExercisesListArgs.fromBundle(requireArguments()).muscleName
        (activity as AppCompatActivity).supportActionBar?.title = muscleName
        recyclerView = binding.recyclerView

        val query: Query = db.collection("exercises").whereEqualTo("bodyPart", muscleName)
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<ExerciseModel> = FirestoreRecyclerOptions.Builder<ExerciseModel>()
            .setQuery(query, ExerciseModel::class.java)
            .build()
        exerciseAdapter = ExercisesRecyclerViewAdapter(firestoreRecyclerOptions)
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.adapter = exerciseAdapter

        exerciseAdapter!!.onItemClick = {
            view?.findNavController()?.navigate(ExercisesListDirections.actionExercisesListToExercise(it.id))
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        exerciseAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        exerciseAdapter!!.stopListening()
    }
}