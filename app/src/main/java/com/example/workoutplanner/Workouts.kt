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
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplanner.databinding.FragmentWorkoutsBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class Workouts : Fragment() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var workoutsAdapter: WorkoutRecyclerViewAdapter? = null
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentWorkoutsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_workouts, container, false)
        binding.addWorkoutButton.setOnClickListener{
            Toast.makeText(context,"Create Workout",Toast.LENGTH_SHORT).show()
        }

        val query: Query = db.collection("workouts")

        val firestoreRecyclerOptions: FirestoreRecyclerOptions<WorkoutModel> = FirestoreRecyclerOptions.Builder<WorkoutModel>()
                .setQuery(query, WorkoutModel::class.java)
                .build()

        workoutsAdapter = WorkoutRecyclerViewAdapter(firestoreRecyclerOptions)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = workoutsAdapter
        workoutsAdapter!!.onItemClick = {
            view?.findNavController()?.navigate(WorkoutsDirections.actionWorkoutsToWorkout(it.toObject(WorkoutModel::class.java)!!, 3))
        }
        workoutsAdapter!!.onMakeCurrenntWorkoutClick = {
            //make this the current workout of the user
            val user: UserModel = UserModel(it.id, Calendar.DAY_OF_WEEK.toLong())
            db.collection("users").document(FirebaseAuth.getInstance().uid!!).set(user)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Sucess", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{
                        Toast.makeText(context, "Error: "+it.message, Toast.LENGTH_SHORT).show()
                    }
        }

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        workoutsAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        workoutsAdapter!!.stopListening()
    }
}