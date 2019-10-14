package com.asama.luong.mvvmsampleapp.ui.auth

import android.view.View
import androidx.lifecycle.ViewModel
import com.asama.luong.mvvmsampleapp.data.repositories.UserRepository
import com.asama.luong.mvvmsampleapp.util.ApiException
import com.asama.luong.mvvmsampleapp.util.Coroutines
import com.asama.luong.mvvmsampleapp.util.NoInternetException

class AuthViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    var email: String? = null
    var password: String? = null
    var authListener: AuthListener? = null

    fun getLoggedInUser() = userRepository.getUser()

    fun onLoginButtonClick(view: View) {
        authListener?.onStarted()
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            //
            authListener?.onFailure("Invalid email or password")
            return
        }

        Coroutines.main {
            try {
                val authResponse = userRepository.userLogin(email!!, password!!)
                authResponse.user?.let {
                    authListener?.onSuccess(it)
                    userRepository.saveUser(it)
                    return@main
                }
                authListener?.onFailure(authResponse.message!!)
            } catch (e : ApiException) {
                authListener?.onFailure(e.message!!)
            } catch (e : NoInternetException) {
                authListener?.onFailure(e.message!!)
            }
        }


    }
}