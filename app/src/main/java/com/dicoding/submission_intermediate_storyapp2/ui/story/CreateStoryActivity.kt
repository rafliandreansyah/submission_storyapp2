package com.dicoding.submission_intermediate_storyapp2.ui.story

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dicoding.submission_intermediate_storyapp2.R
import com.dicoding.submission_intermediate_storyapp2.constant.CAMERA_X_FILE
import com.dicoding.submission_intermediate_storyapp2.constant.IS_CAMERA_BACK
import com.dicoding.submission_intermediate_storyapp2.databinding.ActivityCreateStoryBinding
import com.dicoding.submission_intermediate_storyapp2.databinding.BottomDialogChooseUploadImageBinding
import com.dicoding.submission_intermediate_storyapp2.ui.auth.LoginActivity
import com.dicoding.submission_intermediate_storyapp2.ui.camerax.CameraXActivity
import com.dicoding.submission_intermediate_storyapp2.ui.story.viewmodel.StoryViewModel
import com.dicoding.submission_intermediate_storyapp2.util.reduceFileImage
import com.dicoding.submission_intermediate_storyapp2.util.uriToFile
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File

class CreateStoryActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        private const val REQUEST_CODE_PERMISSION = 101
    }

    private lateinit var binding: ActivityCreateStoryBinding
    private var imgFile: File? = null
    private val storyViewModel: StoryViewModel by viewModels()

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ activityResult ->
        if (activityResult.resultCode == CAMERA_X_RESULT) {
            val myFile = activityResult?.data?.getSerializableExtra(CAMERA_X_FILE) as File
            val isCameraBack = activityResult.data?.getBooleanExtra(IS_CAMERA_BACK, true) as Boolean

            imgFile = reduceFileImage(myFile)

            /*val rotateBitmap = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isCameraBack
            )*/

            binding.imgAddStory.setImageBitmap(BitmapFactory.decodeFile(myFile.path))

        }
    }

    private val launcherImageGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            val myUri = activityResult?.data?.data as Uri
            val createFile = uriToFile(myUri, this@CreateStoryActivity)

            imgFile = reduceFileImage(createFile)

            binding.imgAddStory.setImageURI(myUri)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Story"

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSION
            )
        }

        with(binding) {
            llAddImage.setOnClickListener {
                if (!allPermissionsGranted()) {
                    ActivityCompat.requestPermissions(
                        this@CreateStoryActivity, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSION
                    )
                }else {
                    showDialogSelectAddImage()
                }

            }

            buttonAdd.setOnClickListener {
                validation()
            }
        }

        binding.edAddDescription.addTextChangedListener(watcher())
        statusUpload()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun watcher() : TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    binding.edAddDescription.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                binding.buttonAdd.isEnabled =
                    binding.edAddDescription.text.toString().trim().isNotEmpty()
            }

        }
    }

    private fun validation() {
        val description = binding.edAddDescription.text.toString().trim()

        if (description.isEmpty()) {
            binding.edAddDescription.error = "Deskripsi tidak boleh kosong"
            Toast.makeText(this, "Deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        if (imgFile == null || !imgFile!!.exists()) {
            Toast.makeText(this, "Foto tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading(true)
        storyViewModel.addStory(description, imgFile as File)

    }

    private fun statusUpload(){
        storyViewModel.addStoryData.observe(this) { responseGeneral ->
            if (responseGeneral != null) {
                isLoading(false)
                if (!responseGeneral.error) {
                    val intent = Intent(this, ListStoryActivity::class.java)
                    intent.putExtra("reload", true)
                    startActivity(intent)
                    finish()
                }
                else {
                    if (responseGeneral.message.equals("unauthorized")) {
                        Toast.makeText(this@CreateStoryActivity, "Your token expired, please relogin!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CreateStoryActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    else {
                        Toast.makeText(this@CreateStoryActivity, responseGeneral.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun startCameraX(){
        val intent = Intent(this, CameraXActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startImageGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherImageGallery.launch(chooser)
    }

    private fun showDialogSelectAddImage() {
        val binding: BottomDialogChooseUploadImageBinding = BottomDialogChooseUploadImageBinding.inflate(
            LayoutInflater.from(this))
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogStyle)
        val view = binding.root

        with(binding) {

            llCamera.setOnClickListener {
                startCameraX()
                bottomSheetDialog.dismiss()
            }

            llGallery.setOnClickListener {
                startImageGallery()
                bottomSheetDialog.dismiss()
            }

            imgClose.setOnClickListener {
                bottomSheetDialog.dismiss()
            }

        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    private fun allPermissionsGranted(): Boolean = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Tidak mendapat permission!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isLoading(isL: Boolean) {
        if (isL) {
            binding.rlLoading.visibility = View.VISIBLE
        } else {
            binding.rlLoading.visibility = View.GONE
        }
    }

}