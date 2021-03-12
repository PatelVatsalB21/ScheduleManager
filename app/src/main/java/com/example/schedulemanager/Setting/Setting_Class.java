package com.example.schedulemanager.Setting;

public class Setting_Class {

    public String UserName;
    public Boolean isDarkMode;
    public Boolean isAssitiveNotificationOn;
    public Integer color;
    public String App_Password;
    public Integer SnoozeTime;
//    public Uri profileImage ;

    public Setting_Class() {
    }


//    public Uri getProfileImage() {
//        return profileImage;
//    }
//
//    public void setProfileImage(Uri profileImage) {
//        this.profileImage = profileImage;
//    }

    public Integer getSnoozeTime() {
        return SnoozeTime;
    }

    public void setSnoozeTime(Integer snoozeTime) {
        SnoozeTime = snoozeTime;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public String getApp_Password() {
        return App_Password;
    }

    public void setApp_Password(String app_Password) {
        App_Password = app_Password;
    }

    public Boolean getDarkMode() {
        return isDarkMode;
    }

    public void setDarkMode(Boolean darkMode) {
        isDarkMode = darkMode;
    }

    public Boolean getAssitiveNotificationOn() {
        return isAssitiveNotificationOn;
    }

    public void setAssitiveNotificationOn(Boolean assitiveNotificationOn) {
        isAssitiveNotificationOn = assitiveNotificationOn;
    }
}
