package com.dicoding.submissionaplikasistoryapp.activity

import android.Manifest.permission.CAMERA
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.submissionaplikasistoryapp.R
import com.dicoding.submissionaplikasistoryapp.databinding.ActivityNewStoryBinding
import com.dicoding.submissionaplikasistoryapp.model.NewStoryViewModel
import com.dicoding.submissionaplikasistoryapp.module.createCustomTempFile
import com.dicoding.submissionaplikasistoryapp.module.reduceFileImage
import com.dicoding.submissionaplikasistoryapp.module.uriToFile
import com.dicoding.submissionaplikasistoryapp.repository.MainRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class NewStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewStoryBinding
    private val viewModel by viewModels<NewStoryViewModel>()
    private var getFile: File? = null
    @Inject
    lateinit var mainRepository: MainRepository

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1
        const val AUTHOR = "com.dicoding.submissionaplikasistoryapp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.UI_action_bar_new_story)

        binding.buttonCamera.setOnClickListener {
            val cameraPermission = ContextCompat.checkSelfPermission(this, CAMERA)
            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(CAMERA), REQUEST_CAMERA_PERMISSION)
                }
            } else {
                takePhoto()
            }
        }

        binding.buttonGallery.setOnClickListener {
            startGallery()
        }

        binding.buttonUpload.setOnClickListener {
            if (getFile != null) {
                val description = binding.etStoryDescription.text.toString()

                if (description.isNotEmpty()) {
                    val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
                    val file = reduceFileImage(getFile as File)
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )
                    lifecycleScope.launch {
                        when (viewModel.newStory(descriptionRequestBody, imageMultipart)){
                            is NewStoryViewModel.Result.Success -> {
                                val build: AlertDialog.Builder = AlertDialog.Builder(this@NewStoryActivity)
                                build.setTitle("Message")
                                build.setMessage(getString(R.string.UI_success_upload_story))
                                build.setPositiveButton("OK") { dialog, _ ->
                                    startActivity(Intent(this@NewStoryActivity, MainActivity::class.java))
                                    finish()
                                    dialog.dismiss()
                                }
                                val dialog: AlertDialog = build.create()
                                dialog.show()
                            }
                            is NewStoryViewModel.Result.Error -> {
                                Toast.makeText(this@NewStoryActivity, R.string.UI_error_upload_story, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, R.string.UI_description_required, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, R.string.UI_info_pick_information, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.UI_denied_camera_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.UI_choose_image))
        launcherIntentGallery.launch(chooser)
    }


    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            selectedImg.let { Uri ->
                val myFile = uriToFile(Uri, this)
                getFile = myFile
                binding.ivStoryImagePreview.setImageURI(Uri)
            }
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoUri: Uri = FileProvider.getUriForFile(
                this,
                AUTHOR,
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val result = BitmapFactory.decodeFile(myFile.path)
            getFile = myFile
            binding.ivStoryImagePreview.setImageBitmap(result)
        }
    }
}
