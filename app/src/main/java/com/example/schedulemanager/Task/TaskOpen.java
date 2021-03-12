package com.example.schedulemanager.Task;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.schedulemanager.HomeFrag.UtilsArray_All;
import com.example.schedulemanager.MainActivity;
import com.example.schedulemanager.MainFragments.Fragment_2;
import com.example.schedulemanager.MainFragments.HomePage;
import com.example.schedulemanager.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import static com.example.firebase.Task.UtilsArray_Task.myDb;
import static com.example.schedulemanager.Task.UtilsArray_Task.TasksharedPreferences;
import static com.example.schedulemanager.Task.UtilsArray_Task.task;
import static com.example.schedulemanager.note.GsonConverter.TaskToJson;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

public class TaskOpen extends AppCompatActivity {

    Integer position;
    private static final String TASK_ARRAY_DB = "Task_Array_DB";
    EditText title;
    TextView Date, Time;
    DatePicker datePicker;
    ImageButton back_btn;
    Button save, cancel, Show_Cal, Show_Clock, cancel_date, cancel_time, save_date, save_time;
    Calendar cal, calSet;
    SimpleDateFormat d, t;
    Boolean timePickerVisible = false;
    Boolean CalendarVisible = false;
    Boolean isRepeating = false;
//    long nextAlarmRing = 0;
    SpinnerAdapter spinnerAdapter;
    Spinner taskCategorySpinner;
    Integer LottieSelected;
    RelativeLayout main_layout;
    TimePicker timePicker;
    int year, month, day, hour, min;
    ToggleButton sun, mon, tue, wed, thu, fri, sat;
    RelativeLayout dateLinLayout, timeLinLayout, dateIndicatorLinLayout;
    LinearLayout weekDaysLinLayout;
    CheckBox repeat;
    ArrayList<Boolean> weekdays = new ArrayList<>(7);

    AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_open);

        UtilsArray_Task.initTask(TaskOpen.this);
        final Intent intent = getIntent();
        if (intent != null) {
            Integer i = intent.getIntExtra("position", -1);
            if (i > -1) {
                position = i;

                title = findViewById(R.id.open_task_input_title);
//                desc = findViewById(R.id.open_task_input_desc);
                datePicker = findViewById(R.id.open_task_datepicker);
                save = findViewById(R.id.open_task_save_btn);
                cancel = findViewById(R.id.open_task_cancel_btn);
                cal = Calendar.getInstance();
                calSet = task.get(position).calendar;
                cancel_date = findViewById(R.id.task_open_datePicker_cancel_btn);
                cancel_time = findViewById(R.id.task_open_timepicker_cancel_btn);
                main_layout = findViewById(R.id.task_open_main_rel_layout);
                timePicker = findViewById(R.id.task_open_timePicker);
                Date = findViewById(R.id.open_task_date_indicator);
                Time = findViewById(R.id.open_task_time_indicator);
                Show_Cal = findViewById(R.id.open_task_calendar_show_btn);
                Show_Clock = findViewById(R.id.open_task_timePicker_show_btn);
                taskCategorySpinner = findViewById(R.id.open_task_category_spinner);
                sun = findViewById(R.id.open_task_layout_sun);
                mon = findViewById(R.id.open_task_layout_mon);
                tue = findViewById(R.id.open_task_layout_tue);
                wed = findViewById(R.id.open_task_layout_wed);
                thu = findViewById(R.id.open_task_layout_thu);
                fri = findViewById(R.id.open_task_layout_fri);
                sat = findViewById(R.id.open_task_layout_sat);
                timeLinLayout = findViewById(R.id.task_open_time_lin_layout);
                dateLinLayout = findViewById(R.id.task_open_date_lin_layout);
                save_date = findViewById(R.id.task_open_datePicker_save_btn);
                save_time = findViewById(R.id.task_open_timepicker_save_btn);
                weekDaysLinLayout = findViewById(R.id.open_task_weekDays_lin_layout);
                repeat = findViewById(R.id.open_task_repeat_checkBox);
                back_btn = findViewById(R.id.task_open_back_btn);
                dateIndicatorLinLayout = findViewById(R.id.open_task_calendar_rel_layout);

                isRepeating = task.get(position).isRepeating;
                weekdays = task.get(position).weekDays;
//                Sun = task.get(position).SUN;
//                Mon = task.get(position).MON;
//                Tue = task.get(position).TUE;
//                Wed = task.get(position).WED;
//                Thu = task.get(position).THU;
//                Fri = task.get(position).FRI;
//                Sat = task.get(position).SAT;

                if (isRepeating) {
                    weekDaysLinLayout.setVisibility(View.VISIBLE);
                    dateIndicatorLinLayout.setVisibility(View.GONE);
                    repeat.setChecked(true);
                } else {
                    weekDaysLinLayout.setVisibility(View.GONE);
                    dateIndicatorLinLayout.setVisibility(View.VISIBLE);
                    repeat.setChecked(false);
                }

                sun.setChecked(weekdays.get(0));
                mon.setChecked(weekdays.get(1));
                tue.setChecked(weekdays.get(2));
                wed.setChecked(weekdays.get(3));
                thu.setChecked(weekdays.get(4));
                fri.setChecked(weekdays.get(5));
                sat.setChecked(weekdays.get(6));

                cancel_date.bringToFront();
                cancel_time.bringToFront();

                d = new SimpleDateFormat("dd/MM/yyyy");
                t = new SimpleDateFormat("hh:mm a");

                Date.setText(d.format(calSet.getTimeInMillis()));
                datePicker.setMinDate(cal.getTimeInMillis() - 60000);
                Time.setText(t.format(calSet.getTimeInMillis()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    timePicker.setHour(calSet.get(HOUR_OF_DAY));
                    timePicker.setMinute(calSet.get(MINUTE));
                }

                title.setText(task.get(position).Title);


                spinnerAdapter = new SpinnerAdapter(TaskOpen.this, UtilsArray_Task.category);
                taskCategorySpinner.setAdapter(spinnerAdapter);
                taskCategorySpinner.setSelection(task.get(position).LottieFileRes);
                taskCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        Toast.makeText(TaskOpen.this, "Clicked " + i, Toast.LENGTH_SHORT).show();
                        if (i < 13) LottieSelected = i;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        LottieSelected = task.get(position).LottieFileRes;
                    }
                });

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
//                            calSet.add(DATE, 1);
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


                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

                    @Override
                    public void onTimeChanged(final TimePicker timePicker, int hourOfDay, int minute) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (hourOfDay != 0) {
                                hour = hourOfDay;
                            }
                            min = minute;

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
//                            timeLinLayout.setVisibility(View.INVISIBLE);
//                            timePickerVisible = false;
//                            main_layout.setAlpha(1.0f);

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


                        timeLinLayout.setVisibility(View.INVISIBLE);
                        main_layout.setVisibility(View.VISIBLE);
                        timePickerVisible = false;

                    }
                });

                Show_Clock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideSoftKeyboard();
                        if (timePickerVisible) {
//                            timePicker.setVisibility(View.INVISIBLE);
//                            cancel_time.setVisibility(View.INVISIBLE);
                            timeLinLayout.setVisibility(View.INVISIBLE);
                            main_layout.setVisibility(View.VISIBLE);
                            timePickerVisible = false;
//                    timePicker.setAlpha(0.0f);
                        } else {
                            timePickerVisible = true;
//                            timePicker.setVisibility(View.VISIBLE);
//                            cancel_time.setVisibility(View.VISIBLE);
                            timeLinLayout.setVisibility(View.VISIBLE);
                            main_layout.setVisibility(View.INVISIBLE);
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
                            CalendarVisible = false;
                            main_layout.setVisibility(View.VISIBLE);
                        } else {
                            CalendarVisible = true;
                            main_layout.setVisibility(View.INVISIBLE);
//                            datePicker.setVisibility(View.VISIBLE);
//                            cancel_date.setVisibility(View.VISIBLE);
                            dateLinLayout.setVisibility(View.VISIBLE);

                        }
                    }
                });

                cancel_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        timePicker.setVisibility(View.INVISIBLE);
//                        cancel_time.setVisibility(View.INVISIBLE);
                        timeLinLayout.setVisibility(View.INVISIBLE);
                        main_layout.setVisibility(View.VISIBLE);
                        timePickerVisible = false;
                    }
                });

                cancel_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        datePicker.setVisibility(View.INVISIBLE);
//                        cancel_date.setVisibility(View.INVISIBLE);
                        dateLinLayout.setVisibility(View.INVISIBLE);
                        main_layout.setVisibility(View.VISIBLE);
                        CalendarVisible = false;
                    }
                });

                repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            weekDaysLinLayout.setVisibility(View.VISIBLE);
                            dateIndicatorLinLayout.setVisibility(View.GONE);
                        }
                        else {
                            sun.setChecked(false);
                            mon.setChecked(false);
                            tue.setChecked(false);
                            wed.setChecked(false);
                            thu.setChecked(false);
                            fri.setChecked(false);
                            sat.setChecked(false);
                            weekDaysLinLayout.setVisibility(View.GONE);
                            dateIndicatorLinLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });


                sun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        weekdays.set(0, b);
                    }
                });
                mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        weekdays.set(1, b);
                    }
                });
                tue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        weekdays.set(2, b);
                    }
                });
                wed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        weekdays.set(3, b);
                    }
                });
                thu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        weekdays.set(4, b);
                    }
                });
                fri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        weekdays.set(5, b);
                    }
                });
                sat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        weekdays.set(6, b);
                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideSoftKeyboard();
                        if (validateTask(view)) {
                            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                            long id = System.currentTimeMillis();


//                        All_In_One_Main_Adapter adapter1 = new All_In_One_Main_Adapter(TaskOpen.this);
//                        adapter1.notifyDataSetChanged();

//                        int deleteData = myDb.deleteData(String.valueOf(0));
//                        boolean isUpdate = myDb.updateData(String.valueOf(0), GsonConverter.TaskToJson(task));

                            Intent i = new Intent(getApplicationContext(), TaskReceiver.class);
                            i.putExtra("id", id);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) id, i, PendingIntent.FLAG_UPDATE_CURRENT);

//                        alarmManager.cancel(UtilsArray_Task.getTaskPendingIntentFromPosition(position));

//                        boolean weekDayRepeat = false;
//                        for (int j=0; j<7; j++) {
//                            Log.e("Weekday"+j, String.valueOf(weekdays.get(j)));
//                            if (weekdays.get(j)) weekDayRepeat = true;
//                        }
//                        isRepeating= weekDayRepeat;
                            isRepeating = weekdays.contains(true);

                            Task t = new Task(id, title.getText().toString(), calSet, LottieSelected, isRepeating, false, weekdays);
                            if (isRepeating) {
                                calSet.setTimeInMillis(Task.getTimeToWeeklyRings(t).get(0));
                            }

//                        if (isRepeating) {
//                            // get list of time to ring in milliseconds for each active day, and repeat weekly
//                            List<Long> timeToWeeklyRings = Task.getTimeToWeeklyRings(t);
////                    Calendar calendar = Calendar.getInstance();
////                            for (long millis : timeToWeeklyRings) {
////                        calendar.setTimeInMillis(millis);
////                Log.i(TAG, "Setting weekly repeat at " + calendar.getTime().toString());
//                            nextAlarmRing = timeToWeeklyRings.get(0);
//
////                                if (millis < nextAlarmRing || nextAlarmRing == 0)
////                                    nextAlarmRing = millis;
////                            }
//                        } else {
//                            nextAlarmRing = Task.getTimeToNextRing(calSet);
//
////                    Log.i(TAG, "setting alarm " + alarm.id + " to AlarmManager");
//                        }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                alarmManager.setExact(
                                        AlarmManager.RTC_WAKEUP,
                                        calSet.getTimeInMillis(),
                                        pendingIntent);
                            }

                            UtilsArray_Task.setTask(t, position, TaskOpen.this);
                            UtilsArray_All.ReloadCategoryItems();

                            SharedPreferences.Editor editor = TasksharedPreferences.edit();
                            editor.remove(TASK_ARRAY_DB);
                            editor.putString(TASK_ARRAY_DB, TaskToJson(UtilsArray_Task.getTask()));
                            editor.commit();

                            Task_rec_adapter adapter = new Task_rec_adapter(UtilsArray_Task.getTask());
                            adapter.notifyDataSetChanged();

                            String timeUntilNextRing = Task.getStringOfTimeUntilNextRing(calSet.getTimeInMillis() - System.currentTimeMillis());

                            Snackbar.make(HomePage.fab_main_home, "Task set for " + timeUntilNextRing, Snackbar.LENGTH_SHORT).setAnchorView(HomePage.fab_main_home).show();
//                        Toast.makeText(TaskOpen.this, "Task set for " + timeUntilNextRing, Toast.LENGTH_SHORT).show();

//                        UtilsArray_Task.getTaskPendingIntentFromPosition(position);
//                        UtilsArray_Task.RemoveFromTaskIntent(TaskOpen.this,UtilsArray_Task.getCustomPenIntFromPosition(position));
//                        UtilsArray_Task.AddToTaskIntent(TaskOpen.this,new CustomPenInt(task.size()-1,pendingIntent));

//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(i);

                            finish();
                        }
                    }
                });

                back_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!CalendarVisible&&!timePickerVisible)
                        showExitDialog(TaskOpen.this);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showExitDialog(TaskOpen.this);
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
            showExitDialog(TaskOpen.this);
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
        imm.hideSoftInputFromWindow(TaskOpen.this.getWindow().getCurrentFocus().getWindowToken(), 0);
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

    public Boolean validateTask(View v){
        if (title.getText().toString().isEmpty()){
            makeSnackBar(v,"Task cannot be created without Title");
            return false;
        }
        if (!check_Date_Time(v)){
            makeSnackBar(v, "Date and Time you selected has already passed away");
            return false;
        }
        return true;
    }
}