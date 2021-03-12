package com.example.schedulemanager.MainFragments;

import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.schedulemanager.R;
//import com.example.firebase.email.EmailList;
import com.example.schedulemanager.Setting.Settings_Main;
import com.example.schedulemanager.email.EmailService;
import com.example.schedulemanager.email.Email_rec_Adapter;
import com.example.schedulemanager.email.UtilsArray_Email;

public class Fragment_4 extends Fragment {

    //    DatePicker datePicker;
//    int year, month, day;
//    Calendar calSet;
    Context context;
//    RecyclerView recyclerView;
//    SwipeRefreshLayout swp_layout;
////    RelativeLayout no_event_view;
//    Button new_event_btn;


    static RecyclerView email_rec_view;
//    public static ExtendedFloatingActionButton fab_email;
    public static SwipeRefreshLayout swp_layout;
//    EditText App_Password;
//    Button Save_Password;
    static RelativeLayout null_view, no_App_Password;
//    public  ImageButton error_btn;
    public static Email_rec_Adapter adapter;
//    public static Boolean EmailIsNull = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//     if(Settings_Main.App_Password != null&&!Settings_Main.App_Password.isEmpty())
//        {
        View view = inflater.inflate(R.layout.fragment_4, container, false);

        if (context == null) {
            context = container.getContext();
        }
        email_rec_view = view.findViewById(R.id.email_rec_view);
        swp_layout = view.findViewById(R.id.email_list_swipe_layout);
//        fab_email = view.findViewById(R.id.fab_email_list);
        null_view = view.findViewById(R.id.No_Email_View_Rel_Layout);
//        error_btn = view.findViewById(R.id.Fragment_4_No_App_Password_Error_btn);


        UtilsArray_Email.initMail(context);
        checkPendingEmails(context);

        Fragment_2.SpacesItemDecoration spacesItemDecoration = new Fragment_2.SpacesItemDecoration(15);
        adapter = new Email_rec_Adapter(UtilsArray_Email.getMail());
        email_rec_view.setAdapter(adapter);
        email_rec_view.addItemDecoration(spacesItemDecoration);
        email_rec_view.setLayoutManager(new LinearLayoutManager(context));

        adapter.notifyDataSetChanged();

        swp_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (swp_layout.isRefreshing()) {
                    adapter.notifyDataSetChanged();
                    swp_layout.setRefreshing(false);
                    checkPendingEmails(context);
                }
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


        if(Settings_Main.App_Password != null&&!Settings_Main.App_Password.isEmpty()) {

//            HomePage.fab_main_home.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(context, ScheduleEmail.class);
//                    startActivity(i);
//                }
//            });
//            error_btn.setVisibility(View.INVISIBLE);

        }else {
//            error_btn.setVisibility(View.VISIBLE);
//            error_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    EmailAppPasswordGuide();
//                }
//            });
//
//            fab_email.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(context, "App Password is not given. Please fill it to use this function.", Toast.LENGTH_LONG).show();
//                }
//            });
//
        }

        return view;
        }

//        }
//     else{
//         View view = inflater.inflate(R.layout.no_app_password, container, false);
//
//         App_Password = view.findViewById(R.id.no_app_password_edit_txt);
//         Save_Password = view.findViewById(R.id.no_app_password_save_btn);
//
//         Save_Password.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 if (!App_Password.getText().toString().isEmpty()){
//                     Settings_Main.App_Password = App_Password.getText().toString();
//                     Settings_Main.SaveSettings();
//                 }
//             }
//         });
//         container.getContext().startActivity(new Intent(container.getContext(),HomePage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK));
//
//         return view;
//     }

    public static void emailNullViewUpdater(Email_rec_Adapter adapter){
        if(UtilsArray_Email.mail==null||UtilsArray_Email.getMail().size()==0||adapter.getItemCount()==0){
            null_view.setVisibility(View.VISIBLE);
            email_rec_view.setVisibility(View.INVISIBLE);
//            EmailIsNull = true;
//            Fragment_Home.allItemsNullViewUpdater(Fragment_2.TaskIsNull, EmailIsNull);
        }else {
            null_view.setVisibility(View.INVISIBLE);
            email_rec_view.setVisibility(View.VISIBLE);
//            EmailIsNull = false;
//            Fragment_Home.allItemsNullViewUpdater(Fragment_2.TaskIsNull, EmailIsNull);
        }
    }

    public static void emailNullViewFinish(){
        if(null_view.getVisibility()==View.VISIBLE){
            null_view.setVisibility(View.INVISIBLE);
            email_rec_view.setVisibility(View.VISIBLE);
//            EmailIsNull = false;
//            Fragment_Home.allItemsNullViewUpdater(Fragment_2.TaskIsNull, EmailIsNull);
        }
    }


    public static void Frag4ContextHide(){
//        fab_email.setVisibility(View.GONE);
        swp_layout.setEnabled(false);
    }

    public static void Frag4ContextShow(){
//        fab_email.setVisibility(View.VISIBLE);
        swp_layout.setEnabled(true);
    }

    public static void checkPendingEmails(Context context){

        context.startService(new Intent(context, EmailService.class));

//        Constraints c = new Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build();
//
//        OneTimeWorkRequest mailreq = new OneTimeWorkRequest.Builder(EmailWorker.class)
//                .setConstraints(c)
//                .build();
//
//        WorkManager w =  WorkManager.getInstance();
//        w.enqueue(mailreq);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(Settings_Main.App_Password != null&&!Settings_Main.App_Password.isEmpty()) {

//            fab_email.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(context, ScheduleEmail.class);
//                    startActivity(i);
//                }
//            });
//            error_btn.setVisibility(View.INVISIBLE);

        }else {
//            error_btn.bringToFront();
//            error_btn.setVisibility(View.VISIBLE);
//            error_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    EmailAppPasswordGuide();
//                }
//            });
//            fab_email.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(context, "App Password is not given. Please fill it to use this function.", Toast.LENGTH_LONG).show();
//                }
//            });
        }
        Email_rec_Adapter adapter1 = new Email_rec_Adapter(UtilsArray_Email.getMail());
        adapter1.notifyDataSetChanged();
    }
}




//        calSet = Calendar.getInstance();
//
//        if (context == null) {
//            context = container.getContext();
//        }
//
//        datePicker = view.findViewById(R.id.Fragment_4_calendar_view);
//        recyclerView = view.findViewById(R.id.Fragment_4_event_rec_view);
//        swp_layout = view.findViewById(R.id.Fragment_4_swipe_layout);
////        no_event_view = view.findViewById(R.id.No_Event_View);
////        new_event_when_no_event = view.findViewById(R.id.New_Event_From_Rec);
//        new_event_btn = view.findViewById(R.id.New_Event_Create_Btn);
//
//        datePicker.init(calSet.get(YEAR), calSet.get(MONTH), calSet.get(DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
//
//                if (i != 0) {
//                    year = i;
//                }
//                if (i1 != 0) {
//                    month = i1;
//                }
//                if (i2 != 0) {
//                    day = i2;
//                }
//
//                calSet.set(DAY_OF_MONTH, day);
//                calSet.set(MONTH, month);
//                calSet.set(YEAR, year);

//                final Event_rec_Adapter adapter = new Event_rec_Adapter(UtilsArray_Calendar.events, context, calSet);
//                adapter.notifyDataSetChanged();
//            }
//        });

//        UtilsArray_Calendar.initEvents(context);
//
//        final Event_rec_Adapter adapter = new Event_rec_Adapter(UtilsArray_Calendar.events);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        adapter.notifyDataSetChanged();
//
//        swp_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                if (swp_layout.isRefreshing()) {
//                    adapter.notifyDataSetChanged();
//                    swp_layout.setRefreshing(false);
//                }
//            }
//        });


//        if (!Event_rec_Adapter.EventPresent) {
//
//            recyclerView.setVisibility(View.GONE);
//            no_event_view.setVisibility(View.VISIBLE);
//            new_event_btn.setVisibility(View.GONE);
//
//
//        }else {
//            recyclerView.setVisibility(View.VISIBLE);
//            no_event_view.setVisibility(View.GONE);
//            new_event_btn.setVisibility(View.VISIBLE);
//
//
//        }
//        new_event_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                long id = System.currentTimeMillis();
//                Intent intent = new Intent(context, NewCalendarEvent.class);
//                intent.putExtra("CalDAY", calSet.get(DAY_OF_MONTH));
//                intent.putExtra("CalMONTH", calSet.get(MONTH));
//                intent.putExtra("CalYEAR", calSet.get(YEAR));
//                intent.putExtra("Id", id);
//            }
//        });

//        new_event_when_no_event.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                long id = System.currentTimeMillis();
//                Intent intent = new Intent(context, NewCalendarEvent.class);
//                intent.putExtra("CalDAY", calSet.get(DAY_OF_MONTH));
//                intent.putExtra("CalMONTH", calSet.get(MONTH));
//                intent.putExtra("CalYEAR", calSet.get(YEAR));
//                intent.putExtra("Id", id);
//
//            }
//        });

//        long id = System.currentTimeMillis();
//        Intent intent = new Intent(context, NewCalendarEvent.class);
//        intent.putExtra("CalDAY",calSet.get(DAY_OF_MONTH));
//        intent.putExtra("CalMONTH",calSet.get(MONTH));
//        intent.putExtra("CalYEAR",calSet.get(YEAR));
//        intent.putExtra("Id",id);



//}
