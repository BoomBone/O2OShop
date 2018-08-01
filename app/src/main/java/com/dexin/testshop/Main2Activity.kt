package com.dexin.testshop

import android.Manifest
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class Main2Activity : AppCompatActivity() {
    internal val WRITE_COARSE_LOCATION_REQUEST_CODE = 0x11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                WRITE_COARSE_LOCATION_REQUEST_CODE)
    }
}
