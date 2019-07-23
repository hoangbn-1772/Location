package com.example.locationsample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_geocoder_demo -> moveToScreen(GeocoderActivity::class.java)
            R.id.btn_location_base -> moveToScreen(LocationBaseActivity::class.java)
        }
    }

    private fun initComponents() {
        btn_geocoder_demo.setOnClickListener(this)
        btn_location_base.setOnClickListener(this)
    }

    private fun moveToScreen(screen: Class<*>) {
        Intent(this, screen).apply {
            startActivity(this)
        }
    }
}
