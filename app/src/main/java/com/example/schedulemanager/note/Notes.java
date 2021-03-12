package com.example.schedulemanager.note;

import java.util.Calendar;


public class Notes {

    public long id;

    public  String title;
    public String desc;
    public Calendar calendar;
    public Calendar lastEdited;
    public Boolean EngagedAlarm;
    public Integer BackgroundColor;

    public Notes(long id, String title, String desc, Calendar lastEdited, Boolean engagedAlarm, Integer backgroundColor) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.lastEdited = lastEdited;
        EngagedAlarm = engagedAlarm;
        BackgroundColor = backgroundColor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getBackgroundColor() {
        return BackgroundColor;
    }

    public void setBackgroundColor(Integer backgroundColor) {
        BackgroundColor = backgroundColor;
    }

    public Boolean getEngagedAlarm() {
        return EngagedAlarm;
    }

    public void setEngagedAlarm(Boolean engagedAlarm) {
        EngagedAlarm = engagedAlarm;
    }


    //For updating note



    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Notes() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static Boolean isNoteAheadOfTime(Calendar calGiven){
        Calendar cal = Calendar.getInstance();
        return calGiven.compareTo(cal) > 0;
    }

    public static Notes findNoteFromId(long id){

        for (Notes n : UtilsArraylist.note){
            if (n.id == id){
                return n;
            }
        }

        return null;
    }

}


