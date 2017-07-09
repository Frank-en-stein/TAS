package com.example.smshandler;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by home on 10/25/2016.
 */
public class SmsListener extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    if (Build.VERSION.SDK_INT >= 19) { //KITKAT
                        msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);

                        for (int i = 0; i < msgs.length; i++) {
                            msg_from = msgs[i].getOriginatingAddress();
                            String msgBody = msgs[i].getMessageBody();

                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(context)
                                            .setSmallIcon(R.drawable.sosicon)
                                            .setContentTitle("Emergency ALERT")
                                            .setContentText("asd")
                                            .setColor(Color.RED)
                                            .setPriority(2)
                                            .setLocalOnly(true)
                                            .setCategory(NotificationCompat.CATEGORY_ALARM)
                                            .setVibrate(new long[] {300, 1000, 300,1000, 300, 1000, 300,1000, 300, 1000, 300,1000, 300, 1000, 300,
                                                    1000, 300,1000, 300, 1000, 300})
                                            .setLights(Color.RED, 500, 500)
                                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL))
                                            .setOnlyAlertOnce(true);
                            NotificationManager mNotifyMgr =
                                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

                            mNotifyMgr.notify(1, mBuilder.build());
                        }

                    } else {
                        //Object pdus[] = (Object[]) bundle.get("pdus");
                        //smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);

                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            msg_from = msgs[i].getOriginatingAddress();
                            String msgBody = msgs[i].getMessageBody();
                        }
                    }
                }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }
}
