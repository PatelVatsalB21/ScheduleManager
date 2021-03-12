
package com.example.schedulemanager.MainFragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

//import com.example.firebase.HomeFrag.UtilsArray_All;
//import com.example.firebase.Pop_up_Dialog;
import com.avatarfirst.avatargenlib.AvatarConstants;
import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.example.schedulemanager.CustomRatingDialog.RatingBar;
import com.example.schedulemanager.HomeFrag.UtilsArray_All;
import com.example.schedulemanager.LoginandUser.UserProfile;
import com.example.schedulemanager.MainActivity;
import com.example.schedulemanager.NotifierService;
import com.example.schedulemanager.R;
import com.example.schedulemanager.Setting.AppPasswordActivity;
import com.example.schedulemanager.Setting.Settings_Main;
import com.example.schedulemanager.Task.FullScreenTaskReminder;
import com.example.schedulemanager.Task.NewTask;
import com.example.schedulemanager.Task.UtilsArray_Task;
import com.example.schedulemanager.Trash.TrashMainPage;
import com.example.schedulemanager.Trash.UtilsArray_Trash;
import com.example.schedulemanager.email.ScheduleEmail;
import com.example.schedulemanager.email.UtilsArray_Email;
import com.example.schedulemanager.note.CanvasActivity;
import com.example.schedulemanager.note.NewNoteAddDialog;
import com.example.schedulemanager.note.UtilsArraylist;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.testing.FakeReviewManager;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.File;

import nl.dionsegijn.konfetti.KonfettiView;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //    private static final int PICK_ACCOUNT_REQUEST = 1;
//    private static final int APP_PERMISSION_REQUEST = 102;
    private final int APP_PERMISSION_REQUEST = 2102;
    FirebaseAuth mAuth;
    static FirebaseUser user;
    //    GoogleSignInClient mGoogleSignInClient;
    ImageButton MenuBtn, UserProfile_btn;
    BottomNavigationItemView HomeBtn, TasksBtn, NotesBtn, EmailBtn;
    public static ImageView home, todo, notes, email;
    public LinearLayout homeLin, todoLin, notesLin, emailLin;
    PagerAdapter mPagerAdapter;
    static HomePageViewPager mViewPager;
    static DrawerLayout drawerLayout;
    static NavigationView navigationView;
    RelativeLayout homeScreen;
    static RelativeLayout top_layout;
    public static final float END_SCALE = 0.7f;
    boolean doubleBackToExitPressedOnce = false;
    AlarmManager alarmManager;
    TextView title;
    public static BottomAppBar bottomNavigationView;
    private boolean isAnimating = false;
    public MaterialButton button;
    public static FloatingActionButton fab_main_home;
    Boolean MenuOpened = false;
    public static FloatingActionButton fab1, fab2, fab3;
    TextView fab1_txt, fab2_txt, fab3_txt;
    Float translation = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();

    public static AccelerateDecelerateInterpolator interpolator2 = new AccelerateDecelerateInterpolator();
    Integer pageNumber = 0;
    View revealView;
    public static File imageUri;
    public static String processedUserName;
    public static ImageView profileImg;
    public static TextView username_txt_view;
    public final int UPDATE_REQUEST_CODE = 11111;
    private InstallStateUpdatedListener installStateUpdateListener;
    AppUpdateManager appUpdateManager;
    LinearLayout.LayoutParams params;
    public static KonfettiView konfettiView, konfettiView1;
    public static Boolean actionBarOn = false;
    ReviewManager reviewManager;
    ReviewInfo reviewInfo;
    //    static Context context;

    @Override
    protected void onDestroy() {
        Settings_Main.LoadChangedSettings(HomePage.this);
        UtilsArraylist.UpdateNote(UtilsArraylist.getNote(), HomePage.this);
        UtilsArray_Task.UpdateTask(UtilsArray_Task.getTask(), HomePage.this);
        UtilsArray_Email.UpdateMail(UtilsArray_Email.getMail(), HomePage.this);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Settings_Main.LoadChangedSettings(HomePage.this);
        UtilsArraylist.UpdateNote(UtilsArraylist.getNote(), HomePage.this);
        UtilsArray_Task.UpdateTask(UtilsArray_Task.getTask(), HomePage.this);
        UtilsArray_Email.UpdateMail(UtilsArray_Email.getMail(), HomePage.this);
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdateListener);
        }
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        stopService(new Intent(HomePage.this, NotifierService.class));
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        MenuBtn = findViewById(R.id.NavigationDrawerBtn);
        UserProfile_btn = findViewById(R.id.Home_Page_Profile_btn);
        HomeBtn = findViewById(R.id.HomeBottomNavBtn);
        TasksBtn = findViewById(R.id.TaskBottomNavBtn);
        NotesBtn = findViewById(R.id.NotesBottomNavBtn);
        EmailBtn = findViewById(R.id.EmailBottomNavBtn);
//        searchView = findViewById(R.id.SearchView);
        drawerLayout = findViewById(R.id.Main_Drawer);
        homeScreen = findViewById(R.id.HomeScreen);
        navigationView = findViewById(R.id.navigation_view);
        top_layout = findViewById(R.id.RelLayoutTop);
        title = findViewById(R.id.HomePageAppTitle);
        bottomNavigationView = findViewById(R.id.activity_Home_Bottom_AppBar);
        home = findViewById(R.id.nav_home_app_bar);
        todo = findViewById(R.id.nav_todo_app_bar);
        notes = findViewById(R.id.nav_notes_app_bar);
        email = findViewById(R.id.nav_email_app_bar);
        homeLin = findViewById(R.id.nav_home_app_bar_lin_layout);
        todoLin = findViewById(R.id.nav_todo_app_bar_lin_layout);
        notesLin = findViewById(R.id.nav_notes_app_bar_lin_layout);
        emailLin = findViewById(R.id.nav_email_app_bar_lin_layout);
        fab_main_home = findViewById(R.id.fab_main_app_bar);
        revealView = findViewById(R.id.revealView);
        konfettiView = findViewById(R.id.home_Konfetti);
        konfettiView1 = findViewById(R.id.home_Konfetti_2);
        initFabs();
        reviewManager = new FakeReviewManager(HomePage.this);


        UtilsArraylist.initNote(HomePage.this);
        UtilsArray_Email.initMail(HomePage.this);
        UtilsArray_Task.initTask(HomePage.this);
        UtilsArray_All.initAllArray(HomePage.this);
        Settings_Main.LoadChangedSettings(HomePage.this);
        UtilsArray_Trash.initTrashArrays(HomePage.this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        requestPermissions();

//        context = HomePage.this;
//        UtilsArray_All.initAllArray(HomePage.this);
//        UtilsArray_All.AddToAll(HomePage.this);

//        Log.d("HomePageLOG",String.valueOf(UtilsArray_All.allItemsArrayList.size()));

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), 1);
        mViewPager = findViewById(R.id.Container);

        setUpViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(4);
        home.setSelected(true);
        mViewPager.setCurrentItem(0);

        UserProfile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    startActivity(new Intent(HomePage.this, UserProfile.class));
                } else
                    makeLoginSnackBar();
//                    Toast.makeText(HomePage.this, "Please sign in to use this function", Toast.LENGTH_SHORT).show();
            }
        });

//        HomeBtn.setBackgroundColor(R.drawable.bottom_nav_items_background);
//        TasksBtn.setBackgroundColor(R.drawable.bottom_nav_items_background);
//        NotesBtn.setBackgroundColor(R.drawable.bottom_nav_items_background);
//        EmailBtn.setBackgroundColor(R.drawable.bottom_nav_items_background);


        navigationDrawer();

        homeLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBottomIcon(0);
                if (mViewPager.getCurrentItem() != 0) {
                    mViewPager.setCurrentItem(0);
                }
            }
        });

        todoLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBottomIcon(1);
                if (mViewPager.getCurrentItem() != 1) {
                    mViewPager.setCurrentItem(1);
                }
            }
        });

        notesLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBottomIcon(2);
                if (mViewPager.getCurrentItem() != 2) {
                    mViewPager.setCurrentItem(2);
                }
            }
        });

        emailLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBottomIcon(3);
                if (mViewPager.getCurrentItem() != 3) {
                    mViewPager.setCurrentItem(3);
                }
            }
        });

//        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                fragUpdaterForFab(position);

                if (position == 0) {
                    navigationView.setCheckedItem(R.id.nav_home);
                    selectBottomIcon(0);
                } else if (position == 1) {
                    navigationView.setCheckedItem(R.id.nav_my_todo);
                    selectBottomIcon(1);
                } else if (position == 2) {
                    navigationView.setCheckedItem(R.id.nav_my_notes);
                    selectBottomIcon(2);
                } else if (position == 3) {
                    navigationView.setCheckedItem(R.id.nav_my_email);
                    selectBottomIcon(3);
                }

                if (MenuOpened) {
                    hideFAB(revealView, mViewPager);
                    MenuOpened = false;
                    fab_main_home.animate().rotation(0f).setInterpolator(new OvershootInterpolator()).setDuration(300).start();
                    fab1.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                    fab2.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                    fab3.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                    fab1_txt.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                    fab2_txt.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                    fab3_txt.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        fab_main_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pageNumber == 0) {

                    if (!MenuOpened) {
                        revealFAB(revealView, mViewPager);
//                    cardView.startAnimation(anim_up);
//                    cardView.setVisibility(View.VISIBLE);
                        MenuOpened = true;
//                        fab_main_home.startAnimation(fab_up);
                        fab_main_home.animate().rotation(45f).setInterpolator(new OvershootInterpolator()).setDuration(300).start();
                        fab1.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                        fab2.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                        fab3.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                    fab4.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                        fab1_txt.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                        fab2_txt.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
                        fab3_txt.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
//                    fab4_txt.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

                    } else {
                        hideFAB(revealView, mViewPager);
//                    cardView.startAnimation(anim_down);
//                    cardView.setVisibility(View.GONE);
                        MenuOpened = false;
                        fab_main_home.animate().rotation(0f).setInterpolator(new OvershootInterpolator()).setDuration(300).start();
                        fab1.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                        fab2.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                        fab3.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//                    fab4.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                        fab1_txt.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                        fab2_txt.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
                        fab3_txt.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
//                    fab4_txt.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();

                    }
                    fab_main_home.setEnabled(false);
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            fab_main_home.setEnabled(true);
                        }
                    }, 500);
                } else if (pageNumber == 1) {
                    Intent i = new Intent(HomePage.this, NewTask.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (pageNumber == 2) {
                    Intent intent = new Intent(HomePage.this, NewNoteAddDialog.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (pageNumber == 3) {
                    if (Settings_Main.App_Password != null && !Settings_Main.App_Password.isEmpty()) {
                        Intent i = new Intent(HomePage.this, ScheduleEmail.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        EmailSnackBar();
//                                Toast.makeText(HomePage.this, "Google App Password is not given. Please Click ! button on right or go to Settings.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomePage.this, NewTask.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, NewNoteAddDialog.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Settings_Main.App_Password != null && !Settings_Main.App_Password.isEmpty()) {
                    Intent i = new Intent(HomePage.this, ScheduleEmail.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    EmailSnackBar();
//                    Toast.makeText(HomePage.this, "App Password is not given. Please fill it to use this function.", Toast.LENGTH_LONG).show();
                }
            }
        });

//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//
//                switch (item.getItemId()) {
//                    case R.id.HomeBottomNavBtn:
//                        if (mViewPager.getCurrentItem() != 0) {
//                            mViewPager.setCurrentItem(0);
//                        }
//                        return true;
//                    case R.id.TaskBottomNavBtn:
//                        if (mViewPager.getCurrentItem() != 1) {
//                            mViewPager.setCurrentItem(1);
//                        }
//                        return true;
//                    case R.id.NotesBottomNavBtn:
//                        if (mViewPager.getCurrentItem() != 2) {
//                            mViewPager.setCurrentItem(2);
//                        }
//                        return true;
//                    case R.id.EmailBottomNavBtn:
//                        if (mViewPager.getCurrentItem() != 3) {
//                            mViewPager.setCurrentItem(3);
//                        }
//                        return true;
//                }
//                return false;
//            }
//        });

//        Search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(HomePage.this, "Contacting Google", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        Search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//
//                if (!Search.hasFocus()) {
//                    //this if condition is true when searchview lost focus...
//                    int searchSrcText = Search.getContext().getResources()
//                            .getIdentifier("android:id/search_src_text", null, null);
//                    EditText editText = (EditText) Search.findViewById(searchSrcText);
//                    editText.setCursorVisible(true);
//                    Search.setActivated(true);
//                    Search.setPressed(true);
//                }
//            }
//        });


//        Button mButton= findViewById(R.id.createBtn);
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getBaseContext(), AlwaysOnNotification.class);
//                PendingIntent p = PendingIntent.getBroadcast(getBaseContext(),0,i,PendingIntent.FLAG_NO_CREATE);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),p);
//                }
////                startService(new Intent(HomePage.this, PopUpDialogService.class));
//
//            }
//        });

        //        signOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAuth.signOut();
//                user = mAuth.getCurrentUser();
//                if (mGoogleSignInClient != null) {
//                    mGoogleSignInClient.signOut();
//
//                }
//
//                if (user == null) {
//                    startActivity(new Intent(HomePage.this, MainActivity.class));
//                    Toast.makeText(HomePage.this, "Signed Out successfully ...", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//
//        });
//
//        revokeAccess.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                revokeAccess();
//            }
//        });
//    private void revokeAccess(){
//        /* Firebase sign out */
//        mAuth.signOut();
//        // Google revoke access
//        mGoogleSignInClient.revokeAccess()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull com.example.firebase.Task<Void> task) {
//                        Toast.makeText(HomePage.this, "Signed Out Successfully ...", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:" + getPackageName()));
//            startActivityForResult(intent, APP_PERMISSION_REQUEST);
//        } else {
//            initializeView();
//        }

        appUpdateManager = AppUpdateManagerFactory.create(HomePage.this);


        installStateUpdateListener = new
                InstallStateUpdatedListener() {
                    @Override
                    public void onStateUpdate(InstallState state) {
                        if (state.installStatus() == InstallStatus.DOWNLOADED) {
                            //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                            updateInstallDialog();
                        } else if (state.installStatus() == InstallStatus.INSTALLED) {
                            if (appUpdateManager != null) {
                                appUpdateManager.unregisterListener(installStateUpdateListener);
                            }

                        } else {
                            Log.e("InstallStateUpdated", "InstallStateUpdatedListener: state: " + state.installStatus());
                        }
                    }
                };

        appUpdateManager.registerListener(installStateUpdateListener);

        CheckUpdate();

        Task<ReviewInfo> request;
        request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object

                reviewInfo = task.getResult();
            } else {
                Toast.makeText(this, "Error in review", Toast.LENGTH_SHORT).show();
                // There was some problem, continue regardless of the result.
            }
        });

    }


    private void setUpViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), 1);
        adapter.addFragment(new Fragment_Home());
        adapter.addFragment(new Fragment_2());
        adapter.addFragment(new Fragment_3());
        adapter.addFragment(new Fragment_4());
        viewPager.setAdapter(adapter);
    }


    private void navigationDrawer() {

        //Naviagtion Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

//        if (user==null){
//        navigationView.getMenu().findItem(R.id.nav_logout_btn).setTitle("Log In");
//            navigationView.getMenu().findItem(R.id.nav_logout_btn).setIcon(R.drawable.ic_login);
//        }else {
//            navigationView.getMenu().findItem(R.id.nav_logout_btn).setTitle("Log Out");
//        navigationView.getMenu().findItem(R.id.nav_logout_btn).setIcon(R.drawable.ic_logout);
//        }

        MenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        View oldHeader = navigationView.getHeaderView(0);
        navigationView.removeHeaderView(oldHeader);

        navigationView.inflateHeaderView(R.layout.header_nav_drawer);
        View header = navigationView.getHeaderView(0);
        profileImg = header.findViewById(R.id.header_nav_thumbnail_img_view);
        username_txt_view = header.findViewById(R.id.header_nav_text_view);
        ImageButton edit_profile_nav = header.findViewById(R.id.header_nav_edit_profile_btn);
        if (user != null) {

            if (Settings_Main.User_Name != null && !Settings_Main.User_Name.isEmpty()) {
                username_txt_view.setText(Settings_Main.User_Name);
                processedUserName = Settings_Main.User_Name;
            } else {
                if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
                    username_txt_view.setText(user.getDisplayName());
                    processedUserName = user.getDisplayName();
                } else if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                    username_txt_view.setText("Set your nickname");
                    processedUserName = user.getEmail();
                } else {
                    username_txt_view.setText("Set your nickname");
                }
            }

            imageUri = Settings_Main.loadImageFromStorage(HomePage.this);


            if (imageUri.exists()) {
//                    final InputStream imageStream;
//                    UserProfile.this.getContentResolver().takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION
//                            + Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    imageStream = getContentResolver().openInputStream(Settings_Main.profileBitmap);
//                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Picasso.get().load(imageUri).fit().centerCrop().transform(new UserProfile.CircleTransform()).into(profileImg);


            } else if (processedUserName != null && !processedUserName.isEmpty()) {
                profileImg.setImageDrawable(AvatarGenerator.Companion.avatarImage(HomePage.this, 60, AvatarConstants.Companion.getCIRCLE(), processedUserName));
            } else
                profileImg.setImageDrawable(AvatarGenerator.Companion.avatarImage(HomePage.this, 60, AvatarConstants.Companion.getCIRCLE(), "Schedule Manager"));


//
//            if (user.getDisplayName() != null && !user.getDisplayName().trim().isEmpty()) {
//                thumbnail.setImageDrawable(AvatarGenerator.Companion.avatarImage(HomePage.this, 80, AvatarConstants.Companion.getCIRCLE(), user.getDisplayName()));
//                username_txt_view.setText(user.getDisplayName());
//            } else if (user.getEmail() != null && !user.getEmail().isEmpty()) {
//                thumbnail.setImageDrawable(AvatarGenerator.Companion.avatarImage(HomePage.this, 80, AvatarConstants.Companion.getCIRCLE(), user.getEmail()));
//                username_txt_view.setText("Set your nickname");
//            } else {
//                thumbnail.setImageDrawable(AvatarGenerator.Companion.avatarImage(HomePage.this, 80, AvatarConstants.Companion.getCIRCLE(), getString(R.string.app_name_SplashScreen)));
//                username_txt_view.setText(getString(R.string.app_name_SplashScreen));
//            }

        } else {
            profileImg.setImageDrawable(AvatarGenerator.Companion.avatarImage(HomePage.this, 60, AvatarConstants.Companion.getCIRCLE(), getString(R.string.app_name_SplashScreen)));
            username_txt_view.setText(getString(R.string.app_name_SplashScreen));
        }

        edit_profile_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    startActivity(new Intent(HomePage.this, UserProfile.class));
                } else
                    Snackbar.make(navigationView, "Please Login to use this feature", Snackbar.LENGTH_SHORT)
                            .setAction("Login", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(HomePage.this, MainActivity.class));
                                    finishAffinity();
                                }
                            }).setActionTextColor(HomePage.this.getResources().getColor(R.color.color_yellow))
                            .show();
//                    Toast.makeText(HomePage.this, "Please sign in to use this function", Toast.LENGTH_SHORT).show();
            }
        });

        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                homeScreen.setScaleX(offsetScale);
                homeScreen.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = homeScreen.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                homeScreen.setTranslationX(xTranslation);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.nav_home:
                NavigationViewSelect(0, R.id.nav_home);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_my_notes:
                NavigationViewSelect(2, R.id.nav_my_notes);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_my_todo:
                NavigationViewSelect(1, R.id.nav_my_todo);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_my_email:
                NavigationViewSelect(3, R.id.nav_my_email);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
//            case R.id.nav_my_calender:
//                Intent i3 = new Intent(HomePage.this, Fragment_4.class);
//                startActivity(i3);
//                break;
            case R.id.nav_settings:
                Intent settingIntent = new Intent(HomePage.this, Settings_Main.class);
                startActivity(settingIntent);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_share_app:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey there! I am using Schedule Manager App And this is so awesome. It manages all your tasks, notes, events and also schedules emails. Try it from below link");
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
                NavigationViewSelect(0, R.id.nav_home);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_trash:
                startActivity(new Intent(HomePage.this, TrashMainPage.class));
                break;

            case R.id.nav_rate_us:
                ShowRatingDialog();

                Task<Void> flow = reviewManager.launchReviewFlow(HomePage.this, reviewInfo);
                flow.addOnCompleteListener(task1 -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                        Toast.makeText(HomePage.this, "Error in review flow", Toast.LENGTH_SHORT).show();
                    }
                });

                NavigationViewSelect(0, R.id.nav_home);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

//            case R.id.nav_logout_btn:
//                if(user!=null) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this)
//                            .setTitle("Log Out")
//                            .setMessage("Are you sure you want to Log Out. This will delete all data you didn't backed up in server?")
//                            .setNegativeButton(android.R.string.no, null)
//                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
//                                    Settings_Main.deleteImageFromStorage(HomePage.this);
//                                    Settings_Main.LogoutDeleteUserData(HomePage.this);
//                                    mAuth.signOut();
//                                    user = mAuth.getCurrentUser();
//                                    if (user == null) {
//                                        startActivity(new Intent(HomePage.this, MainActivity.class));
//                                        Toast.makeText(HomePage.this, "Signed Out successfully ...", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                    params = new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.WRAP_CONTENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT
//                    );
//                    params.setMargins(20,0,20,0);
//                    Button nButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                    nButton.setLayoutParams(params);
//                    nButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//                    Button yButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                    yButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//                    yButton.setLayoutParams(params);
//
////                    mAuth.signOut();
////                    user = mAuth.getCurrentUser();
////                    if (user == null) {
////                        startActivity(new Intent(HomePage.this, MainActivity.class));
////                        Toast.makeText(HomePage.this, "Signed Out successfully ...", Toast.LENGTH_SHORT).show();
////                        finish();
////                    } else
////                        Toast.makeText(HomePage.this, "Some error occurred while signing out. Please try again later", Toast.LENGTH_SHORT).show();
//                }else {
//                    startActivity(new Intent(HomePage.this, MainActivity.class));
//                    finish();
//                }
//                break;

            case R.id.nav_about_us:
                startActivity(new Intent(HomePage.this, CanvasActivity.class));
//                AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
//                builder.setTitle("About Us");
//                builder.setMessage("Hey! Hoping that you are enjoying your Schedule Manager. This app is developed with purpose to help people organise their schedule and manage time in better way. If you are a developer and want to improve this app help us on github \n Follow Us on : ");
//                builder.setNegativeButton("Dismiss", null);
//
//                AlertDialog dialog = builder.create();
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.show();
//                dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                break;

            default:
                break;
        }

        return true;
    }


//    private void showGoogleAccountPicker() {
//        Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null,
//                new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
//        startActivityForResult(googlePicker, PICK_ACCOUNT_REQUEST);
//    }

//    @Override
//    protected void onActivityResult(final int requestCode, final int resultCode,
//                                    final Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_ACCOUNT_REQUEST && resultCode == RESULT_OK) {
//            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
//
//        }
//    }


    @Override
    public void onBackPressed() {
        if (MenuOpened) {
            hideFAB(revealView, mViewPager);
            MenuOpened = false;
            fab_main_home.animate().rotation(0f).setInterpolator(new OvershootInterpolator()).setDuration(300).start();
            fab1.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
            fab2.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
            fab3.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
            fab1_txt.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
            fab2_txt.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
            fab3_txt.animate().translationY(translation).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        }
        UtilsArraylist.UpdateNote(UtilsArraylist.getNote(), HomePage.this);
        UtilsArray_Task.UpdateTask(UtilsArray_Task.getTask(), HomePage.this);
        UtilsArray_Email.UpdateMail(UtilsArray_Email.getMail(), HomePage.this);
        if (doubleBackToExitPressedOnce) {
//            UtilsArraylist.UpdateNote(UtilsArraylist.getNote(), HomePage.this);
//            UtilsArraylist.myDb.updateData(String.valueOf(1), GsonConverter.ArrayToJson(UtilsArraylist.note));
            finishAffinity();
            Settings_Main.LoadChangedSettings(HomePage.this);
        }


        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


    }

    private void initializeView() {
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LayoutInflater layoutInflater =
//                        (LayoutInflater)getBaseContext()
//                                .getSystemService(LAYOUT_INFLATER_SERVICE);
//                View popupView = layoutInflater.inflate(R.layout.activity_pop_up__dialog, null);
//                final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//                Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
//
//                Spinner popupSpinner = (Spinner)popupView.findViewById(R.id.popupspinner);
//
//                ArrayAdapter<String> adapter =
//                        new ArrayAdapter<String>(HomePage.this,
//                                android.R.layout.simple_spinner_item, DayOfWeek);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                popupSpinner.setAdapter(adapter);
//
//                btnDismiss.setOnClickListener(new Button.OnClickListener(){
//
//                    @Override
//                    public void onClick(View v) {
//                        popupWindow.dismiss();
//                    }});
//
//                popupWindow.showAsDropDown(mButton, 50, -30);
//            }
//        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == APP_PERMISSION_REQUEST && resultCode == RESULT_OK) {
//            initializeView();
//        } else {
//            Toast.makeText(this, "Draw over other app permission not enable.", Toast.LENGTH_SHORT).show();
//        }
//    }

    public static void hideLayout() {
        HomePage.bottomNavigationView.setVisibility(View.GONE);
        top_layout.setVisibility(View.GONE);
        top_layout.setAlpha(0f);
        fab_main_home.hide();
        fab_main_home.setEnabled(false);
        mViewPager.setPagingEnabled(false);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        actionBarOn = true;
    }

    public static void showLayout() {
//        Animation anim = AnimationUtils.loadAnimation(context, R.anim.pop_down);
//        top_layout.startAnimation(anim);
        fab_main_home.show();
        fab_main_home.setEnabled(true);
        top_layout.animate().alpha(1f).setInterpolator(interpolator2).setDuration(700).start();
        top_layout.setVisibility(View.VISIBLE);
        HomePage.bottomNavigationView.setVisibility(View.VISIBLE);
        mViewPager.setPagingEnabled(true);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        actionBarOn = false;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_search_bar,menu);
//        MenuItem item = menu.findItem(R.id.SearchView);
//        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();
//        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Notes_rec_Adapter n = new Notes_rec_Adapter();
//                n.filterNotes(newText);
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }

    public static void selectBottomIcon(Integer num) {
        if (num == 0) {
            konfettiView.setVisibility(View.INVISIBLE);
            konfettiView1.setVisibility(View.INVISIBLE);
            home.setSelected(true);
            todo.setSelected(false);
            notes.setSelected(false);
            email.setSelected(false);
        } else if (num == 1) {
            home.setSelected(false);
            todo.setSelected(true);
            notes.setSelected(false);
            email.setSelected(false);
        } else if (num == 2) {
            konfettiView.setVisibility(View.INVISIBLE);
            konfettiView1.setVisibility(View.INVISIBLE);
            home.setSelected(false);
            todo.setSelected(false);
            notes.setSelected(true);
            email.setSelected(false);
        } else if (num == 3) {
            konfettiView.setVisibility(View.INVISIBLE);
            konfettiView1.setVisibility(View.INVISIBLE);
            home.setSelected(false);
            todo.setSelected(false);
            notes.setSelected(false);
            email.setSelected(true);
        }
    }

    public static void NavigationViewSelect(Integer pageNumber, Integer viewID) {
        mViewPager.setCurrentItem(pageNumber);
        selectBottomIcon(pageNumber);
//        bottomNavigationView.setSelectedItemId(viewID);
        drawerLayout.closeDrawer(GravityCompat.START);
        navigationView.setCheckedItem(viewID);
    }

    public void initFabs() {
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
//        fab4 = view.findViewById(R.id.fab4);
        fab1_txt = findViewById(R.id.fab1_txt_view);
        fab2_txt = findViewById(R.id.fab2_txt_view);
        fab3_txt = findViewById(R.id.fab3_txt_view);
        fab_main_home.bringToFront();
//        fab4_txt = view.findViewById(R.id.fab4_txt_view);

        fab1.setAlpha(0f);
        fab1.setTranslationY(translation);
        fab2.setAlpha(0f);
        fab2.setTranslationY(translation);
        fab3.setAlpha(0f);
        fab3.setTranslationY(translation);
//        fab4.setAlpha(0f);
//        fab4.setTranslationY(translation);
        fab1_txt.setAlpha(0f);
        fab1_txt.setTranslationY(translation);
        fab2_txt.setAlpha(0f);
        fab2_txt.setTranslationY(translation);
        fab3_txt.setAlpha(0f);
        fab3_txt.setTranslationY(translation);
    }

    private void revealFAB(View view1, View view2) {
//        View view2 = v.findViewById(R.id.Container);
//        View view1 = v.findViewById(R.id.revealView);

        int cx = (fab_main_home.getLeft() + fab_main_home.getRight()) / 2;
        int cy = (fab_main_home.getTop() + fab_main_home.getBottom()) / 2;
        int cxx = view1.getWidth();
        int cyy = view1.getHeight();
        float finalRadius = (float) Math.hypot(cxx, cyy);
        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(view1, cx, cy, 0, finalRadius);
            anim.setDuration(300);
        }
        anim.start();
        view2.animate().alpha(0f).setInterpolator(interpolator).setDuration(250).start();
        view1.setVisibility(View.VISIBLE);
        MenuBtn.setEnabled(false);
        UserProfile_btn.setEnabled(false);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    private void hideFAB(View view1, View view2) {
//        final View view2 = v.findViewById(R.id.Container);
//        final View view1 = v.findViewById(R.id.revealView);

        int cx = (fab_main_home.getLeft() + fab_main_home.getRight()) / 2;
        int cy = (fab_main_home.getTop() + fab_main_home.getBottom()) / 2;
        int cxx = view1.getWidth();
        int cyy = view1.getHeight();
        float initialRadius = (float) Math.hypot(cxx, cyy);
        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(view1, cx, cy, initialRadius, 0);
            anim.setDuration(300);
        }
        if (anim != null) {
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view1.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    view2.animate().alpha(1.0f).setInterpolator(interpolator).setDuration(1000).start();
                }

            });
        }
        anim.start();
        MenuBtn.setEnabled(true);
        UserProfile_btn.setEnabled(true);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void EmailAppPasswordGuide() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(HomePage.this);
        View mView = getLayoutInflater().inflate(R.layout.app_password_guide_layout, null);
        Button open_settings = mView.findViewById(R.id.App_Password_Guide_Layout_setting_btn);
        Button btn_cancel = mView.findViewById(R.id.App_Password_Guide_Layout_dismiss_btn);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        open_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    startActivity(new Intent(HomePage.this, AppPasswordActivity.class));
                } else
                    Snackbar.make(alertDialog.getCurrentFocus(), "Please Login to use this feature", Snackbar.LENGTH_SHORT)
                            .setAction("Login", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(HomePage.this, MainActivity.class));
                                    finishAffinity();
                                }
                            }).setActionTextColor(HomePage.this.getResources().getColor(R.color.color_yellow))
                            .show();
//                    Toast.makeText(HomePage.this, "You need to sign to use this function", Toast.LENGTH_SHORT).show();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationDrawer();
        Log.e("ONRESUME", "CALLED");
//        if (Settings_Main.App_Password != null && !Settings_Main.App_Password.isEmpty()) {
//
//            fab_main_home.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(HomePage.this, ScheduleEmail.class);
//                    startActivity(i);
//                }
//            });
//        } else {
//            fab_main_home.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(HomePage.this, "App Password is not given. Please fill it to use this function.", Toast.LENGTH_LONG).show();
//                }
//            });
//        }
    }

    public void ShowRatingDialog() {
        RatingBar ratingBar = new RatingBar(HomePage.this);
        ratingBar.setCancelable(false);
        ratingBar.show(getSupportFragmentManager(), null);
    }

    public void fragUpdaterForFab(Integer position) {
        pageNumber = position;
    }

    public void CheckUpdate() {
        Log.e("UPDATECHECKER", "Checking Updates");
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                updateDownloadDialog();

            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                importantUpdateDownloadDialog();

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                updateInstallDialog();
            } else {
                Log.e("checkForAppUpdateAvail", "checkForAppUpdateAvailability: something else");
            }
        });
    }

    public void importantUpdateDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
        builder.setTitle("Update Available");
        builder.setMessage("This is high priority update. It might remove bugs and optimize app, alongwith new features");
        builder.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateManager.getAppUpdateInfo().getResult(), AppUpdateType.IMMEDIATE, HomePage.this, UPDATE_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            }
        });
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void updateDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
        builder.setTitle("Update Available");
        builder.setMessage("Install now to get new and refined features");
        builder.setNegativeButton("Remind Me later", null);
        builder.setPositiveButton("Install", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateManager.getAppUpdateInfo().getResult(), AppUpdateType.FLEXIBLE, HomePage.this, UPDATE_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void updateInstallDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
        builder.setTitle("Update Downloaded");
        builder.setMessage("Your Schedule Manager is just on click away from update ");
        builder.setPositiveButton("Install Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (appUpdateManager != null) {
                    appUpdateManager.completeUpdate();
                    appUpdateManager.unregisterListener(installStateUpdateListener);
                }
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.e("UpdateOnActivityResult", "onActivityResult: app download failed");
            }
        } else if (requestCode == APP_PERMISSION_REQUEST) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(HomePage.this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) != 0
                        || ActivityCompat.checkSelfPermission(HomePage.this, Manifest.permission.READ_EXTERNAL_STORAGE) != 0) {
                    Snackbar.make(HomePage.fab_main_home, "Permission Not Granted", Snackbar.LENGTH_SHORT)
                            .setAction("Ask Permission", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    requestPermissions();
                                }
                            }).setActionTextColor(getColor(R.color.color_yellow))
                            .setAnchorView(HomePage.fab_main_home).show();
                }
            }
        }
    }

    public void makeLoginSnackBar() {
        Snackbar.make(fab_main_home, "Please Login to use this feature", Snackbar.LENGTH_SHORT)
                .setAction("Login", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(HomePage.this, MainActivity.class));
                        finishAffinity();
                    }
                }).setActionTextColor(HomePage.this.getResources().getColor(R.color.color_yellow))
                .setAnchorView(fab_main_home)
                .show();
    }

    public void EmailSnackBar() {
        Snackbar.make(HomePage.fab_main_home, "Google App Password is required ", Snackbar.LENGTH_LONG).setAction("More Info", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailAppPasswordGuide();
            }
        }).setActionTextColor(getResources().getColor(R.color.color_yellow)).setAnchorView(HomePage.fab_main_home).show();
    }

    public void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(HomePage.this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(HomePage.this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, APP_PERMISSION_REQUEST);
        }
    }

}

