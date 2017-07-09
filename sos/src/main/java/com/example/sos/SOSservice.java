package com.example.sos;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;

import com.example.sos.constants.KeyConstant;
import com.example.sos.utils.PhoneData;

import locationManager.SmartLocation;


public class SOSservice extends Service
{
    private static final String TAG = SOSservice.class.getSimpleName();

    private static SOSservice screenLockService;
    public static SmartLocation smLoc;
    private MediaPlayer mediaPlayer;

    public SOSservice()
    {
        screenLockService = this;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(PhoneData.getPhoneData(this, KeyConstant.UNLOCK_STR, false))
        {
            if (mediaPlayer == null)
            {
                mediaPlayer = MediaPlayer.create(this, R.raw.sound);
                mediaPlayer.setVolume(0, 0);
                mediaPlayer.setLooping(true);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }

            if(!mediaPlayer.isPlaying())
            {
                mediaPlayer.start();
            }
        }
        else
        {
            stopMediaPlay();
        }




        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onDestroy()
    {
        stopMediaPlay();

        super.onDestroy();
    }


    private void stopMediaPlay()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
        }
    }


    public static void manageService(Context context, SmartLocation smartLocation)
    {
        if (PhoneData.getPhoneData(context, KeyConstant.UNLOCK_STR, false))
        {
            smLoc = smartLocation;
            Intent intent = new Intent(context, SOSservice.class);
            context.startService(intent);
            //MyLogger.lw(TAG, "Service started");
        }
        else
        {
            stopService();
        }
    }

    private static void stopService() {
        try
        {   //safety
            if (screenLockService != null)
            {
                screenLockService.stopSelf();
            }
        } catch (Exception e) {
            //MyLogger.e(e);
        }
    }




    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        Intent restartServiceIntent = new Intent(getApplicationContext(),
                this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(
                getApplicationContext(), 1, restartServiceIntent,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }
}
