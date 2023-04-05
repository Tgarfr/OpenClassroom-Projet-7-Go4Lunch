package com.startup.go4lunch.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.startup.go4lunch.R;
import com.startup.go4lunch.model.Restaurant;

public class RestaurantListAdapter extends ListAdapter<Restaurant, RestaurantListAdapter.ViewHolder> {

    RestaurantListAdapterInterface restaurantListAdapterInterface;

    interface RestaurantListAdapterInterface {
        void clickOnRestaurant(Restaurant restaurant);
    }

    protected RestaurantListAdapter(@NonNull DiffUtil.ItemCallback<Restaurant> diffCallback, RestaurantListAdapterInterface restaurantListAdapterInterface) {
        super(diffCallback);
        this.restaurantListAdapterInterface = restaurantListAdapterInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = getCurrentList().get(position);
        holder.restaurantName.setText(restaurant.getName());
        holder.restaurantAdress.setText(restaurant.getAddress());
        holder.restaurantOpeningTime.setText(restaurant.getOpeningTime());
        holder.restaurantItem.setOnClickListener( view -> restaurantListAdapterInterface.clickOnRestaurant(restaurant));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView restaurantName;
        public TextView restaurantAdress;
        public TextView restaurantOpeningTime;
        public ConstraintLayout restaurantItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            restaurantName = itemView.findViewById(R.id.restaurant_name);
            restaurantAdress = itemView.findViewById(R.id.restaurant_address);
            restaurantOpeningTime = itemView.findViewById(R.id.restaurant_opening_time);
            restaurantItem = (ConstraintLayout) itemView;
        }
    }
}