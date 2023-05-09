package com.startup.go4lunch.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.startup.go4lunch.R;
import com.startup.go4lunch.di.ViewModelFactory;
import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.RestaurantMapMarker;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

public class MapFragment extends Fragment {

    private Context context;
    private MapFragmentViewModel viewModel;
    private MapView mapView;
    private LiveData<Location> locationLiveData;
    private LiveData<String> searchLiveData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MapFragmentViewModel.class);
        viewModel.setLiveDataObserver(getViewLifecycleOwner());
        context = requireContext();

        locationLiveData = viewModel.getLocationLiveData();
        locationLiveData.observe(getViewLifecycleOwner(), location -> updateCenterLocation());

        viewModel.getRestaurantMapMarkerListLiveData().observe(getViewLifecycleOwner(), restaurantMapMarkerListLiveDataObserver);

        searchLiveData = viewModel.getSearchStringLivedata();
        searchLiveData.observe(getViewLifecycleOwner(), s -> updateCenterLocation() );

        Configuration.getInstance().setUserAgentValue(context.getPackageName());
        mapView = view.findViewById(R.id.map_view);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getController().setZoom(18D);
        mapView.setMultiTouchControls(true);
        mapView.setDestroyMode(false);

        view.findViewById(R.id.map_compass_button).setOnClickListener(v -> {
            viewModel.setEndSearch();
            updateCenterLocation();
        });

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
        Configuration.getInstance().save(context,PreferenceManager.getDefaultSharedPreferences(context));
        mapView.onPause();
        super.onPause();
    }

    Observer<List<RestaurantMapMarker>> restaurantMapMarkerListLiveDataObserver = new Observer<List<RestaurantMapMarker>>() {
        @Override
        public void onChanged(List<RestaurantMapMarker> restaurantMapMarkerList) {
            for (RestaurantMapMarker restaurantMapMarker: restaurantMapMarkerList) {
                Restaurant restaurant = restaurantMapMarker.getRestaurant();
                Marker marker = new Marker(mapView);
                if (restaurantMapMarker.getWorkmateLunchOnRestaurant()) {
                    marker.setIcon(AppCompatResources.getDrawable(context,R.mipmap.icon_map_restaurant_marker_green));
                } else {
                    marker.setIcon(AppCompatResources.getDrawable(context,R.mipmap.icon_map_restaurant_marker));
                }
                marker.setTitle(restaurant.getName());
                marker.setPosition(new GeoPoint(restaurant.getLatitude(),restaurant.getLongitude()));
                marker.setRelatedObject(restaurant.getId());
                marker.setOnMarkerClickListener(onMarkerClickListener);
                mapView.getOverlays().add(marker);
            }
            updateCenterLocation();
        }
    };

    private void updateCenterLocation() {
        String search = searchLiveData.getValue();
        if (search == null) {
            centerToCurrentLocation();
        } else {
            Restaurant searchRestaurant = viewModel.getRestaurantFromString(search);
            if (searchRestaurant != null) {
                centerToRestaurantLocation(searchRestaurant);
            } else {
                centerToCurrentLocation();
            }
        }
    }

    private void centerToCurrentLocation() {
        Location currentLocation = locationLiveData.getValue();
        if (currentLocation != null) {
            centerToLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
    }

    private void centerToRestaurantLocation(@NonNull Restaurant restaurant) {
        centerToLocation(restaurant.getLatitude(), restaurant.getLongitude());
    }

    private void centerToLocation(double latitude, double longitude) {
        mapView.getController().setCenter(new GeoPoint(latitude, longitude));
    }

    private final Marker.OnMarkerClickListener onMarkerClickListener = (marker, mapView) -> {
        Intent intent = new Intent(requireContext(), RestaurantDetailActivity.class);
        intent.putExtra("restaurantId", (long) marker.getRelatedObject());
        ActivityCompat.startActivity(requireActivity(), intent, null);
        return false;
    };
}

