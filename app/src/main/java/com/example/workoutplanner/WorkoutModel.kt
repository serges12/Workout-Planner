package com.example.workoutplanner

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class WorkoutModel(
        val name:String?=null,
        val Day1Exercises:List<String>? = null,
        val Day2Exercises:List<String>? = null,
        val Day3Exercises:List<String>? = null,
        val Day4Exercises:List<String>? = null,
        val Day5Exercises:List<String>? = null,
        val Day6Exercises:List<String>? = null,
        val Day7Exercises:List<String>? = null
): Parcelable