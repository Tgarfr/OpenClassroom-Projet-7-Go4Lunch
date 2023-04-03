package com.startup.go4lunch.ui;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.startup.go4lunch.R;
import com.startup.go4lunch.di.ViewModelFactory;
import com.startup.go4lunch.model.Restaurant;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

public class MapFragment extends Fragment {

    private MapView mapView;
    private Context context;
    private LiveData<Location> locationLiveData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        MapFragmentViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MapFragmentViewModel.class);
        locationLiveData = viewModel.getLocationLiveData();
        locationLiveData.observe(getViewLifecycleOwner(), location -> centerToCurrentLocation());
        viewModel.getRestaurantListLiveData().observe(getViewLifecycleOwner(), RestaurantListLiveDataObserver);

        context = requireContext();
        Configuration.getInstance().setUserAgentValue(context.getPackageName());
        mapView = view.findViewById(R.id.map_view);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getController().setZoom(18D);
        mapView.setMultiTouchControls(true);
        mapView.setDestroyMode(false);

        view.findViewById(R.id.map_button).setOnClickListener( v -> centerToCurrentLocation() );

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        Configuration.getInstance().load(context,PreferenceManager.getDefaultSharedPreferences(context));
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(context,PreferenceManager.getDefaultSharedPreferences(context));
        mapView.onPause();
    }

    Observer<List<Restaurant>> RestaurantListLiveDataObserver = new Observer<List<Restaurant>>() {
        @Override
        public void onChanged(List<Restaurant> restaurantList) {

            for (Restaurant restaurant: restaurantList) {
                Marker marker = new Marker(mapView);
                marker.setIcon(AppCompatResources.getDrawable(context,R.mipmap.icon_map_restaurant));
                marker.setTitle(restaurant.getName());
                marker.setPosition(new GeoPoint(restaurant.getLatitude(),restaurant.getLongitude()));
                mapView.getOverlays().add(marker);
            }
            centerToCurrentLocation();
        }
    };

    private void centerToCurrentLocation() {
        if (locationLiveData.getValue() != null) {
            mapView.getController().setCenter(new GeoPoint(locationLiveData.getValue().getLatitude(), locationLiveData.getValue().getLongitude()));
        }
    }
}

