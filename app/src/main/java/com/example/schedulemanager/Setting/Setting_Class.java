package com.example.schedulemanager.Setting;

public class Setting_Class {

    public String UserName;
    public Boolean isDarkMode;
    public Boolean isAssitiveNotificationOn;
    public Integer color;
    public String App_Password;
    public Integer SnoozeTime;

    public Setting_Class() {
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
}
