package com.asama.luong.mvvmsampleapp.data.network.responses

import com.asama.luong.mvvmsampleapp.data.db.entities.User

data class AuthResponse(
    val isSuccesful : Boolean?,
    val message : String?,
    val user: User?
)