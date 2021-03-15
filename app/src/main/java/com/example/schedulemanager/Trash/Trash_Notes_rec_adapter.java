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

import com.example.schedulemanager.R;
import com.example.schedulemanager.note.Notes;
import com.example.schedulemanager.note.UtilsArraylist;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Trash_Notes_rec_adapter extends
        RecyclerView.Adapter<Trash_Notes_rec_adapter.ViewHolder> {

    ArrayList<Notes> trashNotes = new ArrayList<>();
    Context context;
    public static boolean multiSelect = false;
    private ArrayList<Notes> selectedItems = new ArrayList<>();
    private androidx.appcompat.view.ActionMode actionMode;

    public Trash_Notes_rec_adapter(Context c, ArrayList<Notes> tn) {
        context = c;
        trashNotes = tn;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trash_note_layout,
                null);
        ViewHolder holder = new ViewHolder(view);
        if (context == null) {
            context = parent.getContext();
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String title = trashNotes.get(position).title;
        final String desc = trashNotes.get(position).desc;
        holder.card.bringToFront();
        holder.title.setText(title);
        holder.desc.setText(desc);

        holder.card.setOnClickListener(view -> {
            if (multiSelect) {
                selectItem(trashNotes.get(position), holder);
            } else {
                Toast.makeText(context, "Long press item for options",
                        Toast.LENGTH_SHORT).show();
            }
        });

        holder.card.setOnLongClickListener(view -> {
            actionMode = ((AppCompatActivity) view.getContext()).startSupportActionMode(
                    actionModeCallbacks);
            selectItem(trashNotes.get(position), holder);
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            return true;
        });

        if (selectedItems.contains(trashNotes.get(position))) {
            holder.card.setBackgroundColor(
                    context.getResources().getColor(R.color.color_dark_gray));
        } else {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }

    private final androidx.appcompat.view.ActionMode.Callback actionModeCallbacks =
            new androidx.appcompat.view.ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(androidx.appcompat.view.ActionMode actionMode,
                        Menu menu) {
                    multiSelect = true;
                    TrashMainPage.TrashActionOn();
                    menu.add("Restore");
                    menu.add("Delete");
                    actionMode.setTitle(selectedItems.size() + " Selected");
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(androidx.appcompat.view.ActionMode actionMode,
                        Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(androidx.appcompat.view.ActionMode actionMode,
                        MenuItem item) {
                    if (item.getTitle() == "Restore") {
                        for (Notes intItem : selectedItems) {
                            UtilsArraylist.AddToNote(intItem, context);
                            trashNotes.remove(intItem);
                        }
                        notifyDataSetChanged();
                        UtilsArray_Trash.UpdateTrashNote(trashNotes, context);
                        Snackbar.make(Trash_frag_2.trash_notes_rec_view,
                                "Notes restored successfully", Snackbar.LENGTH_SHORT).show();

                    } else if (item.getTitle() == "Delete") {

                        for (Notes intItem : selectedItems) {
                            trashNotes.remove(intItem);
                        }
                        notifyDataSetChanged();
                        UtilsArray_Trash.UpdateTrashNote(trashNotes, context);
                        Snackbar.make(Trash_frag_2.trash_notes_rec_view,
                                "Notes deleted successfully", Snackbar.LENGTH_SHORT).show();

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


    void selectItem(Notes item, ViewHolder holder) {
        if (multiSelect) {
            if (selectedItems.contains(item)) {
                holder.card.setBackgroundColor(
                        context.getResources().getColor(R.color.colorPrimary));

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
                holder.card.setBackgroundColor(
                        context.getResources().getColor(R.color.color_dark_gray));
                actionMode.setTitle(selectedItems.size() + " Selected");
            }
        }
    }

    @Override
    public int getItemCount() {
        if (trashNotes != null) {
            if (!trashNotes.isEmpty()) {
                return trashNotes.size();
            } else {
                Trash_frag_2.trashNotesNullViewUpdater(Trash_Notes_rec_adapter.this);
                return 0;
            }
        } else {
            Trash_frag_2.trashNotesNullViewUpdater(Trash_Notes_rec_adapter.this);
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc;
        RelativeLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.trash_note_layout_title);
            desc = itemView.findViewById(R.id.trash_note_layout_desc);
            card = itemView.findViewById(R.id.trash_card_note_layout_rel_layout);
        }
    }
}
