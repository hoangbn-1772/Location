package com.example.locationsample

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_location_base.*

class LocationBaseActivity : AppCompatActivity(), View.OnClickListener, UserLocation.LocationResult {

    private val userLocation by lazy { UserLocation() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_base)
        initComponents()
        checkPermission()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_get_current_location -> getCurrentLocation()
        }
    }

    override fun getLocation(location: Location?) {
        location ?: return
        Log.d("TAG", "Lat: ${location.latitude}")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
                return
            }
        }
    }

    private fun initComponents() {
        btn_get_current_location.setOnClickListener(this)
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (
                (ContextCompat.checkSelfPermission(this, PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED)
            ) {
                requestPermissions(PERMISSIONS, REQUEST_CODE)
                return
            } else {
                getCurrentLocation()
            }
        }
    }

    private fun getCurrentLocation() {
        val status = userLocation.getLocation(this, this)
        Log.d("TAG", status.toString())
    }

    companion object {
        private const val REQUEST_CODE = 101
        private val PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
