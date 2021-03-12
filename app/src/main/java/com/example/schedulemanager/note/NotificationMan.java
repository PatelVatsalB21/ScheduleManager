package com.example.schedulemanager.note;//package com.example.firebase.note;
//
//
//import android.app.Application;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.media.AudioAttributes;
//import android.media.Ringtone;
//import android.media.RingtoneManager;
//import android.os.Build;
//import android.os.ConditionVariable;
//import android.util.Log;
//
//public class NotificationMan extends Application {
//    public static final String CHANNEL_1_ID = "channel1";
//    public static final String CHANNEL_2_ID = "channel2";
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        createNotificationChannels(NotificationMan.this);
//    }
//
//    public static void createNotificationChannels(Context context) {
//        NotificationChannel channel1 = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            channel1 = new NotificationChannel(
//                    CHANNEL_1_ID,
//                    "Important Notifications",
//                    NotificationManager.IMPORTANCE_HIGH
//            );
//
//        channel1.setDescription("This channel is used to send important notifications to user.");
//            channel1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//            channel1.setShowBadge(true);
//            channel1.enableVibration(true);
//            channel1.enableLights(true);
//            channel1.setVibrationPattern(new long[]{0, 600, 400});
//            channel1.setSound(RingtoneManager.getActualDefaultRingtoneUri(context,RingtoneManager.TYPE_ALARM), Notification.AUDIO_ATTRIBUTES_DEFAULT);
//
//            NotificationChannel channel2 = new NotificationChannel(
//                    CHANNEL_2_ID,
//                    "Low Priority Notifications",
//                    NotificationManager.IMPORTANCE_LOW
//            );
//            channel2.setDescription("This channel is used to send low priority notifications to user");
//            channel2.setShowBadge(false);
//            channel2.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
//            channel2.enableVibration(false);
//            channel2.enableLights(false);
//
//            NotificationManager manager = context.getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel1);
//            manager.createNotificationChannel(channel2);
//            Log.e("NOTIFICATIONMAN","CREATED CHANNELS");
//        }
//    }
//}
