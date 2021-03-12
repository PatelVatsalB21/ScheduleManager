package com.example.schedulemanager.note;

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
import com.example.schedulemanager.Trash.TrashMainPage;
import com.example.schedulemanager.Trash.UtilsArray_Trash;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.example.schedulemanager.note.UtilsArraylist.UpdateNote;

//import com.example.firebase.HomeFrag.All_Rec_Adapter;
//import com.example.firebase.HomeFrag.UtilsArray_All;
//import static com.example.firebase.note.UtilsArraylist.myDb;


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

//        if (notes.get(position).title != null && notes.get(position).desc != null && notes.get(position).calendar != null
//                && notes.get(position).id != null && notes.get(position).EngagedAlarm != null) {

        final String title = notes.get(position).title;
        final String desc = notes.get(position).desc;

//            String titleFinal = title.replace("\n","<br />");
//            String descFinal = desc.replace("\n","<br />");

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
//                holder.menu_btn.setBackgroundColor(UtilsArraylist.note_BG_colorSet.get(notes.get(position).BackgroundColor));
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (multiSelect) {
                    selectItem(notes.get(position), holder);
                } else {
                    Intent intent = new Intent(view.getContext(), Note_Open.class);
                    intent.putExtra("position", position);
                    view.getContext().startActivity(intent);
                }


            }
        });

//        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                new AlertDialog.Builder(context)
//                        .setTitle("Delete Note")
//                        .setMessage("Do You Want To Delete Note")
//                        .setNegativeButton(android.R.string.no, null)
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                               notes.remove(position);
//                               notifyItemRemoved(position);
//                               notifyDataSetChanged();
//                                All_Rec_Adapter adapter1 = new All_Rec_Adapter(context, UtilsArray_All.getAllItemsArrayList());
//                                adapter1.notifyDataSetChanged();
//
//
//                                SharedPreferences.Editor editor =  sharedPreferences.edit();
//                                editor.remove(NOTES_ARRAY_DB);
//                                editor.putString( NOTES_ARRAY_DB , ArrayToJson(notes));
//                                editor.commit();
//
////                               docref.set(UtilsArraylist.note);
//
////                                ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
////                                if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
////                                    getListItems();
////                                } else {
////                                    Toast.makeText(mContext, "Failed to Update in server due to connection error", Toast.LENGTH_SHORT).show();
////                                }
//
////                                db.noteDao().getAllData().remove(position);
//
//                            }
//                        }).create()
//                        .show();
//
//                return true;
//            }
//        });

        holder.menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                final String docId = Notes_rec_Adapter.getSnapshots().getSnapshot(i).getId();
                if (multiSelect) {
                    selectItem(notes.get(position), holder);
                } else {
                    PopupMenu menu = new PopupMenu(v.getContext(), v);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        menu.setGravity(Gravity.END);
                    }
                    menu.getMenu().add("Share").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            String finalShare = "";
                            if (title != null) {
                                finalShare = "Title: " + "\n" + title;
//                                sendIntent.putExtra(Intent.EXTRA_TEXT, "Title: " + title);
                            }
                            if (desc != null) {
//                                sendIntent.putExtra(Intent.EXTRA_TEXT, "Body: " + desc);
                                finalShare = finalShare + "\n" + "\n" + "Body: " + "\n" + desc;
                            }
                            sendIntent.putExtra(Intent.EXTRA_TEXT, finalShare);
                            sendIntent.setType("text/plain");

                            Intent shareIntent = Intent.createChooser(sendIntent, "Share Note");
                            mContext.startActivity(shareIntent);


                            return true;
                        }
                    });

                    menu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            makeSnackBar(position, notes.get(position));
                            UtilsArray_Trash.AddToTrashNotes(notes.get(position), mContext);
                            UtilsArraylist.CancelNoteAlarm(mContext, position);
                            notes.remove(position);
//                        alarmManager.cancel(UtilsArraylist.getNotePendingIntentFromPosition(position));
//                            UtilsArraylist.RemoveFromNoteIntent(mContext, UtilsArraylist.getCustomPenIntFromPosition(position));
                            notifyItemRemoved(position);
                            notifyDataSetChanged();

                            UpdateNote(notes, mContext);

//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.remove(NOTES_ARRAY_DB);
//                        editor.putString(NOTES_ARRAY_DB, GsonConverter.ArrayToJson(notes));
//                        editor.commit();
//                        int deleteData = myDb.deleteData(String.valueOf(1));
//                        boolean isUpdate = myDb.updateData(String.valueOf(1),GsonConverter.ArrayToJson(notes));

//                        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//                        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
//                            getListItems();
//                        } else {
//                            Toast.makeText(mContext, "Failed to Update in server due to connection error", Toast.LENGTH_SHORT).show();
//                        }


//                        DocumentReference docRef = fStore.collection("notes").document(user.getUid()).collection("myNotes").document(docId);
//                        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                // com.example.firebase.note deleted
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(MainActivity.this, "Error in Deleting Note.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        return false;
//                    }
//                });
                            return true;
                        }
                    });

                    if (!notes.get(position).EngagedAlarm) {
                        menu.getMenu().add("Add Reminder").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                Intent intent = new Intent(v.getContext(), Timepicker.class);
                                intent.putExtra("position", position);

                                holder.itemView.getContext().startActivity(intent);


                                return true;
                            }
                        });
                    } else {
                        menu.getMenu().add("Cancel Reminder").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                UtilsArraylist.CancelNoteAlarm(mContext, position);
//                            alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//                            alarmManager.cancel(UtilsArraylist.getNotePendingIntentFromPosition(position));
//                                UtilsArraylist.RemoveFromNoteIntent(mContext, UtilsArraylist.getCustomPenIntFromPosition(position));
//                                note.get(position).EngagedAlarm = false;
                                return true;
                            }
                        });
                    }

                    menu.show();
                }
            }
        });


        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!multiSelect) {
                    actionMode = ((AppCompatActivity) view.getContext()).startSupportActionMode(actionModeCallbacks);
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                }
                selectItem(notes.get(position), holder);
                return true;
            }
        });

        if (selectedItems.contains(notes.get(position))) {
            holder.rel_for_background.setBackgroundColor(Color.LTGRAY);
//            holder.menu_btn.setBackgroundColor(Color.LTGRAY);
        } else {
            if (notes.get(position).BackgroundColor != null) {
                holder.rel_for_background.setCardBackgroundColor(UtilsArraylist.note_BG_colorSet.get(notes.get(position).BackgroundColor));
//                holder.menu_btn.setBackgroundColor(UtilsArraylist.note_BG_colorSet.get(notes.get(position).BackgroundColor));
            } else {
                holder.rel_for_background.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
//                holder.menu_btn.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }

//        }
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


    //else notedb.insertData("This is Sample Tilte", "This is Sample Description");


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
//                Toast.makeText(mContext,"Notes moved to Trash",Toast.LENGTH_SHORT ).show();
//                alarmManager.cancel(UtilsArraylist.getNotePendingIntentFromPosition(notes.indexOf(intItem)));
//                UtilsArraylist.RemoveFromNoteIntent(mContext, UtilsArraylist.getCustomPenIntFromPosition(notes.indexOf(intItem)));

            }
            notifyDataSetChanged();
//            UtilsArray_All.ReloadCategoryItems();
//            All_In_One_Main_Adapter adapter1 = new All_In_One_Main_Adapter(context);
//            adapter1.notifyDataSetChanged();

            UtilsArraylist.UpdateNote(notes, mContext);

//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.remove(NOTES_ARRAY_DB);
//            editor.remove(NOTES_INTENT_DB);
//            editor.putString(NOTES_ARRAY_DB, GsonConverter.ArrayToJson(notes));
//            editor.putString(NOTES_INTENT_DB, GsonConverter.PenInttoJson(UtilsArraylist.notesPendingIntents));
//            editor.commit();

//            int deleteData = myDb.deleteData(String.valueOf(1));
//            boolean isUpdate = myDb.updateData(String.valueOf(1),GsonConverter.ArrayToJson(notes));
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
//            UtilsArray_All.ReloadCategoryItems();
//            All_In_One_Main_Adapter adapter1 = new All_In_One_Main_Adapter(context);
//            adapter1.notifyDataSetChanged();
        }
    };

    void selectItem(Notes item, ViewHolder holder) {
        if (multiSelect) {
            if (selectedItems.contains(item)) {
                Log.d("REMOVESELECT", item.getTitle());
                if (notes.get(notes.indexOf(item)).BackgroundColor != null) {
                    holder.rel_for_background.setCardBackgroundColor(UtilsArraylist.note_BG_colorSet.get(notes.get(notes.indexOf(item)).BackgroundColor));
//                    holder.menu_btn.setBackgroundColor(UtilsArraylist.note_BG_colorSet.get(notes.get(notes.indexOf(item)).BackgroundColor));
                } else {
                    holder.rel_for_background.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
//                    holder.menu_btn.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
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
                Log.d("ADDEDSELECT", item.getTitle());
                selectedItems.add(item);
                holder.rel_for_background.setCardBackgroundColor(context.getResources().getColor(R.color.color_dark_gray));
//                holder.menu_btn.setBackgroundColor(context.getResources().getColor(R.color.color_dark_gray));
                actionMode.setTitle(selectedItems.size() + " Selected");
            }
        }

    }

    private void makeSnackBar() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(HomePage.fab_main_home, "Notes moved to trash", Snackbar.LENGTH_SHORT)
                        .setActionTextColor(mContext.getResources().getColor(R.color.color_yellow))
                        .setAnchorView(HomePage.fab_main_home)
                        .show();
            }
        }, 750);
    }

    public void makeSnackBar(Integer positionToRestore, Notes noteDeleted) {

        Snackbar snackbar = Snackbar
                .make(HomePage.fab_main_home, "Note moved to Trash", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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

                    }
                }).setActionTextColor(mContext.getResources().getColor(R.color.color_yellow)).setAnchorView(HomePage.fab_main_home);

        snackbar.show();

    }

//    Filter filter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//
//            if(charSequence.toString().isEmpty()){
//                FilteredNotes.addAll(UtilsArraylist.note);
//            }else{
//                for (Notes n : UtilsArraylist.note){
//                    if(n.title.toLowerCase().contains(charSequence.toString().toLowerCase())||n.desc.toLowerCase().contains(charSequence.toString().toLowerCase())){
//                        FilteredNotes.add(n);
//                    }
//                }
//            }
//
//            adapterNotes = new Notes_rec_Adapter(mContext, FilteredNotes);
//            adapterNotes.notifyDataSetChanged();
//
//            FilterResults filterResults = new FilterResults();
//            filterResults.values = FilteredNotes;
//            return filterResults;
//        }
//
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//
//        }
//    };
//
//    @Override
//    public Filter getFilter() {
//        return filter;
//    }
//
//    public  void filterNotes(String text){
//        adapterNotes.getFilter().filter(text);
//    }


}

