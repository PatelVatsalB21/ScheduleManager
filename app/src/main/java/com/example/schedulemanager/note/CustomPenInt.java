package com.example.schedulemanager.note;

import android.app.PendingIntent;

public class CustomPenInt {
    public Integer position;
    public PendingIntent pendingIntent;

    public CustomPenInt(Integer position, PendingIntent pendingIntent) {
        this.position = position;
        this.pendingIntent = pendingIntent;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    public void setPendingIntent(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }
}
