package com.example.workoutplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.android.synthetic.main.exercise_layout.view.*
import kotlinx.android.synthetic.main.exercise_layout.view.exerciseImage
import kotlinx.android.synthetic.main.exercise_layout.view.exerciseName
import kotlinx.android.synthetic.main.exercise_with_muscle_layout.view.*

class GetExerciseRecyclerViewAdapter: RecyclerView.Adapter<GetExerciseRecyclerViewAdapter.ViewHolder>() {
    var exercisesList = emptyList<ExerciseModel>()
    var onItemClick: ((ExerciseModel) -> Unit)? = null

    override fun onBindViewHolder(
        holder: GetExerciseRecyclerViewAdapter.ViewHolder,
        position: Int
    ) {
        val entry = exercisesList[position]
        holder.itemView.exerciseName.text = entry.name
        holder.itemView.exerciseMuscle.text = entry.bodyPart
        holder.itemView.exerciseImage.load(entry.imageLink)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                onItemClick?.invoke(exercisesList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.exercise_with_muscle_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return exercisesList.size
    }

    fun setData(exercises: List<ExerciseModel>) {
        exercisesList = exercises
        notifyDataSetChanged()
    }
}