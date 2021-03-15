package com.example.schedulemanager.Task;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.PowerManager;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.schedulemanager.R;

public class TaskReceiver extends BroadcastReceiver {
    private static AudioManager audioManager;
    Context c;
    long id;
    AlarmManager alarmManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (intent.getExtras() != null) {
            c = context;
            UtilsArray_Task.initTask(context);
            id = intent.getLongExtra("id", -1);
            Task taskAssigned = null;
            if (id > -1) taskAssigned = Task.findTaskByID(id);
            if (taskAssigned != null) {
                wakeUpScreen(c);
                c.startActivity(new Intent(c, FullScreenTaskReminder.class).putExtra("id", id));
            }
        }
    }


    public static void sendNotification(Task t, Integer id, Context c) {
        String CHANNEL_MAX_ID = "channel_important";
        Intent activityIntent = new Intent(c, FullScreenTaskReminder.class);
        activityIntent.putExtra("id", t.id);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(c, id, activityIntent, 0);
        RemoteViews notificationLayout = new RemoteViews(c.getPackageName(),
                R.layout.task_notification_layout_collapsed);
        notificationLayout.setImageViewResource(R.id.task_notification_collapsed_lottie_imageView,
                UtilsArray_Task.taskReceiverImages.get(t.LottieFileRes));
        notificationLayout.setTextViewText(R.id.task_notification_collapsed_type_of_task,
                "It's time for " + UtilsArray_Task.category.get(t.LottieFileRes).Category);
        notificationLayout.setTextViewText(R.id.task_notification_collapsed_title, t.Title);

        NotificationChannel channel1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel1 = new NotificationChannel(
                    CHANNEL_MAX_ID,
                    "Important Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel1.setDescription(
                    "This channel is used to send important notifications to user.");
            channel1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel1.setShowBadge(true);
            channel1.enableLights(true);
            channel1.setSound(null, null);
            channel1.setVibrationPattern(null);
            NotificationManager manager = c.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(c, CHANNEL_MAX_ID)
                .setCustomContentView(notificationLayout)
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(UtilsArray_Task.taskReceiverImages.get(t.LottieFileRes))
                .setContentIntent(contentIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setSound(null)
                .setVibrate(null)
                .setOngoing(true);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_INSISTENT
                | Notification.FLAG_AUTO_CANCEL;
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(c);
        managerCompat.notify(1111, notification);
    }

    private void wakeUpScreen(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
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
}
