package com.example.schedulemanager.MainFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    boolean MenuOpened;
    static RecyclerView all_items_rec_view;
    Context mContext;
    Calendar calTomorrow = Calendar.getInstance();
    SwipeRefreshLayout swp_refresh_lay;
    public static RelativeLayout null_View;
    public static All_In_One_Main_Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (mContext == null) {
            mContext = container.getContext();
        }
        calTomorrow.add(Calendar.DAY_OF_YEAR, 1);

        MenuOpened = false;
        all_items_rec_view = view.findViewById(R.id.Fragment_Home_Rec_View);
        swp_refresh_lay = view.findViewById(R.id.Fragment_Home_swipe_layout);
        null_View = view.findViewById(R.id.No_items_Home_View_Rel_Layout);
        UtilsArray_All.ReloadCategoryItems();
        adapter = new All_In_One_Main_Adapter(mContext, UtilsArray_All.getSectionsArraylist());
        all_items_rec_view.setAdapter(adapter);
        all_items_rec_view.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        adapter.notifyDataSetChanged();

        swp_refresh_lay.setOnRefreshListener(() -> {
            if (swp_refresh_lay.isRefreshing()) {
                adapter.notifyDataSetChanged();
                swp_refresh_lay.setRefreshing(false);
            }
        });
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

    public static void allItemsNullViewUpdater(ArrayList<Section> sections) {
        int count = 0;
        if (sections != null && !sections.isEmpty()) {
            for (Section s : sections) {
                if (s.items == null || s.items.size() == 0) {
                    count++;
                }
            }
        } else {
            count = 5;
        }
        if (count == 5) {
            all_items_rec_view.setVisibility(View.INVISIBLE);
            null_View.setVisibility(View.VISIBLE);
        } else {
            all_items_rec_view.setVisibility(View.VISIBLE);
            null_View.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        UtilsArray_All.ReloadCategoryItems();
        All_In_One_Main_Adapter all_in_one_main_adapter = new All_In_One_Main_Adapter(mContext,
                UtilsArray_All.getSectionsArraylist());
        adapter.notifyDataSetChanged();
        all_items_rec_view.setAdapter(all_in_one_main_adapter);
        all_items_rec_view.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
    }
}