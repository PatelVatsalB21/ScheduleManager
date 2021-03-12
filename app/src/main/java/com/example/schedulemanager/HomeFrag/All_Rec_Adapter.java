package com.example.schedulemanager.HomeFrag;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.schedulemanager.R;
import com.example.schedulemanager.Task.UtilsArray_Task;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class All_Rec_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //    final private static int TYPE_NOTE = 0;
    final private static int TYPE_TASK = 1;
    final private static int TYPE_EMAIL = 2;
    Context mContext;
    public Calendar calToday;

//    public static ArrayList<Notes>notes = UtilsArraylist.getNote();
//    public static ArrayList<Email>mail = UtilsArray_Email.getMail();
//    public static ArrayList<Task> task = UtilsArray_Task.getTask();

    public ArrayList<All_In_One> AllInOne_Array = new ArrayList<>();

    public All_Rec_Adapter(Context mContext, ArrayList<All_In_One> a) {
        this.mContext = mContext;
        Log.d("ALLRECSIZE", String.valueOf(a.size()));
        this.AllInOne_Array = a;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        calToday = Calendar.getInstance();

//        if (viewType == TYPE_NOTE) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, null);
//            All_Rec_Adapter.NoteViewHolder holder = new NoteViewHolder(view);
//            return holder;

//        } else
        if (viewType == TYPE_TASK) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_in_one_task_layout, null);
            TaskViewHolder holder = new TaskViewHolder(view);
            return holder;

        } else if(viewType==TYPE_EMAIL){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_in_one_email_layout, null);
            EmailViewHolder holder = new EmailViewHolder(view);
            return holder;

        }

        else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

//        if(getItemViewType(position)==TYPE_NOTE){
//            Log.d("Rec","Note");
//            NoteViewHolder noteViewHolder = (NoteViewHolder) holder;
//
////            Boolean alarm_state = UtilsArraylist.note.get(position).EngagedAlarm;
//
//            final String title = AllInOne_Array.get(position).notesItem.title;
//            final String desc = AllInOne_Array.get(position).notesItem.title;
//
//
//            noteViewHolder.card.bringToFront();
//            noteViewHolder.note_title.setText(title);
//            noteViewHolder.note_desc.setText(desc);
//
//            noteViewHolder.card.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(view.getContext(), Note_Open.class);
//                    intent.putExtra("position", position);
//                    view.getContext().startActivity(intent);
//                }
//            });
//
//        }
//        else
        if (getItemViewType(position) == TYPE_TASK) {

            Log.d("Rec", "Task");
            final TaskViewHolder taskViewHolder = (TaskViewHolder) holder;

            SimpleDateFormat d = new SimpleDateFormat("MMM \n dd");
            SimpleDateFormat t = new SimpleDateFormat("hh:mm a");

            if (AllInOne_Array.get(position).taskItem.isComplete){
//                SimpleDateFormat dShort = new SimpleDateFormat(" dd MMM yyyy");
                taskViewHolder.card.setCardBackgroundColor(mContext.getResources().getColor(R.color.Blue_new_dull_bg));
//                taskViewHolder.date.setTextColor(mContext.getResources().getColor(R.color.Blue_new_dull_txt));
                taskViewHolder.time.setTextColor(mContext.getResources().getColor(R.color.Blue_new_dull_txt));
                taskViewHolder.title.setTextColor(mContext.getResources().getColor(R.color.Blue_new_dull_txt));
                taskViewHolder.time.setText("Task completed");
            }
            else {
                taskViewHolder.card.setCardBackgroundColor(mContext.getResources().getColor(R.color.Blue_new_bg));
//                taskViewHolder.date.setTextColor(mContext.getResources().getColor(R.color.Blue_new_txt));
                taskViewHolder.time.setTextColor(mContext.getResources().getColor(R.color.Blue_new_txt));
                taskViewHolder.title.setTextColor(mContext.getResources().getColor(R.color.Blue_new_txt));
                taskViewHolder.time.setText("Will remind at: " + t.format(AllInOne_Array.get(position).taskItem.calendar.getTimeInMillis()));
            }

            taskViewHolder.title.setText(AllInOne_Array.get(position).taskItem.getTitle());
//            taskViewHolder.desc.setText(AllInOne_Array.get(position).taskItem.Desc);

//            SimpleDateFormat t = new SimpleDateFormat(" hh:ss a");
            taskViewHolder.date.setText(d.format(AllInOne_Array.get(position).taskItem.calendar.getTimeInMillis()));
//            taskViewHolder.time.setText(t.format(AllInOne_Array.get(position).taskItem.calendar.getTimeInMillis()));
//            taskViewHolder.date.setText(d.format(AllInOne_Array.get(position).taskItem.calendar.getTimeInMillis()));
            taskViewHolder.lottieAnimationView.setAnimation(UtilsArray_Task.category.get(AllInOne_Array.get(position).taskItem.getLottieFileRes()).LottieRes);

            if (AllInOne_Array.get(position).taskItem.isRepeating) (taskViewHolder).weekDaysLinLayout.setVisibility(View.VISIBLE);
            else taskViewHolder.weekDaysLinLayout.setVisibility(View.GONE);

            taskViewHolder.sun.setChecked(AllInOne_Array.get(position).taskItem.weekDays.get(0));
            taskViewHolder.mon.setChecked(AllInOne_Array.get(position).taskItem.weekDays.get(1));
            taskViewHolder.tue.setChecked(AllInOne_Array.get(position).taskItem.weekDays.get(2));
            taskViewHolder.wed.setChecked(AllInOne_Array.get(position).taskItem.weekDays.get(3));
            taskViewHolder.thu.setChecked(AllInOne_Array.get(position).taskItem.weekDays.get(4));
            taskViewHolder.fri.setChecked(AllInOne_Array.get(position).taskItem.weekDays.get(5));
            taskViewHolder.sat.setChecked(AllInOne_Array.get(position).taskItem.weekDays.get(6));


//                taskViewHolder.card.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(mContext, TaskOpen.class);
//                    i.putExtra("position", UtilsArray_Task.task.indexOf(AllInOne_Array.get(position).taskItem));
//                    mContext.startActivity(i);
//                }
//            });
        } else if (getItemViewType(position) == TYPE_EMAIL) {

            Log.d("Rec", "Email");
            EmailViewHolder emailViewHolder = (EmailViewHolder) holder;

            SimpleDateFormat d = new SimpleDateFormat("MMM \n dd");
            SimpleDateFormat t = new SimpleDateFormat("hh:mm a");

            emailViewHolder.date.setText(d.format(AllInOne_Array.get(position).emailItem.cal.getTimeInMillis()));
            if (AllInOne_Array.get(position).emailItem.Scheduled){
                if (AllInOne_Array.get(position).emailItem.cal.compareTo(calToday) < 0 &&AllInOne_Array.get(position).emailItem.Scheduled ){
                    emailViewHolder.pendingWarning.setVisibility(View.VISIBLE);
                    emailViewHolder.time.setText("Pending from: "+t.format(AllInOne_Array.get(position).emailItem.cal.getTimeInMillis()));
                }else {
                    emailViewHolder.pendingWarning.setVisibility(View.GONE);
                    emailViewHolder.time.setText("Scheduled for: "+t.format(AllInOne_Array.get(position).emailItem.cal.getTimeInMillis()));
                }
            }else {
                emailViewHolder.time.setText("Sent at: "+t.format(AllInOne_Array.get(position).emailItem.cal.getTimeInMillis()));
            }
            emailViewHolder.subject.setText(AllInOne_Array.get(position).emailItem.getSubject());
            emailViewHolder.to.setText(AllInOne_Array.get(position).emailItem.getTo());



        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllInOne_Showcase_Dialog dialog = new AllInOne_Showcase_Dialog(getItemViewType(position), position, AllInOne_Array);
                AppCompatActivity activity = ((AppCompatActivity)view.getContext());
                dialog.show(activity.getSupportFragmentManager(),null);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (AllInOne_Array != null) {
            if (AllInOne_Array.size() != 0)
                return AllInOne_Array.size();
        }
        return AllInOne_Array.size();
    }

    //    public class NoteViewHolder extends RecyclerView.ViewHolder {
//        EditText new_note_input_title, new_note_input_desc;
//        TextView note_desc, note_title, note_time;
//        MaterialCardView card;
//        View note_view;
//        ImageView menu_btn;
//
//        public NoteViewHolder(@NonNull View itemView) {
//            super(itemView);
//            new_note_input_title = itemView.findViewById(R.id.new_note_input_title);
//            new_note_input_desc = itemView.findViewById(R.id.new_note_input_desc);
//            note_desc = itemView.findViewById(R.id.desc1);
//            card = itemView.findViewById(R.id.card1);
//            note_title = itemView.findViewById(R.id.head1);
//            note_time = itemView.findViewById(R.id.time1);
//            note_view = itemView.findViewById(R.id.view1);
//            menu_btn = itemView.findViewById(R.id.note_layout_menu_btn);
//
//        }
//
//    }
    public class EmailViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        TextView time, date, to, subject,pendingWarning;

        public EmailViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.All_In_One_Email_Date);
            time = itemView.findViewById(R.id.All_In_One_Email_Time);
            to = itemView.findViewById(R.id.All_In_One_Email_Title);
            subject = itemView.findViewById(R.id.All_In_One_Email_Desc);
            card = itemView.findViewById(R.id.All_In_One_Email_Card);
            pendingWarning = itemView.findViewById(R.id.All_In_One_Email_Pending_Warning_Text);

        }
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        TextView title, date, time;
        LottieAnimationView lottieAnimationView;
        ToggleButton sun, mon, tue, wed, thu, fri, sat;
        LinearLayout weekDaysLinLayout;


        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.All_In_One_Task_Title);
            date = itemView.findViewById(R.id.All_In_One_Task_Date);
            time = itemView.findViewById(R.id.All_In_One_Task_Time);
//            time = itemView.findViewById(R.id.All_In_One_Task_Time);
//            desc = itemView.findViewById(R.id.task_desc_layout);
//            delete_task = itemView.findViewById(R.id.task_btn_delete);
            card = itemView.findViewById(R.id.All_In_One_Task_Card_Semi_REL_layout);
            lottieAnimationView = itemView.findViewById(R.id.All_In_One_Task_Lottie);
            sun = itemView.findViewById(R.id.All_In_One_task_layout_sun);
            mon = itemView.findViewById(R.id.All_In_One_task_layout_mon);
            tue = itemView.findViewById(R.id.All_In_One_task_layout_tue);
            wed = itemView.findViewById(R.id.All_In_One_task_layout_wed);
            thu = itemView.findViewById(R.id.All_In_One_task_layout_thu);
            fri = itemView.findViewById(R.id.All_In_One_task_layout_fri);
            sat = itemView.findViewById(R.id.All_In_One_task_layout_sat);
            weekDaysLinLayout = itemView.findViewById(R.id.All_In_One_task_layout_weekDays_lin_layout);
        }

    }

    @Override
    public int getItemViewType(int position) {
//        if (UtilsArray_All.allItemsArrayList.get(position).notesItem != null) {
//            return TYPE_NOTE;
//        } else
        if (AllInOne_Array.get(position).taskItem!=null) {
            return TYPE_TASK;
        } else if (AllInOne_Array.get(position).emailItem != null) {
            return TYPE_EMAIL;
        } else return 0;
    }

}
