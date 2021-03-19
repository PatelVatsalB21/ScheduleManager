package com.example.schedulemanager.note;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.schedulemanager.R;
import com.example.schedulemanager.Utils.GsonConverter;
import com.example.schedulemanager.email.EmailReceiver;

import java.util.ArrayList;
import java.util.Collections;

public class UtilsArraylist {

    public static SharedPreferences sharedPreferences;
    public static ArrayList<Notes> note = new ArrayList<>();
    final private static String NOTES_ARRAY_DB = "Notes_Array_DB";
    static AlarmManager alarmManager;
    public static ArrayList<Integer> note_BG_colorSet = new ArrayList<>();
    public static ArrayList<Integer> note_Txt_colorSet = new ArrayList<>();

    public static void initNote(Context context) {
        sharedPreferences = context.getSharedPreferences("Notes.db", Context.MODE_PRIVATE);
        note = GsonConverter.jsonToArray(sharedPreferences.getString(NOTES_ARRAY_DB, null));
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (note == null || note.size() == 0) {
            note = new ArrayList<>();
        }
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
            UpdateNote(note, context);
        }
    }

    public static void AddToNote(Notes n, Context context) {
        note.add(n);
        Notes_rec_Adapter adapter = new Notes_rec_Adapter(context, UtilsArraylist.getNote());
        adapter.notifyDataSetChanged();
        Collections.sort(note, (n1, n2) -> (n1.lastEdited).compareTo(n2.lastEdited));
        UpdateNote(note, context);
    }

    public static void UpdateNote(ArrayList<Notes> n, Context context) {
        sharedPreferences = context.getSharedPreferences("Notes.db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(NOTES_ARRAY_DB);
        editor.putString(NOTES_ARRAY_DB, GsonConverter.ArrayToJson(n));
        editor.commit();
    }

    public static void ClearNoteArraylist(Context context) {
        ArrayList<Notes> copy = new ArrayList<>(note);
        for (Notes noteItem : copy) {
            UtilsArraylist.CancelNoteAlarm(context, note.indexOf(noteItem));
            note.remove(noteItem);
        }
        UpdateNote(note, context);
        copy.clear();
    }

    public static void CancelNoteAlarm(Context context, Integer position) {
        if (Notes.isNoteAheadOfTime(UtilsArraylist.getNote().get(position).calendar)) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(
                    Context.ALARM_SERVICE);
            Intent i = new Intent(context.getApplicationContext(), EmailReceiver.class);
            i.putExtra("position", position);
            PendingIntent p = PendingIntent.getBroadcast(context.getApplicationContext(),
                    (int) UtilsArraylist.note.get(position).id, i,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            if (p != null) {
                alarmManager.cancel(p);
                p.cancel();
                UtilsArraylist.getNote().get(position).EngagedAlarm = false;
                UtilsArraylist.UpdateNote(UtilsArraylist.getNote(), context);
            }
        }
    }
}