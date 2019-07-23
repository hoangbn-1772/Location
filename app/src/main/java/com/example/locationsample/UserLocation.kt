package com.example.locationsample

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import java.lang.Exception
import java.util.*

class UserLocation {

    private val timer by lazy { Timer() }
    private var lm: LocationManager? = null
    private var locationResult: LocationResult? = null
    private var gps_enabled: Boolean = false
    private var network_enable: Boolean = false

    private val locationListenerGps: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            timer.cancel()
            location?.let { locationResult?.getLocation(it) }
            lm?.removeUpdates(this)
            lm?.removeUpdates(locationListenerNetwork)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }
    }

    private val locationListenerNetwork: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            timer.cancel()
            location?.let { locationResult?.getLocation(it) }
            lm?.removeUpdates(this)
            lm?.removeUpdates(locationListenerGps)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(context: Context, result: LocationResult): Boolean {
        locationResult = result
        if (lm == null) {
            lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        try {
            gps_enabled = lm?.isProviderEnabled(LocationManager.GPS_PROVIDER)!!
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            network_enable = lm?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)!!
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (!gps_enabled && !network_enable) return false

        if (gps_enabled) {
            lm?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0F, locationListenerGps)
        }

        if (network_enable) {
            lm?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0F, locationListenerNetwork)
        }

        timer.schedule(GetLastLocation(), 20000)
        return true
    }

    inner class GetLastLocation : TimerTask() {
        @SuppressLint("MissingPermission")
        override fun run() {
            lm?.removeUpdates(locationListenerGps)
            lm?.removeUpdates(locationListenerNetwork)

            var networkLocation: Location? = null
            var gpsLocation: Location? = null

            if (gps_enabled) {
                gpsLocation = lm?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }

            if (network_enable) {
                networkLocation = lm?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            if (networkLocation != null && gpsLocation != null) {
                if (gpsLocation.time > networkLocation.time) {
                    locationResult?.getLocation(gpsLocation)
                } else {
                    locationResult?.getLocation(networkLocation)
                }
                return
            }

            gpsLocation?.let {
                locationResult?.getLocation(it)
                return
            }

            networkLocation?.let {
                locationResult?.getLocation(it)
                return
            }

            locationResult?.getLocation(null)
        }
    }

    interface LocationResult {
        fun getLocation(location: Location?)
    }
}
