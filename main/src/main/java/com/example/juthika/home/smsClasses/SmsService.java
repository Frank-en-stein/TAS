package com.example.juthika.home.smsClasses;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.juthika.home.*;

/**
 * Created by home on 11/7/2016.
 */

public class SmsService extends Service
{
    private SMSreceiver mSMSreceiver;
    private IntentFilter mIntentFilter;
    protected NotificationCompat.Builder mBuilder;
    protected PowerManager pm;

    @Override
    public void onCreate()
    {
        super.onCreate();

        //SMS event receiver
        mSMSreceiver = new SMSreceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSMSreceiver, mIntentFilter);

        Toast.makeText(this, "kjhsd", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(this, "kjhasdjha", Toast.LENGTH_SHORT).show();
        // Unregister the SMS receiver
        unregisterReceiver(mSMSreceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class SMSreceiver extends BroadcastReceiver
    {
        private final String TAG = this.getClass().getSimpleName();
        NotificationManager mNotifyMgr;

        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle extras = intent.getExtras();

            String strMessage = "";

            if ( extras != null )
            {
                Object[] smsextras = (Object[]) extras.get( "pdus" );

                for ( int i = 0; i < smsextras.length; i++ )
                {
                    SmsMessage smsmsg = SmsMessage.createFromPdu((byte[])smsextras[i]);

                    String strMsgBody = smsmsg.getMessageBody().toString();
                    String strMsgSrc = smsmsg.getOriginatingAddress();


                    if(!strMsgBody.contains("TASsosAlert")) continue;


                    Intent notificationIntent = new Intent(context, sosreceiver.class);
                    notificationIntent.putExtra("msg", strMsgBody);
                    notificationIntent.putExtra("src", strMsgSrc);

                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent ii = PendingIntent.getActivity(context, 0,
                            notificationIntent, 0);

                    mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(com.example.smshandler.R.drawable.sosicon)
                                    .setContentTitle("TAS Emergency ALERT")
                                    .setContentText(strMsgSrc + "is in Danger. Click to view Details...")
                                    .setColor(Color.RED)
                                    .setPriority(2)
                                    .setLocalOnly(true)
                                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                                    .setVibrate(new long[] {300, 1000, 300,1000, 300, 1000, 300,1000, 300, 1000, 300,1000, 300, 1000, 300,
                                            1000, 300,1000, 300, 1000, 300})
                                    .setLights(Color.RED, 500, 500)
                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL))
                                    .setContentIntent(ii)
                                    .setOnlyAlertOnce(false);
                    mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                    pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);

                    new Thread(
                            new Runnable() {
                                public void run() {
                                    synchronized (this) {
                                        try {
                                            wait(5000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mNotifyMgr.notify(1, mBuilder.build());

                                        boolean isScreenOn = pm.isScreenOn();

                                        if(isScreenOn==false)
                                        {

                                            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");

                                            wl.acquire(10000);
                                            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

                                            wl_cpu.acquire(10000);
                                        }
                                    }
                                }
                            }).start();

                    Log.i(TAG, strMessage);
                }


            }

        }

    }
}
