package com.startup.go4lunch.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.startup.go4lunch.repository.SettingsRepository;

public class SettingsDialogFragmentViewModel extends ViewModel {

    private final SettingsRepository settingsRepository;

    public SettingsDialogFragmentViewModel(@NonNull SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public void setAreNotificationsEnable(@NonNull Context context, boolean value) {
        settingsRepository.setAreNotificationsEnable(context, value);
    }

    public boolean getAreNotificationsEnable(@NonNull Context context) {
        return settingsRepository.getAreNotificationsEnable(context);
    }

}