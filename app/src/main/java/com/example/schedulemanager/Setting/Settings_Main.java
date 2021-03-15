package com.example.schedulemanager.Setting;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationManagerCompat;

import com.avatarfirst.avatargenlib.AvatarConstants;
import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.example.schedulemanager.AlwaysOnNotification;
import com.example.schedulemanager.LoginandUser.UserProfile;
import com.example.schedulemanager.MainActivity;
import com.example.schedulemanager.QuickNoteSpinnerItem;
import com.example.schedulemanager.R;
import com.example.schedulemanager.Task.Task_rec_adapter;
import com.example.schedulemanager.Task.UtilsArray_Task;
import com.example.schedulemanager.Trash.UtilsArray_Trash;
import com.example.schedulemanager.email.UtilsArray_Email;
import com.example.schedulemanager.note.GsonConverter;
import com.example.schedulemanager.note.Notes_rec_Adapter;
import com.example.schedulemanager.note.UtilsArraylist;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Settings_Main extends AppCompatActivity {

    private final int APP_PERMISSION_REQUEST = 110;
    RelativeLayout logout_layout, app_password_layout, backup_password_layout, snoozePicker_layout,
            main_layout;
    public SwitchCompat assistive_notification_switch, dark_mode_switch;
    public static ImageView profileImage;
    ImageButton user_profile_btn, back_btn, logout_btn;
    TextView user_Name, below_user_name, logout_txt, logout_email_text, quickNote_txt_view,
            snoozeIndicator;
    static FirebaseAuth mAuth;
    static FirebaseUser user;
    public GoogleSignInClient mGoogleSignInClient;
    static String processedUserName;
    public static Boolean isDarkMode, isAssitiveNotificationOn;
    public static Integer QuickNoteTextColor, Snooze_Time;
    public static SharedPreferences settingsSharedPreferences;
    final private static String SETTINGS_DB = "Settings_Main_DB";
    public static String App_Password, User_Name;
    static Setting_Class Settings_Storage;
    public static RadioButton r1, r2, r3;
    public NumberPicker snoozePicker;
    public Button cancel_snoozePicker, save_snoozePicker;
    public Boolean snoozePickerVisible = false;
    static LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);
        settingsSharedPreferences = getSharedPreferences("Settings.db", MODE_PRIVATE);
        Settings_Storage = GsonConverter.jsonToSettings(
                settingsSharedPreferences.getString(SETTINGS_DB, null));
        dark_mode_switch = findViewById(R.id.Settings_dark_mode_switch);
        assistive_notification_switch = findViewById(
                R.id.Setting_Assistive_Notification_Bar_Switch);
        user_Name = findViewById(R.id.Setting_User_Name);
        below_user_name = findViewById(R.id.Setting_below_username_txt);
        user_profile_btn = findViewById(R.id.Settings_User_Profile_btn);
        profileImage = findViewById(R.id.Settings_user_image);
        back_btn = findViewById(R.id.Setting_back_btn);
        logout_layout = findViewById(R.id.Settings_Logout_Rel_layout);
        logout_txt = findViewById(R.id.Setting_Logout_txt_view);
        logout_email_text = findViewById(R.id.Setting_Logout_email_txt_view);
        app_password_layout = findViewById(R.id.Setting_App_Password_layout);
        backup_password_layout = findViewById(R.id.Settings_Back_Up_Settings_Main_Rel_layout);
        quickNote_txt_view = findViewById(R.id.Settings_Quick_Note_Selection_txt);
        logout_btn = findViewById(R.id.Setting_Logout_logout_btn);
        snoozePicker = findViewById(R.id.Setting_Snooze_minutePicker);
        snoozeIndicator = findViewById(R.id.Setting_Snooze_indicator_txt);
        main_layout = findViewById(R.id.Setting_Main_rel_layout);
        snoozePicker_layout = findViewById(R.id.Setting_Snooze_Picker_layout);
        cancel_snoozePicker = findViewById(R.id.Setting_Snooze_minutePicker_cancel_btn);
        save_snoozePicker = findViewById(R.id.Setting_Snooze_minutePicker_save_btn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(Settings_Main.this, gso);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        LoadChangedSettings(Settings_Main.this);

        final ArrayList<QuickNoteSpinnerItem> colorSet = new ArrayList<>(4);
        colorSet.add(new QuickNoteSpinnerItem("Black", R.color.black));
        colorSet.add(new QuickNoteSpinnerItem("Gray", R.color.color_dark_gray));
        colorSet.add(new QuickNoteSpinnerItem("Yellow", R.color.color_yellow));
        snoozePicker.setMaxValue(60);
        snoozePicker.setMinValue(5);

        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20, 0, 20, 0);

        if (user != null) {
            if (Settings_Main.User_Name != null && !Settings_Main.User_Name.isEmpty()) {
                processedUserName = Settings_Main.User_Name;
                user_Name.setText(processedUserName);
            } else {
                if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
                    processedUserName = user.getDisplayName();
                    user_Name.setText(processedUserName);
                    Settings_Main.User_Name = processedUserName;
                    SaveSettings(Settings_Main.this);
                } else if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                    processedUserName = user.getEmail();
                    user_Name.setText("Set your nickname");
                } else {
                    user_Name.setText("Set your nickname");
                }
            }
            logout_email_text.setText(user.getEmail());
            File imageUri = loadImageFromStorage(Settings_Main.this);

            if (imageUri.exists()) {
                Picasso.get().load(imageUri).fit().centerCrop().transform(
                        new UserProfile.CircleTransform()).into(profileImage);

            } else if (processedUserName != null && !processedUserName.isEmpty()) {
                profileImage.setImageDrawable(
                        AvatarGenerator.Companion.avatarImage(Settings_Main.this, 50,
                                AvatarConstants.Companion.getCIRCLE(), processedUserName));
            } else {
                profileImage.setImageDrawable(
                        AvatarGenerator.Companion.avatarImage(Settings_Main.this, 50,
                                AvatarConstants.Companion.getCIRCLE(), "Schedule Manager"));
            }

            below_user_name.setText("Edit your Profile");
            user_profile_btn.setOnClickListener(
                    view -> startActivity(new Intent(Settings_Main.this, UserProfile.class)));

            app_password_layout.setOnClickListener(
                    view -> startActivity(
                            new Intent(Settings_Main.this, AppPasswordActivity.class)));

            backup_password_layout.setOnClickListener(
                    view -> startActivity(new Intent(Settings_Main.this, BackupSettings.class)));

            logout_txt.setText("Logout");

            logout_layout.setOnClickListener(view -> LogoutDialog());

            logout_btn.setOnClickListener(view -> LogoutDialog());

        } else {
            makeLoginSnackBar();
            user_Name.setText("User has not Signed In");
            logout_email_text.setText("No User");
            logout_txt.setText("Log In");
            logout_layout.setOnClickListener(
                    view -> startActivity(
                            new Intent(Settings_Main.this, MainActivity.class).setFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_NEW_TASK)));

            logout_btn.setOnClickListener(
                    view -> startActivity(
                            new Intent(Settings_Main.this, MainActivity.class).setFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_NEW_TASK)));

            profileImage.setImageDrawable(
                    AvatarGenerator.Companion.avatarImage(Settings_Main.this, 70,
                            AvatarConstants.Companion.getCIRCLE(), "S"));
            user_Name.setText("Schedule Manager");
            below_user_name.setText("Sign in to edit profile");

            user_profile_btn.setOnClickListener(view -> makeLoginSnackBar());

            app_password_layout.setOnClickListener(view -> makeLoginSnackBar());

            backup_password_layout.setOnClickListener(view -> makeLoginSnackBar());
        }

        dark_mode_switch.setChecked(isDarkMode);

        dark_mode_switch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                isDarkMode = true;
            } else {
                isDarkMode = false;
            }
            DarkModeApplier(isDarkMode);
            Settings_Storage.isDarkMode = isDarkMode;
            SaveSettings(Settings_Main.this);

            UtilsArraylist.initNote(Settings_Main.this);
            Notes_rec_Adapter adapter = new Notes_rec_Adapter(Settings_Main.this,
                    UtilsArraylist.note);
            adapter.notifyDataSetChanged();

            Task_rec_adapter adapter1 = new Task_rec_adapter(UtilsArray_Task.task);
            adapter1.notifyDataSetChanged();
        });

        assistive_notification_switch.setOnCheckedChangeListener(
                (compoundButton, b) -> {
                    if (b) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                && !Settings.canDrawOverlays(Settings_Main.this)) {
                            Intent intent = new Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, APP_PERMISSION_REQUEST);
                        } else {
                            isAssitiveNotificationOn = true;
                            createNotification(Settings_Main.this);
                            Settings_Storage.isAssitiveNotificationOn = true;
                            SaveSettings(Settings_Main.this);
                            makeSnackBar(main_layout,
                                    "You will find notification bar in few seconds");
                        }
                    } else {
                        isAssitiveNotificationOn = false;
                        destroyNotification(Settings_Main.this);
                        Settings_Storage.isAssitiveNotificationOn = false;
                        SaveSettings(Settings_Main.this);
                    }
                });

        quickNote_txt_view.setOnClickListener(view -> QuickNoteColorDialog());

        back_btn.setOnClickListener(view -> onBackPressed());

        snoozeIndicator.setText(Snooze_Time + " minutes");
        if (Snooze_Time != 5) snoozePicker.setValue(Snooze_Time);

        snoozeIndicator.setOnClickListener(view -> {
            if (snoozePickerVisible) {
                snoozePicker_layout.setVisibility(View.INVISIBLE);
                main_layout.setVisibility(View.VISIBLE);
                back_btn.setVisibility(View.VISIBLE);
                snoozePickerVisible = false;
            } else {
                main_layout.setVisibility(View.INVISIBLE);
                snoozePicker_layout.setVisibility(View.VISIBLE);
                back_btn.setVisibility(View.INVISIBLE);
                snoozePickerVisible = true;
            }
        });

        save_snoozePicker.setOnClickListener(v -> {

            Snooze_Time = snoozePicker.getValue();
            snoozeIndicator.setText(snoozePicker.getValue() + " minutes");
            Settings_Main.SaveSettings(Settings_Main.this);
            snoozePicker_layout.setVisibility(View.GONE);
            main_layout.setVisibility(View.VISIBLE);
            back_btn.setVisibility(View.VISIBLE);
            snoozePickerVisible = false;
        });

        cancel_snoozePicker.setOnClickListener(view -> {
            if (snoozePickerVisible) {
                snoozePicker_layout.setVisibility(View.INVISIBLE);
                main_layout.setVisibility(View.VISIBLE);
                back_btn.setVisibility(View.VISIBLE);
                snoozePickerVisible = false;
            }
        });
    }

    public static void createNotification(Context context) {
        if (isAssitiveNotificationOn) {
            Intent i = new Intent(context, AlwaysOnNotification.class);
            PendingIntent p = PendingIntent.getBroadcast(context, 0, i,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), p);
            }
        } else {
            destroyNotification(context);
        }
    }

    public static void destroyNotification(Context context) {
        if (!isAssitiveNotificationOn) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(1110);
        } else {
            createNotification(context);
        }
    }

    public void QuickNoteColorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Settings_Main.this);
        View view = getLayoutInflater().inflate(R.layout.quicknote_text_color_dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        r1 = view.findViewById(R.id.quicknote_text_color_radio_1);
        r2 = view.findViewById(R.id.quicknote_text_color_radio_2);
        r3 = view.findViewById(R.id.quicknote_text_color_radio_3);
        Button dismiss = view.findViewById(R.id.quicknote_text_color_close_btn);
        dialog.setCanceledOnTouchOutside(false);

        r1.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) radioSelector(1, Settings_Main.this);
        });

        r2.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) radioSelector(2, Settings_Main.this);
        });

        r3.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) radioSelector(3, Settings_Main.this);
        });

        dismiss.setOnClickListener(view1 -> dialog.dismiss());
        dialog.show();
        if (QuickNoteTextColor == R.color.color_yellow) {
            r1.setChecked(true);
            r2.setChecked(false);
            r3.setChecked(false);
        } else if (QuickNoteTextColor == R.color.black) {
            r1.setChecked(false);
            r2.setChecked(true);
            r3.setChecked(false);
        } else if (QuickNoteTextColor == R.color.color_dark_gray) {
            r1.setChecked(false);
            r2.setChecked(false);
            r3.setChecked(true);
        }
        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public static void radioSelector(Integer num, Context context) {
        if (num == 1) {
            r1.setChecked(true);
            r2.setChecked(false);
            r3.setChecked(false);
            QuickNoteTextColor = R.color.color_yellow;
            Settings_Storage.color = QuickNoteTextColor;
            SaveSettings(context);

        } else if (num == 2) {
            r1.setChecked(false);
            r2.setChecked(true);
            r3.setChecked(false);
            QuickNoteTextColor = R.color.black;
            Settings_Storage.color = QuickNoteTextColor;
            SaveSettings(context);

        } else if (num == 3) {
            r1.setChecked(false);
            r2.setChecked(false);
            r3.setChecked(true);
            QuickNoteTextColor = R.color.color_dark_gray;
            Settings_Storage.color = QuickNoteTextColor;
            SaveSettings(context);

        }
    }

    public static void SaveSettings(Context context) {
        if (App_Password != null) {
            Settings_Storage.App_Password = App_Password;
        }

        if (QuickNoteTextColor != null) {
            Settings_Storage.color = QuickNoteTextColor;
        } else if (Settings_Storage.color != null) Settings_Storage.color = R.color.color_yellow;

        if (isAssitiveNotificationOn != null) {
            Settings_Storage.isAssitiveNotificationOn = isAssitiveNotificationOn;
        } else {
            Settings_Storage.isAssitiveNotificationOn = false;
        }

        if (isDarkMode != null) {
            Settings_Storage.isDarkMode = isDarkMode;
        } else {
            Settings_Storage.isDarkMode = false;
        }

        if (User_Name != null && !User_Name.isEmpty()) {
            Settings_Storage.UserName = User_Name;
        } else {
            Settings_Storage.UserName = "Set User Name";
        }

        if (Snooze_Time != null) {
            Settings_Storage.SnoozeTime = Snooze_Time;
        } else {
            Settings_Storage.SnoozeTime = 10;
        }


        settingsSharedPreferences = context.getSharedPreferences("Settings.db", MODE_PRIVATE);

        SharedPreferences.Editor editor = settingsSharedPreferences.edit();
        editor.remove(SETTINGS_DB);
        editor.putString(SETTINGS_DB, GsonConverter.SettingstoJson(Settings_Storage));
        editor.commit();
    }

    public static void LoadChangedSettings(Context context) {
        settingsSharedPreferences = context.getSharedPreferences("Settings.db", MODE_PRIVATE);
        Settings_Storage = GsonConverter.jsonToSettings(
                settingsSharedPreferences.getString(SETTINGS_DB, null));

        if (Settings_Storage != null) {
            isDarkMode = Settings_Storage.isDarkMode;
            isAssitiveNotificationOn = Settings_Storage.isAssitiveNotificationOn;
            QuickNoteTextColor = Settings_Storage.color;
            App_Password = Settings_Storage.App_Password;
            User_Name = Settings_Storage.UserName;
            Snooze_Time = Settings_Storage.SnoozeTime;

            if (QuickNoteTextColor == null) {
                QuickNoteTextColor = R.color.color_yellow;
            }
            if (isDarkMode == null) {
                isDarkMode = false;
            }
            DarkModeApplier(isDarkMode);

            if (isAssitiveNotificationOn == null) {
                isAssitiveNotificationOn = false;
            }

            if (isAssitiveNotificationOn) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(
                        context)) {
                    isAssitiveNotificationOn = false;
                    Settings_Storage.isAssitiveNotificationOn = false;
                    SaveSettings(context);
                    destroyNotification(context);
                } else {
                    isAssitiveNotificationOn = true;
                    Settings_Storage.isAssitiveNotificationOn = true;
                    SaveSettings(context);
                    createNotification(context);
                }
            } else {
                destroyNotification(context);
            }
            if (Snooze_Time == null) Snooze_Time = 10;
        } else {
            Settings_Storage = new Setting_Class();
            isDarkMode = false;
            DarkModeApplier(isDarkMode);
            isAssitiveNotificationOn = false;
            QuickNoteTextColor = R.color.color_yellow;
            App_Password = null;
            User_Name = null;
            Snooze_Time = 10;
        }
    }

    public static void ResetSettings(Context context) {
        Settings_Storage.App_Password = null;
        Settings_Storage.color = R.color.color_yellow;
        Settings_Storage.isAssitiveNotificationOn = false;
        Settings_Storage.isDarkMode = false;
        Settings_Storage.UserName = null;
        Settings_Storage.SnoozeTime = 10;

        settingsSharedPreferences = context.getSharedPreferences("Settings.db", MODE_PRIVATE);
        SharedPreferences.Editor editor = settingsSharedPreferences.edit();
        editor.remove(SETTINGS_DB);
        editor.putString(SETTINGS_DB, GsonConverter.SettingstoJson(Settings_Storage));
        editor.commit();
        LoadChangedSettings(context);
    }


    public void LogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Settings_Main.this)
                .setTitle("Log Out")
                .setMessage(
                        "Are you sure you want to Log Out. This will delete all data you didn't "
                                + "backed up in server?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                    deleteImageFromStorage(Settings_Main.this);
                    LogoutDeleteUserData(Settings_Main.this);
                    mGoogleSignInClient.signOut();
                    mAuth.signOut();
                    user = mAuth.getCurrentUser();
                    if (user == null) {
                        startActivity(new Intent(Settings_Main.this, MainActivity.class));
                        makeSnackBar(main_layout, "Logged out successfully");
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        Button nButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        nButton.setLayoutParams(params);
        nButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        Button yButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        yButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        yButton.setLayoutParams(params);

    }

    public static void LogoutDeleteUserData(Context context) {
        ResetSettings(context);
        UtilsArraylist.ClearNoteArraylist(context);
        UtilsArray_Task.ClearTaskArraylist(context);
        UtilsArray_Email.ClearEmailArrayList(context);
        UtilsArray_Trash.ClearTrash(context);
    }

    public static File loadImageFromStorage(Context context) {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        File directory = cw.getDir("Schedule Manager", Context.MODE_PRIVATE);
        return new File(directory, "profile.jpg");
    }

    public static Boolean deleteImageFromStorage(Context context) {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        File directory = cw.getDir("Schedule Manager", Context.MODE_PRIVATE);
        File f = new File(directory, "profile.jpg");
        return f.delete();
    }

    @Override
    public void onBackPressed() {
        if (snoozePickerVisible) {
            snoozePicker_layout.setVisibility(View.INVISIBLE);
            main_layout.setVisibility(View.VISIBLE);
            back_btn.setVisibility(View.VISIBLE);
            snoozePickerVisible = false;
        } else {
            finish();
        }
    }

    public static class saveToInternalStorage extends AsyncTask {
        Context context;
        Bitmap bitmapImage;
        ProgressDialog pdialog;

        public saveToInternalStorage(Bitmap bitmap, Context c) {
            context = c;
            bitmapImage = bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new ProgressDialog(context);
            pdialog.setCancelable(false);
            pdialog.setTitle("Please wait! Saving profile image ");
            pdialog.setMessage("It might take some seconds for big images");
            pdialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Object doInBackground(Object[] objects) {
            ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
            File directory = cw.getDir("Schedule Manager", Context.MODE_PRIVATE);
            File mypath = new File(directory, "profile.jpg");
            FileOutputStream fos = null;
            int ratio = 100;
            if (bitmapImage.getAllocationByteCount() < 1000) {
                ratio = 65;
            } else if (bitmapImage.getAllocationByteCount() <= 5000) {
                ratio = 10;
            } else if (bitmapImage.getAllocationByteCount() <= 7000) {
                ratio = 2;
            } else {
                ratio = 1;
            }
            try {
                fos = new FileOutputStream(mypath);
                bitmapImage.compress(Bitmap.CompressFormat.PNG, ratio, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (pdialog.isShowing()) pdialog.dismiss();
            Toast.makeText(context, "Profile image saved successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public static void DarkModeApplier(Boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(
                Settings_Main.this)) {
            isAssitiveNotificationOn = true;
            Settings_Storage.isAssitiveNotificationOn = isAssitiveNotificationOn;
            SaveSettings(Settings_Main.this);
            createNotification(Settings_Main.this);
            makeSnackBar(main_layout, "You will find notification bar in few seconds");
        } else {
            Toast.makeText(this, "Draw over other apps permission not granted",
                    Toast.LENGTH_SHORT).show();
            assistive_notification_switch.setChecked(false);
            isAssitiveNotificationOn = false;
            Settings_Storage.isAssitiveNotificationOn = isAssitiveNotificationOn;
            SaveSettings(Settings_Main.this);
            destroyNotification(Settings_Main.this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void makeLoginSnackBar() {
        Snackbar.make(main_layout, "Please Login to use this feature", Snackbar.LENGTH_SHORT)
                .setAction("Login", view -> {
                    startActivity(new Intent(Settings_Main.this, MainActivity.class));
                    finish();
                }).setActionTextColor(
                Settings_Main.this.getResources().getColor(R.color.color_yellow))
                .show();
    }

    public void makeSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}