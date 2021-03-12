package com.example.schedulemanager.Widget;//package com.example.firebase.Widget;
//
//import android.content.Context;
//import android.content.Intent;
//import android.widget.RemoteViews;
//import android.widget.RemoteViewsService;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.firebase.HomeFrag.All_In_One;
//import com.example.firebase.HomeFrag.All_In_One_Main_Adapter;
//import com.example.firebase.HomeFrag.Section;
//import com.example.firebase.HomeFrag.UtilsArray_All;
//import com.example.firebase.R;
//import com.example.firebase.Task.UtilsArray_Task;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//
//public class WidgetService extends RemoteViewsService {
//
//    @Override
//    public RemoteViewsFactory onGetViewFactory(Intent intent) {
//        return new WidgetServiceItemFactory(getApplicationContext());
//    }
//
//    class WidgetServiceItemFactory implements RemoteViewsFactory{
//        private Context context;
//        Calendar calToday = Calendar.getInstance();
//        Calendar calTomorrow = Calendar.getInstance();
//        ArrayList<Section> sectionArrayList = new ArrayList<>();
//        RecyclerView rec_view;
//
//        public WidgetServiceItemFactory(Context c) {
//            context = c;
//        }
//
//        @Override
//        public void onCreate() {
//            calTomorrow.add(Calendar.DAY_OF_YEAR,1);
//            Section today = new Section();
//            today.Head = "Today";
//            Section tomorrow = new Section();
//            tomorrow.Head = "Tomorrow";
//            Section thisWeek = new Section();
//            thisWeek.Head = "This Week";
//            Section thisMonth = new Section();
//            thisMonth.Head = "This Month";
//            Section Others = new Section();
//            Others.Head="Upcoming";
//
//
//            for (All_In_One item : UtilsArray_All.allItemsArrayList){
//
//                if (item.taskItem != null) {
//
//                    if (item.taskItem.calendar.get(Calendar.DATE) == calToday.get(Calendar.DATE) ) {
//                        today.items.add(item);
//                    } else if(item.taskItem.calendar.get(Calendar.DATE) == calTomorrow.get(Calendar.DATE) ){
//                        tomorrow.items.add(item);
//                    }
//                    else if (item.taskItem.calendar.get(Calendar.WEEK_OF_MONTH) == calToday.get(Calendar.WEEK_OF_MONTH)) {
//
//                        thisWeek.items.add(item);
//                    } else if (item.taskItem.calendar.get(Calendar.MONTH) == calToday.get(Calendar.MONTH)) {
//                        thisMonth.items.add(item);
//                    } else {
//                        Others.items.add(item);
//                    }
//
//                } else {
//                    if (item.emailItem.cal.get(Calendar.DATE) == calToday.get(Calendar.DATE) ) {
//                        today.items.add(item);
//                    } else if(item.emailItem.cal.get(Calendar.DATE) == calTomorrow.get(Calendar.DATE) ){
//                        tomorrow.items.add(item);
//                    }
//                    else if (item.emailItem.cal.get(Calendar.WEEK_OF_MONTH) == calToday.get(Calendar.WEEK_OF_MONTH)) {
//
//                        thisWeek.items.add(item);
//                    } else if (item.emailItem.cal.get(Calendar.MONTH) == calToday.get(Calendar.MONTH)) {
//                        thisMonth.items.add(item);
//                    } else {
//                        Others.items.add(item);
//                    }
//                }
//
//            }
//
//            sectionArrayList = new ArrayList<>();
//            sectionArrayList.add(today);
//            sectionArrayList.add(tomorrow);
//            sectionArrayList.add(thisWeek);
//            sectionArrayList.add(thisMonth);
//            sectionArrayList.add(Others);
//
//
//        }
//
//
//        @Override
//        public void onDataSetChanged() {
//
//        }
//
//        @Override
//        public void onDestroy() {
//
//        }
//
//        @Override
//        public int getCount() {
//            return UtilsArray_All.allItemsArrayList.size();
//        }
//
//        @Override
//        public RemoteViews getViewAt(int i) {
//            RemoteViews views = new RemoteViews(context.getPackageName(),R.id.widget_main_layout_rec_view);
//            final All_In_One_Main_Adapter adapter = new All_In_One_Main_Adapter(context,sectionArrayList);
//            views.setRemoteAdapter(adapter);
//            all_items_rec_view.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
//            adapter.notifyDataSetChanged();
//            return views;
//        }
//
//        @Override
//        public RemoteViews getLoadingView() {
//            return null;
//        }
//
//        @Override
//        public int getViewTypeCount() {
//            return 1;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return 0;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return false;
//        }
//    }
//}
