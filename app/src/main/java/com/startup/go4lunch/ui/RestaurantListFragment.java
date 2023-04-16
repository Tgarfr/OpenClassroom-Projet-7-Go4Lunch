package com.startup.go4lunch.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.startup.go4lunch.R;
import com.startup.go4lunch.di.ViewModelFactory;
import com.startup.go4lunch.model.Restaurant;

import java.util.List;

public class RestaurantListFragment extends Fragment {

    private RestaurantListAdapter restaurantListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_restaurant, container, false);

        RestaurantListFragmentViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantListFragmentViewModel.class);
        LiveData<List<Restaurant>> restaurantListLiveData = viewModel.getRestaurantListLiveData();
        restaurantListLiveData.observe(getViewLifecycleOwner(), restaurantListObserver);

        LiveData<String> restaurantListSearchString = viewModel.getRestaurantListSearchString();
        restaurantListSearchString.observe(getViewLifecycleOwner(), s -> {
            String string = viewModel.getRestaurantListSearchString().getValue();
            if (string != null) {
                restaurantListAdapter.submitList(viewModel.getRestaurantSearchList(string));
            }
        });

        restaurantListAdapter = new RestaurantListAdapter(DIFF_CALLBACK);
        restaurantListAdapter.submitList(restaurantListLiveData.getValue());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_list_restaurant);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(restaurantListAdapter);

        return view;
    }

    public static final DiffUtil.ItemCallback<Restaurant> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Restaurant>() {
                @Override
                public boolean areItemsTheSame(@NonNull Restaurant oldItem, @NonNull Restaurant newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Restaurant oldItem, @NonNull Restaurant newItem) {
                    if (oldItem.getType() == null) {
                        if (newItem.getType() == null) {
                            if (newItem.getType() == null) {
                                return true;
                            }
                            return false;
                        }
                    }
                    return oldItem.getName().equals(newItem.getName()) &
                            oldItem.getAddress().equals(newItem.getAddress()) &
                            oldItem.getType().equals(newItem.getType()) &
                            oldItem.getOpeningTime().equals(newItem.getOpeningTime()) &
                            oldItem.getLatitude() == newItem.getLatitude() &
                            oldItem.getLongitude() == newItem.getLongitude();
                }
            };

    private final Observer<List<Restaurant>> restaurantListObserver = new Observer<List<Restaurant>>() {
        @Override
        public void onChanged(List<Restaurant> restaurantListNew) {
            restaurantListAdapter.submitList(restaurantListNew);
        }
    };
}