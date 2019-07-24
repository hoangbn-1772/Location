package com.example.locationsample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
        initComponents()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_geocoder_demo -> moveToScreen(GeocoderActivity::class.java)
            R.id.btn_location_base -> moveToScreen(LocationBaseActivity::class.java)
            R.id.btn_google_service_demo -> moveToScreen(FusedProviderActivity::class.java)
        }
    }

    private fun initComponents() {
        btn_geocoder_demo.setOnClickListener(this)
        btn_location_base.setOnClickListener(this)
        btn_google_service_demo.setOnClickListener(this)
    }

    private fun moveToScreen(screen: Class<*>) {
        Intent(this, screen).apply {
            startActivity(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return
            }
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (
                (ContextCompat.checkSelfPermission(this, PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED)) {

                requestPermissions(PERMISSIONS, REQUEST_CODE)
                return
            } else {
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 101
        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}
