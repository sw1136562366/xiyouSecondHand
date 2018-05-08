package com.sendhand.xiyousecondhand.view;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.sendhand.xiyousecondhand.MyApplication;
import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.databinding.PublishActivityBinding;
import com.sendhand.xiyousecondhand.entry.Goods;
import com.sendhand.xiyousecondhand.entry.User;
import com.sendhand.xiyousecondhand.util.LogUtil;
import com.sendhand.xiyousecondhand.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;

public class PublishActivity extends BaseActivity {

    private User user;
    private LocationClient mLocationClient;
    private MyLoactionListener myListener = new MyLoactionListener();
    private  PublishActivityBinding bind;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true
        mLocationClient.setLocOption(option);

        bind = DataBindingUtil.setContentView(this, R.layout.publish_activity);
        mLocationClient.start();

        if (getIntent() != null) {
            user =  (User) getIntent().getSerializableExtra("user_data");
        }

        //发布
        bind.activitySelectimgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = bind.title.getText().toString().trim();
                String desc = bind.wordMessage.getText().toString().trim();
                Double price = Double.parseDouble(bind.price.getText().toString().trim());

                Goods good = new Goods();
                good.setTitle(title);
                good.setDesc(desc);
                good.setUser(user);
                good.setPrice(price);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                good.setPublishDate(new BmobDate(new Date()));
                good.setPosition(new BmobGeoPoint(longitude, latitude));
                good.setStatus(1);

                good.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){
                            ToastUtil.showToast(MyApplication.getContext(), "发布成功");
                            finish();
                        }else{
                            ToastUtil.showToast(MyApplication.getContext(), "发布失败");
                            LogUtil.d("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });

            }
        });
    }

    public class MyLoactionListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
            latitude = bdLocation.getLatitude();
            longitude = bdLocation.getLongitude();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder position = new StringBuilder();
                    position.append(bdLocation.getProvince()).append("省");
                    position.append(bdLocation.getCity()).append("市");
                    position.append(bdLocation.getDistrict()).append("区");
                    position.append(bdLocation.getStreet()).append("街道");
//                    if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
//                        position.append("GPS");
//                    } else if(bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
//                        position.append("网络");
//                    }
                    //获取位置
                    bind.location.setText(position);
                }
            });
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
            }

//            MyLocationData locData = new MyLocationData.Builder()
//                    .accuracy(location.getRadius())
//                    // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .direction(100).latitude(location.getLatitude())
//                    .longitude(location.getLongitude()).build();
//            // 设置定位数据
//            baiduMap.setMyLocationData(locData);
//            if (isFirstLocate) {
//                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//                MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll, 16);//设置地图中心及缩放级别
//                baiduMap.animateMapStatus(update);
//                isFirstLocate = false;
//                Toast.makeText(getApplicationContext(), location.getAddrStr(), Toast.LENGTH_SHORT).show();
//            }
        }
    }

}
