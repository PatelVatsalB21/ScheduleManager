package com.example.schedulemanager.email;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedulemanager.MainFragments.HomePage;
import com.example.schedulemanager.R;
import com.example.schedulemanager.Setting.AppPasswordActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AppPasswordSetupActivity extends AppCompatActivity {

    RelativeLayout normal, no_connection;
    Button back, next, retry;
    ImageButton exit, one, two, three, four, five;
    TextView more_info, guide_text;
    WebView webView;
    Integer currentStep = 1;
    LinearLayout.LayoutParams params;
    ConnectivityManager connectivityManager;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_password_setup_activity);
        back = findViewById(R.id.app_password_setup_back_button);
        next = findViewById(R.id.app_password_setup_forward_button);
        exit = findViewById(R.id.app_password_setup_exit_button);
        more_info = findViewById(R.id.app_password_setup_more_info);
        webView = findViewById(R.id.app_password_setup_WebView);
        one = findViewById(R.id.app_password_setup_Step_1);
        two = findViewById(R.id.app_password_setup_Step_2);
        three = findViewById(R.id.app_password_setup_Step_3);
        four = findViewById(R.id.app_password_setup_Step_4);
        five = findViewById(R.id.app_password_setup_Step_5);
        guide_text = findViewById(R.id.app_password_setup_guide_hint);
        no_connection = findViewById(R.id.app_password_setup_no_internet_rel_layout);
        retry = findViewById(R.id.app_password_setup_retry_btn);
        normal = findViewById(R.id.app_password_setup_normal_rel_layout);

        mAuth = FirebaseAuth.getInstance();

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        stepSelector(1);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.contains("https://myaccount.google.com/apppasswords")) {
                    view.loadUrl("javascript:(function() { " +
                            "var x = document.getElementsByClassName(jgvuAb eZEHZc');" +
                            "x[0].click();" +
                            "var y = document.getElementsByClassName('MocG8c YECFcc LMgvRb');" +
                            "y[4].click();" +
                            "var z = document.getElementsByClassName('whsOnd zHQkBf');" +
                            "z[0].click();" +
                            "var a = document.getElementsByClassName('whsOnd zHQkBf');"
                            + " a.setAttribute('value', 'Schedule Manager');");
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

        });

        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void performClick(String str) {
                Toast.makeText(AppPasswordSetupActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        }, "jgvuAb eZEHZc");
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20, 0, 20, 0);
        back.setOnClickListener(view -> {
            if (currentStep != 1) {
                stepSelector(currentStep - 1);
            }
        });
        next.setOnClickListener(view -> {
            if (currentStep != 5) {
                stepSelector(currentStep + 1);
            } else {
                startActivity(new Intent(AppPasswordSetupActivity.this,
                        AppPasswordActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
            }
        });
        exit.setOnClickListener(view -> showExitDialog());

        more_info.setOnClickListener(view -> showInfoDialog());

        retry.setOnClickListener(view -> stepSelector(currentStep));
    }

    public void stepSelector(int num) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (connectivityManager.getActiveNetworkInfo() != null
                    && connectivityManager.getActiveNetworkInfo().isConnected()) {
                no_connection.setVisibility(View.GONE);
                normal.setVisibility(View.VISIBLE);
                if (num == 1) {
                    currentStep = 1;
                    back.setEnabled(false);
                    webView.loadUrl(
                            "https://myaccount.google.com/signinoptions/two-step-verification");
                    one.setImageDrawable(getResources().getDrawable(R.drawable.ic_one));
                    two.setImageDrawable(getResources().getDrawable(R.drawable.ic_two));
                    three.setImageDrawable(getResources().getDrawable(R.drawable.ic_three));
                    four.setImageDrawable(getResources().getDrawable(R.drawable.ic_four));
                    five.setImageDrawable(getResources().getDrawable(R.drawable.ic_five));
                    guide_text.setText("Sign in with " + mAuth.getCurrentUser().getEmail()
                            + " and turn on 2 step verification");
                    next.setText("Next");
                } else if (num == 2) {
                    currentStep = 2;
                    back.setEnabled(true);
                    webView.loadUrl("https://myaccount.google.com/apppasswords");
                    one.setImageDrawable(getResources().getDrawable(R.drawable.ic_correct_mark));
                    two.setImageDrawable(getResources().getDrawable(R.drawable.ic_two));
                    three.setImageDrawable(getResources().getDrawable(R.drawable.ic_three));
                    four.setImageDrawable(getResources().getDrawable(R.drawable.ic_four));
                    five.setImageDrawable(getResources().getDrawable(R.drawable.ic_five));
                    guide_text.setText("Sign in with " + mAuth.getCurrentUser().getEmail()
                            + " and click on select app and select OTHERS option");
                    next.setText("Next");
                } else if (num == 3) {
                    currentStep = 3;
                    back.setEnabled(true);
                    one.setImageDrawable(getResources().getDrawable(R.drawable.ic_correct_mark));
                    two.setImageDrawable(getResources().getDrawable(R.drawable.ic_correct_mark));
                    three.setImageDrawable(getResources().getDrawable(R.drawable.ic_three));
                    four.setImageDrawable(getResources().getDrawable(R.drawable.ic_four));
                    five.setImageDrawable(getResources().getDrawable(R.drawable.ic_five));
                    guide_text.setText("Write Schedule Manager");
                    next.setText("Next");
                } else if (num == 4) {
                    currentStep = 4;
                    back.setEnabled(true);
                    one.setImageDrawable(getResources().getDrawable(R.drawable.ic_correct_mark));
                    two.setImageDrawable(getResources().getDrawable(R.drawable.ic_correct_mark));
                    three.setImageDrawable(getResources().getDrawable(R.drawable.ic_correct_mark));
                    four.setImageDrawable(getResources().getDrawable(R.drawable.ic_four));
                    five.setImageDrawable(getResources().getDrawable(R.drawable.ic_five));
                    guide_text.setText("Click on Generate button");
                    next.setText("Next");
                } else if (num == 5) {
                    currentStep = 5;
                    back.setEnabled(true);
                    one.setImageDrawable(getResources().getDrawable(R.drawable.ic_correct_mark));
                    two.setImageDrawable(getResources().getDrawable(R.drawable.ic_correct_mark));
                    three.setImageDrawable(getResources().getDrawable(R.drawable.ic_correct_mark));
                    four.setImageDrawable(getResources().getDrawable(R.drawable.ic_correct_mark));
                    five.setImageDrawable(getResources().getDrawable(R.drawable.ic_five));
                    guide_text.setText("Copy 16 digit App Password and click on Finish button");
                    next.setText("Finish and Paste");
                }
            } else {
                no_connection.setVisibility(View.VISIBLE);
                normal.setVisibility(View.GONE);
            }
        }
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AppPasswordSetupActivity.this)
                .setTitle("Do you want to exit?")
                .setMessage("Without App Password you cannot use Schedule Email feature")
                .setCancelable(false)
                .setNegativeButton("Back to Setup", null)
                .setPositiveButton("Exit", (dialogInterface, i) -> {
                    startActivity(
                            new Intent(AppPasswordSetupActivity.this, HomePage.class).setFlags(
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    finish();
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button nButton = dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
        nButton.setLayoutParams(params);
        nButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        Button yButton = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        yButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        yButton.setLayoutParams(params);
    }

    private void showInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AppPasswordSetupActivity.this)
                .setTitle("Google App Password")
                .setMessage(
                        "This app password is required as a permission to add mails in respective"
                                + " Gmail account. This won't create any privacy issue. This "
                                + "password is not shared.")
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Dismiss",
                (dialogInterface, i) -> dialog.dismiss());
        dialog.show();
        dialog.getWindow().setTitleColor(getResources().getColor(R.color.colorPrimaryDark));
        Button yButton = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        yButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            showExitDialog();
        }
    }
}