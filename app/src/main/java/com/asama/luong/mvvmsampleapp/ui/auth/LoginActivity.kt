package com.asama.luong.mvvmsampleapp.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.asama.luong.mvvmsampleapp.R
import com.asama.luong.mvvmsampleapp.data.db.AppDatabase
import com.asama.luong.mvvmsampleapp.data.db.entities.User
import com.asama.luong.mvvmsampleapp.data.network.MyApi
import com.asama.luong.mvvmsampleapp.data.repositories.UserRepository
import com.asama.luong.mvvmsampleapp.databinding.ActivityLoginBinding
import com.asama.luong.mvvmsampleapp.ui.home.HomeActivity
import com.asama.luong.mvvmsampleapp.util.hide
import com.asama.luong.mvvmsampleapp.util.show
import com.asama.luong.mvvmsampleapp.util.snackbar
import com.asama.luong.mvvmsampleapp.util.toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), AuthListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val api = MyApi()
        val db = AppDatabase(this)
        val repository = UserRepository(api, db)
        val factory = AuthViewModelFactory(repository)

        val binding : ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        val viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)

        binding.viewmodel = viewModel

        viewModel.authListener = this

        viewModel.getLoggedInUser().observe(this, Observer {user ->
            if (user != null) {
                Intent(this, HomeActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        })
    }

    override fun onStarted() {
        progress_bar.show()
        toast("Login Started")
    }

    override fun onSuccess(user: User) {
        progress_bar.hide()
    }

    override fun onFailure(message: String) {
        progress_bar.hide()
        root_layout.snackbar(message)
    }

}
