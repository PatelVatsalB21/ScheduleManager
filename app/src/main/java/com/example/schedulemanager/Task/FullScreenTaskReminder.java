package com.example.schedulemanager.Task;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

import static com.example.schedulemanager.Task.UtilsArray_Task.task;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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

public class FullScreenTaskReminder extends Activity {

    FloatingActionButton btnLeft, btnRight, centre;
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
    KonfettiView konfettiView, konfettiView1;

    private static final int INNER_CIRCLE_ANIMATOR_DURATION = 500;
    private static final int INNER_CIRCLE_ANIMATOR_START_DELAY = 250;
    private static final int OUTER_CIRCLE_ANIMATOR_DURATION = 500;
    private static final float ANIMATOR_START_VALUE = 0.1f;
    private static final float ANIMATOR_END_VALUE = 1f;
    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR =
            new DecelerateInterpolator();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.full_screen_task_layout);
        UtilsArray_Task.initTask(FullScreenTaskReminder.this);
        long id = getIntent().getLongExtra("id", -1);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(
                    FullScreenTaskReminder.this)) {
                Toast.makeText(FullScreenTaskReminder.this,
                        "Draw over other apps permission denied", Toast.LENGTH_SHORT).show();
                TaskReceiver.sendNotification(taskAssigned, taskAssigned.getId().intValue(),
                        FullScreenTaskReminder.this);
                finishAffinity();
            } else {
                title.setText(taskAssigned.getTitle());
                time.setText(d.format(taskAssigned.getCalendar().getTimeInMillis()));
                lottie.setAnimation(
                        UtilsArray_Task.category.get(taskAssigned.LottieFileRes).LottieRes);
                head.setText("It's time for " + UtilsArray_Task.category.get(
                        taskAssigned.LottieFileRes).Category);
                startService(new Intent(FullScreenTaskReminder.this, NotifierService.class));
                starttime = System.currentTimeMillis();
                createRippleAnimation(circularRippleEffect);
                centre.setOnTouchListener((view, event) -> {
                    animatorSet.end();
                    float initialX = 0;

                    int action = event.getActionMasked();

                    switch (action) {

                        case MotionEvent.ACTION_MOVE:
                            if (initialX < event.getX() && event.getX() - initialX >= 30.0) {
                                expandFab(btnRight, event.getX() - initialX);
                                collapseFab(centre, event.getX() - initialX);
                            }

                            if (initialX > event.getX() && initialX - event.getX() >= 30.0) {
                                expandFab(btnLeft, initialX - event.getX());
                                collapseFab(centre, initialX - event.getX());
                            }
                            break;

                        case MotionEvent.ACTION_UP:
                            float finalX = event.getX();
                            if (initialX < finalX) {
                                if (btnRight.getScaleX() > 1.3f) {
                                    btnLeft.hide();
                                    centre.hide();
                                    snoozeText.setText("Task Completed");
                                    revealFAB(revealView, normalView, btnRight);
                                    konfettiView.build()
                                            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                                            .addColors(UtilsArraylist.note_BG_colorSet)
                                            .setDirection(0.0, 360.0)
                                            .setSpeed(1f, 4f)
                                            .setFadeOutEnabled(true)
                                            .setTimeToLive(3000L)
                                            .addShapes(Shape.Square.INSTANCE,
                                                    Shape.Circle.INSTANCE)
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
                                            .addShapes(Shape.Square.INSTANCE,
                                                    Shape.Circle.INSTANCE)
                                            .addSizes(new Size(6, 5f), new Size(8, 6f))
                                            .setPosition(konfettiView1.getWidth(), 30)
                                            .burst(600);
                                    taskComplete();
                                } else {
                                    btnRight.setScaleY(1f);
                                    btnRight.setScaleX(1f);
                                    centre.setScaleY(1f);
                                    centre.setScaleX(1f);
                                    btnLeft.setScaleY(1f);
                                    btnLeft.setScaleX(1f);
                                }
                            }

                            if (initialX > finalX) {
                                if (btnLeft.getScaleX() > 1.3f) {
                                    btnRight.hide();
                                    centre.hide();
                                    snoozeText.setText(
                                            "Task Snoozed for" + Settings_Main.Snooze_Time
                                                    + "minutes");
                                    revealFAB(revealView, normalView, btnLeft);
                                    Snooze();
                                } else {
                                    btnRight.setScaleY(1f);
                                    btnRight.setScaleX(1f);
                                    centre.setScaleY(1f);
                                    centre.setScaleX(1f);
                                    btnLeft.setScaleY(1f);
                                    btnLeft.setScaleX(1f);
                                    if (!dismissed) createRippleAnimation(circularRippleEffect);
                                }
                            }
                            break;
                    }
                    return FullScreenTaskReminder.super.onTouchEvent(event);
                });
            }
        } else {
            finishAffinity();
        }
    }

    public void expandFab(FloatingActionButton fab, float diff) {
        if (diff <= 700) {
            float percent = diff / 1000;

            if (percent >= 0.7f) percent = 0.7f;
            fab.setScaleX(1f + percent);
            fab.setScaleY(1f + percent);
        }
    }

    public void collapseFab(FloatingActionButton fab, float diff) {
        if (diff <= 700) {
            float percent = diff / 1000;
            if (percent >= 0.7f) percent = 1f;

            fab.setScaleX(1f - percent);
            fab.setScaleY(1f - percent);
        }
    }

    private void revealFAB(View view1, View view2, FloatingActionButton fab) {
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
        view2.animate().alpha(0f).setInterpolator(new OvershootInterpolator()).setDuration(
                700).start();
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

    @Override
    public void onBackPressed() {
    }

    private void createRippleAnimation(CircularRippleEffect circularRippleEffect) {
        animatorSet = new AnimatorSet();
        ObjectAnimator outerCircleAnimator = ObjectAnimator.ofFloat(circularRippleEffect,
                CircularRippleEffect.OUTER_CIRCLE_RADIUS_PROGRESS,
                ANIMATOR_START_VALUE, ANIMATOR_END_VALUE);
        outerCircleAnimator.setDuration(OUTER_CIRCLE_ANIMATOR_DURATION);
        outerCircleAnimator.setInterpolator(DECELERATE_INTERPOLATOR);

        ObjectAnimator innerCircleAnimator = ObjectAnimator.ofFloat(circularRippleEffect,
                CircularRippleEffect.INNER_CIRCLE_RADIUS_PROGRESS,
                ANIMATOR_START_VALUE, ANIMATOR_END_VALUE);
        innerCircleAnimator.setDuration(INNER_CIRCLE_ANIMATOR_DURATION);
        innerCircleAnimator.setStartDelay(INNER_CIRCLE_ANIMATOR_START_DELAY);
        innerCircleAnimator.setInterpolator(DECELERATE_INTERPOLATOR);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(circularRippleEffect, View.ALPHA, 1,
                0.3f);
        alphaAnimator.setDuration(OUTER_CIRCLE_ANIMATOR_DURATION);
        animatorSet.playTogether(outerCircleAnimator, innerCircleAnimator, alphaAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.start();
            }
        });
        animatorSet.start();
    }

    @Override
    protected void onStop() {
        if (taskAssigned != null) {
            if (!dismissed && System.currentTimeMillis() > (starttime + 2000)) {
                TaskReceiver.sendNotification(taskAssigned, taskAssigned.getId().intValue(),
                        FullScreenTaskReminder.this);
            }
        }
        super.onStop();
    }

    @Override
    protected void onStart() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.cancel(1111);
        super.onStart();
    }

    @Override
    protected void onResume() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.cancel(1111);
        super.onResume();
    }

    public void taskComplete() {
        if (Task.findPositionByID(taskAssigned.id) > -1) {
            if (taskAssigned.isRepeating) {
                Intent againIntent = new Intent(this.getApplicationContext(), TaskReceiver.class);
                againIntent.putExtra("id", taskAssigned.id);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        this.getApplicationContext(), (int) taskAssigned.id, againIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                List<Long> timeToWeeklyRings = Task.getTimeToWeeklyRings(taskAssigned);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timeToWeeklyRings.get(0));
                task.get(Task.findPositionByID(taskAssigned.id)).calendar.set(Calendar.DAY_OF_MONTH,
                        calendar.get(Calendar.DAY_OF_MONTH));
                task.get(Task.findPositionByID(taskAssigned.id)).calendar.set(Calendar.MONTH,
                        calendar.get(Calendar.MONTH));
                task.get(Task.findPositionByID(taskAssigned.id)).calendar.set(Calendar.YEAR,
                        calendar.get(Calendar.YEAR));
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeToWeeklyRings.get(0),
                            pendingIntent);
                }
            } else {
                task.get(Task.findPositionByID(taskAssigned.id)).isComplete = true;
                task.get(Task.findPositionByID(taskAssigned.id)).finishTime =
                        Calendar.getInstance();
            }
            UtilsArray_Task.UpdateTask(UtilsArray_Task.getTask(), this);
            Fragment_2.TaskReceiverRefresh(this);
        } else {
            Snackbar.make(FullScreenTaskReminder.this.getCurrentFocus(), "Some error occured",
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    public void Snooze() {
        if (Task.findPositionByID(taskAssigned.id) > -1) {
            Intent intent = new Intent(getApplicationContext(), TaskReceiver.class);
            intent.putExtra("id", taskAssigned.id);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    (int) taskAssigned.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            task.get(Task.findPositionByID(taskAssigned.id)).calendar.setTimeInMillis(
                    taskAssigned.getCalendar().getTimeInMillis() + TimeUnit.MINUTES.toMillis(
                            Settings_Main.Snooze_Time));
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        taskAssigned.calendar.getTimeInMillis(), pendingIntent);
            }
            UtilsArray_Task.UpdateTask(UtilsArray_Task.getTask(), FullScreenTaskReminder.this);
            Fragment_2.TaskReceiverRefresh(FullScreenTaskReminder.this);
        } else {
            Snackbar.make(FullScreenTaskReminder.this.getCurrentFocus(), "Some error occured",
                    Snackbar.LENGTH_SHORT).show();
        }
    }
}
