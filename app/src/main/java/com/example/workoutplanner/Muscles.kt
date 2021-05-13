package com.example.workoutplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplanner.databinding.FragmentMusclesBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Muscles : Fragment() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var musclesAdapter: MusclesRecyclerViewAdapter? = null
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentMusclesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_muscles, container, false)
        val query: Query = db.collection("muscles")

        val firestoreRecyclerOptions: FirestoreRecyclerOptions<MuscleModel> = FirestoreRecyclerOptions.Builder<MuscleModel>()
            .setQuery(query, MuscleModel::class.java)
            .build()

        musclesAdapter = MusclesRecyclerViewAdapter(firestoreRecyclerOptions)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = musclesAdapter
        musclesAdapter!!.onItemClick = {
            view?.findNavController()?.navigate(MusclesDirections.actionMusclesToExercisesList(it.get("name").toString()))
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        musclesAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        musclesAdapter!!.stopListening()
    }
}