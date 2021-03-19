package com.example.schedulemanager.email;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.PowerManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.schedulemanager.MainFragments.HomePage;
import com.example.schedulemanager.NotifierService;
import com.example.schedulemanager.R;


public class EmailReceiver extends BroadcastReceiver {
    long id;
    NotificationManagerCompat managerCompat;
    private String CHANNEL_MAX_ID = "channel_important";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        id = intent.getLongExtra("id", -1);
        UtilsArray_Email.initMail(context);
        Email assignedEmail = Email.findEmailByID(id);
        if (assignedEmail != null) {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
                SendMailTask sm = new SendMailTask(context, assignedEmail.From, assignedEmail.To,
                        assignedEmail.Subject, assignedEmail.Message);
                sm.execute();
                UtilsArray_Email.mail.get(UtilsArray_Email.mail.indexOf(assignedEmail)).Scheduled =
                        !sm.sentSuccessfully;
                Email_rec_Adapter adapter = new Email_rec_Adapter(UtilsArray_Email.mail);
                adapter.notifyDataSetChanged();
                UtilsArray_Email.UpdateMail(UtilsArray_Email.getMail(), context);
            } else {
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
                    channel1.setVibrationPattern(null);
                    channel1.setSound(null, null);
                    NotificationManager manager = context.getSystemService(
                            NotificationManager.class);
                    manager.createNotificationChannel(channel1);
                }
                Intent emailIntent = new Intent(context, HomePage.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, emailIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                String title, text;
                title = "Please turn on internet to send your email ";
                text = assignedEmail.Subject;
                if (text == null) {
                    text = "Empty subject";
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                        CHANNEL_MAX_ID)
                        .setContentIntent(pendingIntent)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setSubText(assignedEmail.To)
                        .setSmallIcon(R.mipmap.ic_note_icon_round)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true)
                        .setOngoing(true)
                        .setCategory(Notification.CATEGORY_ALARM)
                        .setSound(null)
                        .setVibrate(null);

                Notification notification = builder.build();
                notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_INSISTENT
                        | Notification.FLAG_AUTO_CANCEL;
                managerCompat = NotificationManagerCompat.from(context);
                managerCompat.notify(1011, notification);
                wakeUpScreen(context);
                context.sendBroadcast(new Intent(context, NetworkReceiver.class));
                context.startService(new Intent(context, NotifierService.class));
            }
        }
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