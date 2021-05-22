package com.example.workoutplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.workoutplanner.models.ExerciseModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.exercise_layout.view.*

class DailyExercisesRecylerViewAdapter(options: FirestoreRecyclerOptions<ExerciseModel>): FirestoreRecyclerAdapter<ExerciseModel, DailyExercisesRecylerViewAdapter.ViewHolder>(options) {

        var onItemClick: ((ExerciseModel) -> Unit)? = null
        var onLongItemClick: ((ExerciseModel) -> Unit)? = null

    override fun onBindViewHolder(holder: DailyExercisesRecylerViewAdapter.ViewHolder, position: Int, model: ExerciseModel) {
        holder.itemView.exerciseName.text = model.name
        holder.itemView.exerciseImage.load(model.imageLink)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener{
                onItemClick?.invoke(snapshots.getSnapshot(layoutPosition).toObject(ExerciseModel::class.java)!!)
            }
            itemView.setOnLongClickListener{
                onLongItemClick?.invoke(snapshots.getSnapshot(layoutPosition).toObject(ExerciseModel::class.java)!!)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.exercise_layout,parent,false))
    }

}
