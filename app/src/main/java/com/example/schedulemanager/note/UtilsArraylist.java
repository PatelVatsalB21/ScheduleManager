package com.example.schedulemanager.note;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.schedulemanager.R;
import com.example.schedulemanager.email.EmailReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

//import com.example.firebase.HomeFrag.All_Rec_Adapter;
//import com.example.firebase.HomeFrag.UtilsArray_All;
//import com.example.firebase.DBHelper;

public class UtilsArraylist {

    public static SharedPreferences sharedPreferences;

//    public static ArrayList<CustomPenInt> notesPendingIntents = new ArrayList<>();
    public static ArrayList<Notes> note = new ArrayList<>();
    final private static String NOTES_ARRAY_DB = "Notes_Array_DB";
//    final private static String NOTES_INTENT_DB = "Notes_Intent_DB";
    static AlarmManager alarmManager;

    public static ArrayList<Integer> note_BG_colorSet = new ArrayList<>();
    public static ArrayList<Integer> note_Txt_colorSet = new ArrayList<>();


    static Calendar cal = Calendar.getInstance();
//    public static DBHelper myDb;
//    static String Data;
//    public static Context c;


    public static void initNote(Context context) {
//        c = context;
//        myDb = new DBHelper(context);
//        myDb.getWritableDatabase();
//        Cursor res = myDb.getData(1);
//        if (res.move(1)) {
//            Data = res.getString(res.getColumnIndex(DBHelper.COL_2));
//
//        }
//        res.close();
//        note = GsonConverter.jsonToArray(Data);
        sharedPreferences = context.getSharedPreferences("Notes.db", Context.MODE_PRIVATE);
        note = GsonConverter.jsonToArray(sharedPreferences.getString(NOTES_ARRAY_DB, null));
//        notesPendingIntents = GsonConverter.jsonToPenInt(sharedPreferences.getString(NOTES_INTENT_DB, null));
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (note == null || note.size() == 0) {
            note = new ArrayList<>();
        }
////        if (MainActivity.isFirstUse) {
//        if (note == null || note.size() == 0) {
//            note = new ArrayList<>();
//            Notes n = new Notes((long) 1, "this is sample title", "this is sample desc", cal, false,7);
//            note.add(n);
////            myDb.insertData(0, 1, 2, GsonConverter.TaskToJson(UtilsArray_Task.task), GsonConverter.ArrayToJson(note), GsonConverter.EmailToJson(UtilsArray_Email.mail));
////            }
//        }

        note_BG_colorSet.clear();
        note_BG_colorSet.add(context.getResources().getColor(R.color.note_bg_color_1));
        note_BG_colorSet.add(context.getResources().getColor(R.color.note_bg_color_2));
        note_BG_colorSet.add(context.getResources().getColor(R.color.note_bg_color_3));
        note_BG_colorSet.add(context.getResources().getColor(R.color.note_bg_color_4));
        note_BG_colorSet.add(context.getResources().getColor(R.color.note_bg_color_5));
        note_BG_colorSet.add(context.getResources().getColor(R.color.note_bg_color_6));
        note_BG_colorSet.add(context.getResources().getColor(R.color.note_bg_color_7));
        note_BG_colorSet.add(context.getResources().getColor(R.color.colorPrimary));

        note_Txt_colorSet.clear();
        note_Txt_colorSet.add(context.getResources().getColor(R.color.note_txt_color_1));
        note_Txt_colorSet.add(context.getResources().getColor(R.color.note_txt_color_2));
        note_Txt_colorSet.add(context.getResources().getColor(R.color.note_txt_color_3));
        note_Txt_colorSet.add(context.getResources().getColor(R.color.note_txt_color_4));
        note_Txt_colorSet.add(context.getResources().getColor(R.color.note_txt_color_5));
        note_Txt_colorSet.add(context.getResources().getColor(R.color.note_txt_color_6));
        note_Txt_colorSet.add(context.getResources().getColor(R.color.note_txt_color_7));
        note_Txt_colorSet.add(context.getResources().getColor(R.color.colorPrimaryDark));
    }


    public UtilsArraylist() {
    }


    public static ArrayList<Notes> getNote() {
        return note;
    }

    public static void setNote(Notes n, Integer position, Context context) {
        if (note != null) {
            UtilsArraylist.note.set(position, n);
            Notes_rec_Adapter adapter = new Notes_rec_Adapter(context, UtilsArraylist.getNote());
            adapter.notifyDataSetChanged();
//        UtilsArray_All.ReloadCategoryItems();
//       All_In_One_Main_Adapter adapter1 = new All_In_One_Main_Adapter(context);
//        adapter1.notifyDataSetChanged();

            UpdateNote(note, context);

//        sharedPreferences = context.getSharedPreferences("Notes.db", Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(NOTES_ARRAY_DB, GsonConverter.ArrayToJson(note));
//        editor.commit();
//        boolean isUpdate = myDb.updateData(String.valueOf(1), GsonConverter.ArrayToJson(note));
//myDb.insertData(GsonConverter.ArrayToJson(note))
        }
    }

//    public static void setFNote(String s, Context context) {
//        UtilsArraylist.note = GsonConverter.jsonToArray(s);
//        Notes_rec_Adapter adapter = new Notes_rec_Adapter(context, UtilsArraylist.getNote());
//        adapter.notifyDataSetChanged();
////        All_Rec_Adapter adapter1 = new All_Rec_Adapter(context, UtilsArray_All.getAllItemsArrayList());
////        adapter1.notifyDataSetChanged();
//
//        sharedPreferences = context.getSharedPreferences("Notes.db", Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(NOTES_ARRAY_DB, GsonConverter.ArrayToJson(note));
//        editor.commit();
//    }


    public static void AddToNote(Notes n, Context context) {
        note.add(n);
        Notes_rec_Adapter adapter = new Notes_rec_Adapter(context, UtilsArraylist.getNote());
        adapter.notifyDataSetChanged();
//        UtilsArray_All.ReloadCategoryItems();
//        All_In_One_Main_Adapter adapter1 = new All_In_One_Main_Adapter(context);
//        adapter1.notifyDataSetChanged();

        Collections.sort(note, new Comparator<Notes>() {
            @Override
            public int compare(Notes n1, Notes n2) {
                return (n1.lastEdited).compareTo(n2.lastEdited);
            }
        });

        UpdateNote(note, context);

//        sharedPreferences = context.getSharedPreferences("Notes.db", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(NOTES_ARRAY_DB, GsonConverter.ArrayToJson(note));
//        editor.commit();
//        boolean isUpdate = myDb.updateData(String.valueOf(1), GsonConverter.ArrayToJson(note));


    }

    //    public static void DeleteNote(Integer position , Context context){
//        UtilsArraylist.getNote().remove(position);
//        Notes_rec_Adapter adapter = new Notes_rec_Adapter(context, UtilsArraylist.getNote());
//        adapter.notifyItemRemoved(position);
//        adapter.notifyDataSetChanged();
//
//        SharedPreferences.Editor editor =  sharedPreferences.edit();
//        editor.remove(NOTES_ARRAY_DB);
//        editor.putString( NOTES_ARRAY_DB , GsonConverter.ArrayToJson(note));
//        editor.commit();
//
//    }
    public static void UpdateNote(ArrayList<Notes> n, Context context) {

        sharedPreferences = context.getSharedPreferences("Notes.db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(NOTES_ARRAY_DB);
        editor.putString(NOTES_ARRAY_DB, GsonConverter.ArrayToJson(n));
        editor.commit();
//        int deleteData = myDb.deleteData(String.valueOf(1));
//        boolean isUpdate = myDb.updateData(String.valueOf(1), GsonConverter.ArrayToJson(note));

    }

    //
//    public static void AddToNoteIntent(Context context, CustomPenInt c) {
////        notesPendingIntents.add(c);
//
//        sharedPreferences = context.getSharedPreferences("Notes.db", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.remove(NOTES_INTENT_DB);
//        editor.putString(NOTES_INTENT_DB, GsonConverter.PenInttoJson(notesPendingIntents));
//        editor.commit();
//    }
//
//    public static void RemoveFromNoteIntent(Context context, CustomPenInt c) {
//        if (notesPendingIntents != null) {
//            if (notesPendingIntents.contains(c)) {
//                alarmManager.cancel(c.pendingIntent);
//                notesPendingIntents.remove(c);
//                sharedPreferences = context.getSharedPreferences("Notes.db", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.remove(NOTES_INTENT_DB);
//                editor.putString(NOTES_INTENT_DB, GsonConverter.PenInttoJson(notesPendingIntents));
//                editor.commit();
//            }
//        }
//    }
//
//    public static PendingIntent getNotePendingIntentFromPosition(Integer position) {
//        if (notesPendingIntents != null) {
//            for (CustomPenInt i : notesPendingIntents) {
//                if (i.position.equals(position)) {
//                    return i.pendingIntent;
//                }
//            }
//        }
//        return null;
//    }
//
//    public static CustomPenInt getCustomPenIntFromPosition(Integer position) {
//        if (notesPendingIntents != null) {
//            for (CustomPenInt i : notesPendingIntents) {
//                if (i.position.equals(position)) {
//                    return i;
//                }
//            }
//        }
//        return null;
//    }

    public static void ClearNoteArraylist(Context context){
        ArrayList<Notes> copy = new ArrayList<>(note);
        for (Notes noteItem:copy){
            UtilsArraylist.CancelNoteAlarm(context,note.indexOf(noteItem));
            note.remove(noteItem);
        }
        UpdateNote(note,context);
        copy.clear();
    }

    public static void CancelNoteAlarm(Context context,Integer position) {

        if (Notes.isNoteAheadOfTime(UtilsArraylist.getNote().get(position).calendar)) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context.getApplicationContext(), EmailReceiver.class);
            i.putExtra("position", position);
            PendingIntent p = PendingIntent.getBroadcast(context.getApplicationContext(), (int) UtilsArraylist.note.get(position).id, i, PendingIntent.FLAG_UPDATE_CURRENT);
            if (p!=null) {
                alarmManager.cancel(p);
                p.cancel();
                UtilsArraylist.getNote().get(position).EngagedAlarm=false;
                UtilsArraylist.UpdateNote(UtilsArraylist.getNote(), context);
            }
//            else
//                Toast.makeText(context, "Reminder for this note is already deactivated or notified", Toast.LENGTH_SHORT).show();
        }
//        else
//            Toast.makeText(context, "This reminder has already notified", Toast.LENGTH_SHORT).show();
    }
}








