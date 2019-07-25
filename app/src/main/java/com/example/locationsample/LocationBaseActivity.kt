package com.example.locationsample

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.*
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_location_base.*

class LocationBaseActivity : AppCompatActivity(), View.OnClickListener, UserLocation.LocationResult {

    private val mLocationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private val userLocation by lazy { UserLocation() }
    private val geocoder by lazy { Geocoder(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_base)
        initComponents()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_get_location_1 -> getCurrentLocation()
            R.id.btn_get_location_2 -> requestLocation()
        }
    }

    override fun getLocation(location: Location?) {
        location?.let { setupViewOption1(it) }
    }

    private fun initComponents() {
        btn_get_location_1.setOnClickListener(this)
        btn_get_location_2.setOnClickListener(this)
    }

    // option 1
    private fun getCurrentLocation() {
        val status = userLocation.getLocation(this, this)
    }

    private fun setupViewOption1(location: Location) {
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, MAX_RESULT)
        text_latitude.text = location.latitude.toString()
        text_longitude.text = location.longitude.toString()
        text_address.text = addresses[0].getAddressLine(0)
        text_location_provider.text = location.provider
    }

    // option 2
    @SuppressLint("MissingPermission")
    private fun requestLocation() {

        /*this criteria will settle for high accuracy, high power, and cost*/
        val criteria = Criteria().apply {
            accuracy = Criteria.ACCURACY_FINE
            isAltitudeRequired = false
            isBearingRequired = false
            isCostAllowed = true
            powerRequirement = Criteria.POWER_MEDIUM
        }

        val provider = mLocationManager.getBestProvider(criteria, true)
        mLocationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, mLocationListener)
    }

    private fun setupViewOption2(location: Location) {
        val addrs = geocoder.getFromLocation(location.latitude, location.longitude, MAX_RESULT)
        text_latitude_2.text = location.latitude.toString()
        text_longitude_2.text = location.longitude.toString()
        text_address_2.text = addrs[0].getAddressLine(0)
        text_provider_2.text = location.provider
    }

    private val mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            location?.let { setupViewOption2(it) }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
            // try switching to a different provider.
        }

        override fun onProviderDisabled(provider: String?) {
            // try switching to a different provider.
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }

    companion object {
        private const val MAX_RESULT = 1
        private const val MIN_TIME = 20000L
        private const val MIN_DISTANCE = 0F
    }
}
