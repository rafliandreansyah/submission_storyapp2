package com.dicoding.submission_intermediate_storyapp2.ui.story

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.submission_intermediate_storyapp2.R
import com.dicoding.submission_intermediate_storyapp2.databinding.ActivityMapsStoryBinding
import com.dicoding.submission_intermediate_storyapp2.ui.story.viewmodel.StoryViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import com.dicoding.submission_intermediate_storyapp2.util.Result
import com.google.android.gms.maps.model.LatLngBounds

@AndroidEntryPoint
class MapsStoryActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsStoryBinding
    private val storyViewModel: StoryViewModel by viewModels()
    private val boundBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        hideActionBar()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        getDataStoryLocation()
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

    private fun getDataStoryLocation() {

        storyViewModel.getListStoryLocation().observe(this) { responseResult ->
            when(responseResult) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    responseResult.data?.listStory?.forEach {
                        addMarker(LatLng(it.lat as Double, it.lon as Double), it.name as String)
                    }

                    val bounds: LatLngBounds = boundBuilder.build()
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            resources.displayMetrics.widthPixels,
                            resources.displayMetrics.heightPixels,
                            300
                        )
                    )
                }
                else -> {
                    Toast.makeText(this@MapsStoryActivity, responseResult.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun addMarker(latLon: LatLng, creator: String) {
        mMap.addMarker(
            MarkerOptions()
                .position(latLon)
                .title(creator)
        )
        boundBuilder.include(latLon)
    }

}