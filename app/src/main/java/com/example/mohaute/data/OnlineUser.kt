package com.example.mohaute.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OnlineUser(
    val user: UserDetails
): Parcelable

@Parcelize
data class UserId(
    val userId: String
): Parcelable

@Parcelize
data class UserEmail(
    val email: String,
    val password: String
): Parcelable


@Parcelize
data class Email(
    val email: String
): Parcelable

@Parcelize
data class UserResponse(
    val user: UserDetails
): Parcelable

@Parcelize
data class Notice(
    val _id: String,
    val email: String,
    val name: String,
    val details: String,
    val date: String,
    val __v: Int
): Parcelable

@Parcelize
data class BackUp(
    val userResponse: UserResponse,
    val addMeasurements: AddMeasurements
) : Parcelable

@Parcelize
data class MeasurementResponse(
    val measurements: List<Measurements>
) : Parcelable

@Parcelize
data class NoticeResponse(
    val notice: List<Notice>
) : Parcelable

@Parcelize
data class UserDetails(
    val _id: String,
    val email: String,
    val businessName: String,
    val number: String,
    val password: String,
    val image: String,
    val dateAdded: String,
    val __v: Int
):Parcelable

@Parcelize
data class Measurements(
    val _id: String,
    val id: String,
    val name: String,
    val shoulder: String,
    val sleeve: String,
    val neck: String,
    val chest: String,
    val tommy: String,
    val top: String,
    val bicep: String,
    val cuff: String,
    val waist: String,
    val thigh: String,
    val trouser: String,
    val ankle: String,
    val date: String,
    val __v: Int
):Parcelable


@Parcelize
data class AddMeasurements(
    val name: String,
    val shoulder: String,
    val sleeve: String,
    val neck: String,
    val chest: String,
    val tommy: String,
    val top: String,
    val bicep: String,
    val cuff: String,
    val waist: String,
    val thigh: String,
    val trouser: String,
    val ankle: String,
    val date: String
):Parcelable

@Parcelize
data class UpdateMeasurements(
    val name: String,
    val shoulder: String,
    val sleeve: String,
    val neck: String,
    val chest: String,
    val tommy: String,
    val top: String,
    val bicep: String,
    val cuff: String,
    val waist: String,
    val thigh: String,
    val trouser: String,
    val ankle: String,
    val date: String
):Parcelable









