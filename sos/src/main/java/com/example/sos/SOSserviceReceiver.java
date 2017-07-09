package com.example.sos;

/**
 * Created by avash on 10/19/2016.
 */

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smshandler.SmsSender;
import com.example.sos.constants.KeyConstant;
import com.example.sos.utils.PhoneData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import locationManager.SmartLocation;

public class SOSserviceReceiver extends BroadcastReceiver
{
    private static final String TAG = SOSserviceReceiver.class.getSimpleName();

    private static long prevTime=0;

    private static int state = 0;

    private static int diff = 0;

    private static PowerManager pm;

    private static boolean isSingleCall = false;

    private static final int LOCATION_PERMISSION_ID = 1001;

    private SimpleDateFormat sdf, sdf2;

    public SOSserviceReceiver()
    {
        sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        sdf2 = new SimpleDateFormat("HH:mm:ss");
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent != null)
        {
            if(intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION"))
            {
                if(intent.getExtras() != null)
                {
                    if (pm == null)
                    {
                        pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

                    }

                    int prevVolume = intent.getExtras().getInt("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", 0);
                    int currentValue = intent.getExtras().getInt("android.media.EXTRA_VOLUME_STREAM_VALUE", 0);

                    AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                    //Forunlocking only one event will trigger. When phone is open ui popup will also trigger the volume change
                    boolean volumeAction = false;
                    if((currentValue == 0 && prevVolume == 0) || currentValue == maxVolume && prevVolume == maxVolume)
                    {
                        if(!isSingleCall)
                        {
                            isSingleCall = true; // Wait for ui volume change call
                        }
                        else
                        {
                            isSingleCall = false;   //Resetting value
                            volumeAction = true;
                        }
                    }
                    else if(currentValue != 0 && prevVolume != 0 && currentValue != prevVolume)
                    {
                        volumeAction = true;
                    }
                    if(volumeAction) {
                        long currTime = System.currentTimeMillis();
                        if(currTime-prevTime >500) {
                            state = 0;
                            diff = 0;
                        }
                        else if(state==0) {
                            state = (state + 1) % 4;
                            diff = currentValue - prevVolume;
                        }
                        else if(currentValue-prevVolume==-diff ) {
                            state = (state+1)%4;
                            diff = currentValue-prevVolume;
                        }
                    }
                    prevTime = System.currentTimeMillis();
                    if(state==3) {
                        diff = state = 0;

                        // panic mode initiation code goes here
                        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);
                        Toast.makeText(context, "I am here", Toast.LENGTH_SHORT).show();

                        final AudioManager mode = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                        mode.setRingerMode(AudioManager.RINGER_MODE_SILENT);

                        SmartLocation smartLocation = SOSservice.smLoc;

                        String locationtxt = String.format(Locale.ENGLISH, "lat:%.8f\nlong:%.8f\nactivity:%s\nconfidence:%d%%\nTime:%s",
                                smartLocation.currLocation.getLatitude(), smartLocation.currLocation.getLongitude(),
                                smartLocation.getNameFromType(smartLocation.currLocationActivity),
                                smartLocation.currLocationActivity.getConfidence(), smartLocation.currDateTime);
                        Toast.makeText(context, locationtxt, Toast.LENGTH_SHORT).show();

                        try {
                            long x = sdf.parse(smartLocation.currDateTime).getTime();
                            long y = (new Date()).getTime();
                            if(y-x>30*60*1000) {
                                smartLocation.updateNeeded = true;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        new SmsSender(context, locationtxt);

                        //startLocation();

                        //showLast();
                    }
//
//                    if(volumeAction || !isScreenOn(pm)) //when screen is off ui volume change will not happen
//                    {
//                        boolean isEnabled = PhoneData.getPhoneData(context, KeyConstant.UNLOCK_STR, false);
//
//                        if (!isScreenOn(pm) && isEnabled)
//                        {
//
//                            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, KeyConstant.WAKE_TAG_STR);
//                            wakeLock.acquire();
//                            wakeLock.release();
//
//                            //MyLogger.l(TAG, "wake lock done");
//                        }
//                        else if (isScreenOn(pm) && PhoneData.getPhoneData(context, KeyConstant.VOLUME_LOCK_ENABLE_STR, false) && isEnabled)
//                        {
//                            if (System.currentTimeMillis() - prevTime < 1000)
//                            {
//                                //lockScreenNow(context);
//                            }
//                            else
//                            {
//                                //MyLogger.l(TAG, "not a rapic action");
//                            }
//
//                            prevTime = System.currentTimeMillis();
//                        }
//                    }

                }
            }
        }
        else
        {
            //MyLogger.l(TAG, "null Intent recieved");
        }
    }


    @SuppressWarnings("deprecation")
    private boolean isScreenOn(PowerManager pm)
    {
        boolean isScreenOn;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH)
        {
            isScreenOn = pm.isInteractive();
        }
        else
        {
            isScreenOn = pm.isScreenOn();
        }

        return isScreenOn;
    }

}

