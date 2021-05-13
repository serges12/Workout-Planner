package com.example.workoutplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.muscle_layout.view.*

class ExercisesRecyclerViewAdapter(options: FirestoreRecyclerOptions<ExerciseModel>) :
    FirestoreRecyclerAdapter<ExerciseModel, ExercisesRecyclerViewAdapter.ExerciseAdapterViewHolder>(options){

    var onItemClick: ((DocumentSnapshot) -> Unit)? = null

    inner class ExerciseAdapterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener{
                onItemClick?.invoke(snapshots.getSnapshot(layoutPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseAdapterViewHolder {
        return ExerciseAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.exercise_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ExerciseAdapterViewHolder, position: Int, model: ExerciseModel) {
        holder.itemView.exerciseName.text = model.name
        holder.itemView.exerciseImage.load(model.imageLink)
    }

}