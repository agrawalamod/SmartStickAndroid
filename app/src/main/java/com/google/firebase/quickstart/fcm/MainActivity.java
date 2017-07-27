/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.quickstart.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final String TAG = "MainActivity";
    private TextToSpeech tts;
    String[] notifications = {"Notification from Facebook: John added you as a friend.", "Notification from SMS: Don said, please be present for the meeting", "Notification from Phone: Missed call by Prateek", "Notfication from Outlook: Meeting in 15 minutes - Lab Sabha in MPR", "Notification from Outlook: You have 5 new emails and 2 calender invites.", "End of your notifications"};
    int not_index = 0;
    public long time_received;
    public long new_time_received;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts = new TextToSpeech(this, this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]


        Button subscribeButton = (Button) findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("gesture");
                // [END subscribe_topics]

                // Log and toast
                String msg = getString(R.string.msg_subscribed);
                Log.d(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        Button logTokenButton = (Button) findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get token
                String token = FirebaseInstanceId.getInstance().getToken();

                // Log and toast
                String msg = getString(R.string.msg_token_fmt, token);
                Log.d(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        time_received = System.currentTimeMillis();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String gesture = intent.getStringExtra("GestureName");  //get the type of message from MyGcmListenerService 1 - lock or 0 -Unlock
            gesture = gesture.substring(0, gesture.length() - 1);

            Log.d("MainActivity", gesture);
            new_time_received = System.currentTimeMillis();

            Log.v("TimeReceived", Long.toString(time_received) + ", " + Long.toString(new_time_received) + ", " + Long.toString(new_time_received- time_received));
            if (new_time_received - time_received > 3000) {
                time_received = new_time_received;


                switch (gesture) {

                    case "tap":
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        String str = sdf.format(new Date());
                        Toast.makeText(MainActivity.this, "The time is " + str, Toast.LENGTH_SHORT).show();
                        speakOut("The time is " + str);
                        break;
                    case "leftTwist":
                        if (not_index >= 2) {
                            not_index = not_index - 2;
                            Log.v("not_index", String.valueOf(not_index));
                            Toast.makeText(MainActivity.this, notifications[not_index], Toast.LENGTH_SHORT).show();
                            speakOut(notifications[not_index]);
                        } else if (not_index == 1) {
                            not_index = not_index - 1;
                            Log.v("not_index", String.valueOf(not_index));
                            Toast.makeText(MainActivity.this, notifications[not_index], Toast.LENGTH_SHORT).show();
                            speakOut(notifications[not_index]);

                        } else {
                            Toast.makeText(MainActivity.this, "You have 5 notifications.", Toast.LENGTH_SHORT).show();
                            speakOut("You have 5 notifications.");

                        }

                        break;
                    case "rightTwist":
                        if (not_index == 0) {
                            Log.v("not_index", String.valueOf(not_index));
                            speakOut("You have 5 notifications.");
                            Toast.makeText(MainActivity.this, notifications[not_index], Toast.LENGTH_SHORT).show();
                            speakOut(notifications[not_index]);
                            if (not_index < 6) {
                                not_index = not_index + 1;
                            }

                        } else {

                            Log.v("not_index", String.valueOf(not_index));
                            Toast.makeText(MainActivity.this, notifications[not_index], Toast.LENGTH_SHORT).show();
                            speakOut(notifications[not_index]);
                            if (not_index < 6) {
                                not_index = not_index + 1;
                            }

                        }
                        break;

                }


                //speakOut(gesture);
            }
            else
            {
                Log.v("TimeDelay", "Tooo soon!!!");
            }
        }

    };

    @Override
    protected void onResume()
    {
        super.onResume();
        //registerReceiver(statusReceiver,mIntent);
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(broadcastReceiver, new IntentFilter("Gesture"));
    }

    @Override
    public void onInit(int i) {

        if (i == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {

                speakOut("Hello, I am connected to Smart Stick.");            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }


    }
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void speakOut(String text) {

        tts.speak(text, TextToSpeech.QUEUE_ADD, null,"id1");
    }

}
