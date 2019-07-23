package com.example.locationsample

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_location_base.btn_get_location_1
import kotlinx.android.synthetic.main.activity_location_base.btn_get_location_2
import kotlinx.android.synthetic.main.activity_location_base.text_address
import kotlinx.android.synthetic.main.activity_location_base.text_address_2
import kotlinx.android.synthetic.main.activity_location_base.text_latitude
import kotlinx.android.synthetic.main.activity_location_base.text_latitude_2
import kotlinx.android.synthetic.main.activity_location_base.text_longitude
import kotlinx.android.synthetic.main.activity_location_base.text_longitude_2

class LocationBaseActivity : AppCompatActivity(), View.OnClickListener, UserLocation.LocationResult {

    private val mLocationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private val userLocation by lazy { UserLocation() }
    private val geocoder by lazy { Geocoder(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_base)
        initComponents()
        checkPermission()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getCurrentLocation()
                return
            }
        }
    }

    private fun initComponents() {
        btn_get_location_1.setOnClickListener(this)
        btn_get_location_2.setOnClickListener(this)
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (
                (ContextCompat.checkSelfPermission(this, PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED)
            ) {
                requestPermissions(PERMISSIONS, REQUEST_CODE)
                return
            } else {
//                getCurrentLocation()
            }
        }
    }

    /**
     * option 1
     */
    private fun getCurrentLocation() {
        val status = userLocation.getLocation(this, this)
    }

    private fun setupViewOption1(location: Location) {
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, MAX_RESULT)
        text_latitude.text = location.latitude.toString()
        text_longitude.text = location.longitude.toString()
        text_address.text = addresses[0].getAddressLine(0)
        Log.d("TAG", location.provider)
    }

    /**
     * option 2:
     */
    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        val criteria = Criteria().apply {
            accuracy = Criteria.ACCURACY_COARSE
            isAltitudeRequired = false
            isBearingRequired = false
            isCostAllowed = true
            powerRequirement = Criteria.POWER_LOW
        }

        val provider = mLocationManager.getBestProvider(criteria, true)
        Log.d("TAG", "Best Provider $provider")
        mLocationManager.requestLocationUpdates(provider, 20000, 0F, mLocationListener)
    }

    private fun setupViewOption2(location: Location) {
        val addrs = geocoder.getFromLocation(location.latitude, location.longitude, MAX_RESULT)
        text_latitude_2.text = location.latitude.toString()
        text_longitude_2.text = location.longitude.toString()
        text_address_2.text = addrs[0].getAddressLine(0)
        Log.d("TAG", location.provider)
    }

    private val mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            location?.let { setupViewOption2(it) }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }
    }

    companion object {
        private const val REQUEST_CODE = 101
        private val PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        private const val MAX_RESULT = 1
    }
}
