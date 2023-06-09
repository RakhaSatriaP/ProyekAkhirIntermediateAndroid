package com.example.storyapprakha.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapprakha.R
import com.example.storyapprakha.data.user.UserPreference
import com.example.storyapprakha.databinding.ActivityRegisterBinding


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tokenUser")

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        ViewModelFactory(UserPreference.getInstance(dataStore))    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = "Register"
            setDisplayHomeAsUpEnabled(true)
        }

        binding.RegisterButton.isEnabled = false
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString().trim()
                binding.RegisterButton.isEnabled = password.length >= 8
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.RegisterButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isEmpty()) {
                binding.emailEditText.error = getString(R.string.emailkosong)
            } else if (password.isEmpty()) {
                binding.passwordEditText.error = getString(R.string.passwordkosong)
            } else if (name.isEmpty()) {
                binding.nameTextView.error = getString(R.string.namakosong)
            } else {
                if (password.length >= 8) {
                    viewModel.register(name, email, password)
                }
            }
        }

        viewModel.isError.observe(this) {
            if (it == false) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        viewModel.isLoading.observe(this) {
            if (it == true) {
                binding.progressBar2.visibility = View.VISIBLE
            } else {
                binding.progressBar2.visibility = View.INVISIBLE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}