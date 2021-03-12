package com.example.schedulemanager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FloatingWidget extends Service {
    private WindowManager mWindowManager;
    private View mFloatingWidget;
    ImageButton fab_main,fab1,fab2,fab3,fab4,fab5,fab6,fab7,fab8;
    Boolean Open = false;
    OvershootInterpolator interpolator = new OvershootInterpolator();
    private int mWidth, mHeight;
    Point size;
    private Float bezierConstant = 0.551915024494f;
    private int mFabButtonRadius = FloatingActionButton.SIZE_MINI;
    Animation fab_left_to_right_in ;
    Animation fab_right_to_left_in ;
    Animation fab_right_to_left_out;
    Animation fab_left_to_right_out;
    Animation fab_top_to_bottom_out;
    Animation fab_top_to_bottom_in;
    Animation fab_bottom_to_top_out ;
    Animation fab_bottom_to_top_in ;
    Animation fab_up ;
    Animation fab_down ;


    public FloatingWidget() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingWidget = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        final WindowManager.LayoutParams params;

            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                   PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingWidget, params);
//        final View collapsedView = mFloatingWidget.findViewById(R.id.collapse_view);
//        final View expandedView = mFloatingWidget.findViewById(R.id.expanded_container);

        fab_main = mFloatingWidget.findViewById(R.id.fab_main);
        fab1 = mFloatingWidget.findViewById(R.id.fab_1);
        fab2 = mFloatingWidget.findViewById(R.id.fab_2);
        fab3 = mFloatingWidget.findViewById(R.id.fab_3);
        fab4 = mFloatingWidget.findViewById(R.id.fab_4);
        fab5 = mFloatingWidget.findViewById(R.id.fab_5);
        fab6 = mFloatingWidget.findViewById(R.id.fab_6);
        fab7 = mFloatingWidget.findViewById(R.id.fab_7);
        fab8 = mFloatingWidget.findViewById(R.id.fab_8);

        fab_left_to_right_in = AnimationUtils.loadAnimation(FloatingWidget.this, R.anim.fab_floating_left_to_right_in);
        fab_right_to_left_in = AnimationUtils.loadAnimation(FloatingWidget.this, R.anim.fab_floating_left_to_right_in);
        fab_right_to_left_out = AnimationUtils.loadAnimation(FloatingWidget.this, R.anim.fab_floating_left_to_right_in);
        fab_left_to_right_out = AnimationUtils.loadAnimation(FloatingWidget.this, R.anim.fab_floating_left_to_right_in);
        fab_top_to_bottom_out = AnimationUtils.loadAnimation(FloatingWidget.this, R.anim.fab_floating_top_to_bottom_out);
        fab_top_to_bottom_in = AnimationUtils.loadAnimation(FloatingWidget.this, R.anim.fab_floating_top_to_bottom_in);
        fab_bottom_to_top_out = AnimationUtils.loadAnimation(FloatingWidget.this, R.anim.fab_floating_bottom_to_top_out);
        fab_bottom_to_top_in = AnimationUtils.loadAnimation(FloatingWidget.this, R.anim.fab_floating_bottom_to_top_in);
        fab_up = AnimationUtils.loadAnimation(FloatingWidget.this, R.anim.fab_rotate_up);
        fab_down = AnimationUtils.loadAnimation(FloatingWidget.this, R.anim.fab_rotate_down);



        Display display = mWindowManager.getDefaultDisplay();
         size = new Point();
        display.getSize(size);

        final RelativeLayout layout = (RelativeLayout) mFloatingWidget.findViewById(R.id.root_container);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = layout.getMeasuredWidth();
                int height = layout.getMeasuredHeight();
                //To get the accurate middle of the screen we subtract the width of the floating widget in android.
                mWidth = size.x - width;
                mHeight = size.y - height;

            }
        });
        

//
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(params.x == 0 ||params.x ==mWidth) {
                    openMenuVertical();
                }else if(params.y == 0 ||params.y ==mHeight) {
                    openMenuHorizontal();
                }

            }
        });



//        ImageView closeButtonCollapsed = (ImageView) mFloatingWidget.findViewById(R.id.close_btn);
//        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stopSelf();
//            }
//        });
//        ImageView closeButton = (ImageView) mFloatingWidget.findViewById(R.id.close_btn);
//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                collapsedView.setVisibility(View.VISIBLE);
//                expandedView.setVisibility(View.GONE);
//            }
//        });
        mFloatingWidget.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
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

                        Log.d("FLOATING",String.valueOf(params.x));
                        Log.d("FLOATING",String.valueOf(params.y));
                        Log.d("FLOATING",String.valueOf(initialX));
                        Log.d("FLOATING",String.valueOf(initialY));
                        Log.d("FLOATING",String.valueOf(initialTouchY));
                        Log.d("FLOATING",String.valueOf(initialTouchX));



                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);
                        if (Xdiff < 10 && Ydiff < 10) {

                            if(params.x == 0 ||params.x ==mWidth) {
                                openMenuVertical();
                            }else if(params.y == 0 ||params.y ==mHeight) {
                                openMenuHorizontal();
                            }

//                                fab_main.animate().setInterpolator(interpolator).rotation(45f).setDuration(300);
//                                fab1.animate().translationX(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                                fab2.animate().translationX(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                                fab3.animate().translationX(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                                fab4.animate().translationX(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

//                            if (isViewCollapsed()) {
//                                collapsedView.setVisibility(View.GONE);
//                                expandedView.setVisibility(View.VISIBLE);
//                            }
                        }else{

                            float xDiff = event.getRawX() - initialTouchX;
                            float yDiff = event.getRawY() - initialTouchY;

                            int middle = mWidth / 2;
                            int middleY = mHeight/2;
                            float nearestXWall = params.x > middle ? mWidth : 0;
                            params.x = (int) nearestXWall;
                            float nearestYWall = params.y > middleY ? mHeight : 0;
                            params.y = (int) nearestYWall;



//                            if ((Math.abs(xDiff) < 5) && (Math.abs(yDiff) = middle ? mWidth : 0;
//                            params.x = (int) nearestXWall;



                            mWindowManager.updateViewLayout(mFloatingWidget, params);
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingWidget, params);
                        Log.d("MOVEACTION",String.valueOf(params.x));
                        Log.d("MOVEACTION",String.valueOf(params.y));
                        Log.d("MOVEACTION",String.valueOf(initialX));
                        Log.d("MOVEACTION",String.valueOf(initialY));
                        Log.d("MOVEACTION",String.valueOf(initialTouchY));
                        Log.d("MOVEACTION",String.valueOf(initialTouchX));
                        return true;
                }
                return false;
            }
        });
    }
//    private boolean isViewCollapsed() {
//        return mFloatingWidget == null || mFloatingWidget.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
//    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingWidget != null) mWindowManager.removeView(mFloatingWidget);
    }

    private Path createPath() {
        Path path = new Path();
        float c = bezierConstant * mFabButtonRadius;

        path.moveTo(0, mFabButtonRadius);
        path.cubicTo(bezierConstant * mFabButtonRadius, mFabButtonRadius, mFabButtonRadius, bezierConstant * mFabButtonRadius, mFabButtonRadius, 0);
        path.cubicTo(mFabButtonRadius, bezierConstant * mFabButtonRadius * (-1), c, (-1) * mFabButtonRadius, 0, (-1) * mFabButtonRadius);
        path.cubicTo((-1) * c, (-1) * mFabButtonRadius, (-1) * mFabButtonRadius, (-1) * bezierConstant * mFabButtonRadius, (-1) * mFabButtonRadius, 0);
        path.cubicTo((-1) * mFabButtonRadius, bezierConstant * mFabButtonRadius, (-1) * bezierConstant * mFabButtonRadius, mFabButtonRadius, 0, mFabButtonRadius);

        return path;
    }

    public void openMenuHorizontal() {

        if (Open) {
            Open = false;

            fab_main.startAnimation(fab_down);
            fab5.startAnimation(fab_left_to_right_out);
            fab5.setVisibility(View.GONE);
            fab6.startAnimation(fab_left_to_right_out);
            fab6.setVisibility(View.GONE);
            fab7.startAnimation(fab_right_to_left_out);
            fab7.setVisibility(View.GONE);
            fab8.startAnimation(fab_right_to_left_out);
            fab8.setVisibility(View.GONE);

        } else {
            Open = true;

            fab_main.startAnimation(fab_up);
            fab5.startAnimation(fab_left_to_right_in);
            fab5.setVisibility(View.VISIBLE);
            fab6.startAnimation(fab_left_to_right_in);
            fab6.setVisibility(View.VISIBLE);
            fab7.startAnimation(fab_right_to_left_in);
            fab7.setVisibility(View.VISIBLE);
            fab8.startAnimation(fab_right_to_left_in);
            fab8.setVisibility(View.VISIBLE);

        }
    }

    public void openMenuVertical(){
        if(Open){
            Open = false;

            fab_main.startAnimation(fab_down);
            fab1.startAnimation(fab_top_to_bottom_out);
            fab1.setVisibility(View.GONE);
            fab2.startAnimation(fab_top_to_bottom_out);
            fab2.setVisibility(View.GONE);
            fab3.startAnimation(fab_bottom_to_top_out);
            fab3.setVisibility(View.GONE);
            fab4.startAnimation(fab_bottom_to_top_out);
            fab4.setVisibility(View.GONE);

        }
        else{
            Open = true;

            fab_main.startAnimation(fab_up);
            fab1.startAnimation(fab_top_to_bottom_in);
            fab1.setVisibility(View.VISIBLE);
            fab2.startAnimation(fab_top_to_bottom_in);
            fab2.setVisibility(View.VISIBLE);
            fab3.startAnimation(fab_bottom_to_top_in);
            fab3.setVisibility(View.VISIBLE);
            fab4.startAnimation(fab_bottom_to_top_in);
            fab4.setVisibility(View.VISIBLE);

        }
    }

}