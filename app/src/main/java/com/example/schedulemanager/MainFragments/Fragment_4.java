package com.example.schedulemanager.MainFragments;

import android.content.Context;
import android.content.Intent;
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

import com.example.schedulemanager.R;
import com.example.schedulemanager.email.EmailService;
import com.example.schedulemanager.email.Email_rec_Adapter;
import com.example.schedulemanager.email.UtilsArray_Email;

public class Fragment_4 extends Fragment {

    Context context;
    static RecyclerView email_rec_view;
    public static SwipeRefreshLayout swp_layout;
    static RelativeLayout null_view, no_App_Password;
    public static Email_rec_Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_4, container, false);
        if (context == null) {
            context = container.getContext();
        }
        email_rec_view = view.findViewById(R.id.email_rec_view);
        swp_layout = view.findViewById(R.id.email_list_swipe_layout);
        null_view = view.findViewById(R.id.No_Email_View_Rel_Layout);
        UtilsArray_Email.initMail(context);
        checkPendingEmails(context);

        Fragment_2.SpacesItemDecoration spacesItemDecoration = new Fragment_2.SpacesItemDecoration(
                15);
        adapter = new Email_rec_Adapter(UtilsArray_Email.getMail());
        email_rec_view.setAdapter(adapter);
        email_rec_view.addItemDecoration(spacesItemDecoration);
        email_rec_view.setLayoutManager(new LinearLayoutManager(context));
        adapter.notifyDataSetChanged();

        swp_layout.setOnRefreshListener(() -> {
            if (swp_layout.isRefreshing()) {
                adapter.notifyDataSetChanged();
                swp_layout.setRefreshing(false);
                checkPendingEmails(context);
            }
        });

        email_rec_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    public static void emailNullViewUpdater(Email_rec_Adapter adapter) {
        if (UtilsArray_Email.mail == null || UtilsArray_Email.getMail().size() == 0
                || adapter.getItemCount() == 0) {
            null_view.setVisibility(View.VISIBLE);
            email_rec_view.setVisibility(View.INVISIBLE);
        } else {
            null_view.setVisibility(View.INVISIBLE);
            email_rec_view.setVisibility(View.VISIBLE);
        }
    }

    public static void emailNullViewFinish() {
        if (null_view.getVisibility() == View.VISIBLE) {
            null_view.setVisibility(View.INVISIBLE);
            email_rec_view.setVisibility(View.VISIBLE);
        }
    }


    public static void Frag4ContextHide() {
        swp_layout.setEnabled(false);
    }

    public static void Frag4ContextShow() {
        swp_layout.setEnabled(true);
    }

    public static void checkPendingEmails(Context context) {
        context.startService(new Intent(context, EmailService.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        Email_rec_Adapter adapter1 = new Email_rec_Adapter(UtilsArray_Email.getMail());
        adapter1.notifyDataSetChanged();
    }
}