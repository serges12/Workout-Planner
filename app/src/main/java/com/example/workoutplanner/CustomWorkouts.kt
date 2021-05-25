package com.example.workoutplanner

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplanner.databinding.FragmentCustomWorkoutsBinding
import com.example.workoutplanner.models.UserModel
import com.example.workoutplanner.models.WorkoutModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class CustomWorkouts : Fragment() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var customWorkoutsAdapter: WorkoutRecyclerViewAdapter? = null
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCustomWorkoutsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_custom_workouts, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Custom Workouts"
        binding.addWorkoutButton.setOnClickListener{
            showAddWorkoutdialog()
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
            val dialog = android.app.AlertDialog.Builder(context)
            dialog
                    .setItems(arrayOf("Rename","Delete"), DialogInterface.OnClickListener{ dialog, index ->
                        if(index==0){
                            val renameDialogBuilder: AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                            renameDialogBuilder.setTitle("Custom Workout Title")
                            //Set warning message
                            // Set up the input
                            val input = EditText(requireContext())
                            input.setText(it.toObject(WorkoutModel::class.java)?.name)
                            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                            input.hint = "Enter Title"
                            input.inputType = InputType.TYPE_CLASS_TEXT
                            renameDialogBuilder.setView(input)

                            // Set up the buttons
                            renameDialogBuilder.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                                // Rename in database here
                                var title = input.text.toString()

                            })
                            renameDialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })

                            renameDialogBuilder.show()
                        }
                        if(index==1) {
                            val deleteDialogBuilder = android.app.AlertDialog.Builder(context)
                            deleteDialogBuilder
                                    .setTitle("Delete?")
                                    .setMessage("You are about to delete this workout. Proceed?")
                                    .setNegativeButton("No") { dialogInterface, _ ->
                                        dialogInterface.cancel()
                                    }
                                    .setPositiveButton("Yes") { _, _ ->
                                        //Handle Delete here
                                        it.reference.delete()
                                                .addOnSuccessListener {
                                                    Toast.makeText(context, "Deleted.", Toast.LENGTH_SHORT).show()
                                                }
                                                .addOnFailureListener {
                                                    Toast.makeText(context, "Error while deleting:\n" + it.message, Toast.LENGTH_SHORT).show()
                                                }
                                    }
                            val alert = deleteDialogBuilder.create()
                            alert.show()
                        }
            })
            val dialogAlert = dialog.create()
            dialogAlert.show()

        }


        customWorkoutsAdapter!!.onMakeCurrenntWorkoutClick = {
            //make this the current workout of the user
            val user = UserModel(it.id, Calendar.getInstance().time.day.toLong())
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_custom_workouts_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menuAddWorkoutByID) {
            //add workout by ID
            showAddWorkoutByIdDialog()
        }
        return super.onOptionsItemSelected(item) //if we don't add this line, the back button doesn't work because we're overriding this function
    }
    fun showAddWorkoutdialog(){
        val builder: AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Custom Workout Title")
        //Set warning message
        builder.setMessage("Make sure the workout is safe and suitable to prevent injuries.")
        // Set up the input
        val input = EditText(requireContext())
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.hint = "Enter Title"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
            // Here you get get input text from the Edittext
            var title = input.text.toString()
            val workout: WorkoutModel = WorkoutModel(
                    FirebaseAuth.getInstance().uid,
                    title,
                    mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>(), mutableListOf<String>()
            )

            db.collection("workouts").add(workout)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{
                        Toast.makeText(context, "Error: "+it.message, Toast.LENGTH_SHORT).show()
                    }
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })

        builder.show()
    }
    fun showAddWorkoutByIdDialog(){
        val builder: AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Add Workout By ID")

        // Set up the input
        val input = EditText(requireContext())
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Enter Workout ID")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
            if (input.text.toString().trim().isEmpty()){
                Toast.makeText(context, "Workout ID can't be blank", Toast.LENGTH_SHORT).show()
            }
            else {
                // Here you get get input text from the Edittext
                var workoutID = input.text.toString()
                db.collection("workouts").document(workoutID).get()
                        .addOnSuccessListener {
                            if(it.data != null) {
                                //we got the workout, lets add it to the current user
                                val retrievedWorkout: WorkoutModel = it.toObject(WorkoutModel::class.java)!!
                                val workoutToAdd: WorkoutModel = WorkoutModel(
                                        FirebaseAuth.getInstance().uid,
                                        retrievedWorkout.name,
                                        retrievedWorkout.day1Exercises, retrievedWorkout.day2Exercises, retrievedWorkout.day3Exercises, retrievedWorkout.day4Exercises, retrievedWorkout.day5Exercises, retrievedWorkout.day6Exercises, retrievedWorkout.day7Exercises
                                )
                                db.collection("workouts").add(workoutToAdd)
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "Error: " + it.message, Toast.LENGTH_SHORT).show()
                                        }
                            }
                            else{// workout was not found
                                Toast.makeText(context, "Error: Workout not found!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error:" + it.message, Toast.LENGTH_SHORT).show()
                        }
            }
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })

        builder.show()
    }
}