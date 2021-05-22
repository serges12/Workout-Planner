package com.example.workoutplanner.models

data class UserModel(
        val currentWorkout: String?=null,//current workout ID
        val startingDay: Long?=null //starting day is 1 for Monday... 7 for Sunday
)