package com.startup.go4lunch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.startup.go4lunch.R;
import com.startup.go4lunch.di.ViewModelFactory;
import com.startup.go4lunch.model.RestaurantListItem;
import com.startup.go4lunch.model.Restaurant;

public class RestaurantListFragment extends Fragment implements RestaurantListAdapter.RestaurantListAdapterInterface {

    private RestaurantListAdapter restaurantListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_restaurant, container, false);
        RestaurantListFragmentViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantListFragmentViewModel.class);

        viewModel.getRestaurantListLiveData().observe(getViewLifecycleOwner(), restaurantList -> restaurantListAdapter.submitList(viewModel.getListItemRestaurant()));
        viewModel.getRestaurantListSearchString().observe(getViewLifecycleOwner(), string -> restaurantListAdapter.submitList(viewModel.getListItemRestaurant()));

        restaurantListAdapter = new RestaurantListAdapter(DIFF_CALLBACK, this);
        restaurantListAdapter.submitList(viewModel.getListItemRestaurant());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_list_restaurant);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(restaurantListAdapter);

        return view;
    }

    public static final DiffUtil.ItemCallback<RestaurantListItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<RestaurantListItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull RestaurantListItem oldItem, @NonNull RestaurantListItem newItem) {
            return oldItem.getRestaurant().getId() == newItem.getRestaurant().getId();
        }
        @Override
        public boolean areContentsTheSame(@NonNull RestaurantListItem oldItem, @NonNull RestaurantListItem newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public void clickOnRestaurant(@NonNull Restaurant restaurant) {
        Intent intent = new Intent(requireContext(), RestaurantDetailActivity.class);
        intent.putExtra("restaurantId",restaurant.getId());
        ActivityCompat.startActivity(requireActivity(), intent, null);
    }
}