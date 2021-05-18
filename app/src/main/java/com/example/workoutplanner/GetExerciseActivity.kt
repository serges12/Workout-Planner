package com.example.workoutplanner

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_get_exercise.*

class GetExerciseActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val adapter = GetExerciseRecyclerViewAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_exercise)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_get_exercise_activity, menu)

        val searchItem: MenuItem = menu!!.findItem(R.id.menuButtonSearch)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter.filter.filter(p0)
                return false
            }

        })

        return true
    }
}