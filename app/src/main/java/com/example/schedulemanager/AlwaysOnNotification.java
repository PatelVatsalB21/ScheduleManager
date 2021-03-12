package com.example.schedulemanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.schedulemanager.MainFragments.HomePage;
import com.example.schedulemanager.Setting.Settings_Main;
import com.example.schedulemanager.Task.NewTask;
import com.example.schedulemanager.email.ScheduleEmail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//import android.support.v4.media.session.MediaSessionCompat;

public class AlwaysOnNotification extends BroadcastReceiver {

    Context mContext;
    NotificationManagerCompat notificationManager;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private String CHANNEL_Custom_ID = "channel_3";

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        notificationManager = NotificationManagerCompat.from(context);
        if (Settings_Main.isAssitiveNotificationOn!=null) {
            if (Settings_Main.isAssitiveNotificationOn) {
                sendNotification();
            }
        }else {
            Settings_Main.LoadChangedSettings(context);
            Settings_Main.isAssitiveNotificationOn = false;
            Settings_Main.SaveSettings(context);
        }
    }

    private void sendNotification() {
        Log.d("ALWAYSON", "CALLED");
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.always_on_notification_layout);

        Intent activityIntent = new Intent(mContext, HomePage.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(mContext,
                0, activityIntent, 0);

        Intent popUpService = new Intent(mContext, PopUpDialogService.class);
        PendingIntent popUpPendingIntent = PendingIntent.getService(mContext, 0, popUpService, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent task = new Intent(mContext, NewTask.class);
        PendingIntent taskPendingIntent = PendingIntent.getActivity(mContext, 0, task, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent email = new Intent(mContext, ScheduleEmail.class);
        PendingIntent emailPendingIntent = PendingIntent.getActivity(mContext, 0, email, PendingIntent.FLAG_UPDATE_CURRENT);

//        Intent closeDrawerIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//        PendingIntent closeDrawerPenIntent = PendingIntent.getBroadcast(mContext, 0,closeDrawerIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.Always_On_Notification_App_Logo,contentIntent);
        views.setOnClickPendingIntent(R.id.Always_On_Notification_Task,taskPendingIntent);
        views.setOnClickPendingIntent(R.id.Always_On_Notification_Notes,popUpPendingIntent);
        views.setOnClickPendingIntent(R.id.Always_On_Notification_Email,emailPendingIntent);
        views.setOnClickPendingIntent(R.id.Always_On_Notification_Task_txt,taskPendingIntent);
        views.setOnClickPendingIntent(R.id.Always_On_Notification_Notes_txt,popUpPendingIntent);
        views.setOnClickPendingIntent(R.id.Always_On_Notification_Email_txt,emailPendingIntent);
        views.setOnClickPendingIntent(R.id.Always_On_Notification_Task_Layout,taskPendingIntent);
        views.setOnClickPendingIntent(R.id.Always_On_Notification_Notes_Layout,popUpPendingIntent);
        views.setOnClickPendingIntent(R.id.Always_On_Notification_Email_Layout,emailPendingIntent);

//        logo.findViewById(R.id.Always_On_Notification_App_Logo);
//        logo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent home = new Intent(mContext, HomePage.class);
//                mContext.startActivity(home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK));
//            }
//        });
//
//        task.findViewById(R.id.Always_On_Notification_Task);
//        task.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent task = new Intent(mContext, NewTask.class);
//                mContext.startActivity(task.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK));
//            }
//        });
//
//        note.findViewById(R.id.Always_On_Notification_Notes);
//        note.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent popUpService = new Intent(mContext, PopUpDialogService.class);
//                mContext.startService(popUpService);
//            }
//        });
//
//        email.findViewById(R.id.Always_On_Notification_Email);
//        email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent email = new Intent(mContext, ScheduleEmail.class);
//                mContext.startActivity(email.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK));
//            }
//        });
        NotificationChannel channelCustom = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelCustom = new NotificationChannel(
                    CHANNEL_Custom_ID,
                    "Low Priority Notifications",
                    NotificationManager.IMPORTANCE_LOW
            );
        channelCustom.setDescription("This channel is used to send low priority notifications to user");
        channelCustom.setShowBadge(false);
        channelCustom.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        channelCustom.enableVibration(false);
        channelCustom.enableLights(false);

        NotificationManager manager = mContext.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channelCustom);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
         Notification   notification = new NotificationCompat.Builder(mContext, CHANNEL_Custom_ID)
                    .setSmallIcon(R.mipmap.ic_note_icon)
                    .setCustomContentView(views)
                    .setContentIntent(contentIntent)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setNotificationSilent()
                    .setOngoing(true)
                    .setOnlyAlertOnce(true)
                    .setCategory(NotificationCompat.CATEGORY_STATUS)
                    .build();

        notificationManager.notify(1110, notification);
        }
    }
}
