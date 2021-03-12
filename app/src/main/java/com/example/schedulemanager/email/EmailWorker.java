package com.example.schedulemanager.email;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class EmailWorker extends Worker {

    Context context;
    public EmailWorker(@NonNull Context mContext, @NonNull WorkerParameters workerParams) {
        super(mContext, workerParams);
        this.context = mContext;

    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e("EMAILWORKER","CALLED");

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if (UtilsArray_Email.mail != null && !UtilsArray_Email.mail.isEmpty()) {

            for (Email e : UtilsArray_Email.getMail()) {
                Calendar calendar = Calendar.getInstance();
                if (e.Scheduled && calendar.compareTo(e.cal)>0) {

                    Intent i = new Intent(context, EmailReceiver.class);
                    i.putExtra("position", UtilsArray_Email.getMail().indexOf(e));
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) e.id, i, PendingIntent.FLAG_UPDATE_CURRENT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
                    }

                    Log.e("EMAILWORKER","SENDING");

                }
            }
        }

        return Result.retry();

    }



}
