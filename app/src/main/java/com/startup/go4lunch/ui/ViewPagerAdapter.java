package com.startup.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    RestaurantListFragment restaurantListFragment;
    MapFragment mapFragment;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, MapFragment mapFragment, RestaurantListFragment restaurantListFragment) {
        super(fragmentActivity);
        this.mapFragment = mapFragment;
        this.restaurantListFragment = restaurantListFragment;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0 : return mapFragment;
            case 1 : return restaurantListFragment;
            case 2 : return new WorkmateFragment();
        }
        return mapFragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}