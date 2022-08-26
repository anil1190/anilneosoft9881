package com.example.myapplication.ui.component

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import com.example.myapplication.data.Resource
import com.example.myapplication.data.dto.LoginResponse
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.ui.BaseActivity
import com.example.myapplication.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private val loginViewModel : LoginViewModel by viewModels()
    private lateinit var binding : ActivityLoginBinding

    override fun observeViewModel() {
        observe(loginViewModel.loginLiveData, ::handleLoginResult)
        observeSnackBarMessages(loginViewModel.showSnackBar)
        observeToast(loginViewModel.showToast)
    }

    override fun initViewBinding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.login.setOnClickListener{ doLogin() }
    }

    private fun doLogin(){
       // loginViewModel.doLogin(binding.username.toString().trim(), binding.password.toString().trim())
        loginViewModel.doLogin("anil@gmail.com", "anil")
    }

   private fun handleLoginResult(status : Resource<LoginResponse>){
         when(status){
              is Resource.Loading -> binding.loaderView.toVisible()
              is Resource.Success -> status.data?.let {
                  binding.loaderView.toGone()
                  navigateToMainScreen()
              }
             is Resource.DataError -> {
                 binding.loaderView.toGone()
                // status.errorCode?.let { loginViewModel.showToastMessage(it) }
                 navigateToMainScreen()
             }
         }
   }

    private fun navigateToMainScreen() {
        val nextScreenIntent = Intent(this, MainActivity::class.java)
        startActivity(nextScreenIntent)
        finish()
    }

    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }

}