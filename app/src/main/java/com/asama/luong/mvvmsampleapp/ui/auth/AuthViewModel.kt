package com.asama.luong.mvvmsampleapp.ui.auth

import android.content.Intent
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
    var name: String? = null
    var passwordConfirm: String? = null
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

    fun onSignupButtonClick(view: View) {
        authListener?.onStarted()
        if (name.isNullOrEmpty()) {
            authListener?.onFailure("Name is required")
            return
        }
        if (email.isNullOrEmpty()) {
            authListener?.onFailure("Email is required")
            return
        }

        if (password.isNullOrEmpty()) {
            authListener?.onFailure("Please enter a password")
            return
        }
        if (passwordConfirm != password) {
            authListener?.onFailure("Password did not match")
            return
        }

        Coroutines.main {
            try {
                val authResponse = userRepository.userSignup(name!!,email!!, password!!)
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

    fun onSignup(view: View) {
        Intent(view.context, SignUpActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun onLogin(view: View) {
        Intent(view.context, LoginActivity::class.java).also {
            view.context.startActivity(it)
        }
    }
}