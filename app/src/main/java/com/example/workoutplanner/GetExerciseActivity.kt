package com.example.workoutplanner

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_get_exercise.*

class GetExerciseActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_exercise)

        val adapter = GetExerciseRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        var exercises: MutableList<ExerciseModel> = mutableListOf()
        db.collection("exercises").orderBy("bodyPart").get()
            .addOnSuccessListener {
                for(document in it){
                    exercises.add(document.toObject(ExerciseModel::class.java))
                }
                adapter.setData(exercises)
                textViewLoading.visibility = View.INVISIBLE
            }
        adapter.onItemClick = {
            val intent: Intent = Intent()
            intent.putExtra("exerciseName", it.name)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}