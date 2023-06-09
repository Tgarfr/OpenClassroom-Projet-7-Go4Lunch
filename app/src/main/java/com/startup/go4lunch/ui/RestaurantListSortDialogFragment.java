package com.startup.go4lunch.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.startup.go4lunch.R;
import com.startup.go4lunch.model.RestaurantListItem;

public class RestaurantListSortDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_fragment_list_restaurant, null);

        view.findViewById(R.id.restaurant_list_sort_by_name).setOnClickListener(v -> returnSortMethod(RestaurantListItem.SORT_BY_NAME));
        view.findViewById(R.id.restaurant_list_sort_by_distance).setOnClickListener(v -> returnSortMethod(RestaurantListItem.SORT_BY_DISTANCE));
        view.findViewById(R.id.restaurant_list_sort_by_type).setOnClickListener(v -> returnSortMethod(RestaurantListItem.SORT_BY_TYPE));
        view.findViewById(R.id.restaurant_list_sort_by_score).setOnClickListener(v -> returnSortMethod(RestaurantListItem.SORT_BY_SCORE));

        builder.setView(view);
        return builder.create();
    }

    private void returnSortMethod(int sortMethod) {
        Bundle bundle = new Bundle();
        bundle.putInt("SortMethod", sortMethod);
        getParentFragmentManager().setFragmentResult("SortMethod", bundle);
        dismiss();
    }
}