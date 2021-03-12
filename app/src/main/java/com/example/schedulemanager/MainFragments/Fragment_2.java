package com.example.schedulemanager.MainFragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

//import com.example.firebase.HomeFrag.UtilsArray_All;
import com.example.schedulemanager.R;
import com.example.schedulemanager.Task.Task_rec_adapter;
import com.example.schedulemanager.Task.UtilsArray_Task;


public class Fragment_2 extends Fragment {

    static RecyclerView task_rec_view;
    //    public static ExtendedFloatingActionButton fab_task;
    public static SwipeRefreshLayout swp_layout;
    Context mContext;
    static RelativeLayout null_view;
//    RelativeLayout main_layout;
    public static Task_rec_adapter adapter;
//    public static Boolean TaskIsNull = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_2, container, false);

        if (mContext == null) {
            mContext = view.getContext();
        }


        task_rec_view = view.findViewById(R.id.task_rec_view);
        swp_layout = view.findViewById(R.id.Fragment_2_swipe_layout);
//        fab_task = view.findViewById(R.id.fab_task);
        null_view = view.findViewById(R.id.No_Tasks_View_Rel_Layout);


//        UtilsArray_Task.initTask(mContext);
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(10);
        adapter = new Task_rec_adapter(UtilsArray_Task.task);
        task_rec_view.setAdapter(adapter);
        task_rec_view.addItemDecoration(spacesItemDecoration);
        task_rec_view.setLayoutManager(new GridLayoutManager(mContext, 1, RecyclerView.VERTICAL, false));
        adapter.notifyDataSetChanged();





        swp_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (swp_layout.isRefreshing()) {
                    UtilsArray_Task.SortTaskItems();
                    adapter.notifyDataSetChanged();
                    swp_layout.setRefreshing(false);
                }
                //                    UtilsArray_All.ItemChangedForAll();
            }
        });
//        HomePage.fab_main_home.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(mContext, NewTask.class);
//                startActivity(i);
//            }
//        });

        if (UtilsArray_Task.task == null || UtilsArray_Task.task.size() == 0 || adapter.getItemCount() == 0) {
            null_view.setVisibility(View.VISIBLE);
            task_rec_view.setVisibility(View.INVISIBLE);
        } else {
            null_view.setVisibility(View.INVISIBLE);
            task_rec_view.setVisibility(View.VISIBLE);
        }


        task_rec_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    public static void Frag2ContextHide() {
        swp_layout.setEnabled(false);
    }

    public static void Frag2ContextShow() {
        swp_layout.setEnabled(true);
    }

//    public static void taskNullViewUpdater(Task_rec_adapter adapter, Context mContext) {
//        UtilsArray_Task.initTask(mContext);
//        if (UtilsArray_Task.task == null || UtilsArray_Task.task.size() == 0 || adapter.getItemCount() == 0) {
//            null_view.setVisibility(View.VISIBLE);
//            task_rec_view.setVisibility(View.INVISIBLE);
//            TaskIsNull = true;
//            Fragment_Home.allItemsNullViewUpdater(TaskIsNull, Fragment_3.NotesIsNull, Fragment_4.EmailIsNull);
//
//        } else {
//            null_view.setVisibility(View.INVISIBLE);
//            task_rec_view.setVisibility(View.VISIBLE);
//            TaskIsNull = false;
//            Fragment_Home.allItemsNullViewUpdater(TaskIsNull, Fragment_3.NotesIsNull, Fragment_4.EmailIsNull);
//        }
//    }


    public static void taskNullViewCreate() {
        if (null_view.getVisibility() == View.INVISIBLE) {
            null_view.setVisibility(View.VISIBLE);
            task_rec_view.setVisibility(View.INVISIBLE);
//            TaskIsNull = true;
//            Fragment_Home.allItemsNullViewUpdater(TaskIsNull, Fragment_4.EmailIsNull);
        }
    }

    public static void taskNullViewFinish() {
        if (null_view.getVisibility() == View.VISIBLE) {
            null_view.setVisibility(View.INVISIBLE);
            task_rec_view.setVisibility(View.VISIBLE);
//            TaskIsNull = false;
//            Fragment_Home.allItemsNullViewUpdater(TaskIsNull, Fragment_4.EmailIsNull);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        UtilsArray_Task.initTask(mContext);
        Task_rec_adapter adapter1 = new Task_rec_adapter(UtilsArray_Task.task);
        adapter1.notifyDataSetChanged();
        task_rec_view.setAdapter(adapter1);
    }

    public static void TaskReceiverRefresh(Context context){
        UtilsArray_Task.initTask(context);
        Task_rec_adapter adapter1 = new Task_rec_adapter(UtilsArray_Task.task);
        adapter1.notifyDataSetChanged();
        if (task_rec_view!=null) task_rec_view.setAdapter(adapter1);
    }

    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;
        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
            }
        }
    }
}
