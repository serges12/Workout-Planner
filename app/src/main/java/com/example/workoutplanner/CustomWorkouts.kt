package com.example.workoutplanner

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplanner.databinding.FragmentCustomWorkoutsBinding
import com.example.workoutplanner.databinding.FragmentWorkoutsBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class CustomWorkouts : Fragment() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var customWorkoutsAdapter: WorkoutRecyclerViewAdapter? = null
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCustomWorkoutsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_custom_workouts, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Custom Workouts"
        binding.addWorkoutButton.setOnClickListener{
            showdialog()
        }

        //setting up recyclerview
        val query: Query = db.collection("workouts").whereEqualTo("userID", FirebaseAuth.getInstance().uid)
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<WorkoutModel> = FirestoreRecyclerOptions.Builder<WorkoutModel>()
                .setQuery(query, WorkoutModel::class.java)
                .build()
        customWorkoutsAdapter = WorkoutRecyclerViewAdapter(firestoreRecyclerOptions)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = customWorkoutsAdapter

        customWorkoutsAdapter!!.onItemClick = {
            view?.findNavController()?.navigate(CustomWorkoutsDirections.actionCustomWorkoutToWorkout(it.toObject(WorkoutModel::class.java)!!, 1, true, it.id, 1))//starting day is 1 if we only wana see details
        }
        customWorkoutsAdapter!!.onLongItemClick = {
            val dialogBuilder = android.app.AlertDialog.Builder(context)
            dialogBuilder
                    .setTitle("Delete?")
                    .setMessage("You are about to delete this workout. Proceed?")
                    .setNegativeButton("No"){
                        dialogInterface, i ->
                        dialogInterface.cancel()
                    }
                    .setPositiveButton("Yes"){
                        dialogInterface, i ->
                        //Handle Delete here
                        it.reference.delete()
                                .addOnSuccessListener {
                            Toast.makeText(context, "Deleted.", Toast.LENGTH_SHORT).show()
                        }
                                .addOnFailureListener{
                                    Toast.makeText(context, "Error while deleting:\n"+it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            val alert = dialogBuilder.create()
            alert.show()
        }


        customWorkoutsAdapter!!.onMakeCurrenntWorkoutClick = {
            //make this the current workout of the user
            val user: UserModel = UserModel(it.id, Calendar.getInstance().time.day.toLong())
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
        customWorkoutsAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        customWorkoutsAdapter!!.stopListening()
    }

    fun showdialog(){
        val builder: AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Custom Workout Title")

        // Set up the input
        val input = EditText(requireContext())
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Enter Title")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            var title = input.text.toString()
            val workout: WorkoutModel = WorkoutModel(
                    FirebaseAuth.getInstance().uid,
                    title,
                    mutableListOf<String>(), mutableListOf<String>(),mutableListOf<String>(),mutableListOf<String>(),mutableListOf<String>(),mutableListOf<String>(),mutableListOf<String>()
            )

            db.collection("workouts").add(workout)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{
                        Toast.makeText(context, "Error: "+it.message, Toast.LENGTH_SHORT).show()
                    }
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }
}