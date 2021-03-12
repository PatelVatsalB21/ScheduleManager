package com.example.schedulemanager.note;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedulemanager.MainFragments.Fragment_3;
import com.example.schedulemanager.MainFragments.HomePage;
import com.example.schedulemanager.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Stack;


public class NewNoteAddDialog extends AppCompatActivity {
    RelativeLayout card;
    //    Button Save;
    TextView createdTimeStamp;
    ImageButton Cancel, Menu_Btn, undo_btn, redo_btn;
    EditText new_note_input_title, new_note_input_desc;
    //    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Integer backgroundColor;
    Calendar calendar = Calendar.getInstance();
    Stack<UndoRedo> undoStack, redoStack;
    String titleChanged = "", descChanged = "";
    long last_text_edit = 0;
    Handler handler = new Handler();
    Runnable input_finish_checker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note_add_dialog);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        Menu_Btn = findViewById(R.id.new_note_menu_btn);
//        Save = findViewById(R.id.new_note_save_btn);
        Cancel = findViewById(R.id.new_note_cancel_btn);
        new_note_input_desc = findViewById(R.id.new_note_input_desc);
        new_note_input_title = findViewById(R.id.new_note_input_title);
        card = findViewById(R.id.nested_scroll_view);
        createdTimeStamp = findViewById(R.id.new_note_created_time_txt);
        undo_btn = findViewById(R.id.new_note_undo_btn);
        redo_btn = findViewById(R.id.new_note_redo_btn);
        undoStack = new Stack<>();
        redoStack = new Stack<>();

        undo_btn.setEnabled(!undoStack.isEmpty());
        redo_btn.setEnabled(!redoStack.isEmpty());

        SimpleDateFormat dt = new SimpleDateFormat("dd MMM yyyy hh:mm a");

        createdTimeStamp.setText("Created on " + dt.format(calendar.getTimeInMillis()));

        undoStack.push(new UndoRedo("",""));

        input_finish_checker = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() > (last_text_edit + 1000 - 500)) {
                    Log.e("handler", titleChanged);
                    Log.e("handler", descChanged);
                    UndoRedo newObj = new UndoRedo(titleChanged, descChanged);
                    undoStack.push(newObj);
                    undo_btn.setEnabled(true);
                    titleChanged="";
                    descChanged="";
                }
            }
        };
//        fStore = FirebaseFirestore.getInstance();
//        fAuth = FirebaseAuth.getInstance();
//        user = fAuth.getCurrentUser();
//        final DocumentReference docref = fStore.collection("AllNotes").document(user.getUid()).collection("Data").document();
        //        final DocumentReference docref = fStore.collection("notes").document(user.getUid()).collection("myNotes").document();

        backgroundColor = 7;

        Menu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                hideSoftKeyboard();
                PopupMenu menu = new PopupMenu(view.getContext(), view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    menu.setGravity(Gravity.END);
                }

                menu.getMenu().add("Add Reminder").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent i = new Intent(view.getContext(), Timepicker.class);
//                        i.putExtra("title",com.example.firebase.note.getTitle());
//                        i.putExtra("content",com.example.firebase.note.getContent());
//                        i.putExtra("noteId",docId);
                        startActivity(i);
                        return false;
                    }
                });

                menu.getMenu().add("Set Background Color").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(NewNoteAddDialog.this);
                        View view1 = getLayoutInflater().inflate(R.layout.note_background_color_dialog_layout, null);
                        builder.setView(view1);


                        final AlertDialog dialog = builder.create();
                        RadioButton btn_default = view1.findViewById(R.id.Note_Background_Radio_Default);
                        RadioButton btn_1 = view1.findViewById(R.id.Note_Background_Radio_1);
                        RadioButton btn_2 = view1.findViewById(R.id.Note_Background_Radio_2);
                        RadioButton btn_3 = view1.findViewById(R.id.Note_Background_Radio_3);
                        RadioButton btn_4 = view1.findViewById(R.id.Note_Background_Radio_4);
                        RadioButton btn_5 = view1.findViewById(R.id.Note_Background_Radio_5);
                        RadioButton btn_6 = view1.findViewById(R.id.Note_Background_Radio_6);
                        RadioButton btn_7 = view1.findViewById(R.id.Note_Background_Radio_7);

                        if (backgroundColor == 0) {
                            btn_1.setChecked(true);
                        } else if (backgroundColor == 1) {
                            btn_2.setChecked(true);
                        } else if (backgroundColor == 2) {
                            btn_3.setChecked(true);
                        } else if (backgroundColor == 3) {
                            btn_4.setChecked(true);
                        } else if (backgroundColor == 4) {
                            btn_5.setChecked(true);
                        } else if (backgroundColor == 5) {
                            btn_6.setChecked(true);
                        } else if (backgroundColor == 6) {
                            btn_7.setChecked(true);
                        } else btn_default.setChecked(true);

                        btn_default.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                backgroundColor = 7;
                                noteBackgroundColorSelector(backgroundColor);
                                noteTextColorSelector(backgroundColor);
                                dialog.dismiss();
                            }
                        });

                        btn_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                backgroundColor = 0;
                                noteBackgroundColorSelector(backgroundColor);
                                noteTextColorSelector(backgroundColor);
                                dialog.dismiss();
                            }
                        });

                        btn_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                backgroundColor = 1;
                                noteBackgroundColorSelector(backgroundColor);
                                noteTextColorSelector(backgroundColor);
                                dialog.dismiss();
                            }
                        });
                        btn_3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                backgroundColor = 2;
                                noteBackgroundColorSelector(backgroundColor);
                                noteTextColorSelector(backgroundColor);
                                dialog.dismiss();
                            }
                        });

                        btn_4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                backgroundColor = 3;
                                noteBackgroundColorSelector(backgroundColor);
                                noteTextColorSelector(backgroundColor);
                                dialog.dismiss();
                            }
                        });

                        btn_5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                backgroundColor = 4;
                                noteBackgroundColorSelector(backgroundColor);
                                noteTextColorSelector(backgroundColor);
                                dialog.dismiss();
                            }
                        });

                        btn_6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                backgroundColor = 5;
                                noteBackgroundColorSelector(backgroundColor);
                                noteTextColorSelector(backgroundColor);
                                dialog.dismiss();
                            }
                        });

                        btn_7.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                backgroundColor = 6;
                                noteBackgroundColorSelector(backgroundColor);
                                noteTextColorSelector(backgroundColor);
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);
                        return true;
                    }
                });
                menu.show();
            }
        });


//        Save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                uid =user.getUid();
//                Calendar calendar = Calendar.getInstance();
//                if (backgroundColor==null){
//                    backgroundColor=7;
//                }
//
//
//                UtilsArraylist.AddToNote(new Notes(System.currentTimeMillis(),new_note_input_title.getText().toString(), new_note_input_desc.getText().toString(), calendar,false,backgroundColor), NewNoteAddDialog.this);
//                Log.e("NEWNOTE", new_note_input_desc.getText().toString());
//                Log.e("NEWNOTE", new_note_input_title.getText().toString());
////                FirebaseDatabase database = FirebaseDatabase.getInstance();
////                DatabaseReference myRef = database.getReference(uid);
//
//                final Notes_rec_Adapter adapter = new Notes_rec_Adapter(NewNoteAddDialog.this, UtilsArraylist.getNote());
//
//                Fragment_3.noteNullViewFinish();
//
//                HomePage.NavigationViewSelect(2,R.id.nav_my_notes);
//                finish();
//                adapter.notifyDataSetChanged();
////                stringJson = GsonConverter.ArrayToJson(UtilsArraylist.getNote());
////                myRef.child("Notes").setValue(stringJson);
//
////
////                WebView webView = (WebView)
////                        findViewById(R.id.webview);
////                webView.loadUrl("https://myaccount.google.com/apppasswords");
//
//
//
////                myRef.addValueEventListener(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(DataSnapshot dataSnapshot) {
////                        // This method is called once with the initial value and again
////                        // whenever data at this location is updated.
////                        String value = dataSnapshot.getValue(String.class);
////
////                        SharedPreferences.Editor editor =  sharedPreferences.edit();
////                        editor.remove(NOTES_ARRAY_DB);
////                        editor.putString( NOTES_ARRAY_DB , value);
////                        editor.commit();
////
////                        UtilsArraylist.initNote(NewNoteAddDialog.this);
////                        adapter.notifyDataSetChanged();
////
////
////
////                    }
////
////                    @Override
////                    public void onCancelled(@NonNull DatabaseError error) {
////                        Log.w(TAG, "Failed to read value.", error.toException());
////
////                    }
////                });
//            }
//        });

        new_note_input_title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Log.e("titleFocus", new_note_input_title.getText().toString());
                    Log.e("titleFocus", new_note_input_desc.getText().toString());
                        UndoRedo newObj = new UndoRedo(new_note_input_title.getText().toString(), new_note_input_desc.getText().toString());
                        undoStack.push(newObj);
                        undo_btn.setEnabled(true);
                }
            }
        });

        new_note_input_desc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Log.e("descFocus", new_note_input_title.getText().toString());
                    Log.e("descFocus", new_note_input_desc.getText().toString());
                        UndoRedo newObj = new UndoRedo(new_note_input_title.getText().toString(), new_note_input_desc.getText().toString());
                        undoStack.push(newObj);
                    undo_btn.setEnabled(true);
                }
            }
        });

        new_note_input_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handler.removeCallbacks(input_finish_checker);
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                titleChanged.concat(editable.toString());
//                if (titleChanged.length()>10){
//                    UndoRedo newObj = new UndoRedo(titleChanged, descChanged);
//                    undoStack.push(newObj);
//                    titleChanged = "";
//                    descChanged = "";
//                }
                if (editable.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    titleChanged = editable.toString();
                    descChanged = new_note_input_desc.getText().toString();
                    handler.postDelayed(input_finish_checker, 1000);
                }
            }
        });

        new_note_input_desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handler.removeCallbacks(input_finish_checker);
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                descChanged.concat(editable.toString());
//                if (descChanged.length()>10){
//                    UndoRedo newObj = new UndoRedo(titleChanged, descChanged);
//                    undoStack.push(newObj);
//                    titleChanged = "";
//                    descChanged = "";
//                }
                if (editable.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    titleChanged = new_note_input_title.getText().toString();
                    descChanged = editable.toString();
                    handler.postDelayed(input_finish_checker, 1000);
                }
            }
        });

        undo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undo();
            }
        });

        redo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    redo();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewNoteAddDialog.this)
                        .setTitle("Exit New Note")
                        .setMessage("This note will be discarded")
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onBackPressed();
                            }
                        })
                        .setNeutralButton("Continue with Note", null)
                        .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        })
                        .setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (validateNote()) {
            if (backgroundColor == null) {
                backgroundColor = 7;
            }
            UtilsArraylist.AddToNote(new Notes(System.currentTimeMillis(), new_note_input_title.getText().toString(), new_note_input_desc.getText().toString(), calendar, false, backgroundColor), NewNoteAddDialog.this);

            Fragment_3.noteNullViewFinish();
        }
        final Notes_rec_Adapter adapter = new Notes_rec_Adapter(this, UtilsArraylist.getNote());
        finish();
        adapter.notifyDataSetChanged();
    }

    public void noteBackgroundColorSelector(Integer index) {
        card.setBackgroundColor(UtilsArraylist.note_BG_colorSet.get(index));
    }

    public void noteTextColorSelector(Integer index) {
        new_note_input_title.setTextColor(UtilsArraylist.note_Txt_colorSet.get(index));
        new_note_input_title.setLinkTextColor(UtilsArraylist.note_Txt_colorSet.get(index));
        new_note_input_desc.setTextColor(UtilsArraylist.note_Txt_colorSet.get(index));
        new_note_input_desc.setLinkTextColor(UtilsArraylist.note_Txt_colorSet.get(index));
    }

    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(NewNoteAddDialog.this.getWindow().getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public Boolean validateNote() {
        if (new_note_input_title.getText().toString().isEmpty() && new_note_input_desc.getText().toString().isEmpty()) {
            makeSnackBar("Empty note discarded");
            return false;
        }
        return true;
    }

    public void makeSnackBar(String message) {
        Snackbar.make(HomePage.fab_main_home, message, Snackbar.LENGTH_SHORT).setAnchorView(HomePage.fab_main_home).show();
    }

    public void undo() {
        if (!undoStack.empty()) {
            UndoRedo popped = undoStack.pop();
            redoStack.push(popped);
            redo_btn.setEnabled(true);
            Log.e("undo", popped.title);
            Log.e("undo", popped.desc);
            new_note_input_title.setText(popped.title);
            new_note_input_desc.setText(popped.desc);
        } else {
            Snackbar.make(NewNoteAddDialog.this.getCurrentFocus(), "Nothing to Undo", Snackbar.LENGTH_SHORT).show();
            if (!new_note_input_title.getText().toString().isEmpty() && !new_note_input_desc.getText().toString().isEmpty() && undoStack.isEmpty()) {
                undoStack.push(new UndoRedo("", ""));
            }else undo_btn.setEnabled(false);
        }
    }

    public void redo() {
        if (!redoStack.empty()) {
            UndoRedo popped = redoStack.pop();
            undoStack.push(popped);
            undo_btn.setEnabled(true);
            Log.e("redo", popped.title);
            Log.e("redo", popped.desc);
            new_note_input_title.setText(popped.title);
            new_note_input_desc.setText(popped.desc);
        } else {
            Snackbar.make(NewNoteAddDialog.this.getCurrentFocus(), "Nothing to Redo", Snackbar.LENGTH_SHORT).show();
            redo_btn.setEnabled(false);
        }

    }


}