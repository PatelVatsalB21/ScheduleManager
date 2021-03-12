package com.example.schedulemanager.email;//package com.example.firebase.email;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.TimePicker;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.work.Constraints;
//import androidx.work.Data;
//import androidx.work.ExistingWorkPolicy;
//import androidx.work.NetworkType;
//import androidx.work.OneTimeWorkRequest;
//import androidx.work.WorkManager;
//
//import com.example.firebase.AutoEmailStrings;
////import com.example.firebase.HomeFrag.All_In_One_Main_Adapter;
////import com.example.firebase.HomeFrag.All_Rec_Adapter;
////import com.example.firebase.HomeFrag.UtilsArray_All;
//import com.example.firebase.MainFragments.Fragment_4;
//import com.example.firebase.R;
//import com.example.firebase.note.AlarmReciever;
//import com.example.firebase.note.UtilsArraylist;
//
//import java.sql.Time;
//import java.util.Calendar;
//import java.util.Collections;
//
//import static java.util.Calendar.DATE;
//import static java.util.Calendar.DAY_OF_MONTH;
//import static java.util.Calendar.HOUR;
//import static java.util.Calendar.HOUR_OF_DAY;
//import static java.util.Calendar.MINUTE;
//import static java.util.Calendar.MONTH;
//import static java.util.Calendar.SECOND;
//import static java.util.Calendar.YEAR;
//
//public class TimeEmailDialog extends AppCompatActivity {
//
//    private static final String TAG = "TIMEEMAIL";
//    Boolean timepickervisible = false;
//    Button changeview_btn, save_calender_detail_btn, cancel;
//    TimePicker timePicker;
//    DatePicker datePicker;
//    Calendar cal, calSet;
//    Boolean AlarmEngaged = false;
//    String To,Subject, Message, From;
//    Long id;
//    int year, month, day, hour, min;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.timepicker_email);
//        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        init();
//
//
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        if(bundle!=null){
//            id = bundle.getLong("id", -1);
//            To = bundle.getString("to");
//            From = bundle.getString("from");
//            Subject = bundle.getString("subject");
//            Message = bundle.getString("message");
//
//            Log.e(TAG, "onCreate: "+String.valueOf(From) );
//            Log.d("TimeEmail",String.valueOf(To));
//            Log.d("TimeEmail",String.valueOf(Subject));
//            Log.d("TimeEmail",String.valueOf(Message));
//            Log.d("TimeEmail",String.valueOf(id));
//
//        }
//
//
//
//        datePicker.init(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
//
//            @Override
//            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
//                if (i != 0) {
//                    year = i;
//                }
//                if (i1 != 0) {
//                    month = i1;
//                }
//                if (i2 != 0) {
//                    day = i2;
//                }
//            }
//        });
//
//
//        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//
//            @Override
//            public void onTimeChanged(final TimePicker timePicker, int hourOfDay, int minute) {
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (hourOfDay != 0) {
//                        hour = hourOfDay;
//                    }
//                    if (minute != 0) {
//                        min = minute;
//                    }
//
//
//                    calSet.set(HOUR_OF_DAY, hour);
//                    calSet.set(MINUTE, min);
//                    calSet.set(SECOND, 0);
//                    calSet.set(Calendar.MILLISECOND, 0);
//                    calSet.set(DAY_OF_MONTH, day);
//                    calSet.set(MONTH, month);
//                    calSet.set(YEAR, year);
//
//
//                    if (calSet.compareTo(cal) <= 0) {
//                        //Today Set time passed, count to tomorrow
//                        calSet.add(DATE, 1);
//                    }
//
//                     }
//            }
//        });
//
//
//        changeview_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (timepickervisible) {
//                    timePicker.setVisibility(View.INVISIBLE);
//                    datePicker.setVisibility(View.VISIBLE);
//                    changeview_btn.setText("Show Timepicker");
//                    timepickervisible = false;
//                } else {
//                    timepickervisible = true;
//                    timePicker.setVisibility(View.VISIBLE);
//                    datePicker.setVisibility(View.INVISIBLE);
//                    changeview_btn.setText("Show Calender");
//                }
//            }
//        });
//
//
//        save_calender_detail_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                AutoEmailStrings.addStringsForEmail(To, TimeEmailDialog.this);
////                AutoEmailStrings.addStringsForEmail(From, TimeEmailDialog.this);
//
//
//                AlarmEngaged = true;
//
//                Log.d("AlarmService", "Preparing to send notification...: ");
//
//
//
//                if (id > -1) {
//                    int p = calSet.get(HOUR) * 60 + calSet.get(MINUTE);
//                    int m = (cal.get(HOUR)) * 60 + cal.get(MINUTE);
//
////                    if (!timePicker.is24HourView()) {
////                        p = p-12;
////                    }
//
//                    int diff = p - m;
//                    int days = diff / 1440;
//                    int days_rem = diff % 1440;
//                    int mins = days_rem % 60;
//                    int hrs = days_rem / 60;
//
//
//                    int mildiff = diff * 60;
////                    Log.d("Inside save", String.valueOf(calSet.getTimeInMillis()));
////                    Log.d("Inside save", String.valueOf(hour));
////                    Log.d("Inside save", String.valueOf(min));
////                    Log.d("Inside save", String.valueOf(mildiff));
////                    Log.d("Inside save", String.valueOf(calSet.get(DAY_OF_MONTH)));
////                    Log.d("Inside save", String.valueOf(calSet.get(MONTH)));
////                    Log.d("Inside save", String.valueOf(calSet.get(YEAR)));
////                    Log.d("Inside save", String.valueOf(month));
////                    Log.d("Inside save", String.valueOf(day));
////                    Log.d("Inside save", String.valueOf(calSet.get(DATE)));
//                }
////                    SendMailTask sm = new SendMailTask(TimeEmailDialog.this,From, To, Subject,Message);
////                    sm.execute();
//
////                Data data = new Data.Builder()
////                        .putInt("position",UtilsArray_Email.getMail().size())
////                        .build();
////
//////                        EmailWorker e = new EmailWorker(TimeEmailDialog.this,);
//////                        e.getPosition(UtilsArray_Email.getMail().size());
//////
////                        Constraints c = new Constraints.Builder()
////                                .setRequiredNetworkType(NetworkType.CONNECTED)
////                                .build();
////
////                        OneTimeWorkRequest mailreq = new OneTimeWorkRequest.Builder(EmailWorker.class)
////                                .addTag(String.valueOf(calSet.getTimeInMillis()))
////                                .setConstraints(c)
////                                .setInputData(data)
////                                .build();
//
//                         Intent i  = new Intent(TimeEmailDialog.this,EmailReceiver.class);
//                         i.putExtra("position",UtilsArray_Email.mail.size()-1);
//                         PendingIntent p = PendingIntent.getBroadcast(TimeEmailDialog.this, 0,i,PendingIntent.FLAG_UPDATE_CURRENT);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,calSet.getTimeInMillis(),p);
//                }
//                Email email = new Email(id,From,To, Subject,Message,calSet,AlarmEngaged,p);
//                UtilsArray_Email.AddToMail(email,TimeEmailDialog.this);
//                Email_rec_Adapter adapter = new Email_rec_Adapter(UtilsArray_Email.getMail());
//                adapter.notifyDataSetChanged();
//
//
//
//
////                Intent i  = new Intent(TimeEmailDialog.this,EmailService.class);
////                i.putExtra("position",(UtilsArray_Email.mail.size()-1));
////                EmailService.en(TimeEmailDialog.this,i);
//
//// context.startService(i);
////                PendingIntent p = PendingIntent.getService(TimeEmailDialog.this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
////                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),p);
////                }
////
////                         WorkRequestList.addWorkRequest(mailreq);
////                         WorkRequestList.sortWorkRequestList();
////                         WorkRequestList.EnqueueWork();
////                WorkManager w =  WorkManager.getInstance();
////                w.enqueueUniqueWork(Subject,ExistingWorkPolicy.APPEND,WorkRequestList.workRequestList);
//
////                        Intent intent = new Intent(TimeEmailDialog.this,EmailService.class);
////                        intent.putExtra("position",UtilsArray_Email.getMail().size());
////                        startService(intent);
//
//
//
////            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mildiff,19999999, pendingIntent);
////
//
//
////                    if (diff < 60) {
////                        if (mildiff > 60)
////                            Toast.makeText(TimeEmailDialog.this, "Alarm set for " + diff + " minutes from now.", Toast.LENGTH_SHORT).show();
////                        else
////                            Toast.makeText(TimeEmailDialog.this, "Alarm will ring within a minute", Toast.LENGTH_SHORT).show();
////
////                    } else {
////                        if (diff > 1440) {
////                            Toast.makeText(TimeEmailDialog.this, "Alarm set for " + days + " days," + hrs + " hours and " + mins + " minutes from now.", Toast.LENGTH_SHORT).show();
////
////                        } else {
////                            Toast.makeText(TimeEmailDialog.this, "Alarm set for " + (diff / 60) + " hours and " + (diff % 60) + " minutes from now.", Toast.LENGTH_SHORT).show();
////                        }
////                    }
////                } else {
////                    Toast.makeText(TimeEmailDialog.this, "Wrong Input", Toast.LENGTH_SHORT).show();
//
//
//
//
//
//
//                startActivity(new Intent(TimeEmailDialog.this, Fragment_4.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK));
//               // startActivity(new Intent(TimeEmailDialog.this, EmailList.class));
//
//
//            }
//        });
//
//    }
//
//
//
//    public void init() {
//
//        save_calender_detail_btn = findViewById(R.id.email_save_calender_detail_btn);
//        cancel = findViewById(R.id.email_cancel_alarm_btn);
//        changeview_btn = findViewById(R.id.email_changeView_btn);
////        timePicker = findViewById(R.id.email_reminder_timepicker);
////        datePicker = findViewById(R.id.email_reminder_datepicker);
//        calSet = Calendar.getInstance();
//        cal = Calendar.getInstance();
//        year = cal.get(YEAR);
//        month = cal.get(MONTH);
//        day = cal.get(DAY_OF_MONTH);
//        hour = cal.get(HOUR);
//        min = cal.get(MINUTE);
//
//
//
//
//    }
//
//
//}
//
//
//
