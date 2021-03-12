package com.example.schedulemanager.Widget;//package com.example.firebase.Widget;
//
//import android.app.Service;
//import android.content.Intent;
//import android.graphics.PixelFormat;
//import android.graphics.Point;
//import android.os.IBinder;
//import android.util.Log;
//import android.view.Display;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewTreeObserver;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//
//import androidx.annotation.Nullable;
//
//import com.example.firebase.R;
//import com.example.firebase.note.Notes;
//import com.example.firebase.note.UtilsArraylist;
//
//import java.util.Calendar;
//
//public class PopUpDialogService extends Service {
//
//    private WindowManager mWindowManager;
//    private View mFloatingWidget;
//    Point size;
//    EditText title,desc;
//    Button dismiss,save;
//
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        Log.d("POPUPDIALOGCALLED","True");
//        super.onCreate();
//        mFloatingWidget = LayoutInflater.from(this).inflate(R.layout.activity_pop_up__dialog, null);
//        final WindowManager.LayoutParams params;
//
//        params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//                PixelFormat.TRANSPARENT);
//        params.gravity = Gravity.TOP | Gravity.LEFT;
//        params.x = 0;
//        params.y = 0;
//        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//        mWindowManager.addView(mFloatingWidget, params);
//        Display display = mWindowManager.getDefaultDisplay();
//        size = new Point();
//        display.getSize(size);
//        ViewTreeObserver vto = mFloatingWidget.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mFloatingWidget.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            }
//        });
//
//        title = mFloatingWidget.findViewById(R.id.PopUpDialog_Title);
//        desc = mFloatingWidget.findViewById(R.id.PopUpDialog_Desc);
//        save = mFloatingWidget.findViewById(R.id.PopUpDialog_Save_btn);
//        dismiss = mFloatingWidget.findViewById(R.id.PopUpDialog_Dismiss_btn);
//
//        mFloatingWidget.setOnTouchListener(new View.OnTouchListener() {
//            private int initialX;
//            private int initialY;
//            private float initialTouchX;
//            private float initialTouchY;
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        initialX = params.x;
//                        initialY = params.y;
//                        initialTouchX = event.getRawX();
//                        initialTouchY = event.getRawY();
//                        return true;
//
//                    case MotionEvent.ACTION_UP:
//                        int Xdiff = (int) (event.getRawX() - initialTouchX);
//                        int Ydiff = (int) (event.getRawY() - initialTouchY);
//                        if (Xdiff < 10 && Ydiff < 10) {
//                        }
//                        return true;
//
//                    case MotionEvent.ACTION_MOVE:
//                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
//                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
//                        mWindowManager.updateViewLayout(mFloatingWidget, params);
//                        return true;
//                }
//                return false;
//            }
//        });
//
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar calendar = Calendar.getInstance();
//                UtilsArraylist.AddToNote(new Notes(System.currentTimeMillis(),title.getText().toString(), desc.getText().toString(),calendar,false,7), PopUpDialogService.this);
//                stopSelf();
//            }
//        });
//
//        dismiss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stopSelf();
//            }
//        });
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mFloatingWidget != null) mWindowManager.removeView(mFloatingWidget);
//    }
//
//}
