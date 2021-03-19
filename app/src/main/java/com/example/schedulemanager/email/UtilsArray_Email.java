package com.example.schedulemanager.email;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.schedulemanager.HomeFrag.UtilsArray_All;
import com.example.schedulemanager.Utils.GsonConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class UtilsArray_Email {

    public static SharedPreferences EmailsharedPreferences;
    public static ArrayList<Email> mail = new ArrayList<>();
    final private static String EMAIL_ARRAY_DB = "Email_Array_DB";
    static Calendar cal = Calendar.getInstance();

    public static void initMail(Context context) {
        EmailsharedPreferences = context.getSharedPreferences("Email.db", Context.MODE_PRIVATE);
        mail = GsonConverter.jsonToEmail(EmailsharedPreferences.getString(EMAIL_ARRAY_DB, null));
        if (mail == null || mail.size() == 0) {
            mail = new ArrayList<>();
        }
    }

    public UtilsArray_Email() {
    }

    public static ArrayList<Email> getMail() {
        return mail;
    }

    public static void setMail(Email e, Integer position, Context context) {
        UtilsArray_Email.mail.set(position, e);
        Email_rec_Adapter adapter = new Email_rec_Adapter(UtilsArray_Email.getMail());
        adapter.notifyDataSetChanged();
        UtilsArray_All.ReloadCategoryItems();
        EmailsharedPreferences = context.getSharedPreferences("Email.db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = EmailsharedPreferences.edit();
        editor.putString(EMAIL_ARRAY_DB, GsonConverter.EmailToJson(mail));
        editor.commit();
    }

    public static void AddToMail(Email e, Context context) {
        mail.add(e);
        Collections.sort(mail, (e1, e2) -> (e1.cal).compareTo(e2.cal));
        Email_rec_Adapter adapter = new Email_rec_Adapter(mail);
        adapter.notifyDataSetChanged();
        UtilsArray_All.ReloadCategoryItems();
        EmailsharedPreferences = context.getSharedPreferences("Email.db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = EmailsharedPreferences.edit();
        editor.putString(EMAIL_ARRAY_DB, GsonConverter.EmailToJson(mail));
        editor.commit();
    }

    public static void DeleteEmail(Integer i, Context context) {
        UtilsArray_Email.CancelEmailAlarm(context, i);
        UtilsArray_Email.getMail().remove(i);
        Email_rec_Adapter adapter = new Email_rec_Adapter(UtilsArray_Email.getMail());
        adapter.notifyItemRemoved(i);
        adapter.notifyDataSetChanged();
        UtilsArray_All.ReloadCategoryItems();
        UtilsArray_Email.UpdateMail(UtilsArray_Email.getMail(), context);
    }

    public static void UpdateMail(ArrayList<Email> e, Context context) {
        EmailsharedPreferences = context.getSharedPreferences("Email.db", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = EmailsharedPreferences.edit();
        editor.remove(EMAIL_ARRAY_DB);
        editor.putString(EMAIL_ARRAY_DB, GsonConverter.EmailToJson(e));
        editor.commit();
    }

    public static void CancelEmailAlarm(Context context, Integer position) {
        if (Email.isEmailAheadOfTime(UtilsArray_Email.getMail().get(position).cal)) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(
                    Context.ALARM_SERVICE);
            Intent i = new Intent(context.getApplicationContext(), EmailReceiver.class);
            i.putExtra("position", position);
            PendingIntent p = PendingIntent.getBroadcast(context.getApplicationContext(),
                    (int) UtilsArray_Email.mail.get(position).id, i,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            if (p != null) {
                alarmManager.cancel(p);
                p.cancel();
                UtilsArray_Email.getMail().get(position).Scheduled = false;
            }
        }
    }

    public static void ClearEmailArrayList(Context context) {
        ArrayList<Email> copy = new ArrayList<>(mail);
        for (Email emailItem : copy) {
            UtilsArray_Email.CancelEmailAlarm(context, mail.indexOf(emailItem));
            mail.remove(emailItem);
        }
        UtilsArray_Email.UpdateMail(mail, context);
        copy.clear();
    }
}
