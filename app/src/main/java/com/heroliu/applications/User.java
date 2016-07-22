package com.heroliu.applications;

import android.app.Application;
import android.location.LocationListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by Eric on 6/19/16.
 */
public class User  extends Application{
    private String username;
    public LocationClient mLocationClient;
    public MyLocationListener locationListener;
    public TextView locationInfo;
    private LocationClientOption option;
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mLocationClient = new LocationClient(getApplicationContext());
        locationListener = new MyLocationListener();
        option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(locationListener);
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if(location.getLocType() == BDLocation.TypeNetWorkLocation){
                locationInfo.setText(location.getAddrStr());
            }
        }

    }
}
