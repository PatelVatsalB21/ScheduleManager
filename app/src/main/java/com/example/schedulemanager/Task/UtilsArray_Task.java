package com.example.schedulemanager.Task;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

//import com.example.firebase.HomeFrag.All_Rec_Adapter;
//import com.example.firebase.HomeFrag.UtilsArray_All;

//import com.example.firebase.DBHelper;
import com.example.schedulemanager.HomeFrag.UtilsArray_All;
import com.example.schedulemanager.R;
import com.example.schedulemanager.email.EmailReceiver;
import com.example.schedulemanager.note.GsonConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UtilsArray_Task {
    public static SharedPreferences TasksharedPreferences;

    //    public static ArrayList<CustomPenInt> taskPendingIntent = new ArrayList<>();
    public static ArrayList<TaskSpinnerRowItem> category = new ArrayList<>(14);
    public static ArrayList<Task> task = new ArrayList<>();
    public static ArrayList<Integer> taskReceiverImages = new ArrayList<>(14);
    final private static String TASK_ARRAY_DB = "Task_Array_DB";
    //    final private static String TASK_INTENT_DB = "Task_Intent_DB";
    static Calendar cal = Calendar.getInstance();
//    static DBHelper myDb;
//    static String Data;


    public static void initTask(Context context) {
        TasksharedPreferences = context.getSharedPreferences("Task.db", Context.MODE_PRIVATE);
        task = GsonConverter.jsonToTask(TasksharedPreferences.getString(TASK_ARRAY_DB, null));
//        taskPendingIntent = GsonConverter.jsonToPenInt(TasksharedPreferences.getString(TASK_INTENT_DB, null));

//        myDb = new DBHelper(context);

//        Cursor res = myDb.getData(0);
//        if (res.moveToFirst()) {
//            Data = res.getString(0);
//
//        }
//        task = GsonConverter.jsonToTask(Data);

//        category.add(new TaskSpinnerRowItem(R.raw.action,"Action"));
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
        } else SortTaskItems();


//        if (task == null || task.size() == 0) {
//            task = new ArrayList<>();
//
////            Task t = new Task(cal.getTimeInMillis(), "Vatsal", cal, 0, b);
//            task.add(new Task(cal.getTimeInMillis(), "Vatsal", cal, 2, false, false, false, false, false, false, false,false));
//            Log.d("UTILSARRAYTASKSIZE", String.valueOf(UtilsArray_Task.task.size()));
//
//
////            myDb.insertData(0,1,2,GsonConverter.TaskToJson(UtilsArray_Task.task),GsonConverter.ArrayToJson(UtilsArraylist.note),GsonConverter.EmailToJson(UtilsArray_Email.mail));
//        }

        Log.d("UTILSARRAYTASKSIZE", String.valueOf(UtilsArray_Task.task.size()));
//        Log.d("UTILSARRAYTASK", task.get(task.size() - 1).getTitle());


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
//        All_In_One_Main_Adapter adapter1 = new All_In_One_Main_Adapter(context);
//        adapter1.notifyDataSetChanged();


        TasksharedPreferences = context.getSharedPreferences("Task.db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = TasksharedPreferences.edit();
        editor.remove(TASK_ARRAY_DB);
        editor.putString(TASK_ARRAY_DB, GsonConverter.TaskToJson(task));
        editor.commit();

//        boolean isUpdate = myDb.updateData(String.valueOf(0),GsonConverter.TaskToJson(task));
    }

//    public static void setFTask(String s, Context context) {
//        UtilsArraylist.note = GsonConverter.jsonToArray(s);
//        Notes_rec_Adapter adapter = new Notes_rec_Adapter(context, UtilsArraylist.getNote());
//        adapter.notifyDataSetChanged();
////        All_Rec_Adapter adapter1 = new All_Rec_Adapter(context, UtilsArray_All.getAllItemsArrayList());
////        adapter1.notifyDataSetChanged();
//
//        TasksharedPreferences = context.getSharedPreferences("Task.db", Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = TasksharedPreferences.edit();
//        editor.putString(TASK_ARRAY_DB, GsonConverter.TaskToJson(task));
//        editor.commit();
//    }


    public static void AddToTask(Task t, Context context) {
        task.add(t);
        SortTaskItems();
        Task_rec_adapter adapter = new Task_rec_adapter(task);
        Log.d("UTILSARRAYTASK", String.valueOf(task.size()));
//        All_In_One_Main_Adapter adapter1 = new All_In_One_Main_Adapter(context);
//        adapter1.notifyDataSetChanged();


        adapter.notifyDataSetChanged();
        UtilsArray_All.ReloadCategoryItems();

        TasksharedPreferences = context.getSharedPreferences("Task.db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = TasksharedPreferences.edit();
        editor.remove(TASK_ARRAY_DB);
        editor.putString(TASK_ARRAY_DB, GsonConverter.TaskToJson(task));
        editor.commit();

//        boolean isUpdate = myDb.updateData(String.valueOf(0),GsonConverter.TaskToJson(task));
    }

//    public static void DeleteTask(Integer i, Context context) {
//        UtilsArray_Task.getTask().remove(i);
//        Task_rec_adapter adapter = new Task_rec_adapter(UtilsArray_Task.getTask());
//        adapter.notifyItemRemoved(i);
//        adapter.notifyDataSetChanged();
////        All_Rec_Adapter adapter1 = new All_Rec_Adapter(context, UtilsArray_All.getAllItemsArrayList());
////        adapter1.notifyDataSetChanged();
//
////        SharedPreferences.Editor editor = TasksharedPreferences.edit();
////        editor.remove(TASK_ARRAY_DB);
////        editor.putString(TASK_ARRAY_DB, GsonConverter.TaskToJson(task));
////        editor.commit();
//    }

    public static void UpdateTask(ArrayList<Task> t, Context context) {

        SortTaskItems();
        TasksharedPreferences = context.getSharedPreferences("Task.db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = TasksharedPreferences.edit();
        editor.remove(TASK_ARRAY_DB);
        editor.putString(TASK_ARRAY_DB, GsonConverter.TaskToJson(t));
        editor.commit();
//        int deleteData = myDb.deleteData(String.valueOf(0));
//        boolean isUpdate = myDb.updateData(String.valueOf(0),GsonConverter.TaskToJson(task));

    }

//    public static void AddToTaskIntent(Context context,CustomPenInt c){
//        if(taskPendingIntent == null){
//            taskPendingIntent = new ArrayList<>();
//        }
//        taskPendingIntent.add(c);
//        TasksharedPreferences = context.getSharedPreferences("Task.db",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor =  TasksharedPreferences.edit();
//        editor.remove(TASK_INTENT_DB);
//        editor.putString( TASK_INTENT_DB , GsonConverter.PenInttoJson(taskPendingIntent));
//        editor.commit();
//    }
//
//    public static void RemoveFromTaskIntent(Context context,CustomPenInt c) {
//        if (taskPendingIntent.contains(c)) {
//            taskPendingIntent.remove(c);
//            TasksharedPreferences = context.getSharedPreferences("Task.db", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = TasksharedPreferences.edit();
//            editor.remove(TASK_INTENT_DB);
//            editor.putString(TASK_INTENT_DB, GsonConverter.PenInttoJson(taskPendingIntent));
//            editor.commit();
//        }
//    }
//
//    public static PendingIntent getTaskPendingIntentFromPosition(Integer position){
//        for (CustomPenInt i :taskPendingIntent){
//            if (i.position.equals(position)){
//                return i.pendingIntent;
//            }
//        }
//        return null;
//    }
//
//    public static CustomPenInt getCustomPenIntFromPosition(Integer position){
//        for (CustomPenInt i :taskPendingIntent){
//            if (i.position.equals(position)){
//                return i;
//            }
//        }
//        return null;
//    }

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
        Collections.sort(task, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                int isComplete = (t1.isComplete).compareTo(t2.isComplete);
                if (isComplete == 0) {
                    return t1.calendar.compareTo(t1.calendar);
                }
                return isComplete;
            }
        });
        Task_rec_adapter adapter = new Task_rec_adapter(task);
        adapter.notifyDataSetChanged();
    }

    public static void CancelTaskAlarm(Context context, Integer position) {
        if (position > -1) {
            if (Task.isTaskAheadOfTime(UtilsArray_Task.getTask().get(position).calendar)) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(context.getApplicationContext(), EmailReceiver.class);
                i.putExtra("position", position);
                PendingIntent p = PendingIntent.getBroadcast(context.getApplicationContext(), (int) UtilsArray_Task.task.get(position).id, i, PendingIntent.FLAG_UPDATE_CURRENT);
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
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), (int) task.get(position).id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (task.get(position).isRepeating) {

                List<Long> timeToWeeklyRings = Task.getTimeToWeeklyRings(task.get(position));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timeToWeeklyRings.get(0));
                task.get(position).calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
                task.get(position).calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                task.get(position).calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeToWeeklyRings.get(0), pendingIntent);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.get(position).calendar.getTimeInMillis(), pendingIntent);
                }
            }

        }
    }
}
