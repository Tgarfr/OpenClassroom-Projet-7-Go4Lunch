package com.startup.go4lunch.ui;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.startup.go4lunch.R;
import com.startup.go4lunch.di.ViewModelFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int YOUR_LUNCH_RESOURCE_ID = R.id.activity_main_drawer_your_lunch;
    private static final int SETTING_RESOURCE_ID = R.id.activity_main_drawer_settings;
    private static final int LOGOUT_RESOURCE_ID = R.id.activity_main_drawer_logout;
    private final static int LOCATION_REQUEST_CODE = 1;
    private MainActivityViewModel viewModel;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TabLayout tabLayout;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MainActivityViewModel.class);
        firebaseAuth = FirebaseAuth.getInstance();
        getCurrentLocation();
        configureViewPager();
        configureToolBar();
        configureDrawerLayout();
        configureNavigationView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        verifyFirebaseUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setBackgroundColor(getResources().getColor(R.color.white));
        editText.setTextColor(getResources().getColor(R.color.black));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    viewModel.setMapFragmentSearch(query);
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                switch (tabLayout.getSelectedTabPosition()) {
                   case 1: viewModel.setRestaurantListSearch(newText); break;
                   case 2: viewModel.setWorkmateListSearch(newText); break;
                }
                return true;
            }
        });
        return true;
    }

    private void verifyFirebaseUser() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            goLoginActivity();
        } else {
            updateHeaderLayoutWithUserData();
            viewModel.createWorkmate(firebaseUser);
        }
    }

    private void goLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginActivity();
    }

    private void getCurrentLocation() {
        if (!checkLocationPermission()) {
            return;
        }
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(location -> viewModel.updateLocation(location));
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (requestCode == LOCATION_REQUEST_CODE) {
            getCurrentLocation();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void configureViewPager() {
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.menu);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (searchView != null) {
                    switch (tabLayout.getSelectedTabPosition()) {
                        case 0 :
                        case 1 : searchView.setQueryHint(getString(R.string.search_menu_restaurant)); break;
                        case 2 : searchView.setQueryHint(getString(R.string.search_menu_workmate)); break;
                    }
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager.setAdapter(new ViewPagerAdapter(this));

        TabLayoutMediator.TabConfigurationStrategy tabConfigurationStrategy = (tab, position) -> {
            switch (position) {
                case 0 : tab.setText(R.string.tab_map);
                         tab.setIcon(R.drawable.icon_menu_map_24);
                         break;
                case 1 : tab.setText(R.string.tab_restaurant);
                         tab.setIcon(R.drawable.icon_menu_restaurant_24);
                         break;
                case 2 : tab.setText(R.string.tab_workmate);
                         tab.setIcon(R.drawable.icon_menu_workmate_24);
                         break;
            }
        };

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, tabConfigurationStrategy);
        tabLayoutMediator.attach();
    }

    private void configureToolBar() {
        this.toolbar = findViewById(R.id.activity_action_toolbar);
        setSupportActionBar(toolbar);
    }

    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void configureNavigationView() {
        this.navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void updateHeaderLayoutWithUserData() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
             View header = navigationView.getHeaderView(0);
            if (firebaseUser.getPhotoUrl() != null) {
                ImageView avatar = header.findViewById(R.id.activity_main_nav_view_avatar);
                Glide.with(this)
                        .load(firebaseUser.getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(avatar);
            }
            String name = TextUtils.isEmpty(firebaseUser.getDisplayName()) ? getString(R.string.info_no_username_found) : firebaseUser.getDisplayName();
            TextView nameTextView = header.findViewById(R.id.activity_main_nav_view_name);
            nameTextView.setText(name);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case YOUR_LUNCH_RESOURCE_ID: goToRestaurantDetailActivity();
                break;
            case SETTING_RESOURCE_ID: new SettingsDialogFragment().show(getSupportFragmentManager(), null);
                break;
            case LOGOUT_RESOURCE_ID: logout();
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void goToRestaurantDetailActivity() {
        long workmateRestaurantSelectedUid = viewModel.getWorkmateRestaurantSelectedUid(firebaseUser.getUid());
        if (workmateRestaurantSelectedUid != 0) {
            Intent intent = new Intent(this, RestaurantDetailActivity.class);
            intent.putExtra("restaurantId", workmateRestaurantSelectedUid);
            ActivityCompat.startActivity(this, intent, null);
        }
    }
}
