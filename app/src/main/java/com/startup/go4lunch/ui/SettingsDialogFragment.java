package com.startup.go4lunch.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.startup.go4lunch.R;

public class SettingsDialogFragment extends DialogFragment {

    private boolean notificationSet;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_fragment_settings, null);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("go4lunch-preferences", Context.MODE_PRIVATE);
        notificationSet = sharedPreferences.getBoolean("notification-set",true);

        CheckBox notificationCheckbox = view.findViewById(R.id.dialog_fragment_settings_notification_checkbox);
        notificationCheckbox.setChecked(notificationSet);
        notificationCheckbox.setOnClickListener(v -> notificationSet = !notificationSet);

        view.findViewById(R.id.dialog_fragment_settings_ok_button).setOnClickListener(v -> {
            if (notificationSet) {
                sharedPreferences.edit().putBoolean("notification-set",true).apply();
            } else {
                sharedPreferences.edit().putBoolean("notification-set",false).apply();
            }
            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }
}