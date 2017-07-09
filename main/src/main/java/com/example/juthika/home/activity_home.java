package com.example.juthika.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.juthika.home.smsClasses.SmsService;
import com.example.sos.SOSservice;
import com.example.sos.constants.KeyConstant;
import com.example.sos.utils.PhoneData;

import java.util.Locale;

import locationManager.SmartLocation;


public class activity_home extends AppCompatActivity {
    SmartLocation smartLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        if (ContextCompat.checkSelfPermission(activity_home.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity_home.this, new String[]{Manifest.permission.RECEIVE_SMS}, 1005);
            //return;
        }
        smartLocation = new SmartLocation(this, activity_home.this);
        PhoneData.savePhoneData(activity_home.this, KeyConstant.UNLOCK_STR, true);
        SOSservice.manageService(this, smartLocation);
        startService(new Intent(activity_home.this, SmsService.class));

    }

    @Override
    protected void onDestroy() {
        smartLocation.stopLocation();
        //stopService(new Intent(getBaseContext(), activity_home.class));
        super.onDestroy();
    }

    public void report(View v) {
        Intent i = new Intent(activity_home.this, report.class);
        startActivity(i);
    }

    public void setting(View v) {
        Intent i = new Intent(activity_home.this, settings.class);
        startActivity(i);
    }
    public void tutorial(View v) {
        Intent i = new Intent(activity_home.this, tutorial.class);
        startActivity(i);
    }
    public void archieve(View v) {
        Intent i = new Intent(activity_home.this, archieve.class);
        startActivity(i);
    }
    public void func(View v) {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f(Victim Location)", smartLocation.currLocation.getLatitude(),
                smartLocation.currLocation.getLongitude(),smartLocation.currLocation.getLatitude(),
                smartLocation.currLocation.getLongitude());
        showMap(Uri.parse(uri));
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}
