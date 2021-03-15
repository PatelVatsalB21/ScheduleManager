package com.example.schedulemanager.Task;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.example.schedulemanager.HomeFrag.UtilsArray_All;
import com.example.schedulemanager.R;
import com.example.schedulemanager.email.EmailReceiver;
import com.example.schedulemanager.note.GsonConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class UtilsArray_Task {
    public static SharedPreferences TasksharedPreferences;
    public static ArrayList<TaskSpinnerRowItem> category = new ArrayList<>(14);
    public static ArrayList<Task> task = new ArrayList<>();
    public static ArrayList<Integer> taskReceiverImages = new ArrayList<>(14);
    final private static String TASK_ARRAY_DB = "Task_Array_DB";


    public static void initTask(Context context) {
        TasksharedPreferences = context.getSharedPreferences("Task.db", Context.MODE_PRIVATE);
        task = GsonConverter.jsonToTask(TasksharedPreferences.getString(TASK_ARRAY_DB, null));
        category.clear();
        category.add(new TaskSpinnerRowItem(R.raw.meeting, "Meeting"));
        category.add(new TaskSpinnerRowItem(R.raw.working, "Office Work"));
        category.add(new TaskSpinnerRowItem(R.raw.meal_time, "Meal Time"));
        category.add(new TaskSpinnerRowItem(R.raw.hospital, "Medical"));
        category.add(new TaskSpinnerRowItem(R.raw.meditating_lady1, "Meditation"));
        category.add(new TaskSpinnerRowItem(R.raw.exercises, "Exercise"));
        category.add(new TaskSpinnerRowItem(R.raw.game, "Game"));
        category.add(new TaskSpinnerRowItem(R.raw.studying, "Study Time"));
        category.add(new TaskSpinnerRowItem(R.raw.me_time, "Me Time"));
        category.add(new TaskSpinnerRowItem(R.raw.sport_run, "Sports"));
        category.add(new TaskSpinnerRowItem(R.raw.payments, "Payments"));
        category.add(new TaskSpinnerRowItem(R.raw.celebration, "Celebration"));
        category.add(new TaskSpinnerRowItem(R.raw.shopping_cart, "Shopping"));
        category.add(new TaskSpinnerRowItem(R.raw.transport, "Trip"));

        taskReceiverImages.clear();
        taskReceiverImages.add(R.drawable.meeting);
        taskReceiverImages.add(R.drawable.office_work);
        taskReceiverImages.add(R.drawable.meal_time);
        taskReceiverImages.add(R.drawable.medical);
        taskReceiverImages.add(R.drawable.meditation);
        taskReceiverImages.add(R.drawable.exercise);
        taskReceiverImages.add(R.drawable.game);
        taskReceiverImages.add(R.drawable.study);
        taskReceiverImages.add(R.drawable.me_time);
        taskReceiverImages.add(R.drawable.sports);
        taskReceiverImages.add(R.drawable.payments);
        taskReceiverImages.add(R.drawable.celebration);
        taskReceiverImages.add(R.drawable.shopping);
        taskReceiverImages.add(R.drawable.trip);

        if (task == null || task.size() == 0) {
            task = new ArrayList<>();
        } else {
            SortTaskItems();
        }
    }

    public UtilsArray_Task() {
    }

    public static ArrayList<Task> getTask() {
        return task;
    }

    public static void setTask(Task t, Integer position, Context context) {
        UtilsArray_Task.task.set(position, t);
        SortTaskItems();
        Task_rec_adapter adapter = new Task_rec_adapter(UtilsArray_Task.getTask());
        adapter.notifyDataSetChanged();
        UtilsArray_All.ReloadCategoryItems();
        TasksharedPreferences = context.getSharedPreferences("Task.db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = TasksharedPreferences.edit();
        editor.remove(TASK_ARRAY_DB);
        editor.putString(TASK_ARRAY_DB, GsonConverter.TaskToJson(task));
        editor.commit();
    }

    public static void AddToTask(Task t, Context context) {
        task.add(t);
        SortTaskItems();
        Task_rec_adapter adapter = new Task_rec_adapter(task);
        adapter.notifyDataSetChanged();
        UtilsArray_All.ReloadCategoryItems();

        TasksharedPreferences = context.getSharedPreferences("Task.db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = TasksharedPreferences.edit();
        editor.remove(TASK_ARRAY_DB);
        editor.putString(TASK_ARRAY_DB, GsonConverter.TaskToJson(task));
        editor.commit();
    }

    public static void UpdateTask(ArrayList<Task> t, Context context) {

        SortTaskItems();
        TasksharedPreferences = context.getSharedPreferences("Task.db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = TasksharedPreferences.edit();
        editor.remove(TASK_ARRAY_DB);
        editor.putString(TASK_ARRAY_DB, GsonConverter.TaskToJson(t));
        editor.commit();
    }

    public static void ClearTaskArraylist(Context context) {
        ArrayList<Task> copy = new ArrayList<>(task);
        for (Task taskItem : copy) {
            UtilsArray_Task.CancelTaskAlarm(context, UtilsArray_Task.task.indexOf(taskItem));
            task.remove(taskItem);
        }
        UpdateTask(task, context);
        copy.clear();
    }

    public static void SortTaskItems() {
        Collections.sort(task, (t1, t2) -> {
            int isComplete = (t1.isComplete).compareTo(t2.isComplete);
            if (isComplete == 0) {
                return t1.calendar.compareTo(t1.calendar);
            }
            return isComplete;
        });
        Task_rec_adapter adapter = new Task_rec_adapter(task);
        adapter.notifyDataSetChanged();
    }

    public static void CancelTaskAlarm(Context context, Integer position) {
        if (position > -1) {
            if (Task.isTaskAheadOfTime(UtilsArray_Task.getTask().get(position).calendar)) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(
                        Context.ALARM_SERVICE);
                Intent i = new Intent(context.getApplicationContext(), EmailReceiver.class);
                i.putExtra("position", position);
                PendingIntent p = PendingIntent.getBroadcast(context.getApplicationContext(),
                        (int) UtilsArray_Task.task.get(position).id, i,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                if (p != null) {
                    alarmManager.cancel(p);
                    p.cancel();
                }
            }
        }
    }

    public static void ResetTaskAlarms(Context context, Integer position) {
        if (position > -1) {
            CancelTaskAlarm(context, position);
            Intent intent = new Intent(context.getApplicationContext(), TaskReceiver.class);
            intent.putExtra("id", task.get(position).id);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context.getApplicationContext(), (int) task.get(position).id, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(
                    Context.ALARM_SERVICE);

            if (task.get(position).isRepeating) {
                List<Long> timeToWeeklyRings = Task.getTimeToWeeklyRings(task.get(position));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timeToWeeklyRings.get(0));
                task.get(position).calendar.set(Calendar.DAY_OF_MONTH,
                        calendar.get(Calendar.DAY_OF_MONTH));
                task.get(position).calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                task.get(position).calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeToWeeklyRings.get(0),
                            pendingIntent);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                            task.get(position).calendar.getTimeInMillis(), pendingIntent);
                }
            }
        }
    }
}