package com.example.schedulemanager.Task;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.schedulemanager.MainFragments.Fragment_2;
import com.example.schedulemanager.R;

import java.util.Calendar;
import java.util.List;

import static com.example.schedulemanager.Task.UtilsArray_Task.task;

public class TaskReceiver extends BroadcastReceiver {
    private static AudioManager audioManager;
    Context c;
    long id;
//    Integer position;
//    LottieAnimationView lottieAnimationView1,lottieAnimationView2;
    AlarmManager alarmManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("TaskReceiver", "Called");
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (intent.getExtras() != null) {
            c = context;
            UtilsArray_Task.initTask(context);
            id = intent.getLongExtra("id", -1);
            Task taskAssigned = null;

            if (id>-1) taskAssigned = Task.findTaskByID(id);

            if (taskAssigned!=null) {

                wakeUpScreen(c);
                c.startActivity(new Intent(c, FullScreenTaskReminder.class).putExtra("id",id));
//                sendNotification(taskAssigned, (int) id);

//                if (taskAssigned.isRepeating) {
//
//                    Intent againIntent = new Intent(context.getApplicationContext(), TaskReceiver.class);
//                    intent.putExtra("id", id);
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), (int) id, againIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                    List<Long> timeToWeeklyRings = Task.getTimeToWeeklyRings(taskAssigned);
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTimeInMillis(timeToWeeklyRings.get(0));
//                    task.get(task.indexOf(taskAssigned)).calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH));
//                    task.get(task.indexOf(taskAssigned)).calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
//                    task.get(task.indexOf(taskAssigned)).calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
////                    for (long millis : timeToWeeklyRings) {
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeToWeeklyRings.get(0), pendingIntent);
////                    }
//                }else {
//                    task.get(task.indexOf(taskAssigned)).isComplete = true;
//                    task.get(task.indexOf(taskAssigned)).finishTime = Calendar.getInstance();
//                }
//                UtilsArray_Task.UpdateTask(UtilsArray_Task.getTask(), context);
//                Fragment_2.TaskReceiverRefresh(context);
            }
        }
    }


    public static void sendNotification(Task t, Integer id, Context c){
//        lottieAnimationView1.findViewById(R.id.task_collapsed_lottie_view);
//        lottieAnimationView1.setAnimation(UtilsArray_Task.category.get(UtilsArray_Task.task.get(position).LottieFileRes).LottieRes);
//
//        lottieAnimationView2.findViewById(R.id.task_notification_expanded_lottie_view);
//        lottieAnimationView2.setAnimation(UtilsArray_Task.category.get(UtilsArray_Task.task.get(position).LottieFileRes).LottieRes);

        String CHANNEL_MAX_ID = "channel_important";
        Log.e("TASKRECIEVER","Sending Notification");
        Intent activityIntent = new Intent(c, FullScreenTaskReminder.class);
        activityIntent.putExtra("id", t.id);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(c, id, activityIntent, 0);

        RemoteViews notificationLayout = new RemoteViews(c.getPackageName(), R.layout.task_notification_layout_collapsed);
        notificationLayout.setImageViewResource(R.id.task_notification_collapsed_lottie_imageView, UtilsArray_Task.taskReceiverImages.get(t.LottieFileRes));
        notificationLayout.setTextViewText(R.id.task_notification_collapsed_type_of_task, "It's time for "+UtilsArray_Task.category.get(t.LottieFileRes).Category);
        notificationLayout.setTextViewText(R.id.task_notification_collapsed_title, t.Title);

        NotificationChannel channel1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel1 = new NotificationChannel(
                    CHANNEL_MAX_ID,
                    "Important Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            channel1.setDescription("This channel is used to send important notifications to user.");
            channel1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel1.setShowBadge(true);
//            channel1.enableVibration(true);
            channel1.enableLights(true);
            channel1.setSound(null,null);
            channel1.setVibrationPattern(null);
//            channel1.setSound(ringtone, Notification.AUDIO_ATTRIBUTES_DEFAULT);
//            channel1.setVibrationPattern(new long[]{0, 1000, 1000});

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
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
                .setOngoing(true);

        Notification notification = builder.build();
//        notification.sound = ringtone;
        notification.flags = Notification.FLAG_NO_CLEAR| Notification.FLAG_INSISTENT | Notification.FLAG_AUTO_CANCEL;

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(c);
        managerCompat.notify(1111,notification);
    }

    private void wakeUpScreen(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();

        Log.e("screen on......", "" + isScreenOn);
        if (!isScreenOn) {
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
            wl.acquire(10000);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LockCpu");
            wl_cpu.acquire(10000);
        }
    }

}
