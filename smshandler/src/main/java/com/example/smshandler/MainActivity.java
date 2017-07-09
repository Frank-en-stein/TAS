//package com.example.smshandler;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.example.smshandler.phoneNumbers.TelephonyInfo;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
//
//        String imeiSIM1 = telephonyInfo.getImeiSIM1();
//        String imeiSIM2 = telephonyInfo.getImeiSIM2();
//
//        boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
//        boolean isSIM2Ready = telephonyInfo.isSIM2Ready();
//
//        boolean isDualSIM = telephonyInfo.isDualSIM();
//        Log.i("Dual = "," IME1 : " + imeiSIM1 + "\n" +
//                " IME2 : " + imeiSIM2 + "\n" +
//                " IS DUAL SIM : " + isDualSIM + "\n" +
//                " IS SIM1 READY : " + isSIM1Ready + "\n" +
//                " IS SIM2 READY : " + isSIM2Ready + "\n");
//        Toast.makeText(this, imeiSIM1+" "+imeiSIM2, Toast.LENGTH_SHORT).show();
//    }
//}
