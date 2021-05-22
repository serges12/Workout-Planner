package com.example.workoutplanner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutplanner.databinding.FragmentWorkoutBinding
import com.example.workoutplanner.models.ExerciseModel
import com.example.workoutplanner.models.WorkoutModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Workout : Fragment() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var binding: FragmentWorkoutBinding
    var exercisesAdapter: DailyExercisesRecylerViewAdapter? = null
    lateinit var workoutID: String
    private lateinit var workout: WorkoutModel
    private var allowModifications: Boolean = false

    //new way to get result from activities (instead of startActivityForResult which is deprecated
    //see registerForActivityResult documentation here https://developer.android.com/training/basics/intents/result
    val getExerciseResultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if(result.resultCode== Activity.RESULT_OK){
            val intent: Intent? = result.data
            val exerciseName : String?= intent?.getStringExtra("exerciseName")
            if(exerciseName != null){
                when(binding.chipGroup.checkedChipId){
                    R.id.chip1->addExercise(1, exerciseName)
                    R.id.chip2->addExercise(2, exerciseName)
                    R.id.chip3->addExercise(3, exerciseName)
                    R.id.chip4->addExercise(4, exerciseName)
                    R.id.chip5->addExercise(5, exerciseName)
                    R.id.chip6->addExercise(6, exerciseName)
                    R.id.chip7->addExercise(7, exerciseName)
                    else-> Toast.makeText(context, "Error: Invalid day", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout, container, false)
        workout= WorkoutArgs.fromBundle(requireArguments()).workoutModel
        workoutID = WorkoutArgs.fromBundle(requireArguments()).workoutID
        allowModifications = WorkoutArgs.fromBundle(requireArguments()).allowModifications
        var startingDay: Int = WorkoutArgs.fromBundle(requireArguments()).startingDay
        var day: Int = WorkoutArgs.fromBundle(requireArguments()).currentDay
        var query: Query = db.collection("exercises").whereIn("name", mutableListOf("placeholder"))

        var firestoreRecyclerOptions: FirestoreRecyclerOptions<ExerciseModel> = FirestoreRecyclerOptions.Builder<ExerciseModel>()
                .setQuery(query, ExerciseModel::class.java)
                .build()

        exercisesAdapter = DailyExercisesRecylerViewAdapter(firestoreRecyclerOptions)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = exercisesAdapter

        //set current workout name
        binding.WorkoutNameText.text = workout.name
        (activity as AppCompatActivity).supportActionBar?.title = workout.name

        //store in database the starting day and generate names based on that starting day
        //set starting day
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

        updateRecycler(temp+1)

        exercisesAdapter!!.onItemClick = {
            view?.findNavController()?.navigate(WorkoutDirections.actionWorkoutToExercise(it))
        }


        //if we're allowing adding/deleting exercises
        if(allowModifications) {
            //show add exercise
            binding.addExerciseButton.visibility = View.VISIBLE
            //getting an exercise from activity and adding it
            binding.addExerciseButton.setOnClickListener {
                //we want to launch getActivity to get an exercise to then add it
                getExerciseResultContract.launch(Intent(context, GetExerciseActivity::class.java))
                //logic for adding exercise is in the global getExerciseResultContract variable
            }
            //deleting exercise on long click
            exercisesAdapter!!.onLongItemClick = { exercise ->

                val dialogBuilder = android.app.AlertDialog.Builder(context)
                dialogBuilder
                        .setTitle("Remove?")
                        .setMessage("You are about to remove this exercise. Proceed?")
                        .setNegativeButton("No"){
                            dialogInterface, _ ->
                            dialogInterface.cancel()
                        }
                        .setPositiveButton("Yes"){
                            _, _ ->
                            //Handle Delete here
                            when(binding.chipGroup.checkedChipId){
                                R.id.chip1->deleteExercise(1, exercise.name!!)
                                R.id.chip2->deleteExercise(2, exercise.name!!)
                                R.id.chip3->deleteExercise(3, exercise.name!!)
                                R.id.chip4->deleteExercise(4, exercise.name!!)
                                R.id.chip5->deleteExercise(5, exercise.name!!)
                                R.id.chip6->deleteExercise(6, exercise.name!!)
                                R.id.chip7->deleteExercise(7, exercise.name!!)
                                else-> Toast.makeText(context, "Invalid day!", Toast.LENGTH_SHORT).show()
                            }
                        }
                val alert = dialogBuilder.create()
                alert.show()
            }
            //sharing exercise

        }

        binding.chipGroup.setOnCheckedChangeListener{_, checkedId ->
            when(checkedId){
                R.id.chip1->updateRecycler(1)
                R.id.chip2->updateRecycler(2)
                R.id.chip3->updateRecycler(3)
                R.id.chip4->updateRecycler(4)
                R.id.chip5->updateRecycler(5)
                R.id.chip6->updateRecycler(6)
                R.id.chip7->updateRecycler(7)
                else->updateRecycler(1)
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_workout_details_fragment, menu)
        //only show share if we allow modification
        //only share workouts from custom workouts
        if(allowModifications){
            val shareButton = menu.findItem(R.id.menuButtonShare)
            shareButton.isVisible = true
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menuButtonShare) {
            //make intent to share score here
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    ("Check out this custom workout I created in the Workout Planner app!\nYou can add it to your workouts by " +
                            "pasting the ID below in the add button on your custom workout screen\n" + workoutID)
                )
            }.also {
                startActivity(it)
            }
        }
        return super.onOptionsItemSelected(item) //if we don't add this line, the back button doesn't work because we're overriding this function
    }
    private fun updateRecycler(dayNumber: Int){
        var listToQuery = when(dayNumber){
            1->workout.day1Exercises!!.toMutableList()
            2->workout.day2Exercises!!.toMutableList()
            3->workout.day3Exercises!!.toMutableList()
            4->workout.day4Exercises!!.toMutableList()
            5->workout.day5Exercises!!.toMutableList()
            6->workout.day6Exercises!!.toMutableList()
            7->workout.day7Exercises!!.toMutableList()
            else->workout.day1Exercises!!.toMutableList()
        }
        if (listToQuery.isEmpty()){
            listToQuery.add("placeholder") //we have to do this because firebase doesnt handle querying empty lists
            //if list is empty, that means no exercises for the day, so we show the REST DAY text
            binding.restDayText.visibility = View.VISIBLE
        }
        else{
            //list is not empty
            binding.restDayText.visibility = View.INVISIBLE
        }
        var query: Query = db.collection("exercises").whereIn("name", listToQuery).orderBy("bodyPart", Query.Direction.DESCENDING)
        var firestoreRecyclerOptions: FirestoreRecyclerOptions<ExerciseModel> = FirestoreRecyclerOptions.Builder<ExerciseModel>()
                .setQuery(query, ExerciseModel::class.java)
                .build()
        exercisesAdapter!!.updateOptions(firestoreRecyclerOptions)
    }

    private fun addExercise(dayNumber: Int, exerciseName: String){
        when(dayNumber){
            1-> {
                db.collection("workouts").document(workoutID).update("day1Exercises", FieldValue.arrayUnion(exerciseName))
                        .addOnSuccessListener {
                            workout.day1Exercises!!.add(exerciseName)
                            updateRecycler(1)
                            Toast.makeText(context, "Exercise Added!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            showError(it)
                        }
            }
            2-> {
                db.collection("workouts").document(workoutID).update("day2Exercises", FieldValue.arrayUnion(exerciseName))
                        .addOnSuccessListener {
                            workout.day2Exercises!!.add(exerciseName)
                            updateRecycler(2)
                            Toast.makeText(context, "Exercise Added!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            showError(it)
                        }
            }
            3-> {
                db.collection("workouts").document(workoutID).update("day3Exercises", FieldValue.arrayUnion(exerciseName))
                        .addOnSuccessListener {
                            workout.day3Exercises!!.add(exerciseName)
                            updateRecycler(3)
                            Toast.makeText(context, "Exercise Added!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            showError(it)
                        }
            }
            4-> {
                db.collection("workouts").document(workoutID).update("day4Exercises", FieldValue.arrayUnion(exerciseName))
                        .addOnSuccessListener {
                            workout.day4Exercises!!.add(exerciseName)
                            updateRecycler(4)
                            Toast.makeText(context, "Exercise Added!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            showError(it)
                        }
            }
            5-> {
                db.collection("workouts").document(workoutID).update("day5Exercises", FieldValue.arrayUnion(exerciseName))
                        .addOnSuccessListener {
                            workout.day5Exercises!!.add(exerciseName)
                            updateRecycler(5)
                            Toast.makeText(context, "Exercise Added!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            showError(it)
                        }
            }
            6-> {
                db.collection("workouts").document(workoutID).update("day6Exercises", FieldValue.arrayUnion(exerciseName))
                        .addOnSuccessListener {
                            workout.day6Exercises!!.add(exerciseName)
                            updateRecycler(6)
                            Toast.makeText(context, "Exercise Added!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            showError(it)
                        }
            }
            7-> {
                db.collection("workouts").document(workoutID).update("day7Exercises", FieldValue.arrayUnion(exerciseName))
                        .addOnSuccessListener {
                            workout.day7Exercises!!.add(exerciseName)
                            updateRecycler(7)
                            Toast.makeText(context, "Exercise Added!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            showError(it)
                        }
            }
            else-> Toast.makeText(context, "Invalid day!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteExercise(dayNumber: Int, exerciseName: String){
        when(dayNumber){
            1->{
                db.collection("workouts").document(workoutID).update("day1Exercises", FieldValue.arrayRemove(exerciseName))
                        .addOnSuccessListener {
                            workout.day1Exercises!!.removeAll(listOf(exerciseName))
                            updateRecycler(1)
                            Toast.makeText(context, "Exercise Deleted!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            showError(it)
                        }
            }
            2->{
                db.collection("workouts").document(workoutID).update("day2Exercises", FieldValue.arrayRemove(exerciseName))
                        .addOnSuccessListener {
                            workout.day1Exercises!!.removeAll(listOf(exerciseName))
                            updateRecycler(1)
                            Toast.makeText(context, "Exercise Deleted!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            showError(it)
                        }
            }
            3->{
                db.collection("workouts").document(workoutID).update("day3Exercises", FieldValue.arrayRemove(exerciseName))
                        .addOnSuccessListener {
                            workout.day1Exercises!!.removeAll(listOf(exerciseName))
                            updateRecycler(1)
                            Toast.makeText(context, "Exercise Deleted!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            showError(it)
                        }
            }
            4->{
                db.collection("workouts").document(workoutID).update("day4Exercises", FieldValue.arrayRemove(exerciseName))
                        .addOnSuccessListener {
                            workout.day1Exercises!!.removeAll(listOf(exerciseName))
                            updateRecycler(1)
                            Toast.makeText(context, "Exercise Deleted!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            showError(it)
                        }
            }
            5->{
                db.collection("workouts").document(workoutID).update("day5Exercises", FieldValue.arrayRemove(exerciseName))
                        .addOnSuccessListener {
                            workout.day1Exercises!!.removeAll(listOf(exerciseName))
                            updateRecycler(1)
                            Toast.makeText(context, "Exercise Deleted!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            showError(it)
                        }
            }
            6->{
                db.collection("workouts").document(workoutID).update("day6Exercises", FieldValue.arrayRemove(exerciseName))
                        .addOnSuccessListener {
                            workout.day1Exercises!!.removeAll(listOf(exerciseName))
                            updateRecycler(1)
                            Toast.makeText(context, "Exercise Deleted!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            showError(it)
                        }
            }
            7->{
                db.collection("workouts").document(workoutID).update("day7Exercises", FieldValue.arrayRemove(exerciseName))
                        .addOnSuccessListener {
                            workout.day1Exercises!!.removeAll(listOf(exerciseName))
                            updateRecycler(1)
                            Toast.makeText(context, "Exercise Deleted!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            showError(it)
                        }
            }
            else-> Toast.makeText(context, "Invalid day!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showError(error: Exception){
        Toast.makeText(context, "Error: "+error.message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        exercisesAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        exercisesAdapter!!.stopListening()
    }

}