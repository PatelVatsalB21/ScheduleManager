package com.example.schedulemanager.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.schedulemanager.MainFragments.HomePage;
import com.example.schedulemanager.R;
import com.example.schedulemanager.email.AppPasswordSetupActivity;

public class AppPasswordActivity extends AppCompatActivity {


    Button save_App_Password_btn, get_app_password_btn;
    EditText app_password_Edit_txt;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_password);

        save_App_Password_btn = findViewById(R.id.Setting_App_Password_layout_Save_btn);
        app_password_Edit_txt = findViewById(R.id.Setting_App_Password_Password_Edit_txt);
        get_app_password_btn = findViewById(R.id.Setting_App_Password_get_App_Password_btn);
        back = findViewById(R.id.App_Password_layout_Back_btn);

        if (Settings_Main.App_Password == null || Settings_Main.App_Password.isEmpty()) {
            app_password_Edit_txt.setHint("Click here to add App Password");
            save_App_Password_btn.setText("Save");
        } else {
            app_password_Edit_txt.setText(Settings_Main.App_Password);
            save_App_Password_btn.setText("Update");
        }

        get_app_password_btn.setOnClickListener(view -> {
            startActivity(new Intent(AppPasswordActivity.this, AppPasswordSetupActivity.class));
            finish();
        });

        save_App_Password_btn.setOnClickListener(view -> {
            if (!app_password_Edit_txt.getText().toString().isEmpty() && app_password_Edit_txt.getText().toString().trim().length() == 16) {
                Settings_Main.App_Password = app_password_Edit_txt.getText().toString();
                Settings_Main.SaveSettings(AppPasswordActivity.this);
                Settings_Main.LoadChangedSettings(AppPasswordActivity.this);
                app_password_Edit_txt.setText(Settings_Main.App_Password);
                startActivity(new Intent(AppPasswordActivity.this, HomePage.class));
                onBackPressed();
                Toast.makeText(AppPasswordActivity.this, "App Password Saved Successfully", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(AppPasswordActivity.this, "Please Enter 16 digit Valid Password", Toast.LENGTH_SHORT).show();

        });
        back.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}