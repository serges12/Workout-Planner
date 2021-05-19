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

class MusclesRecyclerViewAdapter(options: FirestoreRecyclerOptions<MuscleModel>): FirestoreRecyclerAdapter<MuscleModel, MusclesRecyclerViewAdapter.MuscleViewHolder>(options) {

    var onItemClick: ((DocumentSnapshot) -> Unit)? = null

    override fun onBindViewHolder(holder: MuscleViewHolder, position: Int, model: MuscleModel) {

        holder.itemView.exerciseName.text = model.name.toString()
        holder.itemView.exerciseImage.load(model.imagePath)
    }

    inner class MuscleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener{
                onItemClick?.invoke(snapshots.getSnapshot(layoutPosition))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MuscleViewHolder {
        return MuscleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.muscle_layout,parent,false))
    }
}