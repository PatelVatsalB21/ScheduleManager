package com.example.schedulemanager.note;

import android.app.AlarmManager;
import android.app.MediaRouteButton;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedulemanager.MainFragments.HomePage;
import com.example.schedulemanager.R;
import com.example.schedulemanager.Task.NewTask;
import com.example.schedulemanager.Task.Task;
import com.example.schedulemanager.email.UtilsArray_Email;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

public class Timepicker extends AppCompatActivity {

    Boolean timePickerVisible = false;
    Boolean CalendarVisible = false;
    Button Show_Cal, Show_Clock, cancel_date, cancel_time, save_date, save_time, save, discard;
    TimePicker timePicker;
    DatePicker datePicker;
    Calendar cal, calSet;
    AlarmManager alarmManager;
    Integer position;
    private PendingIntent pendingIntent;
    RelativeLayout dateLinLayout, timeLinLayout, main_layout;
    TextView Date, Time, note_title, note_desc;
    SimpleDateFormat d, t;
    int year, month, day, hour, min;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timepicker);
        init();
        d = new SimpleDateFormat("dd/MM/yyyy");
        t = new SimpleDateFormat("hh:mm a");
        note_title.setText(UtilsArraylist.getNote().get(position).title);
        note_desc.setText(UtilsArraylist.getNote().get(position).desc);
        Date.setText(d.format(calSet.getTimeInMillis()));
        datePicker.setMinDate(cal.getTimeInMillis() - 60000);
        Time.setText(t.format(calSet.getTimeInMillis()));
        datePicker.init(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH),
                (datePicker, i, i1, i2) -> {
                    if (i != 0) {
                        year = i;
                    }
                    if (i1 != 0) {
                        month = i1;
                    }
                    if (i2 != 0) {
                        day = i2;
                    }
                });

        timePicker.setOnTimeChangedListener((timePicker, hourOfDay, minute) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (hourOfDay != 0) {
                    hour = hourOfDay;
                }
                min = minute;
            }
        });

        save_date.setOnClickListener(v -> {
            cal = Calendar.getInstance();
            calSet.set(DAY_OF_MONTH, day);
            calSet.set(MONTH, month);
            calSet.set(YEAR, year);
            check_Date_Time(v);
            Date.setText(d.format(calSet.getTimeInMillis()));
            Time.setText(t.format(calSet.getTimeInMillis()));
            dateLinLayout.setVisibility(View.INVISIBLE);
            main_layout.setVisibility(View.VISIBLE);
            CalendarVisible = false;
        });

        save_time.setOnClickListener(v -> {
            cal = Calendar.getInstance();
            calSet.set(HOUR_OF_DAY, hour);
            calSet.set(MINUTE, min);
            calSet.set(SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            check_Date_Time(v);
            Time.setText(t.format(calSet.getTimeInMillis()));
            Date.setText(d.format(calSet.getTimeInMillis()));
            timeLinLayout.setVisibility(View.INVISIBLE);
            main_layout.setVisibility(View.VISIBLE);
            timePickerVisible = false;
        });

        Show_Clock.setOnClickListener(view -> {
            if (timePickerVisible) {
                timeLinLayout.setVisibility(View.INVISIBLE);
                main_layout.setVisibility(View.VISIBLE);
                timePickerVisible = false;
            } else {
                main_layout.setVisibility(View.INVISIBLE);
                timeLinLayout.setVisibility(View.VISIBLE);
                timePickerVisible = true;
            }
        });

        Show_Cal.setOnClickListener(view -> {
            if (CalendarVisible) {
                dateLinLayout.setVisibility(View.INVISIBLE);
                main_layout.setVisibility(View.VISIBLE);
                CalendarVisible = false;
            } else {
                dateLinLayout.setVisibility(View.VISIBLE);
                main_layout.setVisibility(View.INVISIBLE);
                CalendarVisible = true;
            }
        });

        cancel_time.setOnClickListener(view -> {
            if (timePickerVisible) {
                timeLinLayout.setVisibility(View.INVISIBLE);
                main_layout.setVisibility(View.VISIBLE);
                timePickerVisible = false;
            }
        });

        cancel_date.setOnClickListener(view -> {
            if (CalendarVisible) {
                dateLinLayout.setVisibility(View.INVISIBLE);
                main_layout.setVisibility(View.VISIBLE);
                CalendarVisible = false;
            }
        });

        save.setOnClickListener(view -> {
            if (check_Date_Time(view)) {
                UtilsArraylist.note.get(position).EngagedAlarm = true;
                UtilsArraylist.note.get(position).calendar = calSet;
                UtilsArraylist.UpdateNote(UtilsArraylist.getNote(), Timepicker.this);
                Intent reciever_intent = new Intent(getApplicationContext(), AlarmReciever.class);
                reciever_intent.putExtra("id", UtilsArraylist.note.get(position).id);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) UtilsArraylist.note.get(position).id, reciever_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
                }
                String timeUntilNextRing = Task.getStringOfTimeUntilNextRing(calSet.getTimeInMillis() - System.currentTimeMillis());
                makeSnackBar(view, "Task set for " + timeUntilNextRing);
                finish();
            }
        });

        discard.setOnClickListener(view -> showExitDialog(Timepicker.this));
    }

    public void init() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        discard = findViewById(R.id.cancel_alarm_btn);
        save = findViewById(R.id.save_calender_detail_btn);
        timePicker = findViewById(R.id.note_reminder_timePicker);
        datePicker = findViewById(R.id.note_reminder_datepicker);
        calSet = Calendar.getInstance();
        cal = Calendar.getInstance();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Show_Cal = findViewById(R.id.note_reminder_calendar_show_btn);
        Show_Clock = findViewById(R.id.note_reminder_timePicker_show_btn);
        save_date = findViewById(R.id.note_reminder_datePicker_save_btn);
        save_time = findViewById(R.id.note_reminder_timepicker_save_btn);
        cancel_time = findViewById(R.id.note_reminder_timepicker_cancel_btn);
        cancel_date = findViewById(R.id.note_reminder_datePicker_cancel_btn);
        note_title = findViewById(R.id.note_reminder_title);
        note_desc = findViewById(R.id.note_reminder_note);
        Date = findViewById(R.id.note_reminder_date_indicator);
        Time = findViewById(R.id.note_reminder_time_indicator);
        timeLinLayout = findViewById(R.id.note_reminder_time_lin_layout);
        dateLinLayout = findViewById(R.id.note_reminder_date_lin_layout);
        main_layout = findViewById(R.id.note_reminder_main_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setCurrentHour(cal.get(HOUR_OF_DAY));
            timePicker.setCurrentMinute(cal.get(MINUTE));
        }
    }

    @Override
    public void onBackPressed() {
         if (timePickerVisible || CalendarVisible){
            timeLinLayout.setVisibility(View.INVISIBLE);
            dateLinLayout.setVisibility(View.INVISIBLE);
            main_layout.setVisibility(View.VISIBLE);
            timePickerVisible = false;
            CalendarVisible = false;
        } else {
             showExitDialog(Timepicker.this);
         }
    }

    public void showExitDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Exit")
                .setMessage("Any unsaved changes will be discarded")
                .setPositiveButton("Exit", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    startActivity(new Intent(context, HomePage.class));
                    startActivity(new Intent(Timepicker.this, HomePage.class).putExtra("num",2).putExtra("nav", R.id.nav_my_notes));
                    finish();
                })
                .setNegativeButton("Continue Editing", null)
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void makeSnackBar(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public boolean check_Date_Time(View v){
        cal = Calendar.getInstance();
        if (calSet.compareTo(cal) <= 0) {
            makeSnackBar(v, "Date and Time you selected has already passed away");
            calSet.setTimeInMillis(cal.getTimeInMillis());
            datePicker.updateDate(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH));
            timePicker.setCurrentHour(cal.get(HOUR_OF_DAY));
            timePicker.setCurrentMinute(cal.get(MINUTE));
            return false;
        }else return true;
    }


}



