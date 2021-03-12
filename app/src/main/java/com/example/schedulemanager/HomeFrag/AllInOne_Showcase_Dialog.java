package com.example.schedulemanager.HomeFrag;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.schedulemanager.R;
import com.example.schedulemanager.Task.TaskOpen;
import com.example.schedulemanager.Task.UtilsArray_Task;
import com.example.schedulemanager.email.Email_Open;
import com.example.schedulemanager.email.UtilsArray_Email;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AllInOne_Showcase_Dialog extends DialogFragment {

    TextView type, title , subject, message, time, typeOfTask , pendingWarning;
    CheckBox repeat;
    LottieAnimationView lottie;
    Button back, edit;
    Integer viewType, position;
    LinearLayout weekDaysLinLayout;
    ToggleButton sun, mon, tue, wed, thu, fri, sat;
    ArrayList<All_In_One> AllInOne_Array = new ArrayList<>();
    private Calendar calToday;


    public AllInOne_Showcase_Dialog(Integer ViewType, Integer pos, ArrayList<All_In_One> allList) {
        viewType = ViewType;
        position = pos;
        AllInOne_Array = allList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_in_one_showcase_view_layout, container, false);
        type = view.findViewById(R.id.all_in_one_showcase_type_txt);
        title = view.findViewById(R.id.all_in_one_showcase_title);
//        from = view.findViewById(R.id.all_in_one_showcase_from);
        subject = view.findViewById(R.id.all_in_one_showcase_subject);
        message = view.findViewById(R.id.all_in_one_showcase_message);
        time = view.findViewById(R.id.all_in_one_showcase_time);
        typeOfTask = view.findViewById(R.id.all_in_one_showcase_type_of_task);
        repeat = view.findViewById(R.id.all_in_one_showcase_checkBox);
        lottie = view.findViewById(R.id.all_in_one_showcase_lottie);
        back = view.findViewById(R.id.all_in_one_showcase_back_btn);
        edit = view.findViewById(R.id.all_in_one_showcase_edit_btn);
        weekDaysLinLayout = view.findViewById(R.id.all_in_one_showcase_weekDays_lin_layout);
        sun = view.findViewById(R.id.all_in_one_showcase_sun);
        mon = view.findViewById(R.id.all_in_one_showcase_mon);
        tue = view.findViewById(R.id.all_in_one_showcase_tue);
        wed = view.findViewById(R.id.all_in_one_showcase_wed);
        thu = view.findViewById(R.id.all_in_one_showcase_thu);
        fri = view.findViewById(R.id.all_in_one_showcase_fri);
        sat = view.findViewById(R.id.all_in_one_showcase_sat);
        pendingWarning = view.findViewById(R.id.all_in_one_showcase_Pending_Warning_Text);

        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        SimpleDateFormat d = new SimpleDateFormat("dd/mm/yyyy hh:mm a");
        typeViewHider(viewType);

        if (viewType == 1){
            type.setText("Task");

            title.setText(AllInOne_Array.get(position).taskItem.getTitle());
            if (AllInOne_Array.get(position).taskItem.isComplete){
                time.setText("Task for: "+ d.format(AllInOne_Array.get(position).taskItem.calendar.getTimeInMillis()) +" completed");
            }
            else {
                time.setText("Set for: " + d.format(AllInOne_Array.get(position).taskItem.calendar.getTimeInMillis()));
            }
            typeOfTask.setText("Type of task: "+UtilsArray_Task.category.get(AllInOne_Array.get(position).taskItem.getLottieFileRes()).Category);
            lottie.setAnimation(UtilsArray_Task.category.get(AllInOne_Array.get(position).taskItem.getLottieFileRes()).LottieRes);

            if (AllInOne_Array.get(position).taskItem.isRepeating) {
                weekDaysLinLayout.setVisibility(View.VISIBLE);
                repeat.setVisibility(View.VISIBLE);
                repeat.setChecked(true);
            }
            else {
                weekDaysLinLayout.setVisibility(View.GONE);
                repeat.setVisibility(View.GONE);
                repeat.setChecked(false);
            }

            sun.setChecked(AllInOne_Array.get(position).taskItem.weekDays.get(0));
            mon.setChecked(AllInOne_Array.get(position).taskItem.weekDays.get(1));
            tue.setChecked(AllInOne_Array.get(position).taskItem.weekDays.get(2));
            wed.setChecked(AllInOne_Array.get(position).taskItem.weekDays.get(3));
            thu.setChecked(AllInOne_Array.get(position).taskItem.weekDays.get(4));
            fri.setChecked(AllInOne_Array.get(position).taskItem.weekDays.get(5));
            sat.setChecked(AllInOne_Array.get(position).taskItem.weekDays.get(6));

        }else {

            type.setText("Email");
            time.setText(d.format(AllInOne_Array.get(position).emailItem.cal.getTimeInMillis()));
            if (AllInOne_Array.get(position).emailItem.Scheduled){
                calToday = Calendar.getInstance();
                if (AllInOne_Array.get(position).emailItem.cal.compareTo(calToday) < 0 &&AllInOne_Array.get(position).emailItem.Scheduled ){
                    pendingWarning.setVisibility(View.VISIBLE);
                    time.setText("Pending from: "+ d.format(AllInOne_Array.get(position).emailItem.cal.getTimeInMillis()));
                }else {
                    pendingWarning.setVisibility(View.GONE);
                    time.setText("Scheduled for: "+ d.format(AllInOne_Array.get(position).emailItem.cal.getTimeInMillis()));
                }
            }else {
                time.setText("Sent at: "+ d.format(AllInOne_Array.get(position).emailItem.cal.getTimeInMillis()));
            }
            if (!AllInOne_Array.get(position).emailItem.getSubject().isEmpty()){
                subject.setText(AllInOne_Array.get(position).emailItem.getSubject());
            }else subject.setVisibility(View.GONE);

            if (!AllInOne_Array.get(position).emailItem.getMessage().isEmpty()) {
                message.setText(AllInOne_Array.get(position).emailItem.getMessage());
            }else message.setVisibility(View.GONE);

            title.setText(AllInOne_Array.get(position).emailItem.getTo());
//            from.setText(AllInOne_Array.get(position).emailItem.getFrom());
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer sendPosition;
                if (viewType == 1){
                    sendPosition = UtilsArray_Task.getTask().indexOf(AllInOne_Array.get(position).taskItem);
                    Intent i = new Intent(getContext(), TaskOpen.class);
                    i.putExtra("position", sendPosition);
                    getContext().startActivity(i);

                }else {
                    sendPosition = UtilsArray_Email.getMail().indexOf(AllInOne_Array.get(position).emailItem);
                    Intent intent = new Intent(getContext(), Email_Open.class);
                    intent.putExtra("position", sendPosition);
                    getContext().startActivity(intent);
                }
                dismiss();
            }
        });

        return view;
    }

    public void typeViewHider(Integer type){
        if (type == 1 ){
//            from.setVisibility(View.GONE);
            subject.setVisibility(View.GONE);
            message.setVisibility(View.GONE);
            pendingWarning.setVisibility(View.GONE);
        }else if (type == 2){
            typeOfTask.setVisibility(View.GONE);
            repeat.setVisibility(View.GONE);
            lottie.setVisibility(View.GONE);
            weekDaysLinLayout.setVisibility(View.GONE);
        }

    }
}
