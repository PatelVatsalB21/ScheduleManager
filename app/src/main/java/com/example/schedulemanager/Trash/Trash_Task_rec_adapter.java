package com.example.schedulemanager.Trash;

import android.content.Context;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.schedulemanager.R;
import com.example.schedulemanager.Task.Task;
import com.example.schedulemanager.Task.Task_rec_adapter;
import com.example.schedulemanager.Task.UtilsArray_Task;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Trash_Task_rec_adapter extends RecyclerView.Adapter<Trash_Task_rec_adapter.ViewHolder> {

    ArrayList<Task> trashTasks = new ArrayList<>();
    Context context;
    public static boolean multiSelect = false;
    private ArrayList<Task> selectedItems = new ArrayList<>();
    private androidx.appcompat.view.ActionMode actionMode;

    public Trash_Task_rec_adapter(Context c, ArrayList<Task> tt) {
    context = c;
    trashTasks = tt;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trash_task_layout, null);
        ViewHolder holder = new ViewHolder(view);
        if (context == null) {
            context = parent.getContext();
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.lottieAnimationView.setAnimation(UtilsArray_Task.category.get(trashTasks.get(position).LottieFileRes).LottieRes);
        holder.title.setText(trashTasks.get(position).Title);
        SimpleDateFormat d = new SimpleDateFormat(" dd/MM/yyyy hh:mm a");
        holder.time.setText(d.format(trashTasks.get(position).calendar.getTimeInMillis()));
        holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (multiSelect) {
                    selectItem(trashTasks.get(position), holder);
                } else
                    Toast.makeText(context, "Long press item for options", Toast.LENGTH_SHORT).show();
            }
        });

        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                actionMode = ((AppCompatActivity) view.getContext()).startSupportActionMode(actionModeCallbacks);
                selectItem(trashTasks.get(position), holder);
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                return true;
            }
        });

        if (selectedItems.contains(trashTasks.get(position))) {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.color_dark_gray));
        } else {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }

    private final androidx.appcompat.view.ActionMode.Callback actionModeCallbacks = new androidx.appcompat.view.ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(androidx.appcompat.view.ActionMode actionMode, Menu menu) {
            multiSelect = true;
            TrashMainPage.TrashActionOn();
            menu.add("Restore");
            menu.add("Delete");
            actionMode.setTitle(selectedItems.size() + " Selected");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(androidx.appcompat.view.ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(androidx.appcompat.view.ActionMode actionMode, MenuItem item) {

            if (item.getTitle() == "Restore") {
                for (Task intItem : selectedItems) {
                    UtilsArray_Task.AddToTask(intItem, context);
                    trashTasks.remove(intItem);
                }
                notifyDataSetChanged();
                UtilsArray_Trash.UpdateTrashTask(trashTasks, context);
                Task_rec_adapter adapter = new Task_rec_adapter(UtilsArray_Task.getTask());
                adapter.notifyDataSetChanged();
                Snackbar.make(Trash_frag_1.trash_task_rec_view,"Task restored successfully", Snackbar.LENGTH_SHORT ).show();

            } else if (item.getTitle() == "Delete") {
                        for (Task intItem : selectedItems) {
                            trashTasks.remove(intItem);
                        }
                        notifyDataSetChanged();
                        UtilsArray_Trash.UpdateTrashTask(trashTasks, context);
                        Snackbar.make(Trash_frag_1.trash_task_rec_view,"Task deleted successfully", Snackbar.LENGTH_SHORT ).show();
            }
            actionMode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(androidx.appcompat.view.ActionMode actionMode) {
            multiSelect = false;
            selectedItems.clear();
            TrashMainPage.TrashActionOff();
            notifyDataSetChanged();
        }
    };


    void selectItem(Task item, ViewHolder holder) {
        if (multiSelect) {
            if (selectedItems.contains(item)) {

                holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

                selectedItems.remove(item);
                actionMode.setTitle(selectedItems.size() + " Selected");
                if (selectedItems.size() == 0) {
                    actionMode.finish();
                }
            } else {
                if (selectedItems == null) {
                    selectedItems = new ArrayList<>();
                }
                selectedItems.add(item);
                holder.card.setBackgroundColor(context.getResources().getColor(R.color.color_dark_gray));
                actionMode.setTitle(selectedItems.size() + " Selected");
            }
        }

    }




    @Override
    public int getItemCount() {
        if (trashTasks != null) {
            if (!trashTasks.isEmpty()) {
                return trashTasks.size();
            } else {
                Trash_frag_1.trashTaskNullViewUpdater(Trash_Task_rec_adapter.this);
                return 0;
            }
        } else {
            Trash_frag_1.trashTaskNullViewUpdater(Trash_Task_rec_adapter.this);
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout card;
        TextView title, time;
        LottieAnimationView lottieAnimationView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.trash_task_title_layout);
            time = itemView.findViewById(R.id.trash_task_time_layout);
            card = itemView.findViewById(R.id.trash_task_card_layout);
            lottieAnimationView = itemView.findViewById(R.id.trash_task_layout_lottie_view);

        }
    }
}
