package com.example.schedulemanager.Trash;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.schedulemanager.Task.Task;
import com.example.schedulemanager.email.Email;
import com.example.schedulemanager.Utils.GsonConverter;
import com.example.schedulemanager.note.Notes;
import com.example.schedulemanager.note.UtilsArraylist;

import java.util.ArrayList;
import java.util.Collections;

public class UtilsArray_Trash {

    public static ArrayList<Task> trash_tasks = new ArrayList<>();
    public static ArrayList<Notes> trash_notes = new ArrayList<>();
    public static ArrayList<Email> trash_mails = new ArrayList<>();
    final private static String TRASH_TASK_ARRAY_DB = "Trash_Task_Array_DB";
    final private static String TRASH_NOTES_ARRAY_DB = "Trash_Notes_Array_DB";
    final private static String TRASH_EMAIL_ARRAY_DB = "Trash_Email_Array_DB";
    public static SharedPreferences trashSharedPreferences;

    public UtilsArray_Trash() {
    }

    public static void initTrashArrays(Context context) {

        trashSharedPreferences = context.getSharedPreferences("Trash.db", Context.MODE_PRIVATE);
        trash_notes = GsonConverter.jsonToArray(
                trashSharedPreferences.getString(TRASH_NOTES_ARRAY_DB, null));
        trash_tasks = GsonConverter.jsonToTask(
                trashSharedPreferences.getString(TRASH_TASK_ARRAY_DB, null));
        trash_mails = GsonConverter.jsonToEmail(
                trashSharedPreferences.getString(TRASH_EMAIL_ARRAY_DB, null));

        if (trash_tasks == null || trash_tasks.size() == 0) {
            trash_tasks = new ArrayList<>();
        }

        if (trash_notes == null || trash_notes.size() == 0) {
            trash_notes = new ArrayList<>();
        }

        if (trash_mails == null || trash_mails.size() == 0) {
            trash_mails = new ArrayList<>();
        }
    }

    public static ArrayList<Task> getTrash_tasks() {
        return trash_tasks;
    }

    public static ArrayList<Notes> getTrash_notes() {
        return trash_notes;
    }

    public static ArrayList<Email> getTrash_mails() {
        return trash_mails;
    }

    public static void AddToTrashNotes(Notes n, Context context) {
        trash_notes.add(n);
        Trash_Notes_rec_adapter adapter = new Trash_Notes_rec_adapter(context,
                UtilsArraylist.getNote());
        adapter.notifyDataSetChanged();
        Collections.sort(trash_notes, (n1, n2) -> (n1.lastEdited).compareTo(n2.lastEdited));
        UpdateTrashNote(trash_notes, context);
    }

    public static void UpdateTrashNote(ArrayList<Notes> n, Context context) {
        trashSharedPreferences = context.getSharedPreferences("Trash.db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = trashSharedPreferences.edit();
        editor.remove(TRASH_NOTES_ARRAY_DB);
        editor.putString(TRASH_NOTES_ARRAY_DB, GsonConverter.ArrayToJson(n));
        editor.commit();

    }

    public static void AddToTrashTasks(Task t, Context context) {
        trash_tasks.add(t);
        Trash_Task_rec_adapter adapter = new Trash_Task_rec_adapter(context,
                UtilsArray_Trash.getTrash_tasks());
        adapter.notifyDataSetChanged();
        Collections.sort(trash_tasks, (t1, t2) -> (t1.calendar).compareTo(t2.calendar));
        UpdateTrashTask(trash_tasks, context);
    }

    public static void UpdateTrashTask(ArrayList<Task> t, Context context) {
        trashSharedPreferences = context.getSharedPreferences("Trash.db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = trashSharedPreferences.edit();
        editor.remove(TRASH_TASK_ARRAY_DB);
        editor.putString(TRASH_TASK_ARRAY_DB, GsonConverter.TaskToJson(t));
        editor.commit();
    }

    public static void AddToTrashMail(Email e, Context context) {
        trash_mails.add(e);
        Trash_Email_rec_adapter adapter = new Trash_Email_rec_adapter(context,
                UtilsArray_Trash.getTrash_mails());
        adapter.notifyDataSetChanged();
        Collections.sort(trash_mails, (e1, e2) -> (e1.cal).compareTo(e2.cal));
        UpdateTrashMail(trash_mails, context);
    }

    public static void UpdateTrashMail(ArrayList<Email> e, Context context) {
        trashSharedPreferences = context.getSharedPreferences("Trash.db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = trashSharedPreferences.edit();
        editor.remove(TRASH_EMAIL_ARRAY_DB);
        editor.putString(TRASH_EMAIL_ARRAY_DB, GsonConverter.EmailToJson(e));
        editor.commit();
    }

    public static void ClearTrash(Context context) {
        trashSharedPreferences = context.getSharedPreferences("Trash.db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = trashSharedPreferences.edit();
        editor.remove(TRASH_TASK_ARRAY_DB);
        editor.remove(TRASH_NOTES_ARRAY_DB);
        editor.remove(TRASH_EMAIL_ARRAY_DB);
        editor.commit();
    }
}
