package com.startup.go4lunch.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.startup.go4lunch.R;
import com.startup.go4lunch.di.ViewModelFactory;

public class SettingsDialogFragment extends DialogFragment {

    private boolean notificationSet;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_fragment_settings, null);
        SettingsDialogFragmentViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(SettingsDialogFragmentViewModel.class);

        notificationSet = viewModel.getAreNotificationsEnable(requireContext());

        CheckBox notificationCheckbox = view.findViewById(R.id.dialog_fragment_settings_notification_checkbox);
        notificationCheckbox.setChecked(notificationSet);
        notificationCheckbox.setOnClickListener(v -> notificationSet = !notificationSet);

        view.findViewById(R.id.dialog_fragment_settings_ok_button).setOnClickListener(v -> {
            viewModel.setAreNotificationsEnable(requireContext(), notificationSet);
            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }
}