package com.example.mohaute.repository

import androidx.lifecycle.LiveData
import com.example.mohaute.data.UserDao
import com.example.mohaute.model.User
import kotlinx.coroutines.flow.Flow

class Repository(private val userDao: UserDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }
    suspend fun updateUser(user: User){
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }

    suspend fun deleteAllUser(){
        userDao.deleteAllUser()
    }

    fun searchDatabase(searchQuery: String): Flow<List<User>> {
        return userDao.searchDatabase(searchQuery)
    }


}