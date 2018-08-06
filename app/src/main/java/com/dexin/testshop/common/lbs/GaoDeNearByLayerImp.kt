package com.dexin.testshop.common.lbs

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.nearby.NearbySearch
import com.amap.api.services.nearby.NearbySearchFunctionType
import com.amap.api.services.nearby.NearbySearchResult
import com.amap.api.services.nearby.UploadInfo

/**
 * @author Ting
 * @date 2018/8/6
 */
class GaoDeNearByLayerImp(context: Context) : INearByListener {

    private var mNearbySearch: NearbySearch = NearbySearch.getInstance(context)
    var mNearByListener: CommonNearByListener? = null

    init {

    }

    override fun setNearByListener(commonNearByListener: CommonNearByListener) {
        mNearByListener = commonNearByListener
    }

    override fun onCreate(state: Bundle?) {
        setUpNearBy()
    }

    override fun onResume() {
    }

    override fun onSaveInstanceState(outState: Bundle?) {
    }

    override fun onPause() {
    }

    override fun onDestroy() {
    }

    private fun setUpNearBy() {
        mNearbySearch.addNearbyListener(object : NearbySearch.NearbyListener {
            override fun onUserInfoCleared(p0: Int) {
                if (mNearByListener != null) {
                    mNearByListener!!.onUserInfoCleared(p0)
                }
            }

            override fun onNearbyInfoUploaded(p0: Int) {
                if (mNearByListener != null) {
                    mNearByListener!!.onNearbyInfoUploaded(p0)
                }

            }

            override fun onNearbyInfoSearched(p0: NearbySearchResult?, p1: Int) {
                if (mNearByListener != null) {
                    mNearByListener!!.onNearbyInfoSearched(p0, p1)
                }

            }

        })
    }

    override fun searchNear(latitude: Double, longitude: Double) {
        //设置搜索条件
        val query = NearbySearch.NearbyQuery()
        //设置搜索的中心点
        query.centerPoint = LatLonPoint(latitude, longitude)
        //设置搜索的坐标体系
        query.coordType = NearbySearch.AMAP
        //设置搜索半径
        query.radius = 10000
        //设置查询的时间
        query.timeRange = 10000
        //设置查询的方式驾车还是距离
        query.setType(NearbySearchFunctionType.DISTANCE_SEARCH)
        //调用异步查询接口
        mNearbySearch.searchNearbyInfoAsyn(query)
    }

    override fun uploadLocation(userId: String, latitude: Double, longitude: Double) {
        mNearbySearch.startUploadNearbyInfoAuto({
            val loadInfo = UploadInfo()
            loadInfo.coordType = NearbySearch.AMAP
            //位置信息
            if (latitude != 0.0 && longitude != 0.0) {
                loadInfo.point = LatLonPoint(latitude, longitude)
            } else {
                Log.e("main", "经纬度获取错误")
            }
            //用户id信息
            loadInfo.userID = userId
            loadInfo
        }, 10000)
    }
}