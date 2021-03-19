package com.example.schedulemanager.email;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class EmailService extends JobIntentService {

    Boolean networkOn = false;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (intent.getExtras()!=null){
            networkOn = intent.getBooleanExtra("networkOn",false);
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (UtilsArray_Email.mail != null && !UtilsArray_Email.mail.isEmpty()) {
            for (Email e : UtilsArray_Email.getMail()) {
                if (e.Scheduled && e.cal.getTimeInMillis()<System.currentTimeMillis()) {
                    if (networkOn){
                        Intent i = new Intent(getApplicationContext(), EmailReceiver.class);
                        i.putExtra("id", e.id);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(EmailService.this, (int) e.id, i, PendingIntent.FLAG_UPDATE_CURRENT);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
                        }
                    }
                    else {
                        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
                            Intent i = new Intent(EmailService.this, EmailReceiver.class);
                            i.putExtra("id", e.id);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                sendBroadcast(i);
                            }
                        }
                    }
                }
            }
        }
    }
}






















//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Build;
//import android.os.IBinder;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.work.Constraints;
//import androidx.work.NetworkType;
//import androidx.work.OneTimeWorkRequest;
//import androidx.work.WorkManager;
//import androidx.work.impl.constraints.NetworkState;
//
//import java.util.ArrayList;
//
//import static com.example.firebase.email.UtilsArray_Email.EmailsharedPreferences;
//import static com.example.firebase.note.GsonConverter.IntegerToJson;
//import static com.example.firebase.note.GsonConverter.jsonToInteger;
//
//
//    Boolean restart;
//    public static ArrayList<Integer> notSentEmailList;
//    //    public static SharedPreferences EmailsharedPreferences;
//    final private static String REMAINING_EMAIL_ARRAY_DB = "Email_Pending_DB";
//    AlarmManager alarmManager;
//    ConnectivityManager connMgr;
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void onCreate() {
//        alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
//
//        notSentEmailList = new ArrayList<>();
//        EmailsharedPreferences = this.getSharedPreferences("Email.db", Context.MODE_PRIVATE);
//        notSentEmailList = jsonToInteger(EmailsharedPreferences.getString(REMAINING_EMAIL_ARRAY_DB, null));
//        if (notSentEmailList == null || notSentEmailList.isEmpty()) {
//            notSentEmailList = new ArrayList<>();
//        }
//
//        Log.e("EmailServiceCreated", "Started Service");
//
////        if (notSentEmailList.size() == 0) {
////            stopSelf();
////        }
//
//        sendPending(this);
//        super.onCreate();
//
////        if( restart!=null) {
////            if (restart) {
////                if (position > -1) {
////                    Log.d("EmailService", "Started Service");
////
////
////                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
////                    // ARE WE CONNECTED TO THE NET
////                    if (connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isAvailable() && connMgr.getActiveNetworkInfo().isConnected()) {
////                        Intent i = new Intent(EmailService.this, EmailReceiver.class);
////                        i.putExtra("position", position);
////                        PendingIntent p = PendingIntent.getBroadcast(EmailService.this, (int) UtilsArray_Email.getMail().get(position).getId(),i,PendingIntent.FLAG_UPDATE_CURRENT);
////                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////                            alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),p);
////                        }
////
////                    } else {
////
////                        notSentEmailList.add(position);
////                        sendPending();
////                        SharedPreferences.Editor editor = RemainingEmailsharedPreferences.edit();
////                        editor.putString(REMAINING_EMAIL_ARRAY_DB, IntegerToJson(notSentEmailList));
////                        editor.commit();
////
////
////                    }
////                }
////            }
////        }
//
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
////        position = intent.getIntExtra("position",-1);
//        restart = intent.getBooleanExtra("restart", false);
////        if(position>-1)
////    {
////        Log.d("EmailService","Started Service");
////        Log.d("EmailService",String.valueOf(position));
////
////        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
////        // ARE WE CONNECTED TO THE NET
////        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
////            Intent i = new Intent(EmailService.this,EmailReceiver.class);
////            i.putExtra("position",position);
////            PendingIntent p = PendingIntent.getBroadcast(EmailService.this,0,i,0);
////            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////                alarmManager.setExact(AlarmManager.RTC,System.currentTimeMillis(),p);
////            }
////
////            stopSelf();
////        } else {
////
////            notSentEmailList.add(position);
////            sendPending();
////            SharedPreferences.Editor editor =  RemainingEmailsharedPreferences.edit();
////            editor.putString( REMAINING_EMAIL_ARRAY_DB, IntegerToJson(notSentEmailList));
////            editor.commit();
////
////
////        }
//        if (restart != null) {
//            if (restart) {
//                Log.e("EmailService", "Started Service");
//
////                ConnectivityManager conMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
////                // ARE WE CONNECTED TO THE NET
////                if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
//////                        Intent i = new Intent(EmailService.this, EmailReceiver.class);
////                        i.putExtra("position", position);
////                        PendingIntent p = PendingIntent.getBroadcast(EmailService.this, (int) UtilsArray_Email.getMail().get(position).getId(),i,PendingIntent.FLAG_UPDATE_CURRENT);
////                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////                            alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),p);
//                if (notSentEmailList.size() == 0) {
//                    stopSelf();
//                }
//                sendPending(EmailService.this);
////                }
//            }
//        }
//
//
////        Data data = new Data.Builder()
////                .putInt("position",position)
////                .build();
////
////        Constraints c = new Constraints.Builder()
////                .setRequiredNetworkType(NetworkType.CONNECTED)
////                .build();
////
////        OneTimeWorkRequest mailreq = new OneTimeWorkRequest.Builder(EmailWorker.class)
////                .setConstraints(c)
////                .setInputData(data)
////                .build();
//
//
////        WorkManager w = WorkManager.getInstance();
////        w.enqueue(mailreq);
//
//
////        Intent i  = new Intent(EmailService.this,EmailReceiver.class);
////        i.putExtra("position",position);
//////                context.startService(i);
////        PendingIntent p = PendingIntent.getBroadcast(EmailService.this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
////        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////            alarmManager.setExact(AlarmManager.RTC_WAKEUP, UtilsArray_Email.getMail().get(position).cal.getTimeInMillis(),p);
////        }
//        return START_NOT_STICKY;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.e("EMAILSERVICE", "STOPPED");
//
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
//    //
////    @Nullable
////    @Override
////    public IBinder onBind(Intent intent) {
////        return null;
////    }
//
//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        Intent i = new Intent(getApplicationContext(), EmailService.class);
//        i.putExtra("restart", true);
//        startService(i);
//        super.onTaskRemoved(rootIntent);
//    }
//
//    @Override
//    public void onTrimMemory(int level) {
//        Intent i = new Intent(getApplicationContext(), EmailService.class);
//        i.putExtra("restart", true);
//        startService(i);
//        super.onTrimMemory(level);
//    }
//
//    @Override
//    public void onLowMemory() {
//        Intent i = new Intent(getApplicationContext(), EmailService.class);
//        i.putExtra("restart", true);
//        startService(i);
//        super.onLowMemory();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void sendPending(Context context) {
//
//        Log.e("EMAILSERVICE", "SENDPENDINGSTARTED");
//
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
//
//        if (ni != null && ni.isAvailable() && ni.isConnectedOrConnecting()) {
//            if (notSentEmailList != null && !notSentEmailList.isEmpty()) {
//                for (Integer p : notSentEmailList) {
////                Intent i = new Intent(EmailService.this, EmailReceiver.class);
////                i.putExtra("position", p);
//                    if (UtilsArray_Email.mail.get(p).Scheduled) {
//                        SendMailTask sm = new SendMailTask(EmailService.this, UtilsArray_Email.mail.get(p).From, UtilsArray_Email.mail.get(p).To, UtilsArray_Email.mail.get(p).Subject, UtilsArray_Email.mail.get(p).Message);
//                        sm.execute();
//                        UtilsArray_Email.mail.get(p).Scheduled = false;
//                        notSentEmailList.remove(p);
//                    }
//                }
//            }
//        }else {
////            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
////            Intent intent = new Intent(context,NetworkReceiver.class);
////            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
////
////            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),pendingIntent);
//            Log.e("WORKMANAGER","CALLED");
//            Constraints c = new Constraints.Builder()
//                                .setRequiredNetworkType(NetworkType.CONNECTED)
//                                .build();
//
//            OneTimeWorkRequest mailreq = new OneTimeWorkRequest.Builder(EmailWorker.class)
//                                .setConstraints(c)
//                                .build();
//
//            WorkManager w =  WorkManager.getInstance();
//            w.enqueue(mailreq);
//
//        }
//
//        if (notSentEmailList.size() == 0) {
//            stopSelf();
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void addToPending(Integer position) {
//        if (notSentEmailList == null || notSentEmailList.isEmpty()) {
//            notSentEmailList = new ArrayList<>();
//        }
//        notSentEmailList.add(position);
//        sendPending(EmailService.this);
//
//        SharedPreferences.Editor editor = EmailsharedPreferences.edit();
//        editor.remove(REMAINING_EMAIL_ARRAY_DB);
//        editor.putString(REMAINING_EMAIL_ARRAY_DB, IntegerToJson(notSentEmailList));
//        editor.commit();
//    }
//
//
//}
//
