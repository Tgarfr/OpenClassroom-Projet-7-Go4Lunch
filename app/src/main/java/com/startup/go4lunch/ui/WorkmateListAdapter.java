package com.startup.go4lunch.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.startup.go4lunch.R;
import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.repository.RestaurantRepository;

public class WorkmateListAdapter extends FirestoreRecyclerAdapter<Workmate,WorkmateListAdapter.ViewHolder> {

    RestaurantRepository restaurantRepository;
    Context context;

    public WorkmateListAdapter(@NonNull FirestoreRecyclerOptions<Workmate> options, RestaurantRepository restaurantRepository, Context context) {
        super(options);
        this.restaurantRepository = restaurantRepository;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_workmate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Workmate workmate) {
        if (workmate.getAvatarUri() != null) {
            Glide.with(context)
                    .load(workmate.getAvatarUri())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.workmateAvatar);
        } else {
            holder.workmateAvatar.setImageResource(R.drawable.icon_workmate_avatar_50);
        }
        if (workmate.getRestaurantUid() != 0) {
            Restaurant restaurant = restaurantRepository.getRestaurantFromId(workmate.getRestaurantUid());
            if (restaurant != null) {
                holder.workmateText.setText(workmate.getName()+" is eating "+restaurant.getType()+" ( "+restaurant.getName()+" ) ");
            }
        } else {
            holder.workmateText.setText(workmate.getName()+" hasn't decided yet");
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView workmateText;
        public ImageView workmateAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            workmateText = itemView.findViewById(R.id.item_list_workmate_text);
            workmateAvatar = itemView.findViewById(R.id.item_list_workmate_avatar);
        }
    }

}