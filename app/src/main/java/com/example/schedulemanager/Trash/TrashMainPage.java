package com.example.schedulemanager.Trash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.schedulemanager.MainFragments.HomePage;
import com.example.schedulemanager.MainFragments.HomePageViewPager;
import com.example.schedulemanager.R;
import com.google.android.material.tabs.TabLayout;

public class TrashMainPage extends AppCompatActivity {

    public static HomePageViewPager TrashViewPager;
    public static TabLayout tabLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_main_page);

        UtilsArray_Trash.initTrashArrays(TrashMainPage.this);

        TrashViewPager = findViewById(R.id.trash_Main_View_Pager);
        tabLayout = findViewById(R.id.trash_Main_View_Pager_tab_layout);
        setUpViewPager(TrashViewPager);
        TrashViewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(TrashViewPager);

    }

    private void setUpViewPager(ViewPager viewPager) {
        TrashPagerAdapter adapter = new TrashPagerAdapter(getSupportFragmentManager(), 1);
        adapter.addFragment(new Trash_frag_1());
        adapter.addFragment(new Trash_frag_2());
        adapter.addFragment(new Trash_frag_3());
        viewPager.setAdapter(adapter);
    }

    public static void TrashActionOn(){
        TrashViewPager.setPagingEnabled(false);
        tabLayout.setVisibility(View.GONE);
    }

    public static void TrashActionOff(){
        TrashViewPager.setPagingEnabled(true);
        tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TrashMainPage.this, HomePage.class).putExtra("num",0).putExtra("nav", R.id.nav_home));
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}