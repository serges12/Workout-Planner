package com.example.workoutplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.android.synthetic.main.exercise_layout.view.*

class DailyExercisesRecylerViewAdapter: RecyclerView.Adapter<DailyExercisesRecylerViewAdapter.ViewHolder>() {
        var exercisesList = emptyList<ExerciseModel>()
        var onItemClick: ((ExerciseModel) -> Unit)? = null

    override fun onBindViewHolder(holder: DailyExercisesRecylerViewAdapter.ViewHolder, position: Int) {
        val entry = exercisesList[position]
        holder.itemView.exerciseName.text = entry.name
        holder.itemView.exerciseImage.load(entry.imageLink)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        init {
            view.setOnClickListener{
                onItemClick?.invoke(exercisesList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.exercise_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return exercisesList.size

    }



    fun setData(exercises:List<ExerciseModel>){
        exercisesList = exercises
        notifyDataSetChanged()
    }


}
