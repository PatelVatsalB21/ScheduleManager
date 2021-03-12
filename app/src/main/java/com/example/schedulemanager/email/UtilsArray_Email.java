package com.example.schedulemanager.email;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

//import com.example.firebase.HomeFrag.All_Rec_Adapter;
//import com.example.firebase.HomeFrag.UtilsArray_All;
//import com.example.firebase.DBHelper;
import com.example.schedulemanager.HomeFrag.UtilsArray_All;
import com.example.schedulemanager.note.GsonConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class UtilsArray_Email {

    public static SharedPreferences EmailsharedPreferences;

//    public static ArrayList<CustomPenInt> emailPenInt = new ArrayList<>();
    public static ArrayList<Email> mail = new ArrayList<>();
    final private static String EMAIL_ARRAY_DB = "Email_Array_DB";
//    final private static String EMAIL_INTENT_DB = "Email_Intent_DB";
    static Calendar cal =Calendar.getInstance();
//    public static DBHelper myDb;
//    static String Data;


    public static void initMail(Context context){
        EmailsharedPreferences = context.getSharedPreferences("Email.db",Context.MODE_PRIVATE);
        mail = GsonConverter.jsonToEmail(EmailsharedPreferences.getString(EMAIL_ARRAY_DB, null));
//        emailPenInt = GsonConverter.jsonToPenInt(EmailsharedPreferences.getString(EMAIL_INTENT_DB,null));

//        myDb  = new DBHelper(context);
//        Cursor res = myDb.getData(2);
//        if (res.move(2)) {
//            Data = res.getString(2);
//
//        }
//        mail = GsonConverter.jsonToEmail(Data);

        if(mail == null||mail.size() == 0) {
            mail = new ArrayList<>();
        }


//        if(mail == null||mail.size() == 0){
//            mail = new ArrayList<>();
//
//            Email e = new Email("patelvatsalb21@gmail.com","patelvatsalb21@gmail.com","subject","message",cal,false);
//            mail.add(e);
////            myDb.insertData(0,1,2,GsonConverter.TaskToJson(UtilsArray_Task.task),GsonConverter.ArrayToJson(UtilsArraylist.note),GsonConverter.EmailToJson(UtilsArray_Email.mail));
//        }


    }


    public UtilsArray_Email() {
    }


    public static ArrayList<Email> getMail() {
        return mail;
    }

    public static void setMail(Email e , Integer position, Context context) {
        UtilsArray_Email.mail.set(position, e);
        Email_rec_Adapter adapter = new Email_rec_Adapter(UtilsArray_Email.getMail());
        adapter.notifyDataSetChanged();

        UtilsArray_All.ReloadCategoryItems();
//        All_In_One_Main_Adapter adapter1 = new All_In_One_Main_Adapter(context);
//        adapter1.notifyDataSetChanged();

        EmailsharedPreferences = context.getSharedPreferences("Email.db",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  EmailsharedPreferences.edit();
        editor.putString( EMAIL_ARRAY_DB , GsonConverter.EmailToJson(mail));
        editor.commit();
//               boolean isUpdate = myDb.updateData(String.valueOf(2),GsonConverter.EmailToJson(mail));
    }

//    public static void setFMail(String s,  Context context) {
//        UtilsArraylist.note = GsonConverter.jsonToArray(s);
//        Notes_rec_Adapter adapter = new Notes_rec_Adapter(context,UtilsArraylist.getNote());
//        adapter.notifyDataSetChanged();
//
//        sharedPreferences = context.getSharedPreferences("Email.db",Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor =  sharedPreferences.edit();
//        editor.putString( EMAIL_ARRAY_DB , GsonConverter.EmailToJson(mail));
//        editor.commit();
//    }


    public static void AddToMail(Email e, Context context){
        mail.add(e);



        Collections.sort(mail, new Comparator<Email>() {
            @Override
            public int compare(Email e1, Email e2) {
                return (e1.cal).compareTo(e2.cal);
            }
        });

        Email_rec_Adapter adapter = new Email_rec_Adapter(mail);
        adapter.notifyDataSetChanged();

        UtilsArray_All.ReloadCategoryItems();
//        All_In_One_Main_Adapter adapter1 = new All_In_One_Main_Adapter(context);
//        adapter1.notifyDataSetChanged();

        EmailsharedPreferences = context.getSharedPreferences("Email.db",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  EmailsharedPreferences.edit();
        editor.putString( EMAIL_ARRAY_DB , GsonConverter.EmailToJson(mail));
//        Log.d("SharedP","EmailSaved");
        editor.commit();
//        boolean isUpdate = myDb.updateData(String.valueOf(2),GsonConverter.EmailToJson(mail));
    }

    public static void DeleteEmail(Integer i , Context context){
        UtilsArray_Email.CancelEmailAlarm(context,i);
        UtilsArray_Email.getMail().remove(i);
        Email_rec_Adapter adapter = new Email_rec_Adapter(UtilsArray_Email.getMail());
        adapter.notifyItemRemoved(i);
        adapter.notifyDataSetChanged();

//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(UtilsArray_Email.getEmailPendingIntentFromPosition(i));
//        UtilsArray_Email.RemoveFromEmailIntent(context,UtilsArray_Email.getCustomPenIntFromPosition(i));

        UtilsArray_All.ReloadCategoryItems();
        UtilsArray_Email.UpdateMail(UtilsArray_Email.getMail(),context);
//        All_In_One_Main_Adapter adapter1 = new All_In_One_Main_Adapter(context);
//        adapter1.notifyDataSetChanged();

//        EmailsharedPreferences = context.getSharedPreferences("Email.db",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor =  EmailsharedPreferences.edit();
//        editor.remove(EMAIL_ARRAY_DB);
//        editor.remove(EMAIL_INTENT_DB);
//        editor.putString( EMAIL_ARRAY_DB , GsonConverter.EmailToJson(mail));
//        editor.putString( EMAIL_INTENT_DB , GsonConverter.PenInttoJson(emailPenInt));
//        editor.commit();

//        int deleteData = myDb.deleteData(String.valueOf(2));
//        boolean isUpdate = myDb.updateData(String.valueOf(2),GsonConverter.EmailToJson(mail));


    }

    public static void UpdateMail(ArrayList<Email> e, Context context){


        EmailsharedPreferences = context.getSharedPreferences("Email.db",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  EmailsharedPreferences.edit();
        editor.remove(EMAIL_ARRAY_DB);
        editor.putString( EMAIL_ARRAY_DB , GsonConverter.EmailToJson(e));
        editor.commit();

//        int deleteData = myDb.deleteData(String.valueOf(2));
//        boolean isUpdate = myDb.updateData(String.valueOf(2),GsonConverter.EmailToJson(mail));

    }

//    public static void AddToEmailIntent(Context context,CustomPenInt c){
//        emailPenInt.add(c);
//        EmailsharedPreferences = context.getSharedPreferences("Email.db",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor =  EmailsharedPreferences.edit();
//        editor.remove(EMAIL_INTENT_DB);
//        editor.putString( EMAIL_INTENT_DB , GsonConverter.PenInttoJson(emailPenInt));
//        editor.commit();
//    }
//
//    public static void RemoveFromEmailIntent(Context context,CustomPenInt c) {
//        if (emailPenInt.contains(c)) {
//            emailPenInt.remove(c);
//            EmailsharedPreferences = context.getSharedPreferences("Email.db", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = EmailsharedPreferences.edit();
//            editor.remove(EMAIL_INTENT_DB);
//            editor.putString(EMAIL_INTENT_DB, GsonConverter.PenInttoJson(emailPenInt));
//            editor.commit();
//        }
//    }
//
//    public static PendingIntent getEmailPendingIntentFromPosition(Integer position){
//     for (CustomPenInt i :emailPenInt){
//         if (i.position.equals(position)){
//             return i.pendingIntent;
//         }
//     }
//        return null;
//    }
//
//    public static CustomPenInt getCustomPenIntFromPosition(Integer position){
//        for (CustomPenInt i :emailPenInt){
//            if (i.position.equals(position)){
//                return i;
//            }
//        }
//        return null;
//    }

    public static void CancelEmailAlarm(Context context,Integer position){
        if (Email.isEmailAheadOfTime(UtilsArray_Email.getMail().get(position).cal)) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context.getApplicationContext(), EmailReceiver.class);
            i.putExtra("position", position);
            PendingIntent p = PendingIntent.getBroadcast(context.getApplicationContext(), (int) UtilsArray_Email.mail.get(position).id, i, PendingIntent.FLAG_UPDATE_CURRENT);
            if (p!=null) {
                alarmManager.cancel(p);
                p.cancel();
                UtilsArray_Email.getMail().get(position).Scheduled=false;
            }
//            else
//                Toast.makeText(context, "This email has been already  sent or unscheduled", Toast.LENGTH_SHORT).show();
        }
//        else Toast.makeText(context, "This email has been already sent or unscheduled", Toast.LENGTH_SHORT).show();
    }

    public static void ClearEmailArrayList(Context context){
        ArrayList<Email>copy = new ArrayList<>(mail);
        for (Email emailItem :copy){
            UtilsArray_Email.CancelEmailAlarm(context,mail.indexOf(emailItem));
            mail.remove(emailItem);
        }
        UtilsArray_Email.UpdateMail(mail,context);
        copy.clear();
    }
//    public static int getPositionFromUUID(UUID x){
//        for (Email e: mail){
//            int i = 0;
//            if(e.workID==x){
//                return i;
//            }
//            i++;
//        }
//        return 0;
//    }

}
