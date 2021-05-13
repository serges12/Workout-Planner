package com.example.workoutplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.persistableBundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.android.synthetic.main.muscle_layout.view.*
import kotlinx.android.synthetic.main.workout_layout.view.*

class MusclesRecyclerViewAdapter: RecyclerView.Adapter<MusclesRecyclerViewAdapter.ViewHolder>() {
    var musclesList = emptyList<Muscle>()
    var onItemClick: ((Muscle) -> Unit)? = null
    var pos: Int = 0
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = musclesList[position]
        holder.itemView.MuscleName.text = entry.name.toString()
        holder.itemView.MuscleImage.setImageResource(entry.musclePicture)

    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        init {
            view.setOnClickListener{
                view
                pos = adapterPosition
                onItemClick?.invoke(musclesList[adapterPosition])
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.muscle_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return musclesList.size

    }



    fun setData(muscles:List<Muscle>){
        musclesList = muscles
        notifyDataSetChanged()
    }
}