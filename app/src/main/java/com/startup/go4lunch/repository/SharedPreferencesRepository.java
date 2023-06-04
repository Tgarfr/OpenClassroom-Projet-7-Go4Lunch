package com.startup.go4lunch.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SharedPreferencesRepository {

    private static final String SHAREDPREFERENCES_NAME = "go4lunch-preferences";
    private static final String NOTIFICATION_SETTING_BOOLEAN = "notification-setting";
    private static final boolean NOTIFICATION_SETTING_BOOLEAN_DEFAULT_VALUE = true;

    public void setNotificationSettingBoolean(@NonNull Context context, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(NOTIFICATION_SETTING_BOOLEAN, value).apply();
    }

    public boolean getNotificationSettingBoolean(@NonNull Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(NOTIFICATION_SETTING_BOOLEAN, NOTIFICATION_SETTING_BOOLEAN_DEFAULT_VALUE);
    }

}