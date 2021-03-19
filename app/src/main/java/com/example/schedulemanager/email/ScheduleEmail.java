package com.example.schedulemanager.email;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedulemanager.HomeFrag.UtilsArray_All;
import com.example.schedulemanager.MainFragments.Fragment_4;
import com.example.schedulemanager.MainFragments.HomePage;
import com.example.schedulemanager.R;
import com.example.schedulemanager.Setting.Settings_Main;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ScheduleEmail extends AppCompatActivity {
    EditText Subject, Message, To;
    TextView From, Date, Time;
    ImageButton back_btn;
    Button EmailBtnSend, Show_Cal, Show_Clock, Discard, cancel_date, cancel_time, save_date,
            save_time;
    long id;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TimePicker timePicker;
    DatePicker datePicker;
    Calendar cal, calSet;
    int year, month, day, hour, min;
    Boolean timePickerVisible = false;
    Boolean CalendarVisible = false, AlarmEngaged = false;
    RelativeLayout main_layout;
    RelativeLayout date_lay, time_lay;
    SimpleDateFormat d, t;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_email);
        Settings_Main.LoadChangedSettings(ScheduleEmail.this);
        if (Settings_Main.App_Password == null || Settings_Main.App_Password.isEmpty()) {
            Snackbar.make(this.getCurrentFocus(),
                    "Google App Password is required. Please go to Settings ",
                    Snackbar.LENGTH_SHORT).show();
            startActivity(new Intent(ScheduleEmail.this, HomePage.class).setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        } else {
            Message = findViewById(R.id.email_task_message);
            Subject = findViewById(R.id.email_task_subject);
            To = findViewById(R.id.email_task_to);
            EmailBtnSend = findViewById(R.id.email_task_send_btn);
            From = findViewById(R.id.email_task_from);
            Date = findViewById(R.id.email_task_date_indicator);
            Time = findViewById(R.id.email_task_time_indicator);
            Show_Cal = findViewById(R.id.email_task_calendar_btn);
            Show_Clock = findViewById(R.id.email_task_timePicker_btn);
            timePicker = findViewById(R.id.email_task_timepicker);
            datePicker = findViewById(R.id.email_task_datepicker);
            main_layout = findViewById(R.id.Schedule_email_main_rel_layout);
            date_lay = findViewById(R.id.Schedule_email_main_date_lin_layout);
            time_lay = findViewById(R.id.Schedule_email_main_time_lin_layout);
            cancel_time = findViewById(R.id.email_task_timepicker_cancel_btn);
            cancel_date = findViewById(R.id.email_task_datepicker_cancel_btn);
            Discard = findViewById(R.id.email_task_discard_btn);
            save_date = findViewById(R.id.email_task_datepicker_save_btn);
            save_time = findViewById(R.id.email_task_timepicker_save_btn);
            back_btn = findViewById(R.id.email_task_back_btn);
            calSet = Calendar.getInstance();
            cal = Calendar.getInstance();
            year = cal.get(YEAR);
            month = cal.get(MONTH);
            day = cal.get(DAY_OF_MONTH);
            hour = cal.get(HOUR);
            min = cal.get(MINUTE);
            d = new SimpleDateFormat("dd/MM/yyyy");
            t = new SimpleDateFormat("hh:mm a ");
            final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker.setHour(calSet.get(HOUR_OF_DAY));
                timePicker.setMinute(calSet.get(MINUTE));
            }
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            Date.setText(d.format(calSet.getTimeInMillis()));
            Time.setText(t.format(calSet.getTimeInMillis()));
            From.setText(user.getEmail());

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
            datePicker.setMinDate(calSet.getTimeInMillis());

            timePicker.setOnTimeChangedListener((timePicker, hourOfDay, minute) -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (hourOfDay != 0) {
                        hour = hourOfDay;
                    }
                    if (minute != 0) {
                        min = minute;
                    }
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
                CalendarVisible = false;
                main_layout.setVisibility(View.VISIBLE);
                date_lay.setVisibility(View.INVISIBLE);
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
                time_lay.setVisibility(View.INVISIBLE);
                timePickerVisible = false;
                main_layout.setVisibility(View.VISIBLE);
            });

            Show_Clock.setOnClickListener(view -> {
                hideSoftKeyboard();
                if (timePickerVisible) {
                    timePickerVisible = false;
                    time_lay.setVisibility(View.INVISIBLE);
                    main_layout.setVisibility(View.VISIBLE);
                } else {
                    timePickerVisible = true;
                    time_lay.setVisibility(View.VISIBLE);
                    main_layout.setVisibility(View.INVISIBLE);
                }
            });

            Show_Cal.setOnClickListener(view -> {
                hideSoftKeyboard();
                if (CalendarVisible) {
                    CalendarVisible = false;
                    main_layout.setVisibility(View.VISIBLE);
                    date_lay.setVisibility(View.INVISIBLE);
                } else {
                    CalendarVisible = true;
                    main_layout.setVisibility(View.INVISIBLE);
                    date_lay.setVisibility(View.VISIBLE);
                }
            });

            cancel_time.setOnClickListener(view -> {
                timePickerVisible = false;
                time_lay.setVisibility(View.INVISIBLE);
                main_layout.setVisibility(View.VISIBLE);
            });

            cancel_date.setOnClickListener(view -> {
                CalendarVisible = false;
                date_lay.setVisibility(View.INVISIBLE);
                main_layout.setVisibility(View.VISIBLE);
            });

            EmailBtnSend.setOnClickListener(v -> {
                hideSoftKeyboard();
                if (validateTask(v)) {
                    id = System.currentTimeMillis();
                    AlarmEngaged = true;
                    Intent i = new Intent(getApplicationContext(), EmailReceiver.class);
                    i.putExtra("id", id);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            getApplicationContext(), (int) id, i,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(),
                                pendingIntent);
                    }
                    Email email = new Email(id, user.getEmail(), To.getText().toString(),
                            Subject.getText().toString(), Message.getText().toString(), calSet,
                            AlarmEngaged);
                    UtilsArray_Email.AddToMail(email, ScheduleEmail.this);
                    Email_rec_Adapter adapter = new Email_rec_Adapter(UtilsArray_Email.getMail());
                    adapter.notifyDataSetChanged();
                    UtilsArray_All.ReloadCategoryItems();
                    startActivity(new Intent(ScheduleEmail.this, HomePage.class).putExtra("num",
                            3).putExtra("nav", R.id.nav_my_email));
                    Fragment_4.emailNullViewFinish();
                    finish();
                }
            });

            back_btn.setOnClickListener(view -> {
                if (!CalendarVisible && !timePickerVisible) {
                    showExitDialog(ScheduleEmail.this);
                }
            });

            Discard.setOnClickListener(view -> showExitDialog(ScheduleEmail.this));
        }
    }

    @Override
    public void onBackPressed() {
        if (timePickerVisible || CalendarVisible) {
            time_lay.setVisibility(View.INVISIBLE);
            date_lay.setVisibility(View.INVISIBLE);
            main_layout.setVisibility(View.VISIBLE);
            timePickerVisible = false;
            CalendarVisible = false;
        } else {
            showExitDialog(ScheduleEmail.this);
        }
    }

    public void showExitDialog(Context context) {
        hideSoftKeyboard();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Exit Schedule Email")
                .setMessage("This email will be discarded")
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        startActivity(new Intent(context, HomePage.class).putExtra("num",
                                3).putExtra("nav", R.id.nav_my_email));
                        finish();
                    }
                })
                .setNeutralButton("Continue with Email", null)
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                getResources().getColor(R.color.colorPrimaryDark));
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(
                getResources().getColor(R.color.colorPrimaryDark));
    }

    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(
                ScheduleEmail.this.getWindow().getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void makeSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public boolean check_Date_Time(View v) {
        cal = Calendar.getInstance();
        if (calSet.compareTo(cal) <= 0) {
            makeSnackBar(v, "Date and Time you selected has already passed away");
            calSet.set(DAY_OF_MONTH, cal.get(DAY_OF_MONTH));
            calSet.set(MONTH, cal.get(MONTH));
            calSet.set(YEAR, cal.get(YEAR));
            datePicker.updateDate(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH));
            timePicker.setCurrentHour(cal.get(HOUR_OF_DAY));
            timePicker.setCurrentMinute(cal.get(MINUTE));
            return false;
        } else {
            return true;
        }
    }

    public Boolean validateTask(View v) {
        if (To.getText().toString().isEmpty()) {
            makeSnackBar(v, "Please enter Receiver's email id");
            return false;
        }
        if (Subject.getText().toString().isEmpty() && Message.getText().toString().isEmpty()) {
            makeSnackBar(v, "Empty mail cannot be scheduled");
            return false;
        }
        if (!check_Date_Time(v)) {
            makeSnackBar(v, "Date and Time you selected has already passed away");
            return false;
        }
        return true;
    }
}