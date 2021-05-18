package com.example.workoutplanner

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class WorkoutModel(
        val userID: String?=null,
        val name:String?=null,
        val day1Exercises:List<String>? = null, //these will contain names of exercises
        val day2Exercises:List<String>? = null,
        val day3Exercises:List<String>? = null,
        val day4Exercises:List<String>? = null,
        val day5Exercises:List<String>? = null,
        val day6Exercises:List<String>? = null,
        val day7Exercises:List<String>? = null
): Parcelable