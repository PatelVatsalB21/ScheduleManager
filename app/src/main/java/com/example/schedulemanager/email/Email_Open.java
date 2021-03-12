package com.example.schedulemanager.email;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.schedulemanager.HomeFrag.UtilsArray_All;
import com.example.schedulemanager.MainFragments.Fragment_4;
import com.example.schedulemanager.MainFragments.HomePage;
import com.example.schedulemanager.R;
import com.example.schedulemanager.Task.TaskOpen;
import com.example.schedulemanager.Trash.UtilsArray_Trash;
import com.example.schedulemanager.note.PDF_Creator;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

public class Email_Open extends AppCompatActivity {
    Integer position, id;
    ImageButton back_btn, menu_btn;
    Button EmailBtnSend, Show_Cal, Show_Clock, discard_btn,save_time,save_date;
    EditText To, Message, Subject;
    TextView From, Date, Time;
    Calendar cal, calSet;
    int year, month, day, hour, min;
    Boolean timePickerVisible = false;
    Boolean CalendarVisible = false, AlarmEngaged = false;
    RelativeLayout main_layout;
    Button cancel_date, cancel_time;
    SimpleDateFormat d, t;
    TimePicker timePicker;
    DatePicker datePicker;
    RelativeLayout timeLinLayout, dateLinLayout;
    AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_open);

        Intent intent = getIntent();
        if (intent != null) {
            int i = intent.getIntExtra("position", -1);
            if (i > -1) {
                position = i;
                Log.d("EMAILOPEN", String.valueOf(i));
                Message = findViewById(R.id.Open_email_task_message);
                Subject = findViewById(R.id.Open_email_task_subject);
                To = findViewById(R.id.Open_email_task_to);
                EmailBtnSend = findViewById(R.id.Open_email_task_send_btn);
                From = findViewById(R.id.Open_email_task_from);
                Date = findViewById(R.id.Open_email_task_date_indicator);
                Time = findViewById(R.id.Open_email_task_time_indicator);
                Show_Cal = findViewById(R.id.Open_email_task_calendar_btn);
                Show_Clock = findViewById(R.id.Open_email_task_timePicker_btn);
                timePicker = findViewById(R.id.Open_email_task_timepicker);
                datePicker = findViewById(R.id.Open_email_task_datepicker);
                main_layout = findViewById(R.id.Open_email_main_rel_layout);
                cancel_time = findViewById(R.id.Open_email_task_timepicker_cancel_btn);
                cancel_date = findViewById(R.id.Open_email_task_datepicker_cancel_btn);
                discard_btn = findViewById(R.id.Open_email_task_discard_btn);
                timeLinLayout = findViewById(R.id.Open_email_main_time_lin_layout);
                dateLinLayout = findViewById(R.id.Open_email_main_date_lin_layout);
                save_date = findViewById(R.id.Open_email_task_datepicker_save_btn);
                save_time = findViewById(R.id.Open_email_task_timepicker_save_btn);
                back_btn = findViewById(R.id.Open_email_back_btn);
                menu_btn = findViewById(R.id.Open_email_menu_btn);


                calSet = UtilsArray_Email.mail.get(position).cal;
                cal = Calendar.getInstance();
                year = calSet.get(YEAR);
                month = calSet.get(MONTH);
                day = calSet.get(DAY_OF_MONTH);
                hour = calSet.get(HOUR);
                min = calSet.get(MINUTE);
                d = new SimpleDateFormat("dd/MM/yyyy");
                t = new SimpleDateFormat("hh:mm a");

                From.setText(UtilsArray_Email.mail.get(position).From);
                To.setText(UtilsArray_Email.mail.get(position).To);
                Subject.setText(UtilsArray_Email.mail.get(position).Subject);
                Message.setText(UtilsArray_Email.mail.get(position).Message);


                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    timePicker.setCurrentHour(calSet.get(HOUR_OF_DAY));
                    timePicker.setCurrentMinute(calSet.get(MINUTE));
                }

                Date.setText(d.format(calSet.getTimeInMillis()));
                Time.setText(t.format(calSet.getTimeInMillis()));

                datePicker.init(calSet.get(YEAR), calSet.get(MONTH), calSet.get(DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                        if (i != 0) {
                            year = i;
                        }
                        if (i1 != 0) {
                            month = i1;
                        }
                        if (i2 != 0) {
                            day = i2;
                        }
//                        calSet.set(DAY_OF_MONTH, day);
//                        calSet.set(MONTH, month);
//                        calSet.set(YEAR, year);
//                        if (calSet.compareTo(cal) <= 0) {
//                            //Today Set time passed, count to tomorrow
//                            calSet.add(Calendar.DATE, 1);
//                        }
//                        Date.setText(d.format(calSet.getTimeInMillis()));
//                        Time.setText(t.format(calSet.getTimeInMillis()));
//
//                        datePicker.setVisibility(View.INVISIBLE);
//                        cancel_date.setVisibility(View.INVISIBLE);
//                        dateLinLayout.setVisibility(View.INVISIBLE);
//                        CalendarVisible = false;
//                        main_layout.setAlpha(1.0f);
                    }
                });

                datePicker.setMinDate(calSet.getTimeInMillis() -60000);

                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

                    @Override
                    public void onTimeChanged(final TimePicker timePicker, int hourOfDay, int minute) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (hourOfDay != 0) {
                                hour = hourOfDay;
                            }
                            if (minute != 0) {
                                min = minute;
                            }

//
//                            calSet.set(HOUR_OF_DAY, hour);
//                            calSet.set(MINUTE, min);
//                            calSet.set(SECOND, 0);
//                            calSet.set(Calendar.MILLISECOND, 0);
//
//
//                            if (calSet.compareTo(cal) <= 0) {
//                                //Today Set time passed, count to tomorrow
//                                calSet.add(DATE, 1);
//                            }
//                            Time.setText(t.format(calSet.getTimeInMillis()));
//                            Date.setText(d.format(calSet.getTimeInMillis()));
//
//
//                            timePicker.setVisibility(View.INVISIBLE);
//                            cancel_time.setVisibility(View.INVISIBLE);
//                            timePickerVisible = false;
//                            main_layout.setAlpha(1.0f);
//                            timeLinLayout.setVisibility(View.INVISIBLE);

                        }
                    }
                });

                save_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                    }
                });

                save_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cal = Calendar.getInstance();
                        calSet.set(HOUR_OF_DAY, hour);
                        calSet.set(MINUTE, min);
                        calSet.set(SECOND, 0);
                        calSet.set(Calendar.MILLISECOND, 0);

                        check_Date_Time(v);

                        Time.setText(t.format(calSet.getTimeInMillis()));
                        Date.setText(d.format(calSet.getTimeInMillis()));

                        timePickerVisible = false;
                        main_layout.setVisibility(View.VISIBLE);
                        timeLinLayout.setVisibility(View.INVISIBLE);

                    }
                });

                Show_Clock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideSoftKeyboard();
                        if (timePickerVisible) {
//                            timePicker.setVisibility(View.INVISIBLE);
//                            cancel_time.setVisibility(View.INVISIBLE);
                            timePickerVisible = false;
                            main_layout.setVisibility(View.VISIBLE);
                            timeLinLayout.setVisibility(View.INVISIBLE);
//                    timePicker.setAlpha(0.0f);
                        } else {
                                timePickerVisible = true;
//                                timePicker.setVisibility(View.VISIBLE);
//                                cancel_time.setVisibility(View.VISIBLE);
                                main_layout.setVisibility(View.INVISIBLE);
                                timeLinLayout.setVisibility(View.VISIBLE);
//                    timePicker.setAlpha(1.0f);

                        }
                    }
                });


                Show_Cal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideSoftKeyboard();
                        if (CalendarVisible) {
//                            datePicker.setVisibility(View.INVISIBLE);
//                            cancel_date.setVisibility(View.INVISIBLE);
                            dateLinLayout.setVisibility(View.INVISIBLE);
                            main_layout.setVisibility(View.VISIBLE);
                            CalendarVisible = false;
                        } else {
                                CalendarVisible = true;
                                main_layout.setVisibility(View.INVISIBLE);
//                                datePicker.setVisibility(View.VISIBLE);
//                                cancel_date.setVisibility(View.VISIBLE);
                                dateLinLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });

                cancel_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeLinLayout.setVisibility(View.INVISIBLE);
                        main_layout.setVisibility(View.VISIBLE);
                        timePickerVisible = false;

                    }
                });

                cancel_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CalendarVisible = false;
                        dateLinLayout.setVisibility(View.INVISIBLE);
                        main_layout.setVisibility(View.VISIBLE);
                    }
                });


                EmailBtnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideSoftKeyboard();
                        if (validateTask(v)) {
                            id = (int) UtilsArray_Email.mail.get(position).id;
                            AlarmEngaged = true;

                            UtilsArray_Email.CancelEmailAlarm(Email_Open.this, position);

                            Intent i = new Intent(getApplicationContext(), EmailReceiver.class);
                            i.putExtra("id", UtilsArray_Email.mail.get(position).id);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, i, PendingIntent.FLAG_UPDATE_CURRENT);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
                            }
                            Email email = new Email(id, From.getText().toString(), To.getText().toString(), Subject.getText().toString(), Message.getText().toString(), calSet, AlarmEngaged);
                            UtilsArray_Email.setMail(email, position, Email_Open.this);

//                        alarmManager.cancel(UtilsArray_Email.getEmailPendingIntentFromPosition(position));
//                        UtilsArray_Email.RemoveFromEmailIntent(Email_Open.this,UtilsArray_Email.getCustomPenIntFromPosition(position));
//                        UtilsArray_Email.AddToEmailIntent(Email_Open.this, new CustomPenInt(position, pendingIntent));
                            Email_rec_Adapter adapter = new Email_rec_Adapter(UtilsArray_Email.getMail());
                            adapter.notifyDataSetChanged();
                            UtilsArray_All.ReloadCategoryItems();

                            startActivity(new Intent(Email_Open.this, HomePage.class).putExtra("num", 3).putExtra("nav", R.id.nav_my_email));
                            finish();
                        }
                    }
                });

                menu_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideSoftKeyboard();
                        PopupMenu menu = new PopupMenu(view.getContext(), view);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            menu.setGravity(Gravity.END);
                        }
                        menu.getMenu().add("Save as PDF").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                String finalShare = "";
                                if (!UtilsArray_Email.getMail().get(position).To.isEmpty()) finalShare = "To: " + UtilsArray_Email.getMail().get(position).To;
                                if (!UtilsArray_Email.getMail().get(position).From.isEmpty()) finalShare = finalShare + "\n"  + "\n"+ "From: " + UtilsArray_Email.getMail().get(position).From;
                                if (!UtilsArray_Email.getMail().get(position).Subject.isEmpty()) finalShare = finalShare + "\n" + "\n" + "Subject:" + "\n" + UtilsArray_Email.getMail().get(position).Subject;
                                if (!UtilsArray_Email.getMail().get(position).Message.isEmpty()) finalShare = finalShare + "\n"  + "\n" + "Message:" + "\n" + UtilsArray_Email.getMail().get(position).Message;

                                String Filename = "";
                                if (UtilsArray_Email.getMail().get(position).To.length()>5){
                                    Filename.concat(UtilsArray_Email.getMail().get(position).To.substring(0,4));
                                }else {
                                    Filename.concat(UtilsArray_Email.getMail().get(position).To);
                                }
                                if (String.valueOf(UtilsArray_Email.getMail().get(position).id).length()>5){
                                    Filename.concat(String.valueOf(UtilsArray_Email.getMail().get(position).id).substring(0,4));
                                }else {
                                    Filename.concat(String.valueOf(UtilsArray_Email.getMail().get(position).id));
                                }

                                if (!finalShare.isEmpty()) {
                                    PDF_Creator pdf_creator = new PDF_Creator(Email_Open.this, finalShare, Filename);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                        pdf_creator.createMyPDF();
                                    }
                                }
                                return true;
                            }
                        });

                        if (UtilsArray_Email.getMail().get(position).Scheduled) {
                            menu.getMenu().add("Cancel Schedule").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    Email_rec_Adapter adapter = new Email_rec_Adapter(UtilsArray_Email.getMail());
                                    UtilsArray_Email.CancelEmailAlarm(Email_Open.this, position);
                                    UtilsArray_Email.getMail().get(position).Scheduled = false;
                                    adapter.notifyDataSetChanged();
                                    return true;
                                }
                            });
                        }
                        menu.show();
                    }
                });

                back_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!CalendarVisible&&!timePickerVisible)
                        showExitDialog(Email_Open.this);
                    }
                });

                discard_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showExitDialog(Email_Open.this);
                    }
                });
            }
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
             showExitDialog(Email_Open.this);
         }
    }

    public void showExitDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Exit")
                .setMessage("Any unsaved changes will be discarded")
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        startActivity(new Intent(context, HomePage.class).putExtra("num", 3).putExtra("nav", R.id.nav_my_email));
                        finish();
                    }
                })
                .setNeutralButton("Continue Editing", null)
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    public void hideSoftKeyboard(){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(Email_Open.this.getWindow().getCurrentFocus().getWindowToken(), 0);
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
            calSet.set(DAY_OF_MONTH, cal.get(DAY_OF_MONTH));
            calSet.set(MONTH, cal.get(MONTH));
            calSet.set(YEAR, cal.get(YEAR));
            datePicker.updateDate(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH));
            timePicker.setCurrentHour(cal.get(HOUR_OF_DAY));
            timePicker.setCurrentMinute(cal.get(MINUTE));
            return false;
        }else return true;
    }

    public Boolean validateTask(View v){
        if (To.getText().toString().isEmpty()){
            makeSnackBar(v,"Please enter Receiver's email id");
            return false;
        }
        if (Subject.getText().toString().isEmpty() && Message.getText().toString().isEmpty()){
            makeSnackBar(v,"Empty mail cannot be saved");
            return false;
        }
        if (!check_Date_Time(v)){
            makeSnackBar(v, "Date and Time you selected has already passed away");
            return false;
        }
        return true;
    }
}