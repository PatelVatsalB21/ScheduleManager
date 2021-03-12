package com.example.schedulemanager;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.schedulemanager.Setting.Settings_Main;
import com.example.schedulemanager.note.Notes;
import com.example.schedulemanager.note.UtilsArraylist;

import java.util.Calendar;

public class PopUpDialogService extends Service {

    private WindowManager mWindowManager;
    private View mFloatingWidget;
    Point size;
    EditText title, editText;
    EditText desc;
    TextView head;
    Button dismiss;
    Button save;
    ClipboardManager manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent closeDrawerIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        PopUpDialogService.this.sendBroadcast(closeDrawerIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(PopUpDialogService.this)) {
            Toast.makeText(PopUpDialogService.this, "Draw over other apps permission denied", Toast.LENGTH_SHORT).show();
        } else {

            mFloatingWidget = LayoutInflater.from(this).inflate(R.layout.activity_pop_up__dialog, null);
            final WindowManager.LayoutParams params;

            int LAYOUT_FLAG;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
            }


            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT);
            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = 0;
            params.y = 0;

            params.flags =  WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;


            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            mWindowManager.addView(mFloatingWidget, params);
            Display display = mWindowManager.getDefaultDisplay();
            size = new Point();
            display.getSize(size);

            ViewTreeObserver vto = mFloatingWidget.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mFloatingWidget.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

            title = mFloatingWidget.findViewById(R.id.PopUpDialog_Title);
            desc = mFloatingWidget.findViewById(R.id.PopUpDialog_Desc);
            save = mFloatingWidget.findViewById(R.id.PopUpDialog_Save_btn);
            dismiss = mFloatingWidget.findViewById(R.id.PopUpDialog_Dismiss_btn);
            head = mFloatingWidget.findViewById(R.id.PopUpDialog_Head);

            if (Settings_Main.QuickNoteTextColor != null) {
                head.setTextColor(getResources().getColor(Settings_Main.QuickNoteTextColor));
                title.setTextColor(getResources().getColor(Settings_Main.QuickNoteTextColor));
                title.setHintTextColor(getResources().getColor(Settings_Main.QuickNoteTextColor));
                Log.e("QUICKNOTEPOPUPCOLOR", String.valueOf(Settings_Main.QuickNoteTextColor));
                desc.setTextColor(getResources().getColor(Settings_Main.QuickNoteTextColor));
                desc.setHintTextColor(getResources().getColor(Settings_Main.QuickNoteTextColor));
                save.setTextColor(getResources().getColor(Settings_Main.QuickNoteTextColor));
                dismiss.setTextColor(getResources().getColor(Settings_Main.QuickNoteTextColor));
            }else{
                Settings_Main.LoadChangedSettings(PopUpDialogService.this);
                Settings_Main.QuickNoteTextColor = R.color.color_yellow;
                Settings_Main.SaveSettings(PopUpDialogService.this);
            }

            ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (manager != null && manager.getPrimaryClip() != null && manager.getPrimaryClip().getItemCount() > 0) {
                        title.append(manager.getPrimaryClip().getItemAt(0).getText().toString());
                        Toast.makeText(PopUpDialogService.this, "Text Pasted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (manager != null && manager.getPrimaryClip() != null && manager.getPrimaryClip().getItemCount() > 0) {
                        desc.append(manager.getPrimaryClip().getItemAt(0).getText().toString());
                        Toast.makeText(PopUpDialogService.this, "Text Pasted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipData clipData = ClipData.newPlainText("text", title.getText().toString());
                    if (manager != null) {
                        manager.setPrimaryClip(clipData);
                        Toast.makeText(PopUpDialogService.this, "Text copied to clipboard ", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });

            desc.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipData clipData = ClipData.newPlainText("text", desc.getText().toString());
                    if (manager != null) {
                        manager.setPrimaryClip(clipData);
                        Toast.makeText(PopUpDialogService.this, "Text copied to clipboard ", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });

            mFloatingWidget.setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;

                        case MotionEvent.ACTION_UP:
                            int Xdiff = (int) (event.getRawX() - initialTouchX);
                            int Ydiff = (int) (event.getRawY() - initialTouchY);
                            if (Xdiff < 10 && Ydiff < 10) {
                            }
                            return true;

                        case MotionEvent.ACTION_MOVE:
                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);
                            mWindowManager.updateViewLayout(mFloatingWidget, params);
                            return true;
                    }
                    return false;
                }
            });

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar calendar = Calendar.getInstance();
                    UtilsArraylist.AddToNote(new Notes(System.currentTimeMillis(), title.getText().toString(), desc.getText().toString(), calendar, false, 7), PopUpDialogService.this);
                    stopSelf();
                }
            });

            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopSelf();
                }
            });

        }
    }
        @Override
        public void onDestroy () {
            super.onDestroy();
            if (mFloatingWidget != null) mWindowManager.removeView(mFloatingWidget);
        }


//    class copyPasteCallback implements ActionMode.Callback {
//
//        @Override
//        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            mode.getMenuInflater().inflate(R.menu.copy_paste_menu, menu);
//             manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//            return true;
//        }
//
//        @Override
//        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            return false;
//        }
//
//        @Override
//        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//
//            int id = item.getItemId();
//            if(id == R.id.copy_paste_item_copy){
//                int startSelection = editText.getSelectionStart();
//                int endSelection = editText.getSelectionEnd();
//
//                String selectedText = editText.getText().toString().substring(startSelection, endSelection);
//                ClipData clipData = ClipData.newPlainText("text", selectedText);
//                if (manager != null) {
//                    manager.setPrimaryClip(clipData);
//                    Toast.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();
//                } else
//                    Toast.makeText(getApplicationContext(), "No text to be copied", Toast.LENGTH_SHORT).show();
//            }else if (id == R.id.copy_paste_item_paste){
//                ClipData clipData = manager.getPrimaryClip();
//                if (clipData != null) {
//                    ClipData.Item clipitem = clipData.getItemAt(0);
//                    editText.append(clipitem.getText().toString());
//                    Toast.makeText(getApplicationContext(), "Text Pasted", Toast.LENGTH_SHORT).show();
//
//                } else
//                    Toast.makeText(getApplicationContext(), "ClipBoard is empty", Toast.LENGTH_SHORT).show();
//            }else mode.finish();
//
//
//            return false;
//        }
//
//        @Override
//        public void onDestroyActionMode(ActionMode mode) {
//
//        }
//    }

}
