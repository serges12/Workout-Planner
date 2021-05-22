package com.example.workoutplanner.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ExerciseModel(
    val name:String? = null,
    val description:String? = null,
    val bodyPart:String? = null,
    val videoLink:String? = null,
    val imageLink:String? = null
): Parcelable