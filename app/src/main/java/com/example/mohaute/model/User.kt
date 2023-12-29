package com.example.mohaute.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val customerName: String,
    val currentDate: String,
    val shoulder: Double,
    val sleeve: Double,
    val neck: Double,
    val chest: Double,
    val tommy: Double,
    val top: Double,
    val bicep: Double,
    val cuff: Double,
    val waist: Double,
    val thigh: Double,
    val trouser: Double,
    val ankle: Double

): Parcelable


