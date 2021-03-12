package com.example.schedulemanager.email;//package com.example.firebase.email;
//
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.util.Log;
//
//import androidx.work.ExistingWorkPolicy;
//import androidx.work.OneTimeWorkRequest;
//import androidx.work.WorkContinuation;
//import androidx.work.WorkManager;
//import androidx.work.WorkRequest;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//public class WorkRequestList {
//
//    public static List<OneTimeWorkRequest> workRequestList = new ArrayList<>();
//
//    public static void initWorkRequestList() {
//
//    }
//
//    public static void addWorkRequest(OneTimeWorkRequest w) {
//        workRequestList.add(w);
//    }
//
////    public static void sortWorkRequestList() {
////        Collections.sort(workRequestList, new Comparator<OneTimeWorkRequest>() {
////            @Override
////            public int compare(OneTimeWorkRequest w1, OneTimeWorkRequest w2) {
////                if (UtilsArray_Email.mail.get(UtilsArray_Email.getPositionFromUUID(w1.getId())).cal.getTimeInMillis() > UtilsArray_Email.mail.get(UtilsArray_Email.getPositionFromUUID(w2.getId())).cal.getTimeInMillis())
////                    return -1;
////                if (UtilsArray_Email.mail.get(UtilsArray_Email.getPositionFromUUID(w1.getId())).cal.getTimeInMillis() < UtilsArray_Email.mail.get(UtilsArray_Email.getPositionFromUUID(w2.getId())).cal.getTimeInMillis())
////                    return 1;
////                return 0;
////            }
////        });
////
////
////    }
//
//    public static void EnqueueWork() {
//        WorkManager workManager = WorkManager.getInstance();
//        Integer count = 0;
//        String WorkID = "";
//        for (WorkRequest w : workRequestList) {
//
//            if (count == 0) {
//                workManager.enqueueUniqueWork(String.valueOf(UtilsArray_Email.mail.get(UtilsArray_Email.getPositionFromUUID(w.getId())).cal.getTimeInMillis()), ExistingWorkPolicy.APPEND, (OneTimeWorkRequest) w);
//                WorkID = String.valueOf(UtilsArray_Email.mail.get(UtilsArray_Email.getPositionFromUUID(w.getId())).cal.getTimeInMillis());
//                Log.d("InsideEnqueue", String.valueOf(UtilsArray_Email.mail.get(UtilsArray_Email.getPositionFromUUID(w.getId())).getSubject()));
//                Log.d("InsideEnqueue", String.valueOf(w.getId()));
//                count++;
//            } else {
//                workManager.enqueueUniqueWork(WorkID, ExistingWorkPolicy.APPEND, (OneTimeWorkRequest) w);
//                Log.d("InsideEnqueue", String.valueOf(UtilsArray_Email.mail.get(UtilsArray_Email.getPositionFromUUID(w.getId())).Subject));
//                Log.d("InsideEnqueue", String.valueOf(w.getId()));
//                WorkID = String.valueOf(UtilsArray_Email.mail.get(UtilsArray_Email.getPositionFromUUID(w.getId())).cal.getTimeInMillis());
//                count++;
//            }
//        }
//    }
//
//}
