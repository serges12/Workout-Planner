package com.example.workoutplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.workoutplanner.models.ExerciseModel
import kotlinx.android.synthetic.main.exercise_layout.view.exerciseImage
import kotlinx.android.synthetic.main.exercise_layout.view.exerciseName
import kotlinx.android.synthetic.main.exercise_with_muscle_layout.view.*

class GetExerciseRecyclerViewAdapter: RecyclerView.Adapter<GetExerciseRecyclerViewAdapter.ViewHolder>(), Filterable {
    var exercisesList = mutableListOf<ExerciseModel>()
    var exercisesListFull = mutableListOf<ExerciseModel>()
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
        exercisesList = exercises.toMutableList()
        exercisesListFull = exercisesList.toMutableList()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return exercisesFilter
    }
    private val exercisesFilter: Filter = object: Filter(){
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filteredList: MutableList<ExerciseModel> = mutableListOf<ExerciseModel>()
            if(p0 == null || p0.isEmpty()){
                filteredList.addAll(exercisesListFull)
            }
            else{
                val filterPattern: String = p0.toString().toLowerCase().trim()
                for(exercise in exercisesListFull){
                    //search both name and bodyPart for matches
                    if(exercise.name!!.toLowerCase().contains(filterPattern) || exercise.bodyPart!!.toLowerCase().contains(filterPattern)){
                        filteredList.add(exercise)
                    }
                }
            }
            var result: FilterResults = FilterResults()
            result.values = filteredList
            return result
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            exercisesList.clear()
            exercisesList.addAll(p1?.values as MutableList<ExerciseModel>)
            notifyDataSetChanged()
        }

    }
}