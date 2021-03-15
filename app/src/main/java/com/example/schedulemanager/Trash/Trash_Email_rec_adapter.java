package com.example.schedulemanager.Trash;

import android.content.Context;
import android.util.Log;
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

import com.example.schedulemanager.HomeFrag.UtilsArray_All;
import com.example.schedulemanager.R;
import com.example.schedulemanager.email.Email;
import com.example.schedulemanager.email.Email_rec_Adapter;
import com.example.schedulemanager.email.UtilsArray_Email;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Trash_Email_rec_adapter extends RecyclerView.Adapter<Trash_Email_rec_adapter.ViewHolder> {

    ArrayList<Email> trashMails = new ArrayList<>();
    Context context;
    public static boolean multiSelect = false;
    private ArrayList<Email> selectedItems = new ArrayList<>();
    private androidx.appcompat.view.ActionMode actionMode;


    public Trash_Email_rec_adapter(Context c, ArrayList<Email> te) {
        context = c;
        trashMails = te;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trash_email_layout, null);
        ViewHolder holder = new ViewHolder(view);
        if (context == null) {
            context = parent.getContext();
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy  hh:mm a");
        holder.time.setText(d.format(trashMails.get(position).cal.getTimeInMillis()));
        holder.subject.setText(trashMails.get(position).Subject);
        holder.to.setText(trashMails.get(position).To);
        holder.from.setText(trashMails.get(position).From);

        holder.card.setOnClickListener(view -> {
            if (multiSelect) {
                selectItem(trashMails.get(position), holder);
            } else
                Toast.makeText(context, "Long press item for options", Toast.LENGTH_SHORT).show();
        });

        holder.card.setOnLongClickListener(view -> {
            actionMode = ((AppCompatActivity) view.getContext()).startSupportActionMode(actionModeCallbacks);
            selectItem(trashMails.get(position), holder);
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            return true;
        });

        if (selectedItems.contains(trashMails.get(position))) {
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
                for (Email intItem : selectedItems) {
                    UtilsArray_Email.AddToMail(intItem, context);
                    trashMails.remove(intItem);
                }
                notifyDataSetChanged();
                UtilsArray_Trash.UpdateTrashMail(trashMails, context);
                Email_rec_Adapter adapter = new Email_rec_Adapter(UtilsArray_Email.getMail());
                adapter.notifyDataSetChanged();
                UtilsArray_All.ReloadCategoryItems();
                Snackbar.make(Trash_frag_3.trash_mail_rec_view,"Email restored successfully", Snackbar.LENGTH_SHORT ).show();
            } else if (item.getTitle() == "Delete") {
                for (Email intItem : selectedItems) {
                    trashMails.remove(intItem);
                }
                notifyDataSetChanged();
                UtilsArray_Trash.UpdateTrashMail(trashMails, context);
                Snackbar.make(Trash_frag_3.trash_mail_rec_view,"Email deleted successfully", Snackbar.LENGTH_SHORT ).show();
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

    void selectItem(Email item, ViewHolder holder) {
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
        if (trashMails != null) {
            if (!trashMails.isEmpty()) {
                return trashMails.size();
            } else {
                Trash_frag_3.trashMailNullViewUpdater(Trash_Email_rec_adapter.this);
                return 0;
            }
        } else {
            Trash_frag_3.trashMailNullViewUpdater(Trash_Email_rec_adapter.this);
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout card;
        TextView time,to,subject,from;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.trash_email_layout_time);
            to = itemView.findViewById(R.id.trash_email_layout_to);
            subject = itemView.findViewById(R.id.trash_email_layout_subject);
            from = itemView.findViewById(R.id.trash_email_layout_from_txt_view);
            card = itemView.findViewById(R.id.trash_email_card);
        }
    }
}
