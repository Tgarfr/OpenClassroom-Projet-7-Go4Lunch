package com.startup.go4lunch.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.startup.go4lunch.R;
import com.startup.go4lunch.di.ViewModelFactory;
import com.startup.go4lunch.model.Workmate;

public class WorkmateFragment extends Fragment {

    private FirestoreRecyclerAdapter<Workmate,WorkmateListAdapter.ViewHolder> firestoreRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_workmate, container, false);

        WorkmateFragmentViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(WorkmateFragmentViewModel.class);

        FirestoreRecyclerOptions<Workmate> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Workmate>()
                .setQuery(viewModel.getWorkmateListQuery(), Workmate.class)
                .build();

        firestoreRecyclerAdapter = new WorkmateListAdapter(firestoreRecyclerOptions, viewModel.getRestaurantRepository() ,getContext());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_list_workmate);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(firestoreRecyclerAdapter);

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onStart() {
        super.onStart();
        firestoreRecyclerAdapter.startListening();
        firestoreRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        firestoreRecyclerAdapter.stopListening();
    }
}