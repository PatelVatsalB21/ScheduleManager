package com.example.schedulemanager.note;

import static com.example.schedulemanager.note.UtilsArraylist.UpdateNote;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedulemanager.MainFragments.Fragment_3;
import com.example.schedulemanager.MainFragments.HomePage;
import com.example.schedulemanager.R;
import com.example.schedulemanager.Trash.UtilsArray_Trash;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Notes_rec_Adapter extends RecyclerView.Adapter<Notes_rec_Adapter.ViewHolder> {

    Context context, mContext;
    ArrayList<Notes> notes;
    public static boolean multiSelect = false;
    private ArrayList<Notes> selectedItems = new ArrayList<>();
    private androidx.appcompat.view.ActionMode actionMode;

    public Notes_rec_Adapter(Context context, ArrayList<Notes> notes) {
        this.context = context;
        this.notes = notes;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, null);
        ViewHolder holder = new ViewHolder(view);
        if (mContext == null) {
            mContext = parent.getContext();
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final String title = notes.get(position).title;
        final String desc = notes.get(position).desc;

        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        holder.card.bringToFront();
        holder.note_title.setText(title);
        holder.note_desc.setText(desc);
        if (notes.get(position).EngagedAlarm){
            holder.time.setVisibility(View.VISIBLE);
            holder.time.setText("Reminder set for: " + d.format(notes.get(position).lastEdited.getTimeInMillis()));
        }else holder.time.setVisibility(View.GONE);

        if (notes.get(position).BackgroundColor != null) {
            holder.rel_for_background.setCardBackgroundColor(UtilsArraylist.note_BG_colorSet.get(notes.get(position).BackgroundColor));
            holder.note_title.setTextColor(UtilsArraylist.note_Txt_colorSet.get(notes.get(position).BackgroundColor));
            holder.note_title.setLinkTextColor(UtilsArraylist.note_Txt_colorSet.get(notes.get(position).BackgroundColor));
            holder.note_desc.setTextColor(UtilsArraylist.note_Txt_colorSet.get(notes.get(position).BackgroundColor));
            holder.note_desc.setLinkTextColor(UtilsArraylist.note_Txt_colorSet.get(notes.get(position).BackgroundColor));
            holder.menu_btn.setColorFilter(UtilsArraylist.note_Txt_colorSet.get(notes.get(position).BackgroundColor), PorterDuff.Mode.SRC_IN);
        }

        holder.card.setOnClickListener(view -> {
            if (multiSelect) {
                selectItem(notes.get(position), holder);
            } else {
                Intent intent = new Intent(view.getContext(), Note_Open.class);
                intent.putExtra("position", position);
                view.getContext().startActivity(intent);
            }
        });

        holder.menu_btn.setOnClickListener(v -> {
            if (multiSelect) {
                selectItem(notes.get(position), holder);
            } else {
                PopupMenu menu = new PopupMenu(v.getContext(), v);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    menu.setGravity(Gravity.END);
                }
                menu.getMenu().add("Share").setOnMenuItemClickListener(item -> {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    String finalShare = "";
                    if (title != null) {
                        finalShare = "Title: " + "\n" + title;
                    }
                    if (desc != null) {
                        finalShare = finalShare + "\n" + "\n" + "Body: " + "\n" + desc;
                    }
                    sendIntent.putExtra(Intent.EXTRA_TEXT, finalShare);
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent, "Share Note");
                    mContext.startActivity(shareIntent);
                    return true;
                });

                menu.getMenu().add("Delete").setOnMenuItemClickListener(item -> {
                    makeSnackBar(position, notes.get(position));
                    UtilsArray_Trash.AddToTrashNotes(notes.get(position), mContext);
                    UtilsArraylist.CancelNoteAlarm(mContext, position);
                    notes.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    UpdateNote(notes, mContext);
                    return true;
                });

                if (!notes.get(position).EngagedAlarm) {
                    menu.getMenu().add("Add Reminder").setOnMenuItemClickListener(menuItem -> {
                        Intent intent = new Intent(v.getContext(), Timepicker.class);
                        intent.putExtra("position", position);
                        holder.itemView.getContext().startActivity(intent);
                        return true;
                    });
                } else {
                    menu.getMenu().add("Cancel Reminder").setOnMenuItemClickListener(menuItem -> {
                        UtilsArraylist.CancelNoteAlarm(mContext, position);
                        return true;
                    });
                }
                menu.show();
            }
        });

        holder.card.setOnLongClickListener(view -> {
            if (!multiSelect) {
                actionMode = ((AppCompatActivity) view.getContext()).startSupportActionMode(actionModeCallbacks);
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            }
            selectItem(notes.get(position), holder);
            return true;
        });

        if (selectedItems.contains(notes.get(position))) {
            holder.rel_for_background.setBackgroundColor(Color.LTGRAY);
        } else {
            if (notes.get(position).BackgroundColor != null) {
                holder.rel_for_background.setCardBackgroundColor(UtilsArraylist.note_BG_colorSet.get(notes.get(position).BackgroundColor));
            } else {
                holder.rel_for_background.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (notes != null) {
            if (!notes.isEmpty()) {
                return notes.size();
            } else {
                Fragment_3.notesNullViewUpdater(Notes_rec_Adapter.this);
                return 0;
            }
        } else {
            Fragment_3.notesNullViewUpdater(Notes_rec_Adapter.this);
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView note_desc, note_title, time;
        MaterialCardView card;
        ImageButton menu_btn;
        MaterialCardView rel_for_background;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            note_desc = itemView.findViewById(R.id.note_layout_desc);
            card = itemView.findViewById(R.id.note_layout_card);
            note_title = itemView.findViewById(R.id.note_layout_title);
            time = itemView.findViewById(R.id.note_layout_time);
            menu_btn = itemView.findViewById(R.id.note_layout_menu_btn);
            rel_for_background = itemView.findViewById(R.id.card_note_layout_rel_layout);
        }
    }

    private final androidx.appcompat.view.ActionMode.Callback actionModeCallbacks = new androidx.appcompat.view.ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(androidx.appcompat.view.ActionMode actionMode, Menu menu) {
            HomePage.hideLayout();
            Fragment_3.Frag3ContextHide();
            multiSelect = true;
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
            for (Notes intItem : selectedItems) {
                UtilsArray_Trash.AddToTrashNotes(intItem, context);
                if (intItem.EngagedAlarm) {
                    UtilsArraylist.CancelNoteAlarm(mContext, notes.indexOf(intItem));
                }
                notes.remove(intItem);
                makeSnackBar();
            }
            notifyDataSetChanged();
            UtilsArraylist.UpdateNote(notes, mContext);
            actionMode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(androidx.appcompat.view.ActionMode actionMode) {
            HomePage.showLayout();
            Fragment_3.Frag3ContextShow();
            multiSelect = false;
            selectedItems.clear();
            notifyDataSetChanged();
        }
    };

    void selectItem(Notes item, ViewHolder holder) {
        if (multiSelect) {
            if (selectedItems.contains(item)) {
                if (notes.get(notes.indexOf(item)).BackgroundColor != null) {
                    holder.rel_for_background.setCardBackgroundColor(UtilsArraylist.note_BG_colorSet.get(notes.get(notes.indexOf(item)).BackgroundColor));
                } else {
                    holder.rel_for_background.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                }
                selectedItems.remove(item);
                actionMode.setTitle(selectedItems.size() + " Selected");
                if (selectedItems.size() == 0) {
                    actionMode.finish();
                }
            } else {
                if (selectedItems == null) {
                    selectedItems = new ArrayList<>();
                }
                holder.rel_for_background.setCardBackgroundColor(context.getResources().getColor(R.color.color_dark_gray));
                actionMode.setTitle(selectedItems.size() + " Selected");
            }
        }
    }

    private void makeSnackBar() {
        new Handler().postDelayed(() -> Snackbar.make(HomePage.fab_main_home, "Notes moved to trash", Snackbar.LENGTH_SHORT)
                .setActionTextColor(mContext.getResources().getColor(R.color.color_yellow))
                .setAnchorView(HomePage.fab_main_home)
                .show(), 750);
    }

    public void makeSnackBar(Integer positionToRestore, Notes noteDeleted) {

        Snackbar snackbar = Snackbar
                .make(HomePage.fab_main_home, "Note moved to Trash", Snackbar.LENGTH_LONG)
                .setAction("UNDO", view -> {
                    UtilsArraylist.note.add(positionToRestore, noteDeleted);
                    UpdateNote(UtilsArraylist.getNote(), mContext);
                    notifyDataSetChanged();
                    Fragment_3.noteNullViewFinish();
                    if (noteDeleted.calendar != null && noteDeleted.EngagedAlarm && Notes.isNoteAheadOfTime(noteDeleted.calendar)) {
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(context, AlarmReciever.class);
                        intent.putExtra("position", positionToRestore);
                        PendingIntent nPendingIntent = PendingIntent.getBroadcast(context, (int) noteDeleted.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, noteDeleted.calendar.getTimeInMillis(), nPendingIntent);
                        }
                    }
                    UtilsArray_Trash.trash_notes.remove(noteDeleted);
                    UtilsArray_Trash.UpdateTrashNote(UtilsArray_Trash.getTrash_notes(), mContext);
                }).setActionTextColor(mContext.getResources().getColor(R.color.color_yellow)).setAnchorView(HomePage.fab_main_home);
        snackbar.show();
    }
}