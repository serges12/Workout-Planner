package com.example.workoutplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.muscle_layout.view.*
import kotlinx.android.synthetic.main.workout_layout.view.*

class WorkoutRecyclerViewAdapter(options: FirestoreRecyclerOptions<WorkoutModel>): FirestoreRecyclerAdapter<WorkoutModel, WorkoutRecyclerViewAdapter.WorkoutViewHolder>(options) {

    var onItemClick: ((DocumentSnapshot) -> Unit)? = null
    var onMakeCurrenntWorkoutClick: ((DocumentSnapshot)->Unit)?=null

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int, model: WorkoutModel) {

        holder.itemView.WorkoutName.text = model.name.toString()
    }

    inner class WorkoutViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener{
                onItemClick?.invoke(snapshots.getSnapshot(layoutPosition))
            }
            itemView.buttonMakeCurrentWorkout.setOnClickListener{
                onMakeCurrenntWorkoutClick?.invoke(snapshots.getSnapshot(layoutPosition))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.workout_layout,parent,false))
    }
}