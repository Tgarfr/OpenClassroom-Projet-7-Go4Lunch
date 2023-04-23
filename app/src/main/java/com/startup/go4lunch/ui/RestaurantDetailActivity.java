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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.startup.go4lunch.R;
import com.startup.go4lunch.di.ViewModelFactory;
import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.Workmate;

public class RestaurantDetailActivity extends AppCompatActivity {

    private RestaurantDetailActivityViewModel viewModel;
    private Context context;
    private Restaurant restaurant;
    private Workmate userWorkmate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        context = this;

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantDetailActivityViewModel.class);
        restaurant = viewModel.getRestaurantFromId(getIntent().getExtras().getLong("restaurantId"));
        viewModel.getRestaurantWorkmateVoteLiveData().observe(this, restaurantWorkmateVotes -> updateLikeButton());

        getUserWorkmate();
        setData();
        updateLikeButton();
    }

    private void setData() {
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
            if (viewModel.getRestaurantWorkmateVote(userWorkmate.getUid(), restaurant.getId())) {
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

    private boolean checkCallPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            return false;
        }
        return true;
    }

    @NonNull
    private String setCapitalFistChar(@NonNull String string) {
        char[] char_table = string.toCharArray();
        char_table[0] = Character.toUpperCase(char_table[0]);
        return new String(char_table);
    }

    private void getUserWorkmate() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userWorkmate = viewModel.getWorkmateFromUid(firebaseUser.getUid());
        }
    }

    private void updateLikeButton() {
        if (viewModel.getRestaurantWorkmateVote(userWorkmate.getUid(), restaurant.getId())) {
            ImageView likeIcon = findViewById(R.id.detail_restaurant_like_icon);
            likeIcon.setColorFilter(getResources().getColor(R.color.detail_restaurant_activity_item_disable));
            TextView likeText = findViewById(R.id.detail_restaurant_like_text);
            likeText.setTextColor(getResources().getColor(R.color.detail_restaurant_activity_item_disable));
        } else {
            ImageView likeIcon = findViewById(R.id.detail_restaurant_like_icon);
            likeIcon.setColorFilter(getResources().getColor(R.color.primary_color));
            TextView likeText = findViewById(R.id.detail_restaurant_like_text);
            likeText.setTextColor(getResources().getColor(R.color.primary_color));
        }
    }
}