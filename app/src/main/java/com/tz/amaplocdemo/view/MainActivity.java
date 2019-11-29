package com.tz.amaplocdemo.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tz.amaplocdemo.R;
import com.tz.amaplocdemo.base.Constants;
import com.tz.amaplocdemo.bean.GPS;
import com.tz.amaplocdemo.bean.Line;
import com.tz.amaplocdemo.util.DateUtils;
import com.tz.amaplocdemo.util.SPUtils;
import com.tz.amaplocdemo.util.ToastUtil;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener {
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    AMap aMap;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_end)
    Button btnEnd;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.btn_list)
    Button btnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);
        requestPermission();
        initView();
        initLocation();
    }

    private void initView() {
        if (aMap == null)
            aMap = mapView.getMap();
        //实现定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(60*1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //设置定位蓝点图标的锚点方法。
        myLocationStyle.anchor(0.5f, 0.5f);
        //缩放
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LineListActivity.class));
            }
        });

    }


    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    //声明定位回调监听器
    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    GPS gps = new GPS();
                    gps.setAddr(aMapLocation.getAddress());
                    gps.setLineId(SPUtils.getInt(Constants.SP_COLLECTING_LINE_ID));
                    gps.setCity(aMapLocation.getCity());
                    gps.setProvince(aMapLocation.getProvince());
                    gps.setCounty(aMapLocation.getDistrict());
                    gps.setGatherTime(DateUtils.getTimeLong());
                    gps.setLongitude(aMapLocation.getLongitude());
                    gps.setLatitude(aMapLocation.getLatitude());
                    gps.save();
                    Log.e("tianLog","经纬度"+aMapLocation.getLatitude() +","+aMapLocation.getLongitude());
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };


    @OnClick({R.id.btn_start, R.id.btn_end})
    public void onViewClicked(View view) {
        boolean isCollecting = SPUtils.getBoolean(Constants.SP_IS_COLLECTING, false);//是否在采集中
        switch (view.getId()) {
            case R.id.btn_start:
                if (isCollecting) {
                    ToastUtil.showToastShort("已经在采集中");
                } else {
                    if (TextUtils.isEmpty(etName.getText())) {
                        ToastUtil.showToastShort("请填写线路名称");
                    } else {
                        //启动定位
                        Line line = new Line();
                        line.setCreateTime(DateUtils.getTimeLong());
                        line.setLineName(etName.getText().toString().trim());
                        if (line.save()) {
                            Line lastLine = LitePal.findLast(Line.class);
                            SPUtils.putInt(Constants.SP_COLLECTING_LINE_ID, lastLine.getId());
                            SPUtils.putBoolean(Constants.SP_IS_COLLECTING, true);
                            mLocationClient.startLocation();
                        }
                    }
                }
                break;
            case R.id.btn_end:
                if (isCollecting && mLocationClient != null) {
                    int lastLineId = SPUtils.getInt(Constants.SP_COLLECTING_LINE_ID);
                    mLocationClient.stopLocation();
                    Line line = new Line();
                    line.setEndTime(DateUtils.getTimeLong());
                    line.update(lastLineId);
                    SPUtils.putBoolean(Constants.SP_IS_COLLECTING, false);
                    SPUtils.putInt(Constants.SP_COLLECTING_LINE_ID, -1);
                } else {
                    ToastUtil.showToastShort("当前没有采集");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMyLocationChange(Location location) {

    }

    @SuppressLint("CheckResult")
    private void requestPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        //有权限
                    } else {
                        //没有权限
                        finish();
                    }
                });
    }
}
