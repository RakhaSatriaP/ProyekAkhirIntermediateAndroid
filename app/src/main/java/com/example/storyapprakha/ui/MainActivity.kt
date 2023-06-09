package com.example.storyapprakha.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapprakha.data.user.UserPreference
import com.example.storyapprakha.databinding.ActivityMainBinding


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tokenUser")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory(UserPreference.getInstance(dataStore))    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.title = "List Story"
        setContentView(binding.root)
        viewModel.getToken().observe(this) {
            if (it == "") {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }

        binding.logout.setOnClickListener {
            viewModel.logout()
        }

        binding.upload.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        binding.maps.setOnClickListener{
            val intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }

        val adapter = Adapter()
        viewModel.story.observe(this){
            adapter.submitData(lifecycle,it)

        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

    }

}