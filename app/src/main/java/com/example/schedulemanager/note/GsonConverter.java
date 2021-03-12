package com.example.schedulemanager.note;

//import com.example.firebase.Calendar.calendar;
import com.example.schedulemanager.Setting.Setting_Class;
import com.example.schedulemanager.Task.Task;
import com.example.schedulemanager.email.Email;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GsonConverter {

    public static String ArrayToJson(ArrayList<Notes> notes){
        Gson gson = new Gson();
        return gson.toJson(notes);

    }

    public static ArrayList<Notes> jsonToArray(String json){
        Gson gson = new Gson();
        Type type= new TypeToken<ArrayList<Notes>>(){}.getType();
        return gson.fromJson(json,type);

    }


    public static String EmailToJson(ArrayList<Email> e){
        Gson gson = new Gson();
        return gson.toJson(e);

    }

    public static ArrayList<Email> jsonToEmail(String json){
        Gson gson = new Gson();
        Type type= new TypeToken<ArrayList<Email>>(){}.getType();
        return gson.fromJson(json,type);

    }
    public static String TaskToJson(ArrayList<Task> task){
        Gson gson = new Gson();
        return gson.toJson(task);

    }

    public static ArrayList<Task> jsonToTask(String json){
        Gson gson = new Gson();
        Type type= new TypeToken<ArrayList<Task>>(){}.getType();
        return gson.fromJson(json,type);

    }

   public static String IntegerToJson(ArrayList<Integer> notsent){
        Gson gson = new Gson();
        return gson.toJson(notsent);

    }

    public static ArrayList<Integer> jsonToInteger(String json){
        Gson gson = new Gson();
        Type type= new TypeToken<ArrayList<Integer>>(){}.getType();
        return gson.fromJson(json,type);
    }

    public static String PenInttoJson(ArrayList<CustomPenInt> customPenInts){
        Gson gson = new Gson();
        return gson.toJson(customPenInts);

    }

    public static ArrayList<CustomPenInt> jsonToPenInt(String json){
        Gson gson = new Gson();
        Type type= new TypeToken<ArrayList<CustomPenInt>>(){}.getType();
        return gson.fromJson(json,type);
    }

    public static String SettingstoJson(Setting_Class settings){
        Gson gson = new Gson();
        return gson.toJson(settings);

    }

    public static Setting_Class jsonToSettings(String json){
        Gson gson = new Gson();
        Type type= new TypeToken<Setting_Class>(){}.getType();
        return gson.fromJson(json,type);
    }


//    public static String EventToJson(ArrayList<calendar> events){
//        Gson gson = new Gson();
//        return gson.toJson(events);
//
//    }
//
//    public static ArrayList<calendar> jsonToEvent(String json){
//        Gson gson = new Gson();
//        Type type= new TypeToken<ArrayList<calendar>>(){}.getType();
//        return gson.fromJson(json,type);
//    }


}
