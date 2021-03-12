package com.example.schedulemanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.schedulemanager.MainFragments.Fragment_4;
import com.example.schedulemanager.Setting.Settings_Main;
import com.example.schedulemanager.Task.Task;
import com.example.schedulemanager.Task.TaskReceiver;
import com.example.schedulemanager.Task.UtilsArray_Task;
import com.example.schedulemanager.email.Email;
import com.example.schedulemanager.email.EmailReceiver;
import com.example.schedulemanager.email.UtilsArray_Email;
import com.example.schedulemanager.note.AlarmReciever;
import com.example.schedulemanager.note.Notes;
import com.example.schedulemanager.note.UtilsArraylist;

import java.util.List;
import java.util.Objects;

public class BootReciever extends BroadcastReceiver {

    AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
                    alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                    if (UtilsArray_Email.mail != null && !UtilsArray_Email.mail.isEmpty()) {
                        int eposition = 0;
                        for (Email e : UtilsArray_Email.mail) {
                            if (e.cal != null && e.Scheduled && Email.isEmailAheadOfTime(e.cal)) {
                                Intent eintent = new Intent(context, EmailReceiver.class);
                                eintent.putExtra("position", eposition);
                                PendingIntent ePendingIntent = PendingIntent.getBroadcast(context, (int) e.id, eintent, PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, e.cal.getTimeInMillis(), ePendingIntent);
//                                    UtilsArray_Email.AddToEmailIntent(context,new CustomPenInt(eposition,ePendingIntent));
                            }
                            eposition++;
                        }
                    }

                    if (UtilsArray_Task.task != null && !UtilsArray_Task.task.isEmpty()) {

                        int tposition = 0;
                        for (Task t : UtilsArray_Task.task) {
                            if (!t.isComplete && t.calendar != null && Task.isTaskAheadOfTime(t.calendar)) {
                                Intent tintent = new Intent(context, TaskReceiver.class);
                                tintent.putExtra("position", tposition);
                                PendingIntent tPendingIntent = PendingIntent.getBroadcast(context, (int) t.id, tintent, PendingIntent.FLAG_UPDATE_CURRENT);

                                if (t.isRepeating) {

                                    List<Long> timeToWeeklyRings = Task.getTimeToWeeklyRings(t);
//                                    for (long millis : timeToWeeklyRings) {
                                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeToWeeklyRings.get(0), tPendingIntent);
//                                    }
//                                UtilsArray_Task.AddToTaskIntent(context,new CustomPenInt(tposition,tPendingIntent));
                                } else {
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, Task.getTimeToNextRing(t.calendar), tPendingIntent);
                                }
                                tposition++;
                            }
                        }

                        if (UtilsArraylist.note != null && !UtilsArraylist.note.isEmpty()) {

                            int nposition = 0;
                            for (Notes n : UtilsArraylist.note) {
                                if (n.calendar != null && n.EngagedAlarm && Notes.isNoteAheadOfTime(n.calendar)) {

                                    Intent nintent = new Intent(context, AlarmReciever.class);
                                    nintent.putExtra("position", nposition);
                                    PendingIntent nPendingIntent = PendingIntent.getBroadcast(context, (int) n.id, nintent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, n.calendar.getTimeInMillis(), nPendingIntent);
//                                UtilsArraylist.AddToNoteIntent(context,new CustomPenInt(nposition,nPendingIntent));
                                }
                                nposition++;
                            }
                        }

//                    for (CustomPenInt i : UtilsArray_Email.emailPenInt) {
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, UtilsArray_Email.mail.get(i.position).cal.getTimeInMillis(), i.pendingIntent);
//                    }
//
//                    for (CustomPenInt j : UtilsArray_Task.taskPendingIntent) {
//                        List<Long> timeToWeeklyRings = Task.getTimeToWeeklyRings(UtilsArray_Task.task.get(j.position));
//                        for (long millis : timeToWeeklyRings) {
//                            alarmManager.setRepeating(
//                                    AlarmManager.RTC_WAKEUP,millis,AlarmManager.INTERVAL_DAY * 7,j.pendingIntent);
//                        }
//                    }
//
//                    for (CustomPenInt k : UtilsArraylist.notesPendingIntents) {
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, UtilsArraylist.note.get(k.position).calendar.getTimeInMillis(), k.pendingIntent);
//                    }

                        Fragment_4.checkPendingEmails(context);

//                    if (Settings_Main.isAssitiveNotificationOn){
//                        Settings_Main.createNotification(context);
//                    }
                        Settings_Main.LoadChangedSettings(context);
                        Log.d("BOOTUPRECEIVER", "Called");


                    }
                }else if (intent.getBooleanExtra("NewUserAlarmCreation", false)) {
//                        UtilsArray_Task.taskPendingIntent = new ArrayList<>();
//                        UtilsArraylist.notesPendingIntents = new ArrayList<>();
//                        UtilsArray_Email.emailPenInt = new ArrayList<>();

                        if (UtilsArray_Email.mail != null && !UtilsArray_Email.mail.isEmpty()) {
                            int eposition = 0;
                            for (Email e : UtilsArray_Email.mail) {
                                if (e.cal != null && e.Scheduled && Email.isEmailAheadOfTime(e.cal)) {
                                    Intent eintent = new Intent(context, EmailReceiver.class);
                                    eintent.putExtra("position", eposition);
                                    PendingIntent ePendingIntent = PendingIntent.getBroadcast(context, (int) e.id, eintent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, e.cal.getTimeInMillis(), ePendingIntent);
//                                    UtilsArray_Email.AddToEmailIntent(context,new CustomPenInt(eposition,ePendingIntent));
                                }
                                eposition++;
                            }
                        }

                    if (UtilsArray_Task.task != null && !UtilsArray_Task.task.isEmpty()) {

                        int tposition = 0;
                        for (Task t : UtilsArray_Task.task) {
                            if (!t.isComplete && t.calendar != null && Task.isTaskAheadOfTime(t.calendar)) {
                                Intent tintent = new Intent(context, TaskReceiver.class);
                                tintent.putExtra("position", tposition);
                                PendingIntent tPendingIntent = PendingIntent.getBroadcast(context, (int) t.id, tintent, PendingIntent.FLAG_UPDATE_CURRENT);

                                if (t.isRepeating) {

                                    List<Long> timeToWeeklyRings = Task.getTimeToWeeklyRings(t);
                                    for (long millis : timeToWeeklyRings) {
                                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, millis, AlarmManager.INTERVAL_DAY * 7, tPendingIntent);
                                    }
//                                UtilsArray_Task.AddToTaskIntent(context,new CustomPenInt(tposition,tPendingIntent));
                                } else {
                                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Task.getTimeToNextRing(t.calendar), AlarmManager.INTERVAL_DAY * 7, tPendingIntent);
                                }

                                tposition++;
                            }
                        }
                    }

                        if (UtilsArraylist.note != null && !UtilsArraylist.note.isEmpty()) {

                            int nposition = 0;
                            for (Notes n : UtilsArraylist.note) {
                                if (n.calendar != null && n.EngagedAlarm && Notes.isNoteAheadOfTime(n.calendar)) {

                                    Intent nintent = new Intent(context, AlarmReciever.class);
                                    nintent.putExtra("position", nposition);
                                    PendingIntent nPendingIntent = PendingIntent.getBroadcast(context, (int) n.id, nintent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, n.calendar.getTimeInMillis(), nPendingIntent);
//                                UtilsArraylist.AddToNoteIntent(context,new CustomPenInt(nposition,nPendingIntent));
                                }
                                nposition++;
                            }
                        }
                }
            }
        }
    }
}