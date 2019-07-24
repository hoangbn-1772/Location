package com.example.locationsample

import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_fused_provider.*

class FusedProviderActivity : AppCompatActivity(), View.OnClickListener {

    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val geocoder by lazy { Geocoder(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fused_provider)
        initComponents()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdate()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_get_location_fused -> getLastLocation()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initComponents() {
        btn_get_location_fused.setOnClickListener(this)
    }

    private fun createLocationRequest(): LocationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun getLocationSettings() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(createLocationRequest())

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {

        }
        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    it.startResolutionForResult(this@FusedProviderActivity, REQUEST_CHECK_SETTINGS)
                } catch (e: IntentSender.SendIntentException) {
                    //Ignore the error
                }
            }
        }
    }

    private fun startLocationUpdate() {
        if (ContextCompat.checkSelfPermission(this, MainActivity.PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, MainActivity.PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationProviderClient.requestLocationUpdates(createLocationRequest(), locationCallback, null)
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun getLastLocation() {
        if (
            ContextCompat.checkSelfPermission(this, MainActivity.PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, MainActivity.PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            setupView(it)
        }

        getLocationSettings()
    }

    private fun setupView(location: Location) {
        val addrs = geocoder.getFromLocation(location.latitude, location.longitude, MAX_RESULT)
        text_latitude_fused.text = location.latitude.toString()
        text_longitude_fused.text = location.longitude.toString()
        text_provider_fused.text = location.provider
        text_address_fused.text = addrs[0].getAddressLine(0)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            locationResult ?: return
            for (location in locationResult.locations) {
                setupView(location)
            }
        }

        override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
            super.onLocationAvailability(locationAvailability)
        }
    }

    companion object {
        private const val MAX_RESULT = 1
        private const val REQUEST_CHECK_SETTINGS = 102
    }
}
