package com.startup.go4lunch.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.startup.go4lunch.repository.SharedPreferencesRepository;

public class SettingsDialogFragmentViewModel extends ViewModel {

    private final SharedPreferencesRepository sharedPreferencesRepository;

    public SettingsDialogFragmentViewModel(@NonNull SharedPreferencesRepository sharedPreferencesRepository) {
        this.sharedPreferencesRepository = sharedPreferencesRepository;
    }

    public void setNotificationSettingBoolean(@NonNull Context context, boolean value) {
        sharedPreferencesRepository.setNotificationSettingBoolean(context, value);
    }

    public boolean getNotificationSettingBoolean(@NonNull Context context) {
        return sharedPreferencesRepository.getNotificationSettingBoolean(context);
    }

}