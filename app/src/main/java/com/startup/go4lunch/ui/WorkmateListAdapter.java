package com.startup.go4lunch.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.startup.go4lunch.R;
import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.model.WorkmateListItem;

public class WorkmateListAdapter extends ListAdapter<WorkmateListItem, WorkmateListAdapter.ViewHolder> {

    Context context;
    Resources resources;

    protected WorkmateListAdapter(@NonNull DiffUtil.ItemCallback<WorkmateListItem> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
        this.resources = context.getResources();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_workmate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorkmateListItem workmateListItem = getCurrentList().get(position);
        Workmate workmate = workmateListItem.getWorkmate();
        Restaurant restaurantChoice = workmateListItem.getRestaurantChoice();

        if (workmate.getAvatarUri() != null) {
            Glide.with(context)
                    .load(workmate.getAvatarUri())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.workmateAvatar);
        } else {
            holder.workmateAvatar.setImageResource(R.drawable.icon_workmate_avatar_50);
        }
        if (restaurantChoice != null) {
           holder.workmateText.setText(String.format(resources.getString(R.string.workmate_list_item_text),workmate.getName(),restaurantChoice.getType(),restaurantChoice.getName()));
        } else {
            holder.workmateText.setText(String.format(resources.getString(R.string.workmate_list_item_text_no_restaurant_choice), workmate.getName()));
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