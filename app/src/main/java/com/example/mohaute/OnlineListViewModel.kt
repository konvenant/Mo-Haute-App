package com.example.mohaute

import android.app.Application
import android.app.DownloadManager.Request
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mohaute.data.AddMeasurements
import com.example.mohaute.data.MeasurementResponse
import com.example.mohaute.data.Measurements
import com.example.mohaute.data.Notice
import com.example.mohaute.data.OnlineUser
import com.example.mohaute.data.UpdateMeasurements
import com.example.mohaute.data.UserResponse
import com.example.mohaute.model.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class OnlineListViewModel(application: Application): AndroidViewModel(application) {


    private val _passwordError = MutableLiveData<Boolean>()
    val passwordError: LiveData<Boolean>
        get() = _passwordError

    private val _otherError = MutableLiveData<Boolean>()
    val otherError: LiveData<Boolean>
        get() = _otherError

    private val _catchError = MutableLiveData<Boolean>()
    val catchError: LiveData<Boolean>
        get() = _catchError

    private val _noticeOtherError = MutableLiveData<Boolean>()
    val noticeOtherError: LiveData<Boolean>
        get() = _noticeOtherError

    private val _noticeCatchError = MutableLiveData<Boolean>()
    val noticeCatchError: LiveData<Boolean>
        get() = _noticeCatchError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean>
        get() = _loggedIn

    private val _measurementAdded = MutableLiveData<Boolean>()
    val measurementAdded: LiveData<Boolean>
        get() = _measurementAdded

    private val _measurementUpdated = MutableLiveData<Boolean>()
    val measurementUpdated: LiveData<Boolean>
        get() = _measurementUpdated

    private val _measurementDeleted = MutableLiveData<Boolean>()
    val measurementDeleted: LiveData<Boolean>
        get() = _measurementDeleted

    private val _userAdded = MutableLiveData<Boolean>()
    val userAdded: LiveData<Boolean>
        get() = _userAdded

    private val _foundUser = MutableLiveData<Boolean>()
    val foundUser: LiveData<Boolean>
        get() = _foundUser

    private val _numAndNameAdded = MutableLiveData<Boolean>()
    val numAndNameAdded: LiveData<Boolean>
        get() = _numAndNameAdded

    private val _updateUser = MutableLiveData<Boolean>()
    val updateUser: LiveData<Boolean>
        get() = _updateUser

    private val _updateUserDetails = MutableLiveData<UserResponse>()
    val updateUserDetails: LiveData<UserResponse>
        get() = _updateUserDetails

    private val _measurements = MutableLiveData<List<Measurements>>()
    val measurements: LiveData<List<Measurements>>
        get() = _measurements


    private val _notice = MutableLiveData<List<Notice>>()
    val notice: LiveData<List<Notice>>
        get() = _notice

    private val _userUpdated = MutableLiveData<UserResponse>()
    val userUpdated: LiveData<UserResponse>
        get() = _userUpdated

    private val _uploadImage = MutableLiveData<UserResponse>()
    val uploadImage: LiveData<UserResponse>
        get() = _uploadImage

    private val _uploadStatus = MutableLiveData<UploadStatus>()
    val uploadStatus: LiveData<UploadStatus>
        get() = _uploadStatus

    private val _forgotUser = MutableLiveData<UserResponse>()
    val forgotUser: LiveData<UserResponse>
        get() = _forgotUser

    private val _forgotStatus = MutableLiveData<Boolean>()
    val forgotStatus: LiveData<Boolean>
        get() = _forgotStatus

    private val _forgotNotFound = MutableLiveData<Boolean>()
    val forgotNotFound: LiveData<Boolean>
        get() = _forgotNotFound

    private val _noticeErrorMessage = MutableLiveData<String>()
    val noticeErrorMessage: LiveData<String>
        get() = _noticeErrorMessage

    fun performMeasurementApiCall(userId: String, apiService: ApiService){
        viewModelScope.launch {
            try {
                val response = apiService.getUserMeasurements(userId)
                if (response.isSuccessful){
                    val measure = response.body()?.measurements
                    _measurements.value = measure!!
                } else{
                    _otherError.value = true
                }
            } catch (e: Exception){
                _catchError.value = true
            } finally {
                _isLoading.value = false
            }
        }

    }
    fun performNoticeApiCall(email: String, apiService: ApiService){
        viewModelScope.launch {
            try {
                val response = apiService.getUserNotices(email)
                if (response.isSuccessful){
                    val notice = response.body()?.notice
                    _notice.value = notice!!
                } else{
                    _noticeOtherError.value = true
                }
            } catch (e: Exception){
                _noticeCatchError.value = true
                _noticeErrorMessage.value = e.message.toString()
            }
        }

    }

    fun addNewMeasurement(apiService: ApiService,url:String,name:String,shoulder:String,
                          sleeve:String,neck:String,chest:String,tommy:String,top:String,bicep:String,
                          cuff:String,waist:String,thigh:String,trouser:String,ankle:String,date:String){
        viewModelScope.launch {
            try {
                val response = apiService.postMeasurement(url,name,shoulder,sleeve,neck,chest,
                    tommy,top,bicep,cuff,waist,thigh,trouser,ankle,date)
                _measurementAdded.value = response.isSuccessful
            } catch (e: Exception){
                _measurementAdded.value = false
            }
        }
    }

    fun updateMeasurement(apiService: ApiService,url:String,name:String,shoulder:String,
                          sleeve:String,neck:String,chest:String,tommy:String,top:String,bicep:String,
                          cuff:String,waist:String,thigh:String,trouser:String,ankle:String,date:String){
        viewModelScope.launch {
            try {
                 val response = apiService.updateMeasurement(url,name,shoulder,sleeve,neck, chest, tommy, top, bicep, cuff, waist, thigh, trouser, ankle, date)
                if (response.isSuccessful){
                    _measurementUpdated.value = true
                }else{
                    _measurementAdded.value = false
                }
            } catch (e:Exception){
        _measurementUpdated.value = true
            }
        }
    }

fun deleteMeasurement(measurementId: String,apiService: ApiService){
    viewModelScope.launch {
        try {
            val response = apiService.deleteMeasurement(measurementId)
            _measurementDeleted.value = response.isSuccessful
        } catch (e:Exception){
            _measurementDeleted.value = false
        }
    }
}

    fun addUser(apiService: ApiService, url: String,email:String,password:String){
        viewModelScope.launch {
            try {
                val response = apiService.addUser(url,email,password)
                if (response.isSuccessful){
                    _userAdded.value = true
                } else{
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    val errorMessage = error?.message ?: "Unknown error"
//                            && errorMessage == "Incorrect Password"
                    if(response.code() == 401 ){
                        _foundUser.value = true
                    } else{
                        _userAdded.value = false
                    }

                }

            } catch (e:Exception){
                _userAdded.value = false
            }
        }
    }

    fun addNumAndName(apiService: ApiService,url: String,name: String,number: String){
        viewModelScope.launch {
            try {
                val response = apiService.addNumberAndName(url,name, number)
                val user = response.body()
                _numAndNameAdded.value = response.isSuccessful
                _userUpdated.value =  user!!
            } catch (e:Exception){
                _numAndNameAdded.value = false
            }
        }
    }

    fun updateUser(apiService: ApiService,url: String,name: String,number: String,password: String){
        viewModelScope.launch {
            try {
                val response = apiService.updateUser(url,name, number, password)
                val user = response.body()
                _updateUser.value = response.isSuccessful
                _updateUserDetails.value = user!!
            } catch (e:Exception){
                _updateUser.value = false
            }
        }
    }

    fun updateImage(apiService: ApiService,url: String,imageFile: File){
       val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(),imageFile)
        val imagePart = MultipartBody.Part.createFormData("image",imageFile.name,requestFile)

        _uploadStatus.value = UploadStatus.InProgress

        viewModelScope.launch {
            try {
                val response = apiService.updateImage(url,imagePart)
                if (response.isSuccessful){
                    _uploadStatus.postValue(UploadStatus.Success)
                    val data = response.body()
                    _uploadImage.value = data!!
                } else{
                    _uploadStatus.postValue(UploadStatus.Failure("Image upload failed"))
                }
            } catch (e: Exception){
                e.printStackTrace()
                _uploadStatus.postValue(UploadStatus.Failure("Server Error"))
            }
        }
    }


    fun forgoPassword(apiService: ApiService,url: String,email: String){
        viewModelScope.launch {
            try {
                val response = apiService.forgotPassword(url, email)
                if (response.isSuccessful){
                    _forgotStatus.value = true
                    val data = response.body()
                    _forgotUser.value = data!!
                } else{
                    if(response.code() == 401 ){
                        _forgotNotFound.value = true
                    } else{
                        _forgotStatus.value = false
                    }
                }
            } catch (e:Exception){
               _forgotStatus.value = false
            }
        }
    }
    sealed class UploadStatus{
        object InProgress : UploadStatus()
        object Success : UploadStatus()
        data class Failure(val errorMessage: String): UploadStatus()
    }
}











