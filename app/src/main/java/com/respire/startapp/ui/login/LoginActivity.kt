package com.respire.startapp.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.respire.startapp.databinding.ActivityLoginBinding
import dagger.android.AndroidInjection
import java.util.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), LifecycleOwner {

    @Inject
    lateinit var vmFactory: LoginViewModel.Factory
    lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initViews()
        viewModel = ViewModelProvider(this, vmFactory).get(LoginViewModel::class.java)
        initUiChangesListeners()
        retrieveEntities()
    }

    private fun initUiChangesListeners() {
        lifecycleScope.launchWhenStarted {
            viewModel.entitiesUiState.collect {
                it.data?.let { data ->
                    Toast.makeText(this@LoginActivity, data.accessToken, Toast.LENGTH_SHORT).show()
                }
                it.error?.printStackTrace()
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.errorUiState.collect {
                it?.let {
                    Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun retrieveEntities() {
        viewModel.getEntities()
    }

    private fun initViews() {

    }
}