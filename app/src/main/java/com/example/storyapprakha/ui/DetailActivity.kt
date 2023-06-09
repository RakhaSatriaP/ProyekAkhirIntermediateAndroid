package com.example.storyapprakha.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bumptech.glide.Glide
import com.example.storyapprakha.data.user.UserPreference
import com.example.storyapprakha.databinding.ActivityDetailBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tokenUser")

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory(UserPreference.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = "Detail"
            setDisplayHomeAsUpEnabled(true)
        }

        val id = intent.getStringExtra("id_story")
        val foto = intent.getStringExtra("foto")
        Glide.with(this).load(foto).into(binding.foto)
        if (id != null) {
            viewModel.getDetail(id)
        }
        viewModel.detail.observe(this) {
            binding.nama.text = it.name
            binding.desc.text = it.description

        }
        viewModel.isLoading.observe(this) {
            if (it == true) {
                binding.progressBar4.visibility = View.VISIBLE
            } else {
                binding.progressBar4.visibility = View.INVISIBLE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}