package com.example.schedulemanager.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Task {

    public long id;
    public String Title;
    public Calendar calendar;
    public Calendar finishTime;
    public Integer LottieFileRes;
    public Boolean isRepeating;
    public Boolean isComplete;
    public ArrayList<Boolean> weekDays;

    public Task() {
    }

    public Task(long id, String title, Calendar calendar, Integer lottieFileRes,
            Boolean isRepeating, Boolean isComplete, ArrayList<Boolean> weekDays) {
        this.id = id;
        Title = title;
        this.calendar = calendar;
        LottieFileRes = lottieFileRes;
        this.isRepeating = isRepeating;
        this.isComplete = isComplete;
        this.weekDays = weekDays;
    }

    public Integer getLottieFileRes() {
        return LottieFileRes;
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

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public static long getTimeToNextRing(Calendar cal) {
        if ((cal.getTimeInMillis() + 65000) < System.currentTimeMillis()) {
            return System.currentTimeMillis() + 3000;
        }
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
                if (t.weekDays.get(num - 1)) weekRingTimes.add(getCorrectRingDay(calUpdated, num));
            }
            for (int i = 1; i < calDay; i++) {
                num = calDay - i;
                if (t.weekDays.get(num - 1)) weekRingTimes.add(getCorrectRingDay(calUpdated, num));
            }
            return weekRingTimes;
        } else {
            List<Long> timeOnce = new ArrayList<>();
            timeOnce.add(t.calendar.getTimeInMillis());
            return timeOnce;
        }
    }

    private static long getCorrectRingDay(Calendar calSet, int day) {
        Calendar calToday = Calendar.getInstance();
        int todayDay = calToday.get(Calendar.DAY_OF_WEEK);
        long alarmTime = calSet.getTimeInMillis();

        if (calSet.compareTo(calToday) > 0 && todayDay < day) {
            alarmTime = calSet.getTimeInMillis() + TimeUnit.DAYS.toMillis(day - todayDay);
        } else if (calSet.compareTo(calToday) > 0 && todayDay > day) {
            alarmTime = calSet.getTimeInMillis() + TimeUnit.DAYS.toMillis(7 - todayDay + day);
        } else if (calSet.compareTo(calToday) < 0) {
            calToday.setTimeInMillis(calSet.getTimeInMillis());

            if (todayDay < day) {
                alarmTime = calToday.getTimeInMillis() + TimeUnit.DAYS.toMillis(day - todayDay);
            } else if (day < todayDay) {
                alarmTime = calToday.getTimeInMillis() + TimeUnit.DAYS.toMillis(7 - todayDay + day);
            }
        }
        return alarmTime;
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
        for (int i = 0; i < 7; i++) {
            if (t.weekDays.get(i)) {
                repeating = true;
                break;
            }
        }
        return repeating;
    }

    public static Task findTaskByID(long id) {
        for (Task t : UtilsArray_Task.task) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    public static int findPositionByID(long id) {
        for (Task t : UtilsArray_Task.task) {
            if (t.getId() == id) {
                return UtilsArray_Task.task.indexOf(t);
            }
        }
        return -1;
    }
}