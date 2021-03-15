package com.example.schedulemanager;

public class QuickNoteSpinnerItem {

    public String Color_Name;
    public Integer Color_ID;

    public QuickNoteSpinnerItem(String color_Name, Integer color_ID) {
        Color_Name = color_Name;
        Color_ID = color_ID;
    }

    public String getColor_Name() {
        return Color_Name;
    }

    public Integer getColor_ID() {
        return Color_ID;
    }
}
