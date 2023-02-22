package com.startup.go4lunch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.startup.go4lunch.ui.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureViewPager();
    }

    private void configureViewPager() {
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.menu);

        viewPager.setAdapter(new ViewPagerAdapter(this));

        TabLayoutMediator.TabConfigurationStrategy tabConfigurationStrategy = new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
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
            }
        };

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, tabConfigurationStrategy);
        tabLayoutMediator.attach();
    }
}
