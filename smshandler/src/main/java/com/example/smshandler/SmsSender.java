package com.example.smshandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;

/**
 * Created by home on 11/4/2016.
 */

public class SmsSender {
    private boolean locationUpdate;
    public static final String PREFS_NAME = "TASdata";
    String presetmsg, location;

    public SmsSender(Context context, String locationtxt, boolean locationUpdate) {
        locationUpdate = true;
        location = locationtxt;

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);

        sendSMS(settings.getString("TAScon1", null), "TASsosAlert\nName:"+settings.getString("TASmsg", null)+"\nLocation Update:"+location);
        sendSMS(settings.getString("TAScon2", null), "TASsosAlert\nName:"+settings.getString("TASmsg", null)+"\nLocation Update:"+location);
        sendSMS(settings.getString("TAScon3", null), "TASsosAlert\nName:"+settings.getString("TASmsg", null)+"\nLocation Update:"+location);
    }
    public SmsSender(Context context, String locationtxt) {
        locationUpdate = false;
        location = locationtxt;

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        presetmsg = settings.getString("TASmsg", null);
        sendSMS(settings.getString("TAScon1", null), msg());
        sendSMS(settings.getString("TAScon2", null), msg());
        sendSMS(settings.getString("TAScon3", null), msg());
    }
    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
    private String msg() {
        return String.format("TASsosAlert\nName:%s\nLocation:\n%s",presetmsg, location);
    }
}
