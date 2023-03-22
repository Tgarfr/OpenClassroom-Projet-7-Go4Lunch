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

    MapFragmentViewModel viewModel;
    MapView mapView;
    Context context;
    Location currentLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MapFragmentViewModel.class);
        viewModel.getLocationLiveData().observe(getViewLifecycleOwner(), observerLocationLiveData);
        viewModel.getRestaurantListLiveData().observe(getViewLifecycleOwner(), observerListRestaurantLiveData);

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

    Observer<Location> observerLocationLiveData = new Observer<Location>() {
        @Override
        public void onChanged(Location newLocation) {
            currentLocation = newLocation;
            centerToCurrentLocation();
        }
    };

    Observer<List<Restaurant>> observerListRestaurantLiveData = new Observer<List<Restaurant>>() {
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
        mapView.getController().setCenter(new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()));
    }
}

