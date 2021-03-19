package com.example.schedulemanager.note;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.schedulemanager.Utils.NotifierService;
import com.example.schedulemanager.R;


public class AlarmReciever extends BroadcastReceiver {


    NotificationManagerCompat notificationManager;
    long id;
    Context mContext;
    private String CHANNEL_MAX_ID = "channel_important";

    @Override
    public void onReceive(final Context context, Intent intent) {
        notificationManager = NotificationManagerCompat.from(context);
        UtilsArraylist.initNote(context);
        if (intent != null) {
            id = intent.getLongExtra("id", -1);
            Notes assignedNote = Notes.findNoteFromId(id);
            if (assignedNote != null) {
                mContext = context;
                wakeUpScreen();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                    sendOnChannel1(assignedNote);
                }
            }
        }
    }

    private void wakeUpScreen() {
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (!isScreenOn) {
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
                            | PowerManager.ON_AFTER_RELEASE, "MyLock");
            wl.acquire(10000);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl_cpu = pm.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK, "LockCpu");
            wl_cpu.acquire(10000);
        }
    }

    private void sendOnChannel1(Notes notes) {
        String title = notes.title;
        String message = notes.desc;
        Intent activityIntent = new Intent(mContext, Note_Open.class);
        activityIntent.putExtra("position", UtilsArraylist.note.indexOf(notes));
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext,
                0, activityIntent, 0);

        NotificationChannel channel1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel1 = new NotificationChannel(
                    CHANNEL_MAX_ID,
                    "Important Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription(
                    "This channel is used to send important notifications to user.");
            channel1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel1.setShowBadge(true);
            channel1.enableLights(true);
            channel1.setSound(null, null);
            channel1.setVibrationPattern(null);
            NotificationManager manager = mContext.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
        Notification notification = new NotificationCompat.Builder(mContext, CHANNEL_MAX_ID)
                .setSmallIcon(R.mipmap.ic_note_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSound(null)
                .setVibrate(null)
                .build();

        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_INSISTENT
                | Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notification);
        mContext.startService(new Intent(mContext, NotifierService.class));
    }
}