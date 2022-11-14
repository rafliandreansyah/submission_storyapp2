package com.dicoding.submission_intermediate_storyapp2.ui.camerax

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.dicoding.submission_intermediate_storyapp2.constant.CAMERA_X_FILE
import com.dicoding.submission_intermediate_storyapp2.constant.IS_CAMERA_BACK
import com.dicoding.submission_intermediate_storyapp2.databinding.ActivityCameraXactivityBinding
import com.dicoding.submission_intermediate_storyapp2.ui.story.CreateStoryActivity.Companion.CAMERA_X_RESULT
import com.dicoding.submission_intermediate_storyapp2.util.createFile
import com.dicoding.submission_intermediate_storyapp2.util.rotateFile

class CameraXActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraXactivityBinding
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraXactivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        hideActionBar()
        with(binding){
            imgSwitchCamera.setOnClickListener {
                switchCamera()
            }

            imgPickImage.setOnClickListener {
                takePhoto()
            }
        }

        startCamerax()

    }

    private fun hideActionBar() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun switchCamera() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA

        startCamerax()
    }

    private fun startCamerax(){

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview: Preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.pvCamerax.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            }catch (e: Exception) {
                e.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(this))

    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createFile(application)

        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOption, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val intent = Intent()
                val fileRotate = rotateFile(photoFile, cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA, this@CameraXActivity)
                intent.putExtra(CAMERA_X_FILE, fileRotate)
                intent.putExtra(IS_CAMERA_BACK, cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                setResult(CAMERA_X_RESULT, intent)
                finish()
            }

            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
                Log.e(CameraXActivity::class.java.simpleName, "Error ${exception.message}")
                Toast.makeText(this@CameraXActivity, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
            }

        })

    }


}