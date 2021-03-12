package com.example.schedulemanager.HomeFrag;

import android.content.Context;
import android.util.Log;

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

    //    static ArrayList<Email> emails;
//    static ArrayList<Task> tasks;
    static Context mContext;
    static Calendar calToday = Calendar.getInstance();


    public static void initAllArray(Context context) {

        mContext = context;

        allItemsArrayList = new ArrayList<>();

//        EmailsharedPreferences = context.getSharedPreferences("Email.db",Context.MODE_PRIVATE);
//        sharedPreferences = context.getSharedPreferences("Notes.db",Context.MODE_PRIVATE);
//        TasksharedPreferences = context.getSharedPreferences("Task.db",Context.MODE_PRIVATE);


//        emails = GsonConverter.jsonToEmail(EmailsharedPreferences.getString(EMAIL_ARRAY_DB, null));;
//        tasks = GsonConverter.jsonToTask(TasksharedPreferences.getString(TASK_ARRAY_DB, null));
//        notes = GsonConverter.jsonToArray(sharedPreferences.getString(NOTES_ARRAY_DB, null));

//        emails = UtilsArray_Email.getMail();
//        tasks = UtilsArray_Task.getTask();
//        notes = UtilsArraylist.getNote();


//        if( UtilsArray_Email.getMail() == null|| UtilsArray_Email.getMail().size() == 0){
////            emails = new ArrayList<>();
//
//            Email e = new Email("patelvatsalb21@gmail.com","patelvatsalb21@gmail.com","subject","message",cal,false);
//            emails.add(e);
//        }
//        if(tasks == null|| tasks.size() == 0){
//            tasks = new ArrayList<>();
//
//            Task t = new Task(cal.getTimeInMillis(),"Sample Task",cal, UtilsArray_Task.category.get(0).LottieRes,false,false,false,false,false,false,false);
//            tasks.add(t);
//        }

//        if(notes == null||notes.size() == 0){
//            notes = new ArrayList<>();
//
//            Notes n = new Notes((long)1,"this is sample title", "this is sample desc",false, cal, false);
//            notes.add(n);
//
//        }

        if (allItemsArrayList == null || allItemsArrayList.isEmpty()) {
//            Calendar cal = Calendar.getInstance();
            allItemsArrayList = new ArrayList<>();
        }

        if (UtilsArray_Email.getMail() != null && !UtilsArray_Email.getMail().isEmpty()) {

            for (Email e : UtilsArray_Email.getMail()) {

//            Email e1 = new Email(e.id,e.From,e.To,e.Subject,e.Message,e.cal,e.Scheduled);

                Log.d("EMAILADDING", String.valueOf(e.From));
                allItemsArrayList.add(new All_In_One(e));
            }
        }

        if (UtilsArray_Task.getTask() != null && !UtilsArray_Task.getTask().isEmpty()) {
            for (Task t : UtilsArray_Task.getTask()) {

//            Task t1 = new Task(t.id,t.Title,t.Desc,t.calendar,t.BeforeTime);

                Log.d("EMAILADDING", String.valueOf(t.LottieFileRes));
                allItemsArrayList.add(new All_In_One(t));
            }
        }
//        for (Notes n:notes) {
//
////            Notes n1 = new Notes(n.id,n.title,n.desc,n.priority,n.calendar,n.EngagedAlarm);
//
//            if (n.EngagedAlarm) {
//                Log.d("EMAILADDING",String.valueOf(n.title));
//                allItemsArrayList.add(new All_In_One(n));
//            }
//        }
//        All_Rec_Adapter adapter = new All_Rec_Adapter(context,getAllItemsArrayList());
//        adapter.notifyDataSetChanged();


//            Task t = new Task(System.currentTimeMillis(),"Explore App",cal,UtilsArray_Task.category.get(0).LottieRes,false,false,false,false,false,false,false);
//            allItemsArrayList.add(new All_In_One(t));
//            Task t2 = new Task(System.currentTimeMillis(),"Explore App",cal,UtilsArray_Task.category.get(0).LottieRes,false,false,false,false,false,false,false);
//            allItemsArrayList.add(new All_In_One(t2));
//            Task t1 = new Task(System.currentTimeMillis(),"Explore App",cal,UtilsArray_Task.category.get(0).LottieRes,false,false,false,false,false,false,false);
//            allItemsArrayList.add(new All_In_One(t1));

        if (allItemsArrayList != null && !allItemsArrayList.isEmpty()) {

            Collections.sort(allItemsArrayList, new Comparator<All_In_One>() {
                @Override
                public int compare(All_In_One a1, All_In_One a2) {

//                if (a1.notesItem!=null&&a2.notesItem!=null){
//                    return a1.notesItem.calendar.compareTo(a2.notesItem.calendar);
//                }else
                    if (a1.taskItem != null && a2.taskItem != null) {
                        return a1.taskItem.calendar.compareTo(a2.taskItem.calendar);
                    } else if (a1.emailItem != null && a2.emailItem != null) {
                        return a1.emailItem.cal.compareTo(a2.emailItem.cal);
//                }else  if (a1.notesItem!=null&&a2.taskItem!=null){
//                    return a1.notesItem.calendar.compareTo(a2.taskItem.calendar);
//                }else  if (a1.notesItem!=null&&a2.emailItem!=null){
//                    return a1.notesItem.calendar.compareTo(a2.emailItem.cal);
//                }else  if (a1.taskItem!=null&&a2.notesItem!=null){
//                    return a1.taskItem.calendar.compareTo(a2.notesItem.calendar);
                    } else if (a1.taskItem != null && a2.emailItem != null) {
                        return a1.taskItem.calendar.compareTo(a2.emailItem.cal);
//                }else  if (a1.emailItem!=null&&a2.notesItem!=null){
//                    return a1.emailItem.cal.compareTo(a2.notesItem.calendar);
                    } else if (a1.emailItem != null && a2.taskItem != null) {
                        return a1.emailItem.cal.compareTo(a2.taskItem.calendar);
                    } else return 1;
                }
            });

            Log.d("ALLINONEARRAY", String.valueOf(allItemsArrayList.size()));
        }
//        All_Rec_Adapter adapter1 = new All_Rec_Adapter(context, UtilsArray_All.getAllItemsArrayList());
//        adapter1.notifyDataSetChanged();
    }


    public static ArrayList<All_In_One> getAllItemsArrayList() {
        return allItemsArrayList;
    }

    public static void setAllItemsArrayList(ArrayList<All_In_One> allItemsArrayList) {
        UtilsArray_All.allItemsArrayList = allItemsArrayList;
    }

    public static ArrayList<Section> getSectionsArraylist() {
        return sectionsArraylist;
    }

    public static void setSectionsArraylist(ArrayList<Section> sectionsArraylist) {
        UtilsArray_All.sectionsArraylist = sectionsArraylist;
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

//            Email e1 = new Email(e.id,e.From,e.To,e.Subject,e.Message,e.cal,e.Scheduled);

                Log.d("EMAILADDING", String.valueOf(e.From));
                allItemsArrayList.add(new All_In_One(e));
            }
        }

        if (UtilsArray_Task.getTask() != null && !UtilsArray_Task.getTask().isEmpty()) {
            for (Task t : UtilsArray_Task.getTask()) {

//            Task t1 = new Task(t.id,t.Title,t.Desc,t.calendar,t.BeforeTime);

                Log.d("EMAILADDING", String.valueOf(t.Title));
                allItemsArrayList.add(new All_In_One(t));
            }
        }
//        for (Notes n:notes) {
//
////            Notes n1 = new Notes(n.id,n.title,n.desc,n.priority,n.calendar,n.EngagedAlarm);
//
//            if (n.EngagedAlarm) {
//                Log.d("EMAILADDING",String.valueOf(n.title));
//                allItemsArrayList.add(new All_In_One(n));
//            }
//        }
//        All_Rec_Adapter adapter = new All_Rec_Adapter(mContext,getAllItemsArrayList());
//        adapter.notifyDataSetChanged();

        if (allItemsArrayList == null || allItemsArrayList.isEmpty()) {
//            Calendar cal = Calendar.getInstance();
            allItemsArrayList = new ArrayList<>();

//            Task t = new Task(System.currentTimeMillis(),"Explore App",cal,UtilsArray_Task.category.get(0).LottieRes,false,false,false,false,false,false,false);
//            allItemsArrayList.add(new All_In_One(t));
//            Task t2 = new Task(System.currentTimeMillis(),"Explore App",cal,UtilsArray_Task.category.get(0).LottieRes,false,false,false,false,false,false,false);
//            allItemsArrayList.add(new All_In_One(t2));
//            Task t1 = new Task(System.currentTimeMillis(),"Explore App",cal,UtilsArray_Task.category.get(0).LottieRes,false,false,false,false,false,false,false);
//            allItemsArrayList.add(new All_In_One(t1));

        }

        if (!allItemsArrayList.isEmpty()) {
            Collections.sort(allItemsArrayList, new Comparator<All_In_One>() {
                @Override
                public int compare(All_In_One a1, All_In_One a2) {

//                if (a1.notesItem!=null&&a2.notesItem!=null){
//                    return a1.notesItem.calendar.compareTo(a2.notesItem.calendar);
//                }else
                    if (a1.taskItem != null && a2.taskItem != null) {
                        return a1.taskItem.calendar.compareTo(a2.taskItem.calendar);
                    } else if (a1.emailItem != null && a2.emailItem != null) {
                        return a1.emailItem.cal.compareTo(a2.emailItem.cal);
//                }else  if (a1.notesItem!=null&&a2.taskItem!=null){
//                    return a1.notesItem.calendar.compareTo(a2.taskItem.calendar);
//                }else  if (a1.notesItem!=null&&a2.emailItem!=null){
//                    return a1.notesItem.calendar.compareTo(a2.emailItem.cal);
//                }else  if (a1.taskItem!=null&&a2.notesItem!=null){
//                    return a1.taskItem.calendar.compareTo(a2.notesItem.calendar);
                    } else if (a1.taskItem != null && a2.emailItem != null) {
                        return a1.taskItem.calendar.compareTo(a2.emailItem.cal);
//                }else  if (a1.emailItem!=null&&a2.notesItem!=null){
//                    return a1.emailItem.cal.compareTo(a2.notesItem.calendar);
                    } else if (a1.emailItem != null && a2.taskItem != null) {
                        return a1.emailItem.cal.compareTo(a2.taskItem.calendar);
                    } else return 1;
                }
            });
        }
//        All_Rec_Adapter adapter1 = new All_Rec_Adapter(mContext, UtilsArray_All.getAllItemsArrayList());
//        adapter1.notifyDataSetChanged();
    }

    public static void ReloadCategoryItems() {
        Log.e("SECTIONSARRAYLIST", "CALLED");
        calToday = Calendar.getInstance();
        ItemsSortForAll();

        Section today = new Section();
        today.Head = "Today";
//        Section tomorrow = new Section();
//        tomorrow.Head = "Tomorrow";
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

                            if (item.taskItem.calendar.get(Calendar.DATE) == calToday.get(Calendar.DATE)) {
                                today.items.add(item);
//                            } else if (item.taskItem.calendar.get(Calendar.DAY_OF_MONTH) == calTomorrow.get(Calendar.DAY_OF_MONTH)) {
//                                tomorrow.items.add(item);
                            } else if (item.taskItem.calendar.get(Calendar.WEEK_OF_MONTH) == calToday.get(Calendar.WEEK_OF_MONTH)) {

                                thisWeek.items.add(item);
                            } else if (item.taskItem.calendar.get(Calendar.MONTH) == calToday.get(Calendar.MONTH)) {
                                thisMonth.items.add(item);
                            } else {
                                Others.items.add(item);
                            }
                        } else if (item.taskItem.calendar.compareTo(calToday) < 0 && Task.isRepeating(item.taskItem)) {
//                            ArrayList<Boolean> days = new ArrayList<>(7);
//                            days.add(item.taskItem.SUN);
//                            days.add(item.taskItem.MON);
//                            days.add(item.taskItem.TUE);
//                            days.add(item.taskItem.WED);
//                            days.add(item.taskItem.THU);
//                            days.add(item.taskItem.FRI);
//                            days.add(item.taskItem.SAT);

//                            int i = 0;
//                            boolean tAdded = false;
//                            for (Boolean b : days) {
//
//                                if (b) {
//                                    if (calToday.get(Calendar.DAY_OF_WEEK) + 1 == i) {
//                                        tomorrow.items.add(item);
//                                        tAdded = true;
//                                        break;
//                                    }
//                                }
//                                i++;
//                            }
//                            if (!tAdded) {
                                thisWeek.items.add(item);
//                            }

                        } else Old.items.add(item);
                    }else Old.items.add(item);

                } else {

                    if (item.emailItem.cal.compareTo(calToday) > 0 && item.emailItem.Scheduled) {

                        if (item.emailItem.cal.get(Calendar.DATE) == calToday.get(Calendar.DATE)) {
                            today.items.add(item);
//                        } else if (item.emailItem.cal.get(Calendar.DATE) == calTomorrow.get(Calendar.DATE)) {
//                            tomorrow.items.add(item);

                        } else if (item.emailItem.cal.get(Calendar.WEEK_OF_MONTH) == calToday.get(Calendar.WEEK_OF_MONTH)) {

                            thisWeek.items.add(item);
                        } else if (item.emailItem.cal.get(Calendar.MONTH) == calToday.get(Calendar.MONTH)) {
                            thisMonth.items.add(item);
                        } else {
                            Others.items.add(item);
                        }
                    }else if (item.emailItem.cal.compareTo(calToday) < 0 && item.emailItem.Scheduled){
                        today.items.add(item);
                    }
                    else Old.items.add(item);
                }
            }
        }

        sectionsArraylist = new ArrayList<>();
        if (today.items != null && !today.items.isEmpty()){
            ReloadSections(today);
            sectionsArraylist.add(today);
        }
//        if (tomorrow.items != null && !tomorrow.items.isEmpty()){
//            ReloadSections(tomorrow);
//            sectionsArraylist.add(tomorrow);
//        }
        if (thisWeek.items != null && !thisWeek.items.isEmpty()) {
            ReloadSections(thisWeek);
            sectionsArraylist.add(thisWeek);
        }
        if (thisMonth.items != null && !thisMonth.items.isEmpty()){
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
                } else return 1;
            }
        });
    }
}


