package com.startup.go4lunch.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.startup.go4lunch.R;
import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.RestaurantListItem;

public class RestaurantListAdapter extends ListAdapter<RestaurantListItem, RestaurantListAdapter.ViewHolder> {

    RestaurantListAdapterInterface restaurantListAdapterInterface;

    interface RestaurantListAdapterInterface {
        void clickOnRestaurant(@NonNull Restaurant restaurant);
    }

    protected RestaurantListAdapter(@NonNull DiffUtil.ItemCallback<RestaurantListItem> diffCallback,@NonNull RestaurantListAdapterInterface restaurantListAdapterInterface) {
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
        RestaurantListItem restaurantListItem = getCurrentList().get(position);
        Restaurant restaurant = restaurantListItem.getRestaurant();

        holder.restaurantName.setText(restaurant.getName());
        holder.restaurantAdress.setText(String.format("%s - %s",restaurant.getType(),restaurant.getAddress()));
        holder.restaurantOpeningTime.setText(restaurant.getOpeningTime());
        holder.restaurantDistance.setText(String.format("%sm",restaurantListItem.getDistance()));
        holder.numberOfWorkmate.setText(String.format("(%s)",restaurantListItem.getNumberOfWorkmate()));

        switch ((int) restaurantListItem.getScore()) {
            case 0 : updateStars(holder, false, false, false); break;
            case 1 : updateStars(holder, true, false, false); break;
            case 2 : updateStars(holder, true, true, false); break;
            default : updateStars(holder, true, true, true); break;
        }

        holder.restaurantItem.setOnClickListener( view -> restaurantListAdapterInterface.clickOnRestaurant(restaurant));
    }

    private void updateStars(@NonNull ViewHolder holder, boolean star1, boolean star2, boolean star3) {
        if (star1) {
            holder.star1.setVisibility(View.VISIBLE);
            holder.centerStar1.setVisibility(View.VISIBLE);
        } else {
            holder.star1.setVisibility(View.INVISIBLE);
            holder.centerStar1.setVisibility(View.INVISIBLE);
        }
        if (star2) {
            holder.star2.setVisibility(View.VISIBLE);
            holder.centerStar2.setVisibility(View.VISIBLE);
        } else {
            holder.star2.setVisibility(View.INVISIBLE);
            holder.centerStar2.setVisibility(View.INVISIBLE);
        }
        if (star3) {
            holder.star3.setVisibility(View.VISIBLE);
            holder.centerStar3.setVisibility(View.VISIBLE);
        } else {
            holder.star3.setVisibility(View.INVISIBLE);
            holder.centerStar3.setVisibility(View.INVISIBLE);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView restaurantName;
        public TextView restaurantAdress;
        public TextView restaurantOpeningTime;
        public TextView restaurantDistance;
        public TextView numberOfWorkmate;
        public ConstraintLayout restaurantItem;
        public ImageView star1;
        public ImageView star2;
        public ImageView star3;
        public ImageView centerStar1;
        public ImageView centerStar2;
        public ImageView centerStar3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            restaurantName = itemView.findViewById(R.id.restaurant_name);
            restaurantAdress = itemView.findViewById(R.id.restaurant_address);
            restaurantOpeningTime = itemView.findViewById(R.id.restaurant_opening_time);
            restaurantDistance = itemView.findViewById(R.id.restaurant_distance);
            numberOfWorkmate = itemView.findViewById(R.id.restaurant_number_of_workmate);
            star1 = itemView.findViewById(R.id.restaurant_star_1);
            star2 = itemView.findViewById(R.id.restaurant_star_2);
            star3 = itemView.findViewById(R.id.restaurant_star_3);
            centerStar1 = itemView.findViewById(R.id.restaurant_star_1_center);
            centerStar2 = itemView.findViewById(R.id.restaurant_star_2_center);
            centerStar3 = itemView.findViewById(R.id.restaurant_star_3_center);
            restaurantItem = (ConstraintLayout) itemView;
        }
    }
}