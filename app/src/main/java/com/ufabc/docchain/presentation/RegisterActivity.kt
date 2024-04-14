package com.ufabc.docchain.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ufabc.docchain.R
import com.ufabc.docchain.databinding.RegisterBinding

class RegisterActivity : AppCompatActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var binding: RegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.register)

        setupViews()
    }

    private fun setupViews() {
        binding.registerBackButton.setOnClickListener {
            finish()
        }
    }
}