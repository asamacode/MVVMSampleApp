package com.asama.luong.mvvmsampleapp.data.repositories

import com.asama.luong.mvvmsampleapp.data.db.AppDatabase
import com.asama.luong.mvvmsampleapp.data.db.entities.User
import com.asama.luong.mvvmsampleapp.data.network.MyApi
import com.asama.luong.mvvmsampleapp.data.network.SafeApiRequest
import com.asama.luong.mvvmsampleapp.data.network.responses.AuthResponse

class UserRepository(
    private val api : MyApi,
    private val db : AppDatabase
) : SafeApiRequest(){

    suspend fun userLogin(email: String, password: String) : AuthResponse {
        return apiRequest { api.userLogin(email, password) }
    }

    suspend fun saveUser(user : User) = db.getUserDao().upsert(user)

    fun getUser() = db.getUserDao().getUser()
}