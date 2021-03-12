package com.example.schedulemanager.Setting;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.avatarfirst.avatargenlib.AvatarConstants;
import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.example.schedulemanager.AlwaysOnNotification;
import com.example.schedulemanager.LoginandUser.UserProfile;
import com.example.schedulemanager.MainActivity;
import com.example.schedulemanager.MainFragments.HomePage;
import com.example.schedulemanager.QuickNoteSpinnerItem;
import com.example.schedulemanager.R;
import com.example.schedulemanager.Task.Task_rec_adapter;
import com.example.schedulemanager.Task.UtilsArray_Task;
import com.example.schedulemanager.Trash.UtilsArray_Trash;
import com.example.schedulemanager.email.UtilsArray_Email;
import com.example.schedulemanager.note.GsonConverter;
import com.example.schedulemanager.note.Notes_rec_Adapter;
import com.example.schedulemanager.note.UtilsArraylist;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

public class Settings_Main extends AppCompatActivity {


    private final int APP_PERMISSION_REQUEST = 110;

    RelativeLayout logout_layout, app_password_layout, backup_password_layout, snoozePicker_layout, main_layout;
    public SwitchCompat assistive_notification_switch, dark_mode_switch;
    public static ImageView profileImage;
    ImageButton user_profile_btn, back_btn, logout_btn;
    //    deleteAccount_btn, log_Out, update_Email_ID_btn, update_Password_btn;
    TextView user_Name, below_user_name, logout_txt, logout_email_text, quickNote_txt_view, snoozeIndicator;
    //    email_ID_Edit_Txt, password_Edit_txt;
    static FirebaseAuth mAuth;
    static FirebaseUser user;
    public GoogleSignInClient mGoogleSignInClient;
    static String processedUserName;
    public static Boolean isDarkMode, isAssitiveNotificationOn;
    public static Integer QuickNoteTextColor, Snooze_Time;
    public static SharedPreferences settingsSharedPreferences;
    //    EmailsharedPreferences, TasksharedPreferences, sharedPreferences;
    final private static String SETTINGS_DB = "Settings_Main_DB";
    final private static String EMAIL_ARRAY_DB = "Email_Array_DB";
    final private static String EMAIL_INTENT_DB = "Email_Intent_DB";
    final private static String NOTES_ARRAY_DB = "Notes_Array_DB";
    final private static String NOTES_INTENT_DB = "Notes_Intent_DB";
    final private static String TASK_ARRAY_DB = "Task_Array_DB";
    final private static String TASK_INTENT_DB = "Task_Intent_DB";
    public static String App_Password, User_Name;
    static Setting_Class Settings_Storage;
    //    Spinner quickNoteSpinner;
//    QuickNoteSpinnerAdapter adapter;
//    public static Activity activity;
    public static RadioButton r1, r2, r3;
    public NumberPicker snoozePicker;
    public Button cancel_snoozePicker, save_snoozePicker;
    public Boolean snoozePickerVisible = false;
    static LinearLayout.LayoutParams params;
//    public static Uri profileBitmap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);
        settingsSharedPreferences = getSharedPreferences("Settings.db", MODE_PRIVATE);
//        settings2SharedPreferences = getSharedPreferences(SETTINGS2_DB, MODE_PRIVATE);
        Settings_Storage = GsonConverter.jsonToSettings(settingsSharedPreferences.getString(SETTINGS_DB, null));

        dark_mode_switch = findViewById(R.id.Settings_dark_mode_switch);
        assistive_notification_switch = findViewById(R.id.Setting_Assistive_Notification_Bar_Switch);
//        deleteAccount_btn = findViewById(R.id.Setting_delete_account_btn);
//        log_Out = findViewById(R.id.Setting_LogOut_btn);
        user_Name = findViewById(R.id.Setting_User_Name);
        below_user_name = findViewById(R.id.Setting_below_username_txt);
//        quickNoteSpinner = findViewById(R.id.Setting_Quick_Note_Color_Spinner);
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

//        dark_mode_theme_layout = findViewById(R.id.Settings_dark_mode_switch);
//        dark_mode_indicator = findViewById(R.id.Setting_theme_current_txt);
//        dark_mode_indicator.setText("Light Mode");


//        email_ID_Edit_Txt = findViewById(R.id.Settings_Default_Email_ID_Edit_Txt);
//        update_Email_ID_btn = findViewById(R.id.Settings_Default_Email_ID_Update_btn);
//        password_Edit_txt = findViewById(R.id.Settings_Default_Password_Edit_Txt);
//        update_Password_btn = findViewById(R.id.Settings_Default_Password_Update_btn)

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(Settings_Main.this, gso);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        LoadChangedSettings(Settings_Main.this);

//        ThemeTextUpdater(DarkModeInt);


        final ArrayList<QuickNoteSpinnerItem> colorSet = new ArrayList<>(4);
//        colorSet.add(new QuickNoteSpinnerItem("White", R.drawable.quick_note_white_color));
        colorSet.add(new QuickNoteSpinnerItem("Black", R.color.black));
        colorSet.add(new QuickNoteSpinnerItem("Gray", R.color.color_dark_gray));
        colorSet.add(new QuickNoteSpinnerItem("Yellow", R.color.color_yellow));

        snoozePicker.setMaxValue(60);
        snoozePicker.setMinValue(5);

//        adapter = new QuickNoteSpinnerAdapter(Settings_Main.this, colorSet);
//        quickNoteSpinner.setAdapter(adapter);
//        quickNoteSpinner.setSelection(SelectSpinnerItem());
//        quickNoteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                QuickNoteTextColor = colorSet.get(i).Color_ID;
//                Log.e("QUICKNOTETEXTCOLORSelec", String.valueOf(i));
//                Log.e("QUICKNOTETEXTCOLOR", String.valueOf(QuickNoteTextColor));
//                Settings_Storage.color = QuickNoteTextColor;
//                SaveSettings(Settings_Main.this);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                Log.e("QUICKNOTETEXTCOLOR", String.valueOf(QuickNoteTextColor));
//            }
//        });

//        quickNoteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.d("QUCIKNOTETEXTCOLORClick", String.valueOf(QuickNoteTextColor));
//                QuickNoteTextColor = colorSet.get(i).Color_ID;
//                Settings_Storage.color = QuickNoteTextColor;
//                SaveSettings(Settings_Main.this);
//            }
//        });

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
//                    final InputStream imageStream;
//                    UserProfile.this.getContentResolver().takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION
//                            + Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    imageStream = getContentResolver().openInputStream(Settings_Main.profileBitmap);
//                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                Picasso.get().load(imageUri).fit().centerCrop().transform(new UserProfile.CircleTransform()).into(profileImage);

            } else if (processedUserName != null && !processedUserName.isEmpty()) {
                profileImage.setImageDrawable(AvatarGenerator.Companion.avatarImage(Settings_Main.this, 50, AvatarConstants.Companion.getCIRCLE(), processedUserName));
            } else
                profileImage.setImageDrawable(AvatarGenerator.Companion.avatarImage(Settings_Main.this, 50, AvatarConstants.Companion.getCIRCLE(), "Schedule Manager"));


            below_user_name.setText("Edit your Profile");
            user_profile_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Settings_Main.this, UserProfile.class));
                }
            });


//            email_ID_Edit_Txt.setText(user.getEmail());

//            update_Email_ID_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(Settings_Main.this)
//                            .setTitle("Update Email Id")
//                            .setMessage("Are you sure you want to update email id. This will step cannot be reverted and you need to login again with new email id?")
//                            .setNegativeButton(android.R.string.no, null)
//                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    verifyAndUpdateEmail(email_ID_Edit_Txt.getText().toString(), Settings_Main.this);
//                                }
//                            });
//
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//
//                }
//            });

//            update_Password_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(Settings_Main.this)
//                            .setTitle("Update Password")
//                            .setMessage("Are you sure you want to update password. This will step cannot be reverted and you need to login again with new password?")
//                            .setNegativeButton(android.R.string.no, null)
//                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    verifyAndUpdatePassword(password_Edit_txt.getText().toString(), Settings_Main.this);
//                                }
//                            });
//
//
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//                }
//            });


//            deleteAccount_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(Settings_Main.this)
//                            .setTitle("Delete Account")
//                            .setMessage("Are you sure you want to delete account. This will remove all your data from server?")
//                            .setNegativeButton(android.R.string.no, null)
//                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    mDatabase.child(uid).removeValue();
//                                    user = mAuth.getCurrentUser();
//                                    user.delete();
//                                    if (user == null) {
//                                        Toast.makeText(Settings_Main.this, "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(Settings_Main.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//                                        finish();
//                                    } else {
//                                        user.delete();
//                                        Toast.makeText(Settings_Main.this, "Some error occurred while deleting account", Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(Settings_Main.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//                                        finish();
//                                    }
//                                }
//                            });
//
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//
//
//                }
//            });

            app_password_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Settings_Main.this, AppPasswordActivity.class));
                }
            });

            backup_password_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Settings_Main.this, BackupSettings.class));
                }
            });

            logout_txt.setText("Logout");

            logout_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LogoutDialog();
                }
            });

            logout_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LogoutDialog();
                }
            });

        } else {
            makeLoginSnackBar();
//            Toast.makeText(Settings_Main.this, "You need to login to use all features", Toast.LENGTH_LONG).show();
            user_Name.setText("User has not Signed In");
//            email_ID_Edit_Txt.setHint("User has not Signed In ");
//            email_ID_Edit_Txt.setEnabled(false);
//            password_Edit_txt.setHint("User has not Signed In ");
//            password_Edit_txt.setEnabled(false);
//            user_profile_btn.setEnabled(false);
//            deleteAccount_btn.setEnabled(false);
//            update_Password_btn.setEnabled(false);
//            update_Email_ID_btn.setEnabled(false);

            logout_email_text.setText("No User");
            logout_txt.setText("Log In");
            logout_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Settings_Main.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });

            logout_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Settings_Main.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });

            profileImage.setImageDrawable(AvatarGenerator.Companion.avatarImage(Settings_Main.this, 70, AvatarConstants.Companion.getCIRCLE(), "S"));
            user_Name.setText("Schedule Manager");
            below_user_name.setText("Sign in to edit profile");

            user_profile_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeLoginSnackBar();
//                    Toast.makeText(Settings_Main.this, "You need to login to use this features", Toast.LENGTH_SHORT).show();
                }
            });

            app_password_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeLoginSnackBar();
//                    Toast.makeText(Settings_Main.this, "You need to login to use this features", Toast.LENGTH_SHORT).show();
                }
            });

            backup_password_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeLoginSnackBar();
//                    Toast.makeText(Settings_Main.this, "You need to login to use this features", Toast.LENGTH_SHORT).show();
                }
            });
        }

//        dark_mode_theme_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SelectThemeDialog(Settings_Main.this);
//            }
//        });

        assistive_notification_switch.setChecked(isAssitiveNotificationOn);
//        assistive_notification_switch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(Settings_Main.this)) {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                            Uri.parse("package:" + getPackageName()));
//                    startActivityForResult(intent, APP_PERMISSION_REQUEST);
//                }
//            }
//        });


        dark_mode_switch.setChecked(isDarkMode);

        dark_mode_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isDarkMode = true;
                } else {
                    isDarkMode = false;
                }
                DarkModeApplier(isDarkMode);
                Settings_Storage.isDarkMode = isDarkMode;
                SaveSettings(Settings_Main.this);

                UtilsArraylist.initNote(Settings_Main.this);
                Notes_rec_Adapter adapter = new Notes_rec_Adapter(Settings_Main.this, UtilsArraylist.note);
                adapter.notifyDataSetChanged();

                Task_rec_adapter adapter1 = new Task_rec_adapter(UtilsArray_Task.task);
                adapter1.notifyDataSetChanged();

            }
        });

        assistive_notification_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(Settings_Main.this)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, APP_PERMISSION_REQUEST);
                    } else {
                        isAssitiveNotificationOn = true;
                        createNotification(Settings_Main.this);
                        Settings_Storage.isAssitiveNotificationOn = true;
                        SaveSettings(Settings_Main.this);
                        makeSnackBar(main_layout, "You will find notification bar in few seconds");
//                        Toast.makeText(Settings_Main.this, "You will find notification bar in few seconds", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    isAssitiveNotificationOn = false;
                    destroyNotification(Settings_Main.this);
                    Settings_Storage.isAssitiveNotificationOn = false;
                    SaveSettings(Settings_Main.this);
                }
            }
        });

        quickNote_txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuickNoteColorDialog();
            }
        });


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        snoozeIndicator.setText(Snooze_Time + " minutes");
        if (Snooze_Time!=5)snoozePicker.setValue(Snooze_Time);

        snoozeIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        save_snoozePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snooze_Time = snoozePicker.getValue();
                snoozeIndicator.setText(snoozePicker.getValue() + " minutes");
                Settings_Main.SaveSettings(Settings_Main.this);
                snoozePicker_layout.setVisibility(View.GONE);
                main_layout.setVisibility(View.VISIBLE);
                back_btn.setVisibility(View.VISIBLE);
                snoozePickerVisible = false;
            }
        });

        cancel_snoozePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (snoozePickerVisible) {
                    snoozePicker_layout.setVisibility(View.INVISIBLE);
                    main_layout.setVisibility(View.VISIBLE);
                    back_btn.setVisibility(View.VISIBLE);
                    snoozePickerVisible = false;
                }
            }
        });

    }

    public static void createNotification(Context context) {

        if (isAssitiveNotificationOn) {

            Intent i = new Intent(context, AlwaysOnNotification.class);
            PendingIntent p = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), p);
            }
        } else destroyNotification(context);
    }

    public static void destroyNotification(Context context) {
        if (!isAssitiveNotificationOn) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(1110);
        } else createNotification(context);
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

        r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) radioSelector(1, Settings_Main.this);
            }
        });

        r2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) radioSelector(2, Settings_Main.this);
            }
        });

        r3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) radioSelector(3, Settings_Main.this);
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
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
        dialog.getWindow().setLayout(400,ViewGroup.LayoutParams.WRAP_CONTENT);
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


//    public void AppPasswordDialog(){
//        final AlertDialog.Builder alert = new AlertDialog.Builder(Settings_Main.this);
//        View mView = getLayoutInflater().inflate(R.layout.app_password_custom_dialog,null);
//        alert.setView(mView);
//        final AlertDialog alertDialog = alert.create();
//        final EditText txt_inputText = mView.findViewById(R.id.App_Password_Custom_Dialog_Edit_txt);
//        Button btn_cancel = mView.findViewById(R.id.App_Password_Custom_Dialog_btn_cancel);
//        Button btn_okay = mView.findViewById(R.id.App_Password_Custom_Dialog_btn_save);
//        alertDialog.setCanceledOnTouchOutside(false);
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
//        btn_okay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!txt_inputText.getText().toString().isEmpty()) {
//                    Settings_Storage = GsonConverter.jsonToSettings(settingsSharedPreferences.getString(SETTINGS_DB, null));
//                    Settings_Storage.setApp_Password(txt_inputText.getText().toString());
//                    alertDialog.dismiss();
//                    SaveSettings();
//                }else Toast.makeText(Settings_Main.this,"Please Enter Valid Password",Toast.LENGTH_LONG).show();
//            }
//        });
//        alertDialog.show();
//    }

//    public static void SelectThemeDialog(Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.theme_selection_layout, null);
//        builder.setView(view);
//
//        AlertDialog dialog = builder.create();
//        dialog.setCanceledOnTouchOutside(false);
//
//        r1 = view.findViewById(R.id.theme_layout_1_radio_1);
//        r2 = view.findViewById(R.id.theme_layout_2_radio_1);
//        r3 = view.findViewById(R.id.theme_layout_3_radio_1);
//        r4 = view.findViewById(R.id.theme_layout_4_radio_1);
//
//        radioSelector(DarkModeInt,context);
//
//        r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) radioSelector(1,context);
//                DarkModeInt = 1;
//                dark_mode_indicator.setText("Light Mode");
//                SaveSettings(context);
//                dialog.dismiss();
//            }
//        });
//
//
//        r2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) radioSelector(2,context);
//                DarkModeInt = 2;
//                SaveSettings(context);
//                dialog.dismiss();
//            }
//        });
//
//
//        r3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) radioSelector(-1,context);
//                DarkModeInt = -1;
//                SaveSettings(context);
//                dialog.dismiss();
//            }
//        });
//
//
//        r4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) radioSelector(3,context);
//                DarkModeInt = 3;
//                SaveSettings(context);
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }


    public static void SaveSettings(Context context) {

        if (App_Password != null)
            Settings_Storage.App_Password = App_Password;

        if (QuickNoteTextColor != null)
            Settings_Storage.color = QuickNoteTextColor;
        else if (Settings_Storage.color != null) Settings_Storage.color = R.color.color_yellow;

        if (isAssitiveNotificationOn != null)
            Settings_Storage.isAssitiveNotificationOn = isAssitiveNotificationOn;
        else Settings_Storage.isAssitiveNotificationOn = false;

        if (isDarkMode != null)
            Settings_Storage.isDarkMode = isDarkMode;
        else Settings_Storage.isDarkMode = false;
//        Settings_Storage.profileImage= profileBitmap;

        if (User_Name != null && !User_Name.isEmpty())
            Settings_Storage.UserName = User_Name;
        else Settings_Storage.UserName = "Set User Name";

        if (Snooze_Time != null)
            Settings_Storage.SnoozeTime = Snooze_Time;
        else Settings_Storage.SnoozeTime = 10;


        settingsSharedPreferences = context.getSharedPreferences("Settings.db", MODE_PRIVATE);
//        Settings_Storage = GsonConverter.jsonToSettings(settingsSharedPreferences.getString(SETTINGS_DB, null));

        SharedPreferences.Editor editor = settingsSharedPreferences.edit();
        editor.remove(SETTINGS_DB);
        editor.putString(SETTINGS_DB, GsonConverter.SettingstoJson(Settings_Storage));
//        Log.e("SAVEDSETTINGSMAIN",GsonConverter.SettingstoJson(Settings_Storage));
        editor.commit();

    }

    public static void LoadChangedSettings(Context context) {
        settingsSharedPreferences = context.getSharedPreferences("Settings.db", MODE_PRIVATE);

//        Log.e("LOADEDSETTINGSMAIN",settingsSharedPreferences.getString(SETTINGS_DB, null));
        Settings_Storage = GsonConverter.jsonToSettings(settingsSharedPreferences.getString(SETTINGS_DB, null));


        if (Settings_Storage != null) {
            isDarkMode = Settings_Storage.isDarkMode;
            isAssitiveNotificationOn = Settings_Storage.isAssitiveNotificationOn;
            QuickNoteTextColor = Settings_Storage.color;
            App_Password = Settings_Storage.App_Password;
            User_Name = Settings_Storage.UserName;
            Snooze_Time = Settings_Storage.SnoozeTime;
//            profileBitmap = Settings_Storage.profileImage;


            if (QuickNoteTextColor == null) {
                QuickNoteTextColor = R.color.color_yellow;
            }
            if (isDarkMode == null) {
                isDarkMode = false;
            }
//            ThemeApplier(DarkModeInt);
            DarkModeApplier(isDarkMode);

            if (isAssitiveNotificationOn == null) {
                isAssitiveNotificationOn = false;
            }

            if (isAssitiveNotificationOn) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
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
            } else destroyNotification(context);

            if (Snooze_Time == null) Snooze_Time = 10;

//            if (App_Password==null){
//                App_password_Edit_txt.setText("Click here to add App Password");
//            }else App_password_Edit_txt.setText(App_Password);

        } else {
            Settings_Storage = new Setting_Class();
            isDarkMode = false;
//            ThemeApplier(DarkModeInt);
            DarkModeApplier(isDarkMode);
            isAssitiveNotificationOn = false;
            QuickNoteTextColor = R.color.color_yellow;
            App_Password = null;
            User_Name = null;
            Snooze_Time = 10;
//            profileBitmap = null;
//            App_password_Edit_txt.setText("Click here to add App Password");
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
                .setMessage("Are you sure you want to Log Out. This will delete all data you didn't backed up in server?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        deleteImageFromStorage(Settings_Main.this);
                        LogoutDeleteUserData(Settings_Main.this);
                        mGoogleSignInClient.signOut();
                        mAuth.signOut();
                        user = mAuth.getCurrentUser();
                        if (user == null) {
                            startActivity(new Intent(Settings_Main.this, MainActivity.class));
                            makeSnackBar(main_layout, "Logged out successfully");
//                            Toast.makeText(Settings_Main.this, "Signed Out successfully ...", Toast.LENGTH_SHORT).show();
                            finish();
                        }
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
//        settingsSharedPreferences = context.getSharedPreferences("Settings.db", MODE_PRIVATE);
//        SharedPreferences.Editor settingsEditor = settingsSharedPreferences.edit();
////        settingsEditor.remove(SETTINGS_DB);
//        settingsEditor.clear();
//        settingsEditor.commit();

////        EmailsharedPreferences = context.getSharedPreferences("Email.db", Context.MODE_PRIVATE);
//        SharedPreferences.Editor emailEditor = EmailsharedPreferences.edit();
////        emailEditor.remove(EMAIL_ARRAY_DB);
////        emailEditor.remove(EMAIL_INTENT_DB);
//        emailEditor.clear();
//        emailEditor.commit();
//
////        TasksharedPreferences = context.getSharedPreferences("Task.db", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = TasksharedPreferences.edit();
////        editor.remove(TASK_ARRAY_DB);
////        editor.remove(TASK_INTENT_DB);
//        editor.clear();
//        editor.commit();
//
////        sharedPreferences = context.getSharedPreferences("Notes.db", Context.MODE_PRIVATE);
//        SharedPreferences.Editor notesEditor = sharedPreferences.edit();
////        notesEditor.remove(NOTES_ARRAY_DB);
////        notesEditor.remove(NOTES_INTENT_DB);
//        notesEditor.clear();
//        notesEditor.commit();
        ResetSettings(context);
        UtilsArraylist.ClearNoteArraylist(context);
        UtilsArray_Task.ClearTaskArraylist(context);
        UtilsArray_Email.ClearEmailArrayList(context);
        UtilsArray_Trash.ClearTrash(context);
    }

//    public static void LoginLoadDataFromServer(final Context context){
//
//        final ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
//            if (user.getUid() != null) {
//                Query thisUser = mDatabase.child(user.getUid());
//                if (thisUser != null) {
//                    thisUser.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            AllData = (List<String>) snapshot.getValue();
//                            if (AllData != null && !AllData.isEmpty()) {
//                                UtilsArraylist.notesPendingIntents.clear();
//                                UtilsArray_Task.taskPendingIntent.clear();
//                                UtilsArray_Email.emailPenInt.clear();
//                                UtilsArray_Task.UpdateTask(GsonConverter.jsonToTask(AllData.get(0)), context);
//                                UtilsArraylist.UpdateNote(GsonConverter.jsonToArray(AllData.get(1)), context);
//                                UtilsArray_Email.UpdateMail(GsonConverter.jsonToEmail(AllData.get(2)), context);
//
//                                UtilsArray_All.ReloadCategoryItems();
//                                alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), PendingIntent.getBroadcast(context, 0, new Intent(context, BootReciever.class).putExtra("", true), PendingIntent.FLAG_UPDATE_CURRENT));
//                                }
//
//
//                                Toast.makeText(context, "Data Restoring ", Toast.LENGTH_LONG).show();
//                            } else
//                                Toast.makeText(context, "User data not found in server", Toast.LENGTH_LONG).show();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Toast.makeText(context, "Some Error Occurred", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    Toast.makeText(context, "No user data exists for current user", Toast.LENGTH_SHORT).show();
//                }
//            } else
//                Toast.makeText(context, "User was not recognised. If you want to restore data go to settings ", Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(context, "Please connect to internet to get data from server or do it manually later from settings", Toast.LENGTH_LONG).show();
//        }
//    }


//    public Integer SelectSpinnerItem() {
//        if (QuickNoteTextColor != null) {
//            if (QuickNoteTextColor == R.color.black) {
//                return 0;
//            } else if (QuickNoteTextColor == R.color.color_light_white) {
//                return 1;
//            } else return 2;
//        }
//        return null;
//    }

//    public static String saveToInternalStorage(Bitmap bitmapImage, Context context) {
//        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
//        // path to /data/data/yourapp/app_data/imageDir
//        File directory = cw.getDir("Schedule Manager", Context.MODE_PRIVATE);
//        // Create imageDir
//        File mypath=new File(directory,"profile.jpg");
//
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(mypath);
//            // Use the compress method on the BitMap object to write image to the OutputStream
//            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return directory.getAbsolutePath();
//    }

    public static File loadImageFromStorage(Context context) {

        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("Schedule Manager", Context.MODE_PRIVATE);
        return new File(directory, "profile.jpg");
    }

    public static Boolean deleteImageFromStorage(Context context) {

        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
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
        } else
            finish();
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
//            Toast.makeText(context, "Saving profile image", Toast.LENGTH_SHORT).show();
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
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("Schedule Manager", Context.MODE_PRIVATE);
            // Create imageDir
            File mypath = new File(directory, "profile.jpg");
            FileOutputStream fos = null;
            int ratio = 100;
            if (bitmapImage.getAllocationByteCount() < 1000) {
                ratio = 65;
            } else if (bitmapImage.getAllocationByteCount() <= 5000) {
                ratio = 10;
            } else if (bitmapImage.getAllocationByteCount() <= 7000) {
                ratio = 2;
            } else ratio = 1;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
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


//    public static void radioSelector(Integer num,Context context) {
//        if (num == 1) {
//            r1.setChecked(true);
//            r2.setChecked(false);
//            r3.setChecked(false);
//            r4.setChecked(false);
//
//            ThemeApplier(1);
//            ThemeTextUpdater(1);
//
//        } else if (num == 2) {
//            r1.setChecked(false);
//            r2.setChecked(true);
//            r3.setChecked(false);
//            r4.setChecked(false);
//
//            ThemeApplier(2);
//            ThemeTextUpdater(2);
//
//        } else if (num == -1) {
//            r1.setChecked(false);
//            r2.setChecked(false);
//            r3.setChecked(true);
//            r4.setChecked(false);
//
//            ThemeApplier(-1);
//            ThemeTextUpdater(-1);
//
//        } else if (num == 3) {
//            r1.setChecked(false);
//            r2.setChecked(false);
//            r3.setChecked(false);
//            r4.setChecked(true);
//
//            ThemeApplier(3);
//            ThemeTextUpdater(3);
//
//        }
//        UtilsArraylist.initNote(context);
//        Notes_rec_Adapter adapter = new Notes_rec_Adapter(context,UtilsArraylist.note);
//        adapter.notifyDataSetChanged();
//    }

//    public static void ThemeApplier(Integer num) {
//        if (num == 1) {
//
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//
//        } else if (num == 2) {
//
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//
//        } else if (num == -1) {
//
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
//
//        } else if (num == 3) {
//
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
//            dark_mode_indicator.setText("As per battery");
//
//        }
//    }
//
//    public static void ThemeTextUpdater(Integer num){
//        if (num == 1) {
//            dark_mode_indicator.setText("Light Mode");
//        } else if (num == 2) {
//            dark_mode_indicator.setText("Night Mode");
//        } else if (num == -1) {
//            dark_mode_indicator.setText("Follow System");
//        } else if (num == 3) {
//            dark_mode_indicator.setText("As per battery");
//        }
//
//
//        }

    public static void DarkModeApplier(Boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(Settings_Main.this)){
            isAssitiveNotificationOn = true;
            Settings_Storage.isAssitiveNotificationOn = isAssitiveNotificationOn;
            SaveSettings(Settings_Main.this);
            createNotification(Settings_Main.this);
            makeSnackBar(main_layout, "You will find notification bar in few seconds");
//            Toast.makeText(Settings_Main.this, "You will find notification bar in few seconds", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Draw over other apps permission not granted", Toast.LENGTH_SHORT).show();
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
                .setAction("Login", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Settings_Main.this, MainActivity.class));
                        finish();
                    }
                }).setActionTextColor(Settings_Main.this.getResources().getColor(R.color.color_yellow))
                .show();
    }

    public void makeSnackBar(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}


