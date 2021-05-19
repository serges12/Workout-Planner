package com.example.workoutplanner

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class WorkoutModel(
        val userID: String?=null,
        val name:String?=null,
        val day1Exercises:MutableList<String>? = null, //these will contain names of exercises
        val day2Exercises:MutableList<String>? = null,
        val day3Exercises:MutableList<String>? = null,
        val day4Exercises:MutableList<String>? = null,
        val day5Exercises:MutableList<String>? = null,
        val day6Exercises:MutableList<String>? = null,
        val day7Exercises:MutableList<String>? = null
): Parcelable