package com.google.firebase.quickstart.fcm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class NotificationListenerExampleService extends NotificationListenerService {


    private Context context;

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();

    }
    @Override

    public void onNotificationPosted(StatusBarNotification sbn) {


        String pack = sbn.getPackageName();
        String ticker;

        CharSequence tickerText = sbn.getNotification().tickerText;
        if(tickerText == null)
        {
            ticker = "";

        }
        else
        {
            ticker = tickerText.toString();
        }
        Bundle extras = sbn.getNotification().extras;
        long time = sbn.getNotification().when;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text").toString();





        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("package", pack);
        msgrcv.putExtra("ticker", ticker);
        msgrcv.putExtra("title", title);
        msgrcv.putExtra("text", text);

        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);


    }
}
