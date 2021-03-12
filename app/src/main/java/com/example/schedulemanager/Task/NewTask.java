package com.example.schedulemanager.Task;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedulemanager.HomeFrag.UtilsArray_All;
import com.example.schedulemanager.MainFragments.Fragment_2;
import com.example.schedulemanager.MainFragments.HomePage;
import com.example.schedulemanager.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

public class NewTask extends AppCompatActivity{

    private static final String TASK_ARRAY_DB = "Task_Array_DB";
    EditText title;
    TextView Date, Time;
    DatePicker datePicker;
    ImageButton back_btn;
    Button save, cancel, Show_Cal, Show_Clock, cancel_date, cancel_time, save_date, save_time;
    Calendar cal, calSet;
    SpinnerAdapter spinnerAdapter;
    Spinner taskCategorySpinner;
    Integer LottieSelected;
    SimpleDateFormat d, t;
    Boolean timePickerVisible = false;
    Boolean CalendarVisible = false;
    TimePicker timePicker;
    int year, month, day, hour, min;
    ToggleButton sun, mon, tue, wed, thu, fri, sat;
    CheckBox repeat;
    Boolean isRepeating = false;
    RelativeLayout main_layout, dateLinLayout, timeLinLayout, dateIndicatorLinLayout;
    LinearLayout weekdaysLinLayout;
    ArrayList<Boolean> weekdays = new ArrayList<>(7);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        title = findViewById(R.id.new_task_input_title);
//        desc = findViewById(R.id.new_task_input_desc);
        datePicker = findViewById(R.id.new_task_datepicker);
        save = findViewById(R.id.new_task_save_btn);
        cancel = findViewById(R.id.new_task_cancel_btn);
        cal = Calendar.getInstance();
        calSet = Calendar.getInstance();
        taskCategorySpinner = findViewById(R.id.new_task_category_spinner);
        cancel_date = findViewById(R.id.new_task_datePicker_cancel_btn);
        cancel_time = findViewById(R.id.new_task_timepicker_cancel_btn);
        main_layout = findViewById(R.id.new_task_main_rel_layout);
        timePicker = findViewById(R.id.new_task_timePicker);
        Date = findViewById(R.id.new_task_date_indicator);
        Time = findViewById(R.id.new_task_time_indicator);
        Show_Cal = findViewById(R.id.new_task_calendar_show_btn);
        Show_Clock = findViewById(R.id.new_task_timePicker_show_btn);
        sun = findViewById(R.id.new_task_layout_sun);
        mon = findViewById(R.id.new_task_layout_mon);
        tue = findViewById(R.id.new_task_layout_tue);
        wed = findViewById(R.id.new_task_layout_wed);
        thu = findViewById(R.id.new_task_layout_thu);
        fri = findViewById(R.id.new_task_layout_fri);
        sat = findViewById(R.id.new_task_layout_sat);
        dateLinLayout = findViewById(R.id.new_task_date_lin_layout);
        timeLinLayout = findViewById(R.id.new_task_time_lin_layout);
        save_date = findViewById(R.id.new_task_datePicker_save_btn);
        save_time = findViewById(R.id.new_task_timepicker_save_btn);
        repeat = findViewById(R.id.new_task_repeat_checkBox);
        weekdaysLinLayout = findViewById(R.id.new_task_weekDays_lin_layout);
        back_btn = findViewById(R.id.new_task_back_btn);
        dateIndicatorLinLayout = findViewById(R.id.new_task_calendar_rel_layout);

        cancel_date.bringToFront();
        cancel_time.bringToFront();

        d = new SimpleDateFormat("dd/MM/yyyy");
        t = new SimpleDateFormat("hh:mm a");

        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        sun.setChecked(false);
        mon.setChecked(false);
        tue.setChecked(false);
        wed.setChecked(false);
        thu.setChecked(false);
        fri.setChecked(false);
        sat.setChecked(false);

        for (int i = 0; i < 7; i++) {
            weekdays.add(false);
        }

        Date.setText(d.format(calSet.getTimeInMillis()));
        datePicker.setMinDate(cal.getTimeInMillis() - 60000);
        Time.setText(t.format(calSet.getTimeInMillis()));

        spinnerAdapter = new SpinnerAdapter(NewTask.this, UtilsArray_Task.category);
        taskCategorySpinner.setAdapter(spinnerAdapter);
        taskCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(NewTask.this, "Clicked " + i, Toast.LENGTH_SHORT).show();
                if (i < 13) LottieSelected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                LottieSelected = 2;
            }
        });

        datePicker.init(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
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
//                calSet.set(DAY_OF_MONTH, day);
//                calSet.set(MONTH, month);
//                calSet.set(YEAR, year);
//                if (calSet.compareTo(cal) <= 0) {
//                    //Today Set time passed, count to tomorrow
//                    calSet.add(Calendar.DATE, 1);
//                }
//                Date.setText(d.format(calSet.getTimeInMillis()));
//                Time.setText(t.format(calSet.getTimeInMillis()));
//
//                datePicker.setVisibility(View.INVISIBLE);
//                cancel_date.setVisibility(View.INVISIBLE);
//                dateLinLayout.setVisibility(View.INVISIBLE);
//                CalendarVisible = false;
//                main_layout.setAlpha(1.0f);


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

//                    calSet.set(HOUR_OF_DAY, hour);
//                    calSet.set(MINUTE, min);
//                    calSet.set(SECOND, 0);
//                    calSet.set(Calendar.MILLISECOND, 0);
//
//
//                    if (calSet.compareTo(cal) <= 0) {
//                        //Today Set time passed, count to tomorrow
//                        calSet.add(DATE, 1);
//                    }
//                    Time.setText(t.format(calSet.getTimeInMillis()));
//                    Date.setText(d.format(calSet.getTimeInMillis()));
//
//                    timePicker.setVisibility(View.INVISIBLE);
//                    cancel_time.setVisibility(View.INVISIBLE);
//                    timeLinLayout.setVisibility(View.INVISIBLE);
//                    timePickerVisible = false;
//                    main_layout.setAlpha(1.0f);
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

                timeLinLayout.setVisibility(View.GONE);
                main_layout.setVisibility(View.VISIBLE);
                timePickerVisible = false;
            }
        });

        Show_Clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                if (timePickerVisible) {
//                    timePicker.setVisibility(View.INVISIBLE);
//                    cancel_time.setVisibility(View.INVISIBLE);
//                    save_time.setVisibility(View.INVISIBLE);
                    timeLinLayout.setVisibility(View.INVISIBLE);
                    main_layout.setVisibility(View.VISIBLE);
                    timePickerVisible = false;
//                    timePicker.setAlpha(0.0f);
                } else {
//                    timePicker.setVisibility(View.VISIBLE);
//                    cancel_time.setVisibility(View.VISIBLE);
//                    save_time.setVisibility(View.VISIBLE);
                    timeLinLayout.setVisibility(View.VISIBLE);
                    main_layout.setVisibility(View.INVISIBLE);
                    timePickerVisible = true;
//                    timePicker.setAlpha(1.0f);
                }
            }
        });

        Show_Cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                if (CalendarVisible) {
//                    datePicker.setVisibility(View.INVISIBLE);
//                    cancel_date.setVisibility(View.INVISIBLE);
//                    save_date.setVisibility(View.INVISIBLE);
                    dateLinLayout.setVisibility(View.INVISIBLE);
                    main_layout.setVisibility(View.VISIBLE);
                    CalendarVisible = false;
                } else {
//                    datePicker.setVisibility(View.VISIBLE);
//                    cancel_date.setVisibility(View.VISIBLE);
//                    save_date.setVisibility(View.VISIBLE);
                    dateLinLayout.setVisibility(View.VISIBLE);
                    main_layout.setVisibility(View.INVISIBLE);
                    CalendarVisible = true;
                }
            }
        });

        cancel_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timePickerVisible) {
                    timeLinLayout.setVisibility(View.INVISIBLE);
                    main_layout.setVisibility(View.VISIBLE);
                    timePickerVisible = false;
                }
            }
        });

        cancel_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CalendarVisible) {
                    dateLinLayout.setVisibility(View.INVISIBLE);
                    main_layout.setVisibility(View.VISIBLE);
                    CalendarVisible = false;
                }
            }
        });

        repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    weekdaysLinLayout.setVisibility(View.VISIBLE);
                    dateIndicatorLinLayout.setVisibility(View.GONE);
                }
                else{
                    sun.setChecked(false);
                    mon.setChecked(false);
                    tue.setChecked(false);
                    wed.setChecked(false);
                    thu.setChecked(false);
                    fri.setChecked(false);
                    sat.setChecked(false);
                    weekdaysLinLayout.setVisibility(View.GONE);
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
                    long id = System.currentTimeMillis();
                    //                All_In_One_Main_Adapter adapter1 = new All_In_One_Main_Adapter(NewTask.this);
//                adapter1.notifyDataSetChanged();
//                int deleteData = myDb.deleteData(String.valueOf(1));
//                boolean isUpdate = myDb.updateData(String.valueOf(1), GsonConverter.TaskToJson(task));


//                long nextAlarmRing = 0;

//                boolean weekDayRepeat = false;
//                for (int j=0; j<7; j++) {
//                    Log.e("Weekday"+j, String.valueOf(weekdays.get(j)));
//                    if (weekdays.get(j)) weekDayRepeat = true;
//                    if (weekdays.contains(true)) Log.e("WEEKDAYS CONTAINS"," TRUE");
//                }
//                isRepeating = weekDayRepeat;
                    isRepeating = weekdays.contains(true);

                    Task t = new Task(id, title.getText().toString(), calSet, LottieSelected, isRepeating, false, weekdays);

                    Intent intent = new Intent(getApplicationContext(), TaskReceiver.class);
                    intent.putExtra("id", id);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    if (isRepeating) {
                        calSet.setTimeInMillis(Task.getTimeToWeeklyRings(t).get(0));
                    }

//                if (isRepeating) {
//                    // get list of time to ring in milliseconds for each active day, and repeat weekly
//                    List<Long> timeToWeeklyRings = Task.getTimeToWeeklyRings(t);
////                    Calendar calendar = Calendar.getInstance();
////                    for (long millis : timeToWeeklyRings) {
////                        calendar.setTimeInMillis(millis);
////                Log.i(TAG, "Setting weekly repeat at " + calendar.getTime().toString());
//                    nextAlarmRing = timeToWeeklyRings.get(0);
//                    //                        if (millis < nextAlarmRing || nextAlarmRing == 0) nextAlarmRing = millis;
////                    }
//
//                } else {
//                    nextAlarmRing = Task.getTimeToNextRing(calSet);
//
////                    Log.i(TAG, "setting alarm " + alarm.id + " to AlarmManager");
//                }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
                    }

                    UtilsArray_Task.AddToTask(t, NewTask.this);
                    Task_rec_adapter adapter = new Task_rec_adapter(UtilsArray_Task.getTask());
                    adapter.notifyDataSetChanged();
                    UtilsArray_All.ReloadCategoryItems();

//                UtilsArray_Task.AddToTaskIntent(NewTask.this,new CustomPenInt(task.size()-1,pendingIntent));

                    String timeUntilNextRing = Task.getStringOfTimeUntilNextRing(calSet.getTimeInMillis() - System.currentTimeMillis());

                    Snackbar.make(HomePage.fab_main_home, "Task set for " + timeUntilNextRing, Snackbar.LENGTH_SHORT).setAnchorView(HomePage.fab_main_home).show();
//                Toast.makeText(NewTask.this, "Task set for " + timeUntilNextRing, Toast.LENGTH_SHORT).show();

                    Fragment_2.taskNullViewFinish();
                    finish();
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!timePickerVisible&&!CalendarVisible)
                showExitDialog(NewTask.this);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitDialog(NewTask.this);
            }
        });
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
             showExitDialog(NewTask.this);
         }
    }

    public void showExitDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Exit New Task")
                .setMessage("This task will be discarded")
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                })
                .setNeutralButton("Continue with Task", null)
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    public void hideSoftKeyboard(){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(NewTask.this.getWindow().getCurrentFocus().getWindowToken(), 0);
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