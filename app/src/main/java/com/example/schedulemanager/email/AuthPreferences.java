package com.example.schedulemanager.email;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthPreferences {
    private static final String KEY_USER = "userid";
    private static final String KEY_TOKEN = "mytoken";

    private final SharedPreferences preferences;

    public AuthPreferences(Context context) {
        preferences = context.getSharedPreferences("authoris", Context.MODE_PRIVATE);
    }

    public void setUser(String user) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USER, user);
        editor.apply();
    }

    public void setToken(String password) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_TOKEN, password);
        editor.apply();
    }

    public String getUser() {
        return preferences.getString(KEY_USER, null);
    }
}
