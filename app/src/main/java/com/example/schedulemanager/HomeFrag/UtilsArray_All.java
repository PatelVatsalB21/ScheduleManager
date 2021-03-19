package com.example.schedulemanager.HomeFrag;

import android.content.Context;

import com.example.schedulemanager.Task.Task;
import com.example.schedulemanager.Task.UtilsArray_Task;
import com.example.schedulemanager.email.Email;
import com.example.schedulemanager.email.UtilsArray_Email;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class UtilsArray_All {

    public static ArrayList<Section> sectionsArraylist = new ArrayList<>();
    public static ArrayList<All_In_One> allItemsArrayList = new ArrayList<>();
    static Context mContext;
    static Calendar calToday = Calendar.getInstance();


    public static void initAllArray(Context context) {

        mContext = context;

        allItemsArrayList = new ArrayList<>();

        if (allItemsArrayList == null || allItemsArrayList.isEmpty()) {
            allItemsArrayList = new ArrayList<>();
        }

        if (UtilsArray_Email.getMail() != null && !UtilsArray_Email.getMail().isEmpty()) {

            for (Email e : UtilsArray_Email.getMail()) {
                allItemsArrayList.add(new All_In_One(e));
            }
        }

        if (UtilsArray_Task.getTask() != null && !UtilsArray_Task.getTask().isEmpty()) {
            for (Task t : UtilsArray_Task.getTask()) {
                allItemsArrayList.add(new All_In_One(t));
            }
        }

        if (allItemsArrayList != null && !allItemsArrayList.isEmpty()) {

            Collections.sort(allItemsArrayList, (a1, a2) -> {
                if (a1.taskItem != null && a2.taskItem != null) {
                    return a1.taskItem.calendar.compareTo(a2.taskItem.calendar);
                } else if (a1.emailItem != null && a2.emailItem != null) {
                    return a1.emailItem.cal.compareTo(a2.emailItem.cal);
                } else if (a1.taskItem != null && a2.emailItem != null) {
                    return a1.taskItem.calendar.compareTo(a2.emailItem.cal);
                } else if (a1.emailItem != null && a2.taskItem != null) {
                    return a1.emailItem.cal.compareTo(a2.taskItem.calendar);
                } else {
                    return 1;
                }
            });
        }
    }

    public static ArrayList<Section> getSectionsArraylist() {
        return sectionsArraylist;
    }

    public static void ItemsSortForAll() {
        ArrayList<Boolean> b = new ArrayList<>(7);
        b.add(false);
        b.add(false);
        b.add(false);
        b.add(false);
        b.add(false);
        b.add(false);
        b.add(false);
        allItemsArrayList.clear();

        if (UtilsArray_Email.getMail() != null && !UtilsArray_Email.getMail().isEmpty()) {
            for (Email e : UtilsArray_Email.getMail()) {
                allItemsArrayList.add(new All_In_One(e));
            }
        }
        if (UtilsArray_Task.getTask() != null && !UtilsArray_Task.getTask().isEmpty()) {
            for (Task t : UtilsArray_Task.getTask()) {
                allItemsArrayList.add(new All_In_One(t));
            }
        }
        if (allItemsArrayList == null || allItemsArrayList.isEmpty()) {
            allItemsArrayList = new ArrayList<>();
        }
        if (!allItemsArrayList.isEmpty()) {
            Collections.sort(allItemsArrayList, (a1, a2) -> {
                if (a1.taskItem != null && a2.taskItem != null) {
                    return a1.taskItem.calendar.compareTo(a2.taskItem.calendar);
                } else if (a1.emailItem != null && a2.emailItem != null) {
                    return a1.emailItem.cal.compareTo(a2.emailItem.cal);
                } else if (a1.taskItem != null && a2.emailItem != null) {
                    return a1.taskItem.calendar.compareTo(a2.emailItem.cal);
                } else if (a1.emailItem != null && a2.taskItem != null) {
                    return a1.emailItem.cal.compareTo(a2.taskItem.calendar);
                } else {
                    return 1;
                }
            });
        }
    }

    public static void ReloadCategoryItems() {
        calToday = Calendar.getInstance();
        ItemsSortForAll();
        Section today = new Section();
        today.Head = "Today";
        Section thisWeek = new Section();
        thisWeek.Head = "This Week";
        Section thisMonth = new Section();
        thisMonth.Head = "This Month";
        Section Others = new Section();
        Others.Head = "Upcoming";
        Section Old = new Section();
        Old.Head = "Completed";

        if (allItemsArrayList != null && !allItemsArrayList.isEmpty()) {
            for (All_In_One item : UtilsArray_All.allItemsArrayList) {
                if (item.taskItem != null) {
                    if (!item.taskItem.isComplete) {
                        if (item.taskItem.calendar.compareTo(calToday) > 0) {
                            if (item.taskItem.calendar.get(Calendar.DATE) == calToday.get(
                                    Calendar.DATE)) {
                                today.items.add(item);
                            } else if (item.taskItem.calendar.get(Calendar.WEEK_OF_MONTH)
                                    == calToday.get(Calendar.WEEK_OF_MONTH)) {
                                thisWeek.items.add(item);
                            } else if (item.taskItem.calendar.get(Calendar.MONTH) == calToday.get(
                                    Calendar.MONTH)) {
                                thisMonth.items.add(item);
                            } else {
                                Others.items.add(item);
                            }
                        } else if (item.taskItem.calendar.compareTo(calToday) < 0
                                && Task.isRepeating(item.taskItem)) {
                            thisWeek.items.add(item);
                        } else {
                            Old.items.add(item);
                        }
                    } else {
                        Old.items.add(item);
                    }
                } else {
                    if (item.emailItem.cal.compareTo(calToday) > 0 && item.emailItem.Scheduled) {
                        if (item.emailItem.cal.get(Calendar.DATE) == calToday.get(Calendar.DATE)) {
                            today.items.add(item);
                        } else if (item.emailItem.cal.get(Calendar.WEEK_OF_MONTH) == calToday.get(
                                Calendar.WEEK_OF_MONTH)) {
                            thisWeek.items.add(item);
                        } else if (item.emailItem.cal.get(Calendar.MONTH) == calToday.get(
                                Calendar.MONTH)) {
                            thisMonth.items.add(item);
                        } else {
                            Others.items.add(item);
                        }
                    } else if (item.emailItem.cal.compareTo(calToday) < 0
                            && item.emailItem.Scheduled) {
                        today.items.add(item);
                    } else {
                        Old.items.add(item);
                    }
                }
            }
        }
        sectionsArraylist = new ArrayList<>();
        if (today.items != null && !today.items.isEmpty()) {
            ReloadSections(today);
            sectionsArraylist.add(today);
        }
        if (thisWeek.items != null && !thisWeek.items.isEmpty()) {
            ReloadSections(thisWeek);
            sectionsArraylist.add(thisWeek);
        }
        if (thisMonth.items != null && !thisMonth.items.isEmpty()) {
            ReloadSections(thisMonth);
            sectionsArraylist.add(thisMonth);
        }
        if (Others.items != null && !Others.items.isEmpty()) {
            ReloadSections(Others);
            sectionsArraylist.add(Others);
        }
        if (Old.items != null && !Old.items.isEmpty()) {
            ReloadSections(Old);
            sectionsArraylist.add(Old);
        }
        All_In_One_Main_Adapter adapter = new All_In_One_Main_Adapter(mContext, sectionsArraylist);
        adapter.notifyDataSetChanged();
    }

    public static void ReloadSections(Section section) {
        Collections.sort(section.items, new Comparator<All_In_One>() {
            @Override
            public int compare(All_In_One a1, All_In_One a2) {
                if (a1.taskItem != null && a2.taskItem != null) {
                    return a1.taskItem.calendar.compareTo(a2.taskItem.calendar);
                } else if (a1.emailItem != null && a2.emailItem != null) {
                    return a1.emailItem.cal.compareTo(a2.emailItem.cal);
                } else if (a1.taskItem != null && a2.emailItem != null) {
                    return a1.taskItem.calendar.compareTo(a2.emailItem.cal);
                } else if (a1.emailItem != null && a2.taskItem != null) {
                    return a1.emailItem.cal.compareTo(a2.taskItem.calendar);
                } else {
                    return 1;
                }
            }
        });
    }
}


