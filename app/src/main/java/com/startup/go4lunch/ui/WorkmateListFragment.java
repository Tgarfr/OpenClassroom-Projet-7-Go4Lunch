package com.startup.go4lunch.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.startup.go4lunch.R;
import com.startup.go4lunch.di.ViewModelFactory;
import com.startup.go4lunch.model.WorkmateListItem;

public class WorkmateListFragment extends Fragment {

    private WorkmateListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_workmate, container, false);
        WorkmateListFragmentViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(WorkmateListFragmentViewModel.class);

        viewModel.getWorkmateListLiveData().observe(getViewLifecycleOwner(), workmateList -> adapter.submitList(viewModel.getWorkmateListItemList()));

        viewModel.getWorkmateListSearchStringLiveData().observe(getViewLifecycleOwner(), string -> adapter.submitList(viewModel.getWorkmateListItemList()));

        adapter = new WorkmateListAdapter(DIFF_CALLBACK,requireContext());
        adapter.submitList(viewModel.getWorkmateListItemList());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_list_workmate);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    public static final DiffUtil.ItemCallback<WorkmateListItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<WorkmateListItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull WorkmateListItem oldItem, @NonNull WorkmateListItem newItem) {
            return oldItem.getWorkmate().getUid().equals(newItem.getWorkmate().getUid());
        }

        @Override
        public boolean areContentsTheSame(@NonNull WorkmateListItem oldItem, @NonNull WorkmateListItem newItem) {
            return oldItem.equals(newItem);
        }
    };
}