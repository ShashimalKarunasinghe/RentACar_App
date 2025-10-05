package com.example.rentacar.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Parcelable so we can pass a whole Car object in an Intent extra.
 * (Exactly what your tutorial describes with @Parcelize.)
 */
@Parcelize
data class Car(
    val id: String,
    val name: String,
    val model: String,
    val year: Int,
    val rating: Float,       // 1..5 (supports halves)
    val kilometres: Int,
    val dailyCost: Int,      // credits per day
    val imageRes: Int,       // R.drawable.carX
    var isFavourite: Boolean = false
) : Parcelable
