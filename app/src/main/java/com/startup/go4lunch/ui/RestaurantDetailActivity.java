package com.startup.go4lunch.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.startup.go4lunch.R;
import com.startup.go4lunch.di.ViewModelFactory;
import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.Workmate;
import com.startup.go4lunch.model.WorkmateListItem;

public class RestaurantDetailActivity extends AppCompatActivity {

    private final Context context = this;
    private RestaurantDetailActivityViewModel viewModel;
    private Restaurant restaurant;
    private Workmate userWorkmate;
    private boolean vote;
    private WorkmateListItemListAdapter adapter;
    private ImageView validButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantDetailActivityViewModel.class);
        getRestaurant();
        getUserWorkmate();
    }

    private void getRestaurant() {
        viewModel.getRestaurantLiveData(getIntent().getExtras().getLong("restaurantId")).observe(this, restaurant -> {
            if (restaurant != null) {
                setData(restaurant);
            }
        });
    }

    private void getUserWorkmate() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            viewModel.getUserWorkmateLiveData(firebaseUser.getUid()).observe(this, workmate -> {
                userWorkmate = workmate;
                if (restaurant != null) {
                    configureLikeButton();
                    configureValidButton();
                    configureWorkmateList();
                }
            });
        }
    }

    private void setData(@NonNull Restaurant restaurant) {
        this.restaurant = restaurant;
        TextView nameTextView = findViewById(R.id.activity_restaurant_detail_name);
        nameTextView.setText(restaurant.getName());

        String restaurantType;
        if (restaurant.getType() == null) {
            restaurantType = "Type not provided";
        } else {
            restaurantType = setCapitalFistChar(restaurant.getType());
        }
        TextView textTextView = findViewById(R.id.activity_restaurant_detail_text);
        textTextView.setText(String.format("%s - %s", restaurantType, restaurant.getAddress()));

        if (restaurant.getPhone() == null) {
            ImageView callIcon = findViewById(R.id.detail_restaurant_call_icon);
            callIcon.setColorFilter(getResources().getColor(R.color.detail_restaurant_activity_item_disable));
            TextView callText = findViewById(R.id.detail_restaurant_call_text);
            callText.setTextColor(getResources().getColor(R.color.detail_restaurant_activity_item_disable));
        } else {
            findViewById(R.id.detail_restaurant_call_button).setOnClickListener(onClickCallIcon);
        }
        if (restaurant.getWebsite() == null) {
            ImageView websiteIcon = findViewById(R.id.detail_restaurant_website_icon);
            websiteIcon.setColorFilter(getResources().getColor(R.color.detail_restaurant_activity_item_disable));
            TextView callText = findViewById(R.id.detail_restaurant_website_text);
            callText.setTextColor(getResources().getColor(R.color.detail_restaurant_activity_item_disable));
        } else {
            findViewById(R.id.detail_restaurant_website_button).setOnClickListener(onClickWebsiteIcon);
        }
        findViewById(R.id.detail_restaurant_like_button).setOnClickListener(onClickLikeIcon);

        configureLikeButton();
        configureValidButton();
        configureWorkmateList();
    }

    private final  View.OnClickListener onClickCallIcon = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (restaurant.getPhone() != null) {
                if(checkCallPermission()) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + restaurant.getPhone()));
                    startActivity(intent);
                }
            }
        }
    };

    private final View.OnClickListener onClickLikeIcon = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (vote) {
                viewModel.removeRestaurantWorkmateVote(userWorkmate.getUid(), restaurant.getId());
            } else {
                viewModel.setRestaurantWorkmateVote(userWorkmate.getUid(), restaurant.getId());
            }
            updateLikeButton();
        }
    };

    private final View.OnClickListener onClickWebsiteIcon = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (restaurant.getWebsite() != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getWebsite()));
                startActivity(intent);
            }
        }
    };

    private final View.OnClickListener onClickValidButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (userWorkmate.getRestaurantSelectedUid() != null && userWorkmate.getRestaurantSelectedUid() == restaurant.getId()) {
                validButton.setColorFilter(getResources().getColor(R.color.black));
                viewModel.setRestaurantSelected(userWorkmate.getUid(), 0L);
            } else {
                validButton.setColorFilter(getResources().getColor(R.color.detail_activity_valid_button_green));
                viewModel.setRestaurantSelected(userWorkmate.getUid(), restaurant.getId());
            }
        }
    };

    private void configureWorkmateList() {
        if (restaurant != null && userWorkmate != null) {
            adapter = new WorkmateListItemListAdapter(DIFF_CALLBACK,context);
            viewModel.getWorkmateListItemLiveData(restaurant.getId()).observe(this, workmateListItems -> adapter.submitList(workmateListItems));
            RecyclerView recyclerView = findViewById(R.id.detail_restaurant_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        }
    }

    private void configureValidButton() {
        if (restaurant != null && userWorkmate != null) {
            validButton = findViewById(R.id.activity_restaurant_detail_valid_button);
            if (userWorkmate.getRestaurantSelectedUid() != null &&  userWorkmate.getRestaurantSelectedUid() == restaurant.getId()) {
                validButton.setColorFilter(getResources().getColor(R.color.detail_activity_valid_button_green));
            }
            validButton.setOnClickListener(onClickValidButton);
        }
    }

    private void configureLikeButton() {
        if (restaurant != null && userWorkmate != null) {
            viewModel.getRestaurantWorkmateVoteLiveData(userWorkmate.getUid(), restaurant.getId()).observe(this, restaurantWorkmateVote -> {
                vote = restaurantWorkmateVote;
                updateLikeButton();
            });
        }
    }

    private void updateLikeButton() {
        if (restaurant != null && userWorkmate != null) {
            ImageView likeIcon = findViewById(R.id.detail_restaurant_like_icon);
            if (vote) {
                likeIcon.setColorFilter(getResources().getColor(R.color.detail_restaurant_activity_item_disable));
                TextView likeText = findViewById(R.id.detail_restaurant_like_text);
                likeText.setTextColor(getResources().getColor(R.color.detail_restaurant_activity_item_disable));
            } else {
                likeIcon.setColorFilter(getResources().getColor(R.color.primary_color));
                TextView likeText = findViewById(R.id.detail_restaurant_like_text);
                likeText.setTextColor(getResources().getColor(R.color.primary_color));
            }
        }
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

    private boolean checkCallPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            return false;
        }
        return true;
    }

    @NonNull
    public String setCapitalFistChar(@NonNull String string) {
        char[] char_table = string.toCharArray();
        char_table[0] = Character.toUpperCase(char_table[0]);
        return new String(char_table);
    }
}