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

    public void setColor_Name(String color_Name) {
        Color_Name = color_Name;
    }

    public Integer getColor_ID() {
        return Color_ID;
    }

    public void setColor_ID(Integer color_ID) {
        Color_ID = color_ID;
    }
}
