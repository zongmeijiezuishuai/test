package com.example.idea_test.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.*;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.example.idea_test.R;
import com.example.idea_test.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import pub.devrel.easypermissions.EasyPermissions;

public class MapFragment extends Fragment implements AMapLocationListener, LocationSource,
        PoiSearch.OnPoiSearchListener, AMap.OnMapClickListener, AMap.OnMapLongClickListener,
        GeocodeSearch.OnGeocodeSearchListener {

    private static final String TAG = "MapFragment";
    private static final int REQUEST_PERMISSIONS = 9527;
    private static final int PARSE_SUCCESS_CODE = 1000;

    // 地图相关
    private MapView mapView;
    private AMap aMap;
    private FloatingActionButton fabPoi;

    // 定位相关
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;

    // 搜索相关
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private String cityCode;
    private GeocodeSearch geocodeSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.map_view);
        fabPoi = view.findViewById(R.id.fab_poi);

        mapView.onCreate(savedInstanceState);
        initMap();
        initLocation();
        initSearch();
        initView();

        checkingAndroidVersion();

        return view;
    }

    private void initSearch() {
        try {
            geocodeSearch = new GeocodeSearch(requireContext());
            geocodeSearch.setOnGeocodeSearchListener(this);
        } catch (AMapException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "初始化地理编码搜索失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkingAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions();
        } else {
            startLocation();
        }
    }

    private void requestPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (EasyPermissions.hasPermissions(requireContext(), permissions)) {
            showMsg("正在获取位置...");
            startLocation();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "需要位置权限来提供定位服务",
                    REQUEST_PERMISSIONS,
                    permissions
            );
        }
    }

    private void startLocation() {
        if (mLocationClient != null) {
            mLocationClient.startLocation();
            showMsg("正在定位中...");
        }
    }

    private void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void initLocation() {
        try {
            mLocationClient = new AMapLocationClient(requireContext());
            mLocationClient.setLocationListener(this);
            mLocationOption = new AMapLocationClientOption();

            // 设置为设备传感器模式（GPS）
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
            mLocationOption.setGpsFirst(true);
            mLocationOption.setOnceLocationLatest(true);
            mLocationOption.setNeedAddress(true);
            mLocationOption.setHttpTimeOut(6000);
            mLocationOption.setLocationCacheEnable(false);
            mLocationClient.setLocationOption(mLocationOption);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "初始化定位失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            showMsg("定位失败，返回的AMapLocation为null");
            return;
        }

        if (aMapLocation.getErrorCode() == 0) {
            String address = aMapLocation.getAddress();
            String district = aMapLocation.getDistrict();
            showMsg("定位成功，当前位置：" + (district != null ? district : address));

            stopLocation();

            if (mListener != null) {
                mListener.onLocationChanged(aMapLocation);
                // 移动到当前位置
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new com.amap.api.maps.model.LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 15f));
            }

            fabPoi.show();
            cityCode = aMapLocation.getCityCode();
        } else {
            showMsg("定位失败: " + aMapLocation.getErrorInfo());
            Log.e(TAG, "location error, errcode: " + aMapLocation.getErrorCode() +
                    ", errInfo: " + aMapLocation.getErrorInfo());
        }
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setLocationSource(this);

            // 设置自定义定位蓝点
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
            myLocationStyle.interval(2000);
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.setMyLocationEnabled(true);

            // 地图设置
            aMap.showIndoorMap(true);
            aMap.setOnMapClickListener(this);
            aMap.setOnMapLongClickListener(this);

            UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            uiSettings.setScaleControlsEnabled(false);
            uiSettings.setMyLocationButtonEnabled(true);
        }
    }

    private void showMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mLocationClient != null) {
            startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    private void initView() {
        fabPoi.setOnClickListener(v -> {
            if (cityCode == null || cityCode.isEmpty()) {
                Toast.makeText(getContext(), "请先获取当前位置", Toast.LENGTH_SHORT).show();
                return;
            }

            query = new PoiSearch.Query("购物", "", cityCode);
            query.setPageSize(10);
            query.setPageNum(1);

            try {
                poiSearch = new PoiSearch(requireContext(), query);
                poiSearch.setOnPoiSearchListener(this);
                poiSearch.searchPOIAsyn();
            } catch (AMapException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "POI搜索初始化失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (EasyPermissions.hasPermissions(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            startLocation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        stopLocation();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onMapClick(com.amap.api.maps.model.LatLng latLng) {
        showMsg("点击了地图，经度："+latLng.longitude+"，纬度："+latLng.latitude);
        latLonToAddress(latLng);
    }

    @Override
    public void onMapLongClick(com.amap.api.maps.model.LatLng latLng) {
        showMsg("长按了地图，经度："+latLng.longitude+"，纬度："+latLng.latitude);
        latLonToAddress(latLng);
    }

    @Override
    public void onPoiSearched(com.amap.api.services.poisearch.PoiResult poiResult, int i) {}

    @Override
    public void onPoiItemSearched(com.amap.api.services.core.PoiItem poiItem, int i) {}

    @Override
    public void onGeocodeSearched(com.amap.api.services.geocoder.GeocodeResult geocodeResult, int i) {}

    @Override
    public void onRegeocodeSearched(com.amap.api.services.geocoder.RegeocodeResult regeocodeResult, int rCode) {
        if(rCode == PARSE_SUCCESS_CODE){
            RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
            showMsg("地址："+regeocodeAddress.getFormatAddress());
        }else {
            showMsg("获取地址失败");
        }
    }

    private void latLonToAddress(com.amap.api.maps.model.LatLng latLng) {
        com.amap.api.services.core.LatLonPoint latLonPoint =
                new com.amap.api.services.core.LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 20, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
    }
}