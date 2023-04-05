package com.startup.go4lunch.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.startup.go4lunch.R;
import com.startup.go4lunch.di.ViewModelFactory;
import com.startup.go4lunch.model.Restaurant;

public class RestaurantDetailActivity extends AppCompatActivity {

    private Context context;
    private Restaurant restaurant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        context = this;

        RestaurantDetailActivityViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantDetailActivityViewModel.class);
        restaurant = viewModel.getRestaurantFromId(getIntent().getExtras().getLong("restaurantId"));

        setData();
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

        FrameLayout callButton = findViewById(R.id.detail_restaurant_call_button);
        callButton.setOnClickListener(onClickCallIcon);
        FrameLayout websiteButton = findViewById(R.id.detail_restaurant_website_button);
        websiteButton.setOnClickListener(onClickWebsiteIcon);
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
}