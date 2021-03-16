package com.example.schedulemanager.note;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedulemanager.MainFragments.HomePage;
import com.example.schedulemanager.NotifierService;
import com.example.schedulemanager.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Stack;

public class Note_Open extends AppCompatActivity {

    RelativeLayout card;
    TextView modifiedTimeStamp;
    ImageButton Back, menu_btn, undo_btn, redo_btn;
    EditText note_title, note_desc;
    Integer note_position, backgroundColor;
    Stack<UndoRedo> undoStack, redoStack;
    String titleChanged = "", descChanged = "", title, desc;
    long last_text_edit = 0;
    Handler handler = new Handler();
    Runnable input_finish_checker;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            note_position = extras.getInt("position");
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_open);
        UtilsArraylist.initNote(Note_Open.this);
        stopService(new Intent(Note_Open.this, NotifierService.class));
        Back = findViewById(R.id.note_open_back);
        note_desc = findViewById(R.id.note_open_desc);
        note_title = findViewById(R.id.note_open_title);
        menu_btn = findViewById(R.id.note_open_menu_btn);
        card = findViewById(R.id.notes_open_rel_layout);
        modifiedTimeStamp = findViewById(R.id.note_open_timeStamp);
        undo_btn = findViewById(R.id.note_open_undo_btn);
        redo_btn = findViewById(R.id.note_open_redo_btn);
        undoStack = new Stack<>();
        redoStack = new Stack<>();

        Intent intent = getIntent();
        if (intent != null) {
            note_position = intent.getIntExtra("position", -1);
            if (note_position > -1) {
                title = UtilsArraylist.getNote().get(note_position).title;
                desc = UtilsArraylist.getNote().get(note_position).desc;
                SimpleDateFormat dt = new SimpleDateFormat("dd MMM yyyy hh:mm a");
                modifiedTimeStamp.setText("Last modified on " + dt.format(
                        UtilsArraylist.getNote().get(note_position).lastEdited.getTimeInMillis()));
                note_title.setText(title);
                note_desc.setText(desc);
                undo_btn.setEnabled(false);
                redo_btn.setEnabled(false);
                undoStack.push(new UndoRedo(title, desc));

                input_finish_checker = () -> {
                    if (System.currentTimeMillis() > (last_text_edit + 500)) {
                        Log.e("handler", titleChanged);
                        Log.e("handler", descChanged);
                        UndoRedo newObj = new UndoRedo(titleChanged, descChanged);
                        undoStack.push(newObj);
                        undo_btn.setEnabled(true);
                        titleChanged = "";
                        descChanged = "";
                    }
                };

                if (UtilsArraylist.note.get(note_position).BackgroundColor != null) {
                    backgroundColor = UtilsArraylist.note.get(note_position).BackgroundColor;
                    noteBackgroundColorSelector(backgroundColor);
                    noteTextColorSelector(backgroundColor);
                }

                menu_btn.setOnClickListener(view -> {
                    hideSoftKeyboard();
                    PopupMenu menu = new PopupMenu(view.getContext(), view);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        menu.setGravity(Gravity.END);
                    }

                    menu.getMenu().add("Save as PDF").setOnMenuItemClickListener(menuItem -> {
                        String finalShare = "";
                        if (title != null) {
                            finalShare = "Title: " + "\n" + title;
                        }
                        if (desc != null) {
                            finalShare = finalShare + "\n" + "\n" + "Note: " + "\n" + desc;
                        }

                        String idShare = "";
                        if (String.valueOf(UtilsArraylist.note.get(note_position).id).length()
                                > 5) {
                            idShare = String.valueOf(
                                    UtilsArraylist.note.get(note_position).id).substring(0, 4);
                        } else {
                            idShare = String.valueOf(UtilsArraylist.note.get(note_position).id);
                        }

                        if (!finalShare.isEmpty()) {
                            String fileName;
                            if (title != null) {
                                if (title.length() > 5) {
                                    fileName = title.substring(0, 4).concat(idShare);
                                } else {
                                    fileName = title.concat(idShare);
                                }
                            } else if (desc != null) {
                                if (desc.length() > 5) {
                                    fileName = desc.substring(0, 4).concat(idShare);
                                } else {
                                    fileName = desc.concat(idShare);
                                }

                            } else {
                                fileName = "notes".concat(idShare);
                            }

                            PDF_Creator pdf_creator = new PDF_Creator(Note_Open.this, finalShare,
                                    fileName);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                pdf_creator.createMyPDF();
                            }
                        }
                        return true;
                    });

                    menu.getMenu().add("Set Background Color").setOnMenuItemClickListener(
                            menuItem -> {
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        Note_Open.this);
                                View view1 = getLayoutInflater().inflate(
                                        R.layout.note_background_color_dialog_layout, null);
                                builder.setView(view1);
                                final AlertDialog dialog = builder.create();
                                RadioButton btn_default = view1.findViewById(
                                        R.id.Note_Background_Radio_Default);
                                RadioButton btn_1 = view1.findViewById(
                                        R.id.Note_Background_Radio_1);
                                RadioButton btn_2 = view1.findViewById(
                                        R.id.Note_Background_Radio_2);
                                RadioButton btn_3 = view1.findViewById(
                                        R.id.Note_Background_Radio_3);
                                RadioButton btn_4 = view1.findViewById(
                                        R.id.Note_Background_Radio_4);
                                RadioButton btn_5 = view1.findViewById(
                                        R.id.Note_Background_Radio_5);
                                RadioButton btn_6 = view1.findViewById(
                                        R.id.Note_Background_Radio_6);
                                RadioButton btn_7 = view1.findViewById(
                                        R.id.Note_Background_Radio_7);

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
                                } else {
                                    btn_default.setChecked(true);
                                }

                                btn_default.setOnClickListener(view2 -> {
                                    backgroundColor = 7;
                                    noteBackgroundColorSelector(backgroundColor);
                                    noteTextColorSelector(backgroundColor);
                                    dialog.dismiss();
                                });

                                btn_1.setOnClickListener(view2 -> {
                                    backgroundColor = 0;
                                    noteBackgroundColorSelector(backgroundColor);
                                    noteTextColorSelector(backgroundColor);
                                    dialog.dismiss();
                                });

                                btn_2.setOnClickListener(view2 -> {
                                    backgroundColor = 1;
                                    noteBackgroundColorSelector(backgroundColor);
                                    noteTextColorSelector(backgroundColor);
                                    dialog.dismiss();
                                });

                                btn_3.setOnClickListener(view2 -> {
                                    backgroundColor = 2;
                                    noteBackgroundColorSelector(backgroundColor);
                                    noteTextColorSelector(backgroundColor);
                                    dialog.dismiss();
                                });

                                btn_4.setOnClickListener(view2 -> {
                                    backgroundColor = 3;
                                    noteBackgroundColorSelector(backgroundColor);
                                    noteTextColorSelector(backgroundColor);
                                    dialog.dismiss();
                                });

                                btn_5.setOnClickListener(view2 -> {
                                    backgroundColor = 4;
                                    noteBackgroundColorSelector(backgroundColor);
                                    noteTextColorSelector(backgroundColor);
                                    dialog.dismiss();
                                });

                                btn_6.setOnClickListener(view2 -> {
                                    backgroundColor = 5;
                                    noteBackgroundColorSelector(backgroundColor);
                                    noteTextColorSelector(backgroundColor);
                                    dialog.dismiss();
                                });

                                btn_7.setOnClickListener(view2 -> {
                                    backgroundColor = 6;
                                    noteBackgroundColorSelector(backgroundColor);
                                    noteTextColorSelector(backgroundColor);
                                    dialog.dismiss();
                                });

                                dialog.show();
                                dialog.getWindow().setLayout(400,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                return true;
                            });
                    menu.show();
                });

                note_title.setOnFocusChangeListener((view, hasFocus) -> {
                    if (!hasFocus) {
                        UndoRedo newObj = new UndoRedo(note_title.getText().toString(),
                                note_desc.getText().toString());
                        undo_btn.setEnabled(true);
                        undoStack.push(newObj);
                    }
                });

                note_desc.setOnFocusChangeListener((view, hasFocus) -> {
                    if (!hasFocus) {
                        UndoRedo newObj = new UndoRedo(note_title.getText().toString(),
                                note_desc.getText().toString());
                        undo_btn.setEnabled(true);
                        undoStack.push(newObj);
                    }
                });

                note_title.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                            int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        handler.removeCallbacks(input_finish_checker);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() > 0) {
                            last_text_edit = System.currentTimeMillis();
                            titleChanged = editable.toString();
                            descChanged = note_desc.getText().toString();
                            handler.postDelayed(input_finish_checker, 1000);
                        }
                    }
                });
                note_desc.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                            int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        handler.removeCallbacks(input_finish_checker);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() > 0) {
                            last_text_edit = System.currentTimeMillis();
                            titleChanged = note_title.getText().toString();
                            descChanged = editable.toString();
                            handler.postDelayed(input_finish_checker, 1000);
                        }
                    }
                });

                undo_btn.setOnClickListener(view -> undo());

                redo_btn.setOnClickListener(view -> redo());

                Back.setOnClickListener(view -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Note_Open.this)
                            .setTitle("Exit")
                            .setMessage("Any unsaved changes will be discarded")
                            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onBackPressed();
                                }
                            })
                            .setNeutralButton("Continue Editing", null)
                            .setNegativeButton("Exit", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                                startActivity(new Intent(Note_Open.this, HomePage.class).putExtra(
                                        "num", 2).putExtra("nav", R.id.nav_my_notes));
                                finish();
                            })
                            .setCancelable(false);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(
                            getResources().getColor(R.color.colorPrimaryDark));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                            getResources().getColor(R.color.colorPrimaryDark));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                            getResources().getColor(R.color.colorPrimaryDark));
                });

                note_desc.setMovementMethod(new Note_Open.TextViewLinkHandler() {
                    @Override
                    public void onLinkClick(String url) {
                        showUrlOptions(Note_Open.this, url);
                    }
                });

                note_title.setMovementMethod(new Note_Open.TextViewLinkHandler() {
                    @Override
                    public void onLinkClick(String url) {
                        showUrlOptions(Note_Open.this, url);
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (validateNote(Note_Open.this.getCurrentFocus())) {
            if (!UtilsArraylist.note.get(note_position).title.equals(
                    note_title.getText().toString()) || !UtilsArraylist.note.get(
                    note_position).desc.equals(note_desc.getText().toString())) {
                UtilsArraylist.setNote(new Notes(UtilsArraylist.note.get(note_position).id,
                                note_title.getText().toString(), note_desc.getText().toString(),
                                Calendar.getInstance(), false, backgroundColor), note_position,
                        Note_Open.this);
                Notes_rec_Adapter adapter = new Notes_rec_Adapter(Note_Open.this,
                        UtilsArraylist.getNote());
                adapter.notifyItemChanged(note_position);
                adapter.notifyDataSetChanged();
            }
            finish();
        }
    }

    public void noteBackgroundColorSelector(Integer index) {
        card.setBackgroundColor(UtilsArraylist.note_BG_colorSet.get(index));
    }

    public void noteTextColorSelector(Integer index) {
        note_title.setTextColor(UtilsArraylist.note_Txt_colorSet.get(index));
        note_title.setLinkTextColor(UtilsArraylist.note_Txt_colorSet.get(index));
        note_desc.setTextColor(UtilsArraylist.note_Txt_colorSet.get(index));
        note_desc.setLinkTextColor(UtilsArraylist.note_Txt_colorSet.get(index));
    }

    public abstract static class TextViewLinkHandler extends LinkMovementMethod {

        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {

            if (event.getAction() != MotionEvent.ACTION_UP) {
                return super.onTouchEvent(widget, buffer, event);
            }
            int x = (int) event.getX();
            int y = (int) event.getY();
            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();
            x += widget.getScrollX();
            y += widget.getScrollY();
            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);
            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
            if (link.length != 0) {
                onLinkClick(link[0].getURL());
            }
            return true;
        }

        abstract public void onLinkClick(String url);
    }

    public void showUrlOptions(Context context, String url) {
        CharSequence[] options = {"Open", "Edit"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(url)
                .setItems(options, (dialog, item) -> {
                    if (options[item].equals("Edit")) {
                        dialog.dismiss();
                    } else if (options[item].equals("Open")) {
                        hideSoftKeyboard();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }
                })
                .setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(Note_Open.this.getWindow().getCurrentFocus().getWindowToken(),
                0);
    }

    public Boolean validateNote(View v) {
        if (note_title.getText().toString().isEmpty() && note_desc.getText().toString().isEmpty()) {
            makeSnackBar(v, "Empty note cannot be saved");
            return false;
        }
        return true;
    }

    public void makeSnackBar(View v, String message) {
        Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void undo() {
        if (!undoStack.empty()) {
            UndoRedo popped = undoStack.pop();
            redoStack.push(popped);
            redo_btn.setEnabled(true);
            note_title.setText(popped.title);
            note_desc.setText(popped.desc);
        } else {
            makeSnackBar(Note_Open.this.getCurrentFocus(), "Nothing to Undo");
            if (!note_title.getText().toString().equals(title)
                    && !note_desc.getText().toString().equals(desc) && undoStack.isEmpty()) {
                undoStack.push(new UndoRedo(title, desc));
            } else {
                undo_btn.setEnabled(false);
            }
        }
    }

    public void redo() {
        if (!redoStack.empty()) {
            UndoRedo popped = redoStack.pop();
            undoStack.push(popped);
            undo_btn.setEnabled(true);
            note_title.setText(popped.title);
            note_desc.setText(popped.desc);
        } else {
            makeSnackBar(Note_Open.this.getCurrentFocus(), "Nothing to Redo");
            redo_btn.setEnabled(false);
        }
    }
}

