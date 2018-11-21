package com.android.library.ui;

import android.os.Bundle;

import com.android.baselibrary.baseui.BaseActivity;
import com.android.library.R;

import butterknife.BindView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

public class MapShowActivity extends BaseActivity {

    @BindView(R.id.map)
    MapView mMapView;

    private AMap aMap;
    private Marker geoMarker;

    private double Longitude;
    private double Latitude;
    private LatLng mLatLng;

    @Override
    protected boolean isStatusBarOverlap() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_map_show;
    }

    @Override
    protected void initViewsAndEvents() {
        aMap = mMapView.getMap();
        aMap.setTrafficEnabled(true);
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
        geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f));

        Longitude = getIntent().getDoubleExtra("Longitude", 0);
        Latitude = getIntent().getDoubleExtra("Latitude", 0);
        mLatLng = new LatLng(Latitude, Longitude);
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
        geoMarker.setPosition(mLatLng);

        UiSettings uiSettings =  aMap.getUiSettings();

        uiSettings.setLogoBottomMargin(-50);//隐藏logo
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

}
