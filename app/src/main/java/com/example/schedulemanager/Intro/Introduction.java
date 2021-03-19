package com.example.schedulemanager.Intro;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.schedulemanager.MainFragments.HomePage;
import com.example.schedulemanager.R;
import com.example.schedulemanager.email.AppPasswordSetupActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Introduction extends AppCompatActivity {

    ViewPager IntroductionPager;
    PagerAdapter adapter;
    Button skip, next;
    FirebaseAuth mAuth;
    ImageView img1, img2, img3, img4;
    private final int APP_PERMISSION_REQUEST = 2102;
    private final int APP_OVERLAY_PERMISSION_REQUEST = 2120;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        skip = findViewById(R.id.introduction_view_pager_skip_btn);
        next = findViewById(R.id.introduction_view_pager_next_btn);
        img1 = findViewById(R.id.tab1_imgView);
        img2 = findViewById(R.id.tab2_imgView);
        img3 = findViewById(R.id.tab3_imgView);
        img4 = findViewById(R.id.tab4_imgView);
        IntroductionPager = findViewById(R.id.introduction_view_pager);
        adapter = new IntroPagerAdapter(getSupportFragmentManager(), 1);
        setUpViewPager(IntroductionPager);
        mAuth = FirebaseAuth.getInstance();
        IntroductionPager.setOffscreenPageLimit(3);
        IntroductionPager.setCurrentItem(0, true);
        img1.setSelected(true);

        IntroductionPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    skip.setText("Skip");
                    next.setText("Next");
                    img1.setSelected(true);
                    img2.setSelected(false);
                    img3.setSelected(false);
                    img4.setSelected(false);
                } else if (position == 1) {
                    skip.setText("Back");
                    next.setText("Next");
                    img1.setSelected(false);
                    img2.setSelected(true);
                    img3.setSelected(false);
                    img4.setSelected(false);
                } else if (position == 2) {
                    skip.setText("Back");
                    next.setText("Next");
                    img1.setSelected(false);
                    img2.setSelected(false);
                    img3.setSelected(true);
                    img4.setSelected(false);
                } else if (position == 3) {
                    skip.setText("Back");
                    next.setText("Finish");
                    img1.setSelected(false);
                    img2.setSelected(false);
                    img3.setSelected(false);
                    img4.setSelected(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        skip.setOnClickListener(view -> {
            if (IntroductionPager.getCurrentItem() == 0) {
                requestOverlayPerm();
            } else {
                IntroductionPager.setCurrentItem(IntroductionPager.getCurrentItem() - 1);
            }
        });

        next.setOnClickListener(view -> {
            if (IntroductionPager.getCurrentItem() != 3) {
                IntroductionPager.setCurrentItem(IntroductionPager.getCurrentItem() + 1);
            } else {
                requestOverlayPerm();
            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        IntroPagerAdapter adapter = new IntroPagerAdapter(getSupportFragmentManager(), 1);
        adapter.addFragment(new IntroFrag_Main());
        adapter.addFragment(new IntroFrag_1());
        adapter.addFragment(new IntroFrag_2());
        adapter.addFragment(new IntroFrag_3());
        viewPager.setAdapter(adapter);
    }

    public void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(Introduction.this,
                    new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(
                Introduction.this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, APP_PERMISSION_REQUEST);
        }
    }

    public void requestOverlayPerm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(
                Introduction.this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, APP_OVERLAY_PERMISSION_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_OVERLAY_PERMISSION_REQUEST) {
            requestPermissions();
        } else if (requestCode == APP_PERMISSION_REQUEST) {
            if (mAuth.getCurrentUser() != null) {
                startActivity(
                        new Intent(Introduction.this, AppPasswordSetupActivity.class).setFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            } else {
                startActivity(new Intent(Introduction.this, HomePage.class).setFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
            finish();
        }
    }
}