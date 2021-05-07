package com.example.workoutplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.android.synthetic.main.workout_layout.view.*

class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private var workoutList = emptyList<Workout>()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.workout_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return workoutList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = workoutList[position]
        holder.itemView.WorkoutName.text = entry.name.toString()
        holder.itemView.WorkoutID.text = entry.workoutID.toString()
        holder.itemView.WorkoutImage.load(entry.workoutPicture)

        holder.itemView.setOnClickListener(){
            Toast.makeText(holder.itemView.context, "Position: ${position + 1}", Toast.LENGTH_SHORT).show()
        }
    }

    fun setData(workouts:List<Workout>){
        workoutList = workouts
        notifyDataSetChanged()
    }
}