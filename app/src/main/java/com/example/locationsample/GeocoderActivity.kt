package com.example.locationsample

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_geocoder.*

class GeocoderActivity : AppCompatActivity(), View.OnClickListener {

    private val geocoder by lazy { Geocoder(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geocoder)
        initComponents()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_get_from_location -> getFromLocation()
            R.id.btn_get_location_name_1 -> fromLocationName1()
            R.id.btn_get_location_name -> fromLocation()
        }
    }

    private fun initComponents() {
        btn_get_from_location.setOnClickListener(this)
        btn_get_location_name_1.setOnClickListener(this)
        btn_get_location_name.setOnClickListener(this)
    }

    private fun getFromLocation() {
        val address = geocoder.getFromLocation(LATITUDE, LONGITUDE, MAX_RESULT)
        Log.d("TAG", address.size.toString())
    }

    private fun fromLocation() {
        val addresses = geocoder.getFromLocationName(LOCATION_NAME, MAX_RESULT)
        Log.d("TAG", addresses.size.toString())
    }

    private fun fromLocationName1() {
        val addresses = geocoder.getFromLocationName(
            LOCATION_NAME,
            MAX_RESULT,
            LOWER_LEFT_LATITUDE,
            LOWER_LEFT_LONGITUDE,
            UPPER_RIGHT_LATITUDE, UPPER_RIGHT_LONGITUDE)

        Log.d("TAG", addresses.size.toString())
    }

    companion object {
        private const val LATITUDE = 21.0168415
        private const val LONGITUDE = 105.7837524
        private const val MAX_RESULT = 5

        private const val LOCATION_NAME = "Keangnam Landmark Tower 72, Phạm Hùng, Keangnam, Mễ Trì, Nam Từ Liêm, Hanoi"
        private const val LOWER_LEFT_LATITUDE = 21.016689
        private const val LOWER_LEFT_LONGITUDE = 105.782941
        private const val UPPER_RIGHT_LATITUDE = 21.017014
        private const val UPPER_RIGHT_LONGITUDE = 105.784462
    }
}
