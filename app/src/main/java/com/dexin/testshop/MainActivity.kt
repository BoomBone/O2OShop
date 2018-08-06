package com.dexin.testshop

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.amap.api.services.nearby.*
import com.dexin.testshop.common.lbs.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var latitude = 0.0
    var longitude = 0.0
    private val WRITE_COARSE_LOCATION_REQUEST_CODE = 0x11
    private lateinit var mLbsLayer: ILbsLayer
    private lateinit var mNearByLayer: INearByListener
    var uploadLocation = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()
        initTestMap(savedInstanceState)
        initListener()
    }

    private fun initTestMap(savedInstanceState: Bundle?) {
        mLbsLayer = GaoDeLocationLayerImpl(this)
        mLbsLayer.onCreate(savedInstanceState)
        mLbsLayer.setLocationChangeListener(object : CommonLocationChangeListener {

            override fun onLocationChanged(location: AMapLocation) {
                val code = location.errorCode
                Log.e("main", "定位返回错误码=$code")
                if (code == 0) {
                    //显示定位城市
                    val cityName = location.city
                    //获取地区编码
                    val cityCode = location.adCode
                    //获取纬度
                    latitude = location.latitude
                    //获取精度
                    longitude = location.longitude
                    //存储定位城市
                    Log.e("main", "经度=$latitude，纬度=$longitude")
                    if (uploadLocation) {
                        mNearByLayer.uploadLocation(mInputNumEt.text.toString().trim(), latitude, longitude)
                    }
                }
            }

        })

        /**
         * 检索附近的人
         */
        mNearByLayer = GaoDeNearByLayerImp(this)
        mNearByLayer.onCreate(savedInstanceState)
        mNearByLayer.setNearByListener(object : CommonNearByListener {
            override fun onUserInfoCleared(p0: Int) {

            }

            override fun onNearbyInfoUploaded(p0: Int) {
                //返回码详见文档
                Log.e("main","onNearbyInfoUploaded=$p0")
            }

            override fun onNearbyInfoSearched(nearbySearchResult: NearbySearchResult?, resultCode: Int) {

            }

        })

    }

    private fun initListener() {
        button.setOnClickListener {
            if (mInputNumEt.text.toString().trim().isNotEmpty()) {
                Log.e("main", mInputNumEt.text.toString().trim())
                uploadLocation = true
            } else {
                Toast.makeText(this, "请输入用户id", Toast.LENGTH_SHORT).show()
                Log.e("main", "用户输入为空")
            }
        }
    }

    /*-------------------------NearByListener End-------------------------------------------*/


    /*--------------------------权限请求 Start-----------------------------------------------*/
    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    WRITE_COARSE_LOCATION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }
    /*--------------------------权限请求 End-----------------------------------------------*/

    override fun onResume() {
        super.onResume()
        mLbsLayer.onResume()
    }

    override fun onPause() {
        super.onPause()
        mLbsLayer.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mLbsLayer.onSaveInstanceState(outState)
    }
}