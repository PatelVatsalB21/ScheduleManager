package com.example.schedulemanager.MainFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.schedulemanager.HomeFrag.All_In_One_Main_Adapter;
import com.example.schedulemanager.HomeFrag.Section;
import com.example.schedulemanager.HomeFrag.UtilsArray_All;
import com.example.schedulemanager.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Fragment_Home extends Fragment {
//    FloatingActionButton fab,fab1,fab2,fab3;
    //    FloatingActionButton fab4;
//    TextView fab1_txt,fab2_txt,fab3_txt;
    //    TextView fab4_txt;
//    Float translation = 100f;
//    OvershootInterpolator interpolator = new OvershootInterpolator();
    //    CardView cardView;
    boolean MenuOpened;
    static RecyclerView all_items_rec_view;
    ImageButton cardView_new_note_btn, cardView_new_email_btn;
    Context mContext;
//    Animation anim_up;
//    Animation anim_down;
//    Animation fab_up;
//    Animation fab_down;
//    ArrayList<Section> sectionArrayList = UtilsArray_All.sectionsArraylist;
    Calendar calToday = Calendar.getInstance();
    Calendar calTomorrow = Calendar.getInstance();
    SwipeRefreshLayout swp_refresh_lay;
    public static RelativeLayout null_View;
    public static All_In_One_Main_Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (mContext == null) {
            mContext = container.getContext();
        }

        calTomorrow.add(Calendar.DAY_OF_YEAR,1);

//
//        fab = view.findViewById(R.id.fab_home_frag);
//        fab1 = view.findViewById(R.id.fab1);
//        fab2 = view.findViewById(R.id.fab2);
//        fab3 = view.findViewById(R.id.fab3);
////        fab4 = view.findViewById(R.id.fab4);
//        fab1_txt = view.findViewById(R.id.fab1_txt_view);
//        fab2_txt = view.findViewById(R.id.fab2_txt_view);
//        fab3_txt = view.findViewById(R.id.fab3_txt_view);
//        fab.bringToFront();
////        fab4_txt = view.findViewById(R.id.fab4_txt_view);
//
//        fab1.setAlpha(0f);
//        fab1.setTranslationY(translation);
//        fab2.setAlpha(0f);
//        fab2.setTranslationY(translation);
//        fab3.setAlpha(0f);
//        fab3.setTranslationY(translation);
////        fab4.setAlpha(0f);
////        fab4.setTranslationY(translation);
//        fab1_txt.setAlpha(0f);
//        fab1_txt.setTranslationY(translation);
//        fab2_txt.setAlpha(0f);
//        fab2_txt.setTranslationY(translation);
//        fab3_txt.setAlpha(0f);
//        fab3_txt.setTranslationY(translation);
////        fab4_txt.setAlpha(0f);
////        fab4_txt.setTranslationY(translation);


//        cardView = view.findViewById(R.id.NewTaskCard);
        MenuOpened = false;
        all_items_rec_view = view.findViewById(R.id.Fragment_Home_Rec_View);
        swp_refresh_lay = view.findViewById(R.id.Fragment_Home_swipe_layout);
        null_View = view.findViewById(R.id.No_items_Home_View_Rel_Layout);
//        cardView_new_note_btn = view.findViewById(R.id.NewTaskCard_new_note);
//        cardView_new_email_btn = view.findViewById(R.id.NewTaskCard_new_email);

//
//        anim_up = AnimationUtils.loadAnimation(mContext, R.anim.pop_down);
//        anim_down = AnimationUtils.loadAnimation(mContext, R.anim.pop_up);
//        fab_up = AnimationUtils.loadAnimation(mContext, R.anim.fab_rotate_up);
//        fab_down = AnimationUtils.loadAnimation(mContext, R.anim.fab_rotate_down);


//        cardView_new_note_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, NewNoteAddDialog.class);
//                startActivity(intent);
//            }
//        });
//
//        cardView_new_email_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, ScheduleEmail.class);
//                startActivity(intent);
//            }
//        });

//        Section today = new Section();
//        today.Head = "Today";
//        Section tomorrow = new Section();
//        tomorrow.Head = "Tomorrow";
//        Section thisWeek = new Section();
//        thisWeek.Head = "This Week";
//        Section thisMonth = new Section();
//        thisMonth.Head = "This Month";
//        Section Others = new Section();
//        Others.Head="Upcoming";



//        for (All_In_One item : UtilsArray_All.allItemsArrayList){
//
////            if (item.notesItem != null) {
////
////                if (item.notesItem.calendar.get(Calendar.DATE) == calToday.get(Calendar.DATE) ) {
////                    today.items.add(item);
////                } else if(item.notesItem.calendar.get(Calendar.DATE) == calTomorrow.get(Calendar.DATE) ){
////                    tomorrow.items.add(item);
////                }
////                else if (item.notesItem.calendar.get(Calendar.WEEK_OF_MONTH) == calToday.get(Calendar.WEEK_OF_MONTH)) {
////
////                    thisWeek.items.add(item);
////                } else if (item.notesItem.calendar.get(Calendar.MONTH) == calToday.get(Calendar.MONTH)) {
////                    thisMonth.items.add(item);
////                } else {
////                    Others.items.add(item);
////                }
//
////            } else
//            if (item.taskItem != null) {
//
//                if (item.taskItem.calendar.get(Calendar.DATE) == calToday.get(Calendar.DATE) ) {
//                    today.items.add(item);
//                } else if(item.taskItem.calendar.get(Calendar.DATE) == calTomorrow.get(Calendar.DATE) ){
//                    tomorrow.items.add(item);
//                }
//                else if (item.taskItem.calendar.get(Calendar.WEEK_OF_MONTH) == calToday.get(Calendar.WEEK_OF_MONTH)) {
//
//                    thisWeek.items.add(item);
//                } else if (item.taskItem.calendar.get(Calendar.MONTH) == calToday.get(Calendar.MONTH)) {
//                    thisMonth.items.add(item);
//                } else {
//                    Others.items.add(item);
//                }
//
//            } else {
//                if (item.emailItem.cal.get(Calendar.DATE) == calToday.get(Calendar.DATE) ) {
//                    today.items.add(item);
//                } else if(item.emailItem.cal.get(Calendar.DATE) == calTomorrow.get(Calendar.DATE) ){
//                    tomorrow.items.add(item);
//                }
//                else if (item.emailItem.cal.get(Calendar.WEEK_OF_MONTH) == calToday.get(Calendar.WEEK_OF_MONTH)) {
//
//                    thisWeek.items.add(item);
//                } else if (item.emailItem.cal.get(Calendar.MONTH) == calToday.get(Calendar.MONTH)) {
//                    thisMonth.items.add(item);
//                } else {
//                    Others.items.add(item);
//                }
//            }
//
//
////            if ((item.notesItem.calendar!=null&&item.notesItem.calendar.after(calToday.getTimeInMillis()))||(item.taskItem.calendar!=null&&item.taskItem.calendar.after(calToday.getTimeInMillis()))||(item.emailItem.cal!=null&&item.emailItem.cal.after(calToday.getTimeInMillis())))
////            {
////                if ((item.notesItem.calendar!=null&&item.notesItem.calendar.get(Calendar.DATE) == calToday.get(Calendar.DATE) )|| (item.taskItem.calendar!=null&&item.taskItem.calendar.get(Calendar.DATE) == calToday.get(Calendar.DATE)) || (item.emailItem.cal!=null&&item.emailItem.cal.get(Calendar.DATE) == calToday.get(Calendar.DATE))) {
////                    today.items.add(item);
////                } else if(item.notesItem.calendar.get(Calendar.DATE) == calTomorrow.get(Calendar.DATE) || item.taskItem.calendar.get(Calendar.DATE) == calTomorrow.get(Calendar.DATE) || item.emailItem.cal.get(Calendar.DATE) == calTomorrow.get(Calendar.DATE)){
////                    tomorrow.items.add(item);
////                }
////                else if ((item.notesItem.calendar!=null&&item.notesItem.calendar.get(Calendar.WEEK_OF_MONTH) == calToday.get(Calendar.WEEK_OF_MONTH) )|| (item.taskItem.calendar!=null&&item.taskItem.calendar.get(Calendar.WEEK_OF_MONTH) == calToday.get(Calendar.WEEK_OF_MONTH) )|| (item.emailItem.cal!=null&&item.emailItem.cal.get(Calendar.WEEK_OF_MONTH) == calToday.get(Calendar.WEEK_OF_MONTH))) {
////
////                    thisWeek.items.add(item);
////                } else if ((item.notesItem.calendar!=null&&item.notesItem.calendar.get(Calendar.MONTH) == calToday.get(Calendar.MONTH)) || (item.taskItem.calendar!=null&&item.taskItem.calendar.get(Calendar.MONTH) == calToday.get(Calendar.MONTH)) || (item.emailItem.cal!=null&&item.emailItem.cal.get(Calendar.MONTH) == calToday.get(Calendar.MONTH))) {
////                    thisMonth.items.add(item);
////                } else {
////                    Others.items.add(item);
////                }
////
////            }
//
//        }

//        sectionArrayList = new ArrayList<>();
//        sectionArrayList.add(today);
//        sectionArrayList.add(tomorrow);
//        sectionArrayList.add(thisWeek);
//        sectionArrayList.add(thisMonth);
//        sectionArrayList.add(Others);
//
//        UtilsArray_All.sectionsArraylist = sectionArrayList;

        UtilsArray_All.ReloadCategoryItems();

        adapter = new All_In_One_Main_Adapter(mContext, UtilsArray_All.getSectionsArraylist());
        all_items_rec_view.setAdapter(adapter);
        all_items_rec_view.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        adapter.notifyDataSetChanged();

        swp_refresh_lay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swp_refresh_lay.isRefreshing()) {
                    adapter.notifyDataSetChanged();
                    swp_refresh_lay.setRefreshing(false);
                }
            }

        });




//        fab.setVisibility(View.VISIBLE);
////        cardView.setVisibility(View.GONE);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (!MenuOpened) {
//                    revealFAB(view);
////                    cardView.startAnimation(anim_up);
////                    cardView.setVisibility(View.VISIBLE);
//                    MenuOpened = true;
//                    fab.startAnimation(fab_up);
//                    fab1.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                    fab2.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                    fab3.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
////                    fab4.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                    fab1_txt.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                    fab2_txt.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                    fab3_txt.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
////                    fab4_txt.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//
//                } else {
//                    hideFAB(view);
////                    cardView.startAnimation(anim_down);
////                    cardView.setVisibility(View.GONE);
//                    MenuOpened = false;
//                    fab.startAnimation(fab_down);
//                    fab1.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//                    fab2.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//                    fab3.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
////                    fab4.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//                    fab1_txt.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//                    fab2_txt.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//                    fab3_txt.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
////                    fab4_txt.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//
//
//
//                }
//
//
//            }
//        });

//        HomePage.fab_main_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, "FragHome", Toast.LENGTH_SHORT).show();
//            }
//        });


        all_items_rec_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    HomePage.fab_main_home.hide();
                } else {
                    if (!HomePage.actionBarOn) {
                        HomePage.fab_main_home.show();
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return view;
    }

//    private void revealFAB(View v) {
//        View view2 = v.findViewById(R.id.Fragment_Home_swipe_layout);
//        View view1 = v.findViewById(R.id.revealView);
//        int cx = (fab.getLeft() + fab.getRight()) / 2;
//        int cy = (fab.getTop() + fab.getBottom()) / 2;
//        int cxx = view1.getWidth();
//        int cyy = view1.getHeight();
//        float finalRadius = (float) Math.hypot(cxx, cyy);
//        Animator anim = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            anim = ViewAnimationUtils.createCircularReveal(view1, cx, cy, 0, finalRadius);
//        }
//
//        view2.animate().alpha(0f).setInterpolator(interpolator).setDuration(500).start();
//        anim.start();
//        view1.setVisibility(View.VISIBLE);
//
//    }
//
//    private void hideFAB(View v) {
//        final View view2 = v.findViewById(R.id.Fragment_Home_swipe_layout);
//        final View view1 = v.findViewById(R.id.revealView);
//        int cx = (fab.getLeft() + fab.getRight()) / 2;
//        int cy = (fab.getTop() + fab.getBottom()) / 2;
//        int cxx = view1.getWidth();
//        int cyy = view1.getHeight();
//        float initialRadius = (float) Math.hypot(cxx, cyy);
//        Animator anim = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            anim = ViewAnimationUtils.createCircularReveal(view1, cx, cy, initialRadius, 0);
//        }
//        anim.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                view1.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onAnimationStart(Animator animation) {
//                super.onAnimationStart(animation);
//                view2.animate().alpha(1.0f).setInterpolator(interpolator).setDuration(1000).start();
//            }
//
//        });
//        anim.start();
//    }

//    public static void allItemsNullViewUpdater(Boolean task,Boolean email){
//        if (task&&email){
//            null_View.setVisibility(View.VISIBLE);
//            all_items_rec_view.setVisibility(View.INVISIBLE);
//        }else {
//            null_View.setVisibility(View.INVISIBLE);
//            all_items_rec_view.setVisibility(View.VISIBLE);
//        }
//    }

//    public static void nullViewCreate(){
//        if (null_View.getVisibility() == View.VISIBLE) {
//            null_View.setVisibility(View.VISIBLE);
//            all_items_rec_view.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    public static void nullViewFinish(){
//        if (null_View.getVisibility()== View.INVISIBLE) {
//            null_View.setVisibility(View.INVISIBLE);
//            all_items_rec_view.setVisibility(View.VISIBLE);
//        }
//    }

    public static void allItemsNullViewUpdater(ArrayList<Section> sections){
//        Log.e("HOMEFRAG", "NULLVIEWUPDATER CALLED");
        int count = 0;
        if (sections!=null && !sections.isEmpty()) {
            for (Section s : sections) {
                if (s.items == null || s.items.size() == 0) {
                    count++;
                }
            }
//            Log.e("HOMEFRAG", String.valueOf(count));

        }else count = 5;
        if (count==5){
            all_items_rec_view.setVisibility(View.INVISIBLE);
            null_View.setVisibility(View.VISIBLE);
        }else {
            all_items_rec_view.setVisibility(View.VISIBLE);
            null_View.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        UtilsArray_All.ReloadCategoryItems();
        All_In_One_Main_Adapter all_in_one_main_adapter = new All_In_One_Main_Adapter(mContext,UtilsArray_All.getSectionsArraylist());
        adapter.notifyDataSetChanged();
        all_items_rec_view.setAdapter(all_in_one_main_adapter);
        all_items_rec_view.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));

    }
}










