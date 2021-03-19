package com.example.schedulemanager.Setting;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedulemanager.Utils.BootReciever;
import com.example.schedulemanager.HomeFrag.UtilsArray_All;
import com.example.schedulemanager.LoginAndUser.SplashScreen;
import com.example.schedulemanager.MainFragments.Fragment_2;
import com.example.schedulemanager.R;
import com.example.schedulemanager.Task.UtilsArray_Task;
import com.example.schedulemanager.email.Email_rec_Adapter;
import com.example.schedulemanager.email.UtilsArray_Email;
import com.example.schedulemanager.Utils.GsonConverter;
import com.example.schedulemanager.note.Notes_rec_Adapter;
import com.example.schedulemanager.note.UtilsArraylist;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BackupSettings extends AppCompatActivity {

    ImageButton update_Data, restore_data, back;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;
    private DatabaseReference mDatabase;
    private List<String> AllData = new ArrayList<>();
    LinearLayout.LayoutParams params;
    AlarmManager alarmManager;
    public static ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_settings);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (user.getUid() != null) {
            uid = user.getUid();
        }
        update_Data = findViewById(R.id.Setting_Update_Data_btn);
        restore_data = findViewById(R.id.Setting_Restore_Data_btn);
        back = findViewById(R.id.Backup_Settings_layout_Back_btn);
        pdialog = new ProgressDialog(BackupSettings.this);
        pdialog.setCancelable(false);

        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20, 0, 20, 0);

        update_Data.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(BackupSettings.this)
                    .setTitle("Update Data")
                    .setMessage(
                            "Are you sure you want to update data. This will store all your "
                                    + "created data in server?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {

                        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(
                                Context.CONNECTIVITY_SERVICE);
                        if (conMgr.getActiveNetworkInfo() != null
                                && conMgr.getActiveNetworkInfo().isAvailable()
                                && conMgr.getActiveNetworkInfo().isConnected()) {
                            pdialog.setTitle(" Updating User Data");
                            pdialog.setMessage("Fetching user data in server. Please wait!");
                            pdialog.show();

                            if (!UtilsArray_Task.task.isEmpty() | !UtilsArraylist.note.isEmpty()
                                    | !UtilsArray_Email.mail.isEmpty()) {
                                AllData.add(0, GsonConverter.TaskToJson(UtilsArray_Task.task));
                                AllData.add(1, GsonConverter.ArrayToJson(UtilsArraylist.note));
                                AllData.add(2, GsonConverter.EmailToJson(UtilsArray_Email.mail));

                                mDatabase.child(uid).setValue(AllData).addOnCompleteListener(
                                        task -> Toast.makeText(BackupSettings.this,
                                                "Data Updated Successfully",
                                                Toast.LENGTH_SHORT).show()).addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(BackupSettings.this,
                                                        "Some Error Occurred",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(BackupSettings.this, "Nothing to upload",
                                        Toast.LENGTH_SHORT).show();
                            }
                            if (pdialog.isShowing()) pdialog.dismiss();
                        } else {
                            Toast.makeText(BackupSettings.this, "Please Check Internet Connection",
                                    Toast.LENGTH_SHORT).show();
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

        });
        restore_data.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(BackupSettings.this)
                    .setTitle("Restore Data")
                    .setMessage(
                            "Are you sure you want to restore data. This will remove all changes "
                                    + "made including data created by you?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {

                        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(
                                Context.CONNECTIVITY_SERVICE);
                        if (conMgr.getActiveNetworkInfo() != null
                                && conMgr.getActiveNetworkInfo().isAvailable()
                                && conMgr.getActiveNetworkInfo().isConnected()) {
                            pdialog.setTitle(" Restoring User Data");
                            pdialog.setMessage("Fetching user data from server. Please wait!");
                            pdialog.show();

                            Query thisUser = mDatabase.child(uid);
                            thisUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    AllData = (List<String>) snapshot.getValue();
                                    if (AllData != null && AllData.size() != 0) {
                                        if (!GsonConverter.jsonToTask(AllData.get(0)).isEmpty()
                                                | !GsonConverter.jsonToArray(
                                                AllData.get(1)).isEmpty()
                                                | !GsonConverter.jsonToEmail(
                                                AllData.get(2)).isEmpty()) {
                                            UtilsArray_Task.UpdateTask(
                                                    GsonConverter.jsonToTask(AllData.get(0)),
                                                    BackupSettings.this);
                                            UtilsArraylist.UpdateNote(
                                                    GsonConverter.jsonToArray(AllData.get(1)),
                                                    BackupSettings.this);
                                            UtilsArray_Email.UpdateMail(
                                                    GsonConverter.jsonToEmail(AllData.get(2)),
                                                    BackupSettings.this);

                                            Fragment_2.TaskReceiverRefresh(BackupSettings.this);
                                            UtilsArraylist.initNote(BackupSettings.this);
                                            UtilsArray_Email.initMail(BackupSettings.this);
                                            Notes_rec_Adapter adapter2 = new Notes_rec_Adapter(
                                                    BackupSettings.this, UtilsArraylist.getNote());
                                            adapter2.notifyDataSetChanged();
                                            Email_rec_Adapter adapter3 = new Email_rec_Adapter(
                                                    UtilsArray_Email.getMail());
                                            adapter3.notifyDataSetChanged();
                                            UtilsArray_All.ReloadCategoryItems();

                                            alarmManager = (AlarmManager) getSystemService(
                                                    ALARM_SERVICE);
                                            if (Build.VERSION.SDK_INT
                                                    >= Build.VERSION_CODES.KITKAT) {
                                                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                                                        System.currentTimeMillis(),
                                                        PendingIntent.getBroadcast(
                                                                BackupSettings.this, 0,
                                                                new Intent(BackupSettings.this,
                                                                        BootReciever.class).putExtra(
                                                                        "", true),
                                                                PendingIntent.FLAG_UPDATE_CURRENT));
                                            }
                                            Toast.makeText(BackupSettings.this,
                                                    "Data Restored Successfully.Restarting App to"
                                                            + " apply changes. ",
                                                    Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(BackupSettings.this,
                                                    SplashScreen.class).setFlags(
                                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                            | Intent.FLAG_ACTIVITY_NEW_TASK));
                                            finish();
                                        } else {
                                            Toast.makeText(BackupSettings.this,
                                                    "No data found for your account ",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(BackupSettings.this,
                                                "No data found for your account ",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(BackupSettings.this, "Some Error Occurred",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                            if (pdialog.isShowing()) pdialog.dismiss();
                        } else {
                            Toast.makeText(BackupSettings.this, "Please Check Internet Connection",
                                    Toast.LENGTH_SHORT).show();
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

        });

        back.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}