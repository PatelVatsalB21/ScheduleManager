package com.example.schedulemanager.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Task {

    public long id;
    public String Title;
    //    public String Desc;
    public Calendar calendar;
    public Calendar finishTime;
    //    public Boolean isActive;
    public Integer LottieFileRes;
    public Boolean isRepeating;
    public Boolean isComplete;
    public ArrayList<Boolean> weekDays;

    public Task() {
    }

    public Task(long id, String title, Calendar calendar, Integer lottieFileRes, Boolean isRepeating, Boolean isComplete, ArrayList<Boolean> weekDays) {
        this.id = id;
        Title = title;
        this.calendar = calendar;
        LottieFileRes = lottieFileRes;
        this.isRepeating = isRepeating;
        this.isComplete = isComplete;
        this.weekDays = weekDays;
    }

    public Calendar getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Calendar finishTime) {
        this.finishTime = finishTime;
    }

    public ArrayList<Boolean> getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(ArrayList<Boolean> weekDays) {
        this.weekDays = weekDays;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    public Integer getLottieFileRes() {
        return LottieFileRes;
    }

    public void setLottieFileRes(Integer lottieFileRes) {
        LottieFileRes = lottieFileRes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

//    public String getDesc() {
//        return Desc;
//    }

//    public void setDesc(String desc) {
//        Desc = desc;
//    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

//    public Boolean getIsActive() {
//        return isActive;
//    }
//
//    public void setIsActive(Boolean isActive) {
//        this.isActive = isActive;
//    }


    public static long getTimeToNextRing(Calendar cal) {
        if ((cal.getTimeInMillis() + 65000) < System.currentTimeMillis()) { // alarm time has passed for today
            return System.currentTimeMillis()+3000;
        }

//        Log.i("Alarm.java", "Alarm ring time is " + calendar.getTime().toString());
        return cal.getTimeInMillis();
    }

    public static List<Long> getTimeToWeeklyRings(Task t) {

        Calendar calUpdated = Calendar.getInstance();
        if (t.isRepeating) {
            calUpdated.set(Calendar.MINUTE, t.calendar.get(Calendar.MINUTE));
            calUpdated.set(Calendar.HOUR, t.calendar.get(Calendar.HOUR));
            int calDay, num;
            calDay = calUpdated.get(Calendar.DAY_OF_WEEK);

            List<Long> weekRingTimes = new ArrayList<>();
            for (int i = 0; i <= 7 - calDay; i++) {
                num = calDay + i;
                if (t.weekDays.get(num-1)) weekRingTimes.add(getCorrectRingDay(calUpdated, num));
            }
            for (int i = 1; i < calDay; i++) {
                num = calDay - i;
                if (t.weekDays.get(num-1)) weekRingTimes.add(getCorrectRingDay(calUpdated, num));
            }
//
//        if (t.weekDays.get(0)) weekRingTimes.add(getCorrectRingDay(calendar, 1));
//        if (t.weekDays.get(1)) weekRingTimes.add(getCorrectRingDay(calendar, 2));
//        if (t.weekDays.get(2)) weekRingTimes.add(getCorrectRingDay(calendar, 3));
//        if (t.weekDays.get(3)) weekRingTimes.add(getCorrectRingDay(calendar, 4));
//        if (t.weekDays.get(4)) weekRingTimes.add(getCorrectRingDay(calendar, 5));
//        if (t.weekDays.get(5)) weekRingTimes.add(getCorrectRingDay(calendar, 6));
//        if (t.weekDays.get(6)) weekRingTimes.add(getCorrectRingDay(calendar, 7));

            return weekRingTimes;
        }else {
            List<Long>timeOnce = new ArrayList<>();
            timeOnce.add(t.calendar.getTimeInMillis());
            return timeOnce;
        }
    }

    private static long getCorrectRingDay(Calendar calSet, int day) {
        Calendar calToday = Calendar.getInstance();
        int todayDay = calToday.get(Calendar.DAY_OF_WEEK);
        long alarmTime = calSet.getTimeInMillis();

        if (calSet.compareTo(calToday) > 0 && todayDay < day) {

//            calSet.add(Calendar.DAY_OF_MONTH, day - todayDay);
            alarmTime = calSet.getTimeInMillis() + TimeUnit.DAYS.toMillis(day - todayDay);

        } else if (calSet.compareTo(calToday) > 0 && todayDay > day) {

//            calSet.add(Calendar.DAY_OF_MONTH, 7 - setDay);
//            calSet.add(Calendar.DAY_OF_MONTH, day);
            alarmTime = calSet.getTimeInMillis() + TimeUnit.DAYS.toMillis(7 - todayDay + day);

        } else if (calSet.compareTo(calToday) < 0) {

//            calToday.set(Calendar.HOUR_OF_DAY, calSet.get(Calendar.HOUR_OF_DAY));
//            calToday.set(Calendar.MINUTE, calSet.get(Calendar.MINUTE));
            calToday.setTimeInMillis(calSet.getTimeInMillis());

            if (todayDay < day) {

//                calToday.add(Calendar.DAY_OF_MONTH, day - todayDay);
                alarmTime = calToday.getTimeInMillis() + TimeUnit.DAYS.toMillis(day - todayDay);

            } else if (day < todayDay) {

//                calToday.add(Calendar.DAY_OF_MONTH, 7 - todayDay);
//                calToday.add(Calendar.DAY_OF_MONTH, day);
                alarmTime = calToday.getTimeInMillis() + TimeUnit.DAYS.toMillis(7 - todayDay + day);

            }
        }
        return alarmTime;
//
//
//
//        if (System.currentTimeMillis() < alarmTime || todayDay != setDay)
//        {
//            if (setDay > todayDay) { // Alarm is active a later day of the week
//                alarmTime += TimeUnit.MILLISECONDS.convert(setDay - todayDay, TimeUnit.DAYS);
//            } else { // Have to move the alarm time to next week's active day
//                alarmTime += TimeUnit.MILLISECONDS.convert(7 - todayDay, TimeUnit.DAYS);
//                alarmTime += TimeUnit.MILLISECONDS.convert(setDay, TimeUnit.DAYS);
//            }
//        }else {
////            alarmTime = getTimeToNextRing(cal);
//        }
//        return alarmTime;
    }

    public static String getStringOfTimeUntilNextRing(long millisToRing) {
        long seconds = millisToRing / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        minutes -= hours * 60;
        hours -= days * 24;

        if (days == 0 && hours == 0 && minutes == 0) {
            return "less than a minute";
        }

        StringBuilder sbTime = new StringBuilder();
        if (days >= 1) {
            sbTime.append(days);
            sbTime.append(days > 1 ? " days" : " day");
            if (hours >= 1 || minutes >= 1) {
                sbTime.append(", ");
            }
        }
        if (hours >= 1) {
            sbTime.append(hours);
            sbTime.append(hours > 1 ? " hours" : " hour");
            if (minutes >= 1) {
                sbTime.append(", ");
            }
        }
        if (minutes >= 1) {
            sbTime.append(minutes);
            sbTime.append(minutes > 1 ? " minutes" : " minute");
        }

        return sbTime.toString();
    }

    public static Boolean isTaskAheadOfTime(Calendar calGiven) {
        Calendar cal = Calendar.getInstance();
        return calGiven.compareTo(cal) > 0;
    }

    public static Boolean isRepeating(Task t) {
        boolean repeating = false;
        for (int i=0; i<7; i++) {
            if (t.weekDays.get(i)) repeating = true;
        }
        return repeating;
    }

    public static Task findTaskByID(long id){

        for (Task t : UtilsArray_Task.task){
            if (t.getId()==id){
                return t;
            }
        }
        return null;
    }

    public static int findPositionByID(long id){

        for (Task t : UtilsArray_Task.task){
            if (t.getId()==id){
                return UtilsArray_Task.task.indexOf(t);
            }
        }
        return -1;
    }

}