package com.example.schedulemanager.Intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.schedulemanager.R;

public class IntroFrag_3 extends Fragment {

//    Context context;
//    RadioButton r1,r2,r3,r4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_frag_3, container, false);
//
//        if (context == null){
//            context = container.getContext();
//        }
//
//        r1 = view.findViewById(R.id.intro_frag_3_theme_layout_1_radio_1);
//        r2 = view.findViewById(R.id.intro_frag_3_theme_layout_2_radio_1);
//        r3 = view.findViewById(R.id.intro_frag_3_theme_layout_3_radio_1);
//        r4 = view.findViewById(R.id.intro_frag_3_theme_layout_4_radio_1);
//
//        if (DarkModeInt !=null)  radioSelector(DarkModeInt);
//        else radioSelector(1);
//
//        r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b)radioSelector(1);
//                DarkModeInt = 1;
//                SaveSettings(context);
//            }
//        });
//
//
//        r2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b)radioSelector(2);
//                DarkModeInt = 2;
//                SaveSettings(context);
//            }
//        });
//
//
//        r3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b)radioSelector(-1);
//                DarkModeInt = -1;
//                SaveSettings(context);
//            }
//        });
//
//
//        r4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b)radioSelector(3);
//                DarkModeInt = 3;
//                SaveSettings(context);
//            }
//        });

        return view;
    }
//
//    public void radioSelector(Integer num){
//        if (num ==1){
//            r1.setChecked(true);
//            r2.setChecked(false);
//            r3.setChecked(false);
//            r4.setChecked(false);
//
//            ThemeApplier(1);
//
//        }else if (num ==2){
//            r1.setChecked(false);
//            r2.setChecked(true);
//            r3.setChecked(false);
//            r4.setChecked(false);
//
//            ThemeApplier(2);
//
//        }else if (num == -1){
//            r1.setChecked(false);
//            r2.setChecked(false);
//            r3.setChecked(true);
//            r4.setChecked(false);
//
//            ThemeApplier(-1);
//
//        }else if (num == 3){
//            r1.setChecked(false);
//            r2.setChecked(false);
//            r3.setChecked(false);
//            r4.setChecked(true);
//
//            ThemeApplier(3);
//
//        }
//    }


}
