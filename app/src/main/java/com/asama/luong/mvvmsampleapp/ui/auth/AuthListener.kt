package com.asama.luong.mvvmsampleapp.ui.auth

import com.asama.luong.mvvmsampleapp.data.db.entities.User

interface AuthListener {
    fun onStarted()
    fun onSuccess(user : User)
    fun onFailure(message: String)
}