package com.example.schedulemanager.Task;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.schedulemanager.MainFragments.Fragment_2;
import com.example.schedulemanager.NotifierService;
import com.example.schedulemanager.R;
import com.example.schedulemanager.Setting.Settings_Main;
import com.example.schedulemanager.note.UtilsArraylist;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static com.example.schedulemanager.Task.UtilsArray_Task.task;

public class FullScreenTaskReminder extends Activity {

    FloatingActionButton btnLeft, btnRight , centre;
    RelativeLayout revealView;
    RelativeLayout normalView;
    TextView head, title, time, snoozeText;
    LottieAnimationView lottie;
    SimpleDateFormat d;
    CircularRippleEffect circularRippleEffect;
    Task taskAssigned;
    Boolean dismissed = false;
    long starttime;
    AnimatorSet animatorSet;
    //    private Animator rippleAnimator;
    KonfettiView konfettiView, konfettiView1;

    private static final int INNER_CIRCLE_ANIMATOR_DURATION = 500;
    private static final int INNER_CIRCLE_ANIMATOR_START_DELAY = 250;
    private static final int OUTER_CIRCLE_ANIMATOR_DURATION = 500;
    private static final float ANIMATOR_START_VALUE = 0.1f;
    private static final float ANIMATOR_END_VALUE = 1f;
    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    String TAG = "FullScreen";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "Called");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.full_screen_task_layout);

//        seekBar = findViewById(R.id.full_screen_task_seekBar);
//        seekBar.setProgress(50);
        UtilsArray_Task.initTask(FullScreenTaskReminder.this);
        long id = getIntent().getLongExtra("id", -1);
        Log.e("TaskID", String.valueOf(id));
        taskAssigned = Task.findTaskByID(id);

        if (taskAssigned != null) {

            btnLeft = findViewById(R.id.full_screen_task_btn1);
            btnRight = findViewById(R.id.full_screen_task_btn2);
            centre = findViewById(R.id.full_screen_task_btn3);
            revealView = findViewById(R.id.full_screen_task_reveal_view);
            normalView = findViewById(R.id.full_screen_task_normal_view);
            head = findViewById(R.id.full_screen_task_head_txt);
            title = findViewById(R.id.full_screen_task_title_txt);
            time = findViewById(R.id.full_screen_task_time);
            lottie = findViewById(R.id.full_screen_task_lottie_view);
            d = new SimpleDateFormat("MMM dd   hh:mm:a");
            konfettiView = findViewById(R.id.full_screen_task_Konfetti);
            konfettiView1 = findViewById(R.id.full_screen_task_Konfetti_2);
            snoozeText = findViewById(R.id.full_screen_task_snooze_txt);

            ButterKnife.bind(this);
            circularRippleEffect = findViewById(R.id.full_screen_task_ripple_container);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(FullScreenTaskReminder.this)) {
                Toast.makeText(FullScreenTaskReminder.this, "Draw over other apps permission denied", Toast.LENGTH_SHORT).show();
                Log.e(TAG,"Sending Notification");
                TaskReceiver.sendNotification(taskAssigned, taskAssigned.getId().intValue(),FullScreenTaskReminder.this);
                finishAffinity();
            } else {

                title.setText(taskAssigned.getTitle());
                time.setText(d.format(taskAssigned.getCalendar().getTimeInMillis()));
                lottie.setAnimation(UtilsArray_Task.category.get(taskAssigned.LottieFileRes).LottieRes);;
                head.setText("It's time for "+UtilsArray_Task.category.get(taskAssigned.LottieFileRes).Category);
                startService(new Intent(FullScreenTaskReminder.this, NotifierService.class));
                starttime = System.currentTimeMillis();

//                rippleContainer.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        rippleContainer.setButtonSelected(!rippleContainer.isButtonSelected());
//                    }
//                });
//                rippleAnimator = createRippleAnimation(circularRippleEffect);
//                startRipple();
//                rippleRevealView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        bigReveal(rippleContainer, rippleRevealView);
//                    }
//                });

                createRippleAnimation(circularRippleEffect);

                centre.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        animatorSet.end();
                        float initialX = 0;

                        int action = event.getActionMasked();

                        switch (action) {

                            case MotionEvent.ACTION_DOWN:
                                initialX = event.getX();
                                Log.d(TAG, "Action was DOWN");
                                break;

                            case MotionEvent.ACTION_MOVE:
                                if (initialX < event.getX() && event.getX() - initialX >= 30.0) {
                                    expandFab(btnRight, event.getX() - initialX);
                                    collapseFab(centre, event.getX() - initialX);
//                                    btnRight.animate().setDuration(500).scaleX(1.3f).scaleY(1.3f).setInterpolator(new OvershootInterpolator()).start();
//                                    centre.animate().setDuration(700).scaleX(0.1f).scaleY(0.1f).setInterpolator(new OvershootInterpolator()).start();
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            revealFAB(revealView,normalView, btnRight);
//                                        }
//                                    }, 500);
                                    Log.d("Left to Right", String.valueOf(event.getX() - initialX));
                                }

                                if (initialX > event.getX() && initialX - event.getX() >= 30.0) {
                                    expandFab(btnLeft, initialX - event.getX());
                                    collapseFab(centre, initialX - event.getX());
//                                    btnLeft.animate().setDuration(500).scaleX(1.3f).scaleY(1.3f).setInterpolator(new OvershootInterpolator()).start();
//                                    centre.animate().setDuration(700).scaleX(0.1f).scaleY(0.1f).setInterpolator(new OvershootInterpolator()).start();
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            revealFAB(revealView,normalView, btnRight);
//                                        }
//                                    }, 500);
                                    Log.d("Right to Left", String.valueOf(initialX - event.getX()));
                                }
                                Log.d(TAG, "Action was MOVE");
                                break;

                            case MotionEvent.ACTION_UP:
                                float finalX = event.getX();
                                Log.d(TAG, "Action was UP");

                                if (initialX < finalX) {
                                    if (btnRight.getScaleX() > 1.3f) {
                                        btnLeft.hide();
                                        centre.hide();
                                        snoozeText.setText("Task Completed");
//                                    btnRight.animate().setDuration(500).scaleX(1.3f).scaleY(1.3f).setInterpolator(new OvershootInterpolator()).start();
//                                    centre.animate().setDuration(700).scaleX(0.1f).scaleY(0.1f).setInterpolator(new OvershootInterpolator()).start();
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//
//                                        }
//                                    }, 500);
                                        revealFAB(revealView, normalView, btnRight);
                                        konfettiView.build()
                                                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                                                .addColors(UtilsArraylist.note_BG_colorSet)
                                                .setDirection(0.0, 360.0)
                                                .setSpeed(1f, 4f)
                                                .setFadeOutEnabled(true)
                                                .setTimeToLive(3000L)
                                                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                                                .addSizes(new Size(6, 5f), new Size(8, 6f))
                                                .setPosition(0, 30)
                                                .burst(600);

                                        konfettiView1.build()
                                                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                                                .addColors(UtilsArraylist.note_BG_colorSet)
                                                .setDirection(0.0, 360.0)
                                                .setSpeed(1f, 4f)
                                                .setFadeOutEnabled(true)
                                                .setTimeToLive(3000L)
                                                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                                                .addSizes(new Size(6, 5f), new Size(8, 6f))
                                                .setPosition(konfettiView1.getWidth(), 30)
                                                .burst(600);
                                        taskComplete();
//                                        task.get(Task.findPositionByID(id)).isComplete = true;
//                                        UtilsArray_Task.UpdateTask(UtilsArray_Task.getTask(), FullScreenTaskReminder.this);
//                                        Fragment_2.TaskReceiverRefresh(FullScreenTaskReminder.this);

                                    } else {
                                        btnRight.setScaleY(1f);
                                        btnRight.setScaleX(1f);
                                        centre.setScaleY(1f);
                                        centre.setScaleX(1f);
                                        btnLeft.setScaleY(1f);
                                        btnLeft.setScaleX(1f);
                                    }
                                    Log.d(TAG, "Left to Right swipe performed");
                                }

                                if (initialX > finalX) {
                                    if (btnLeft.getScaleX() > 1.3f) {
                                        btnRight.hide();
                                        centre.hide();
                                        snoozeText.setText("Task Snoozed for" + Settings_Main.Snooze_Time +"minutes");
//                                    btnLeft.animate().setDuration(500).scaleX(1.3f).scaleY(1.3f).setInterpolator(new OvershootInterpolator()).start();
//                                    centre.animate().setDuration(700).scaleX(0.1f).scaleY(0.1f).setInterpolator(new OvershootInterpolator()).start();
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//
//                                        }
//                                    }, 500);
                                        revealFAB(revealView, normalView, btnLeft);
                                        Snooze();
                                        Log.d("Right to Left", String.valueOf(initialX - event.getX()));
                                        Log.d(TAG, "Right to Left swipe performed");
                                    } else {
                                        btnRight.setScaleY(1f);
                                        btnRight.setScaleX(1f);
                                        centre.setScaleY(1f);
                                        centre.setScaleX(1f);
                                        btnLeft.setScaleY(1f);
                                        btnLeft.setScaleX(1f);
                                        if (!dismissed)createRippleAnimation(circularRippleEffect);
                                    }
                                }
                                break;

                            case MotionEvent.ACTION_CANCEL:
                                Log.d(TAG, "Action was CANCEL");
                                break;

                            case MotionEvent.ACTION_OUTSIDE:
                                Log.d(TAG, "Movement occurred outside bounds of current screen element");
                                break;
                        }
                        return FullScreenTaskReminder.super.onTouchEvent(event);
                    }
                });


//                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//                    if(seekBar.getProgress()>60){
//                        seekBar.setThumb(getResources().getDrawable(R.drawable.ic_double_done));
//                        btnRight.animate().setDuration(500).scaleX(1.3f).scaleY(1.3f).setInterpolator(new OvershootInterpolator()).start();
//                        seekBar.setProgress(100);
//                        seekBar.getThumb().setAlpha(0);
//                        Toast.makeText(FullScreenTaskReminder.this, "Stop Tracking progress more than 60", Toast.LENGTH_SHORT).show();
//                    }else if (seekBar.getProgress() < 40){
//                        btnRight.animate().setDuration(500).scaleX(1.3f).scaleY(1.3f).setInterpolator(new OvershootInterpolator()).start();
//                        seekBar.setProgress(0);
//                        seekBar.getThumb().setAlpha(0);
//                        Toast.makeText(FullScreenTaskReminder.this, "Stop Tracking progress less than 40", Toast.LENGTH_SHORT).show();
//                    }else {
//                        seekBar.setProgress(50);
//                    }
//
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//                    Toast.makeText(FullScreenTaskReminder.this, "Start Tracking", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int progress,
//                                              boolean fromUser) {
//                    if(progress>60){
//                        seekBar.setThumb(getResources().getDrawable(R.drawable.ic_double_done));
//                    }else if (progress <40){
//                        seekBar.setThumb(getResources().getDrawable(R.drawable.ic_done));
//                    }
//
//                }
//            });
            }
        } else {
            finishAffinity();
            Log.e("TaskNull", String.valueOf(id));
        }
    }

    public void expandFab(FloatingActionButton fab, float diff){
            if (diff <= 700) {
                float percent = diff / 1000;
//        float increasedSize = fab.getWidth() * percent;

                if (percent >= 0.7f) percent = 0.7f;
                Log.e("diff", String.valueOf(diff));
                Log.e("percent", String.valueOf(percent));
//        Log.e("increase", String.valueOf(increasedSize));

                fab.setScaleX(1f + percent);
                fab.setScaleY(1f + percent);
            }

//        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(fab,
//                "scaleX", 1.3f);
//        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(fab,
//                "scaleY", 1.3f);
//
//        AnimatorSet scaleDown = new AnimatorSet();
//        scaleDown.play(scaleDownX).with(scaleDownY);
//
//        scaleDown.start();


    }

    public void collapseFab(FloatingActionButton fab, float diff){
        if (diff <= 700) {
            float percent = diff / 1000;
            if (percent >= 0.7f) percent = 1f;

            Log.e("diff", String.valueOf(diff));
            Log.e("percent", String.valueOf(percent));
//        Log.e("decrease", String.valueOf(reducedSize));

            fab.setScaleX(1f - percent);
            fab.setScaleY(1f - percent);
        }
//        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(fab,
//                "scaleX", 0.8f);
//        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(fab,
//                "scaleY", 0.8f);
//
//        AnimatorSet scaleDown = new AnimatorSet();
//        scaleDown.play(scaleDownX).with(scaleDownY);
//
//        scaleDown.start();
    }

    private void revealFAB(View view1, View view2, FloatingActionButton fab) {
//        View view2 = v.findViewById(R.id.Container);
//        View view1 = v.findViewById(R.id.revealView);

        int cx = (fab.getLeft() + fab.getRight()) / 2;
        int cy = (fab.getTop() + fab.getBottom()) / 2;
        int cxx = view2.getWidth();
        int cyy = view2.getHeight();
        float finalRadius = (float) Math.hypot(cxx, cyy);
        Animator anim = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(view1, cx, cy, 0, finalRadius);
            anim.setDuration(400);
        }
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                snoozeText.setVisibility(View.VISIBLE);
            }
        });
        anim.start();
        view2.animate().alpha(0f).setInterpolator(new OvershootInterpolator()).setDuration(700).start();
        view1.setVisibility(View.VISIBLE);
        stopService(new Intent(FullScreenTaskReminder.this, NotifierService.class));
        dismissed = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finishAffinity();
            }
        }, 8000);
    }

//    public void bigReveal(View layout ,View normalLayout){
//        revealCenter(layout , normalLayout, centre);
//        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(layout, "alpha",  1f, 0f);
//        fadeOut.setDuration(500);
////        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(layout, "alpha", 0f, 1f);
////        fadeIn.setDuration(2000);
//
////        final AnimatorSet mAnimationSet = new AnimatorSet();
//
//        fadeOut.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                bigReveal(layout, normalLayout);
//            }
//        });
//        fadeOut.start();
////        mAnimationSet.play(fadeOut);
//
////        mAnimationSet.addListener(new AnimatorListenerAdapter() {
////            @Override
////            public void onAnimationEnd(Animator animation) {
////                super.onAnimationEnd(animation);
////                mAnimationSet.start();
////            }
////        });
////        mAnimationSet.start();
//    }
//
//    private void revealCenter(View view1, View view2, FloatingActionButton fab) {
////        View view2 = v.findViewById(R.id.Container);
////        View view1 = v.findViewById(R.id.revealView);
//
//        int cx = (fab.getLeft() + fab.getRight()) / 2;
//        int cy = (fab.getTop() + fab.getBottom()) / 2;
//        int cxx = view2.getWidth();
//        int cyy = view2.getHeight();
//        float finalRadius = (float) Math.hypot(cxx, cyy);
//        Animator anim = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            anim = ViewAnimationUtils.createCircularReveal(view1, cx, cy, 0, finalRadius);
//            anim.setDuration(500);
//        }
//        anim.start();
//        view1.setVisibility(View.VISIBLE);
//    }

    @Override
    public void onBackPressed() {
    }

    private void createRippleAnimation(CircularRippleEffect circularRippleEffect) {
        Log.e("Animator", "Called");
        animatorSet = new AnimatorSet();
        ObjectAnimator outerCircleAnimator = ObjectAnimator.ofFloat(circularRippleEffect, CircularRippleEffect.OUTER_CIRCLE_RADIUS_PROGRESS,
                ANIMATOR_START_VALUE, ANIMATOR_END_VALUE);
        outerCircleAnimator.setDuration(OUTER_CIRCLE_ANIMATOR_DURATION);
        outerCircleAnimator.setInterpolator(DECELERATE_INTERPOLATOR);

        ObjectAnimator innerCircleAnimator = ObjectAnimator.ofFloat(circularRippleEffect, CircularRippleEffect.INNER_CIRCLE_RADIUS_PROGRESS,
                ANIMATOR_START_VALUE, ANIMATOR_END_VALUE);
        innerCircleAnimator.setDuration(INNER_CIRCLE_ANIMATOR_DURATION);
        innerCircleAnimator.setStartDelay(INNER_CIRCLE_ANIMATOR_START_DELAY);
        innerCircleAnimator.setInterpolator(DECELERATE_INTERPOLATOR);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(circularRippleEffect, View.ALPHA, 1, 0.3f);
        alphaAnimator.setDuration(OUTER_CIRCLE_ANIMATOR_DURATION);
        animatorSet.playTogether(outerCircleAnimator, innerCircleAnimator, alphaAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("Animator", "Listener");
                animatorSet.start();
            }
        });
        animatorSet.start();
        //        return animatorSet;
    }

    @Override
    protected void onStop() {
        if (taskAssigned!=null) {
            if (!dismissed &&  System.currentTimeMillis()> (starttime +2000)) {
                TaskReceiver.sendNotification(taskAssigned, taskAssigned.getId().intValue(), FullScreenTaskReminder.this);
                Log.e(TAG, "OnStopNotification");
            }
        }
        super.onStop();
    }

    @Override
    protected void onStart() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1111);
        super.onStart();
    }

    @Override
    protected void onResume() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1111);
        super.onResume();
    }

    //    public void makeKonfetti(){
//
//    }

    public void taskComplete(){
        if (Task.findPositionByID(taskAssigned.id)>-1) {
            if (taskAssigned.isRepeating) {
                Intent againIntent = new Intent(this.getApplicationContext(), TaskReceiver.class);
                againIntent.putExtra("id", taskAssigned.id);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), (int) taskAssigned.id, againIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                List<Long> timeToWeeklyRings = Task.getTimeToWeeklyRings(taskAssigned);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timeToWeeklyRings.get(0));
                task.get(Task.findPositionByID(taskAssigned.id)).calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
                task.get(Task.findPositionByID(taskAssigned.id)).calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                task.get(Task.findPositionByID(taskAssigned.id)).calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeToWeeklyRings.get(0), pendingIntent);
                }
            } else {
                task.get(Task.findPositionByID(taskAssigned.id)).isComplete = true;
                task.get(Task.findPositionByID(taskAssigned.id)).finishTime = Calendar.getInstance();
            }
            UtilsArray_Task.UpdateTask(UtilsArray_Task.getTask(), this);
            Fragment_2.TaskReceiverRefresh(this);
        }else Snackbar.make(FullScreenTaskReminder.this.getCurrentFocus(), "Some error occured", Snackbar.LENGTH_SHORT).show();
    }

    public void Snooze(){
        if (Task.findPositionByID(taskAssigned.id)>-1) {
            Intent intent = new Intent(getApplicationContext(), TaskReceiver.class);
            intent.putExtra("id", taskAssigned.id);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) taskAssigned.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            task.get(Task.findPositionByID(taskAssigned.id)).calendar.setTimeInMillis(taskAssigned.getCalendar().getTimeInMillis() + TimeUnit.MINUTES.toMillis(Settings_Main.Snooze_Time));
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, taskAssigned.calendar.getTimeInMillis(), pendingIntent);
            }
            UtilsArray_Task.UpdateTask(UtilsArray_Task.getTask(), FullScreenTaskReminder.this);
            Fragment_2.TaskReceiverRefresh(FullScreenTaskReminder.this);
        }else Snackbar.make(FullScreenTaskReminder.this.getCurrentFocus(), "Some error occured", Snackbar.LENGTH_SHORT).show();
    }

    //    public void setButtonSelected(final boolean isSelected) {
//        if (isSelected) {
//            if (rippleAnimator.isRunning()) {
//                return;
//            }
//            rippleAnimator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    circularRippleEffect.setInnerCircleRadiusProgress(0);
//                    circularRippleEffect.setOuterCircleRadiusProgress(0);
//                    rippleAnimator.removeListener(this);
//                }
//            });
//            rippleAnimator.start();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
////                    circleButton.setButtonSelected(true);
//                }
//            }, OUTER_CIRCLE_ANIMATOR_DURATION);
//        }
//    }


//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public void startRipple() {
//        Log.e("Animator", "Called");
////            if (rippleAnimator.isRunning()) {
////                Log.e("Animator", "Is Running");
////           new Handler().postDelayed(new Runnable() {
////               @Override
////               public void run() {
////                   rippleAnimator.start();
////               }
////           }, 100);
////            }
//
//            rippleAnimator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    circularRippleEffect.setInnerCircleRadiusProgress(0);
//                    circularRippleEffect.setOuterCircleRadiusProgress(0);
////                    rippleAnimator.removeListener(this);
////                    rippleAnimator.start();
//                }
//            });
//            rippleAnimator.start();
////            postDelayed(new Runnable() {
////                @Override
////                public void run() {
//////                    circleButton.setButtonSelected(true);
////                    isButtonSelected = true;
////                }
////            }, OUTER_CIRCLE_ANIMATOR_DURATION);
//        }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        rippleAnimator.start();
//    }
}
