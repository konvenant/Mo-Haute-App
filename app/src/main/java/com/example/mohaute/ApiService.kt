package com.example.mohaute

import android.media.Image
import com.example.mohaute.data.AddMeasurements
import com.example.mohaute.data.MeasurementResponse
import com.example.mohaute.data.Measurements
import com.example.mohaute.data.NoticeResponse
import com.example.mohaute.data.OnlineUser
import com.example.mohaute.data.UpdateMeasurements
import com.example.mohaute.data.UserDetails
import com.example.mohaute.data.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {
    @GET("users/{userEmail}/{password}")
    suspend fun getUserByIdAndPassword(@Path("userEmail")userEmail: String, @Path("password") password:String): Response<UserResponse>

    @GET("measurements/{userId}")
    suspend fun getUserMeasurements(@Path("userId")userId: String): Response<MeasurementResponse>

    @GET("notice/{email}")
    suspend fun getUserNotices(@Path("email")email: String): Response<NoticeResponse>

    @FormUrlEncoded
    @POST
    suspend fun postMeasurement(
        @Url url: String,
        @Field("name") name: String,
        @Field("shoulder") shoulder: String,
        @Field("sleeve") sleeve:String,
        @Field("neck") neck: String,
        @Field("chest") chest:String,
        @Field("tommy") tommy: String,
        @Field("top") top:String,
        @Field("bicep") bicep: String,
        @Field("cuff") cuff:String,
        @Field("waist") waist:String,
        @Field("thigh") thigh:String,
        @Field("trouser") trouser:String,
        @Field("ankle") ankle:String,
        @Field("date") date:String
    ): Response<Unit>

    @FormUrlEncoded
    @PUT
    suspend fun updateMeasurement(
        @Url url: String,
        @Field("name") name: String,
        @Field("shoulder") shoulder: String,
        @Field("sleeve") sleeve:String,
        @Field("neck") neck: String,
        @Field("chest") chest:String,
        @Field("tommy") tommy: String,
        @Field("top") top:String,
        @Field("bicep") bicep: String,
        @Field("cuff") cuff:String,
        @Field("waist") waist:String,
        @Field("thigh") thigh:String,
        @Field("trouser") trouser:String,
        @Field("ankle") ankle:String,
        @Field("date") date:String
    ): Response<Unit>

    @DELETE("customer/{measurementId}")
    suspend fun deleteMeasurement(
        @Path("measurementId") measurementId: String
    ) : Response<Unit>


    @FormUrlEncoded
    @POST
    suspend fun addUser(
        @Url url: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<Unit>

    @FormUrlEncoded
    @PATCH
    suspend fun addNumberAndName(
        @Url url: String,
        @Field("businessName") name: String,
        @Field("number") number: String
    ): Response<UserResponse>

    @FormUrlEncoded
    @PATCH
    suspend fun updateUser(
        @Url url: String,
        @Field("businessName") name: String,
        @Field("number") number: String,
        @Field("password") password: String
    ): Response<UserResponse>

    @Multipart
    @POST
    suspend fun updateImage(
        @Url url: String,
        @Part image: MultipartBody.Part
    ): Response<UserResponse>

    @FormUrlEncoded
    @POST
    suspend fun forgotPassword(
        @Url url: String,
        @Field("email") email: String
    ): Response<UserResponse>
}






