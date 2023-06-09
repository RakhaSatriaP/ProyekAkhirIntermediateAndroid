package com.example.storyapprakha.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapprakha.data.user.UserPreference
import com.example.storyapprakha.databinding.ActivityUploadBinding
import com.example.storyapprakha.utils.createCustomTempFile
import com.example.storyapprakha.utils.rotateFile
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tokenUser")

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private val viewModel: UploadViewModel by viewModels {
        ViewModelFactory(UserPreference.getInstance(dataStore))    }

    private var storyPhoto: File? = null
    private lateinit var currentPhotoPath: String
    private val launchIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                rotateFile(file)
                storyPhoto = file
                binding.imageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = "Upload"
            setDisplayHomeAsUpEnabled(true)
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        binding.Upload.setOnClickListener {
            val desc = binding.DescEditText.text.toString()
            viewModel.upload(storyPhoto, desc)
        }

        viewModel.isLoading.observe(this) {
            if (it == true) {
                binding.progressBar5.visibility = View.VISIBLE
            } else {
                binding.progressBar5.visibility = View.INVISIBLE
            }
        }

        viewModel.isError.observe(this) {
            if (it == false) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Gagal upload", Toast.LENGTH_SHORT).show()
            }
        }

        binding.camera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)
            createCustomTempFile(application).also {
                val photoUri: Uri = FileProvider.getUriForFile(
                    this@UploadActivity,
                    "com.example.storyapprakha",
                    it
                )

                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                launchIntentCamera.launch(intent)
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