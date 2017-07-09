////This is a module pilot, for test only
//package com.example.sos;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.telephony.TelephonyManager;
//import android.widget.Toast;
//
//import com.example.sos.constants.KeyConstant;
//import com.example.sos.utils.PhoneData;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        //add these lines to module caller
//        PhoneData.savePhoneData(MainActivity.this, KeyConstant.UNLOCK_STR, true);
//        SOSservice.manageService(MainActivity.this);
//
//        TelephonyManager mTelephonyMgr =
//                (TelephonyManager)MainActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
//
//        int MY_PERMISSION = 1;
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            MY_PERMISSION = 0;
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
//
//            }
//            else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSION);
//            }
//        }
//
//        //if(MY_PERMISSION!=0) {
//            String sSimSerial = mTelephonyMgr.getSimSerialNumber();
//            Toast.makeText(MainActivity.this, sSimSerial, Toast.LENGTH_LONG).show();
//        //}
//    }
//}
