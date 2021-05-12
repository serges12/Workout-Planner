package com.example.workoutplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.android.synthetic.main.workout_layout.view.*

class WorkoutRecyclerViewAdapter: RecyclerView.Adapter<WorkoutRecyclerViewAdapter.ViewHolder>() {
    var workoutList = emptyList<Workout>()
    var onItemClick: ((Workout) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = workoutList[position]
        holder.itemView.WorkoutName.text = entry.name.toString()
        holder.itemView.WorkoutID.text = entry.workoutID.toString()
        holder.itemView.WorkoutImage.load(entry.workoutPicture)

    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        init {
            view.setOnClickListener{
                onItemClick?.invoke(workoutList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.workout_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return workoutList.size

    }



    fun setData(workouts:List<Workout>){
        workoutList = workouts
        notifyDataSetChanged()
    }
}