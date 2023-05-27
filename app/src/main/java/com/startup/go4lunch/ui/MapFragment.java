package com.startup.go4lunch.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.startup.go4lunch.R;
import com.startup.go4lunch.di.ViewModelFactory;
import com.startup.go4lunch.model.Restaurant;
import com.startup.go4lunch.model.RestaurantMapMarker;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.util.List;

public class MapFragment extends Fragment {

    private final static int LOCATION_REQUEST_CODE = 1;
    private Context context;
    private MapFragmentViewModel viewModel;
    private MapView mapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        context = requireContext();

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MapFragmentViewModel.class);
        viewModel.getMapCenterLocationLiveData().observe(getViewLifecycleOwner(),
                location -> mapView.getController().setCenter(new GeoPoint(location.getLatitude(), location.getLongitude())) );
        viewModel.getRestaurantMapMarkerListLiveData().observe(getViewLifecycleOwner(), restaurantMapMarkerListLiveDataObserver);

        Configuration.getInstance().setUserAgentValue(context.getPackageName());
        mapView = view.findViewById(R.id.map_view);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getController().setZoom(18D);
        mapView.setMultiTouchControls(true);
        mapView.setDestroyMode(false);

        view.findViewById(R.id.map_compass_button).setOnClickListener(v -> {
            viewModel.setEndSearch();
            getCurrentLocation();
        } );

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
            List<Overlay> overlayList= mapView.getOverlays();
            for (Overlay overlay: overlayList) {
                if (overlay instanceof Marker) {
                    overlayList.remove(overlay);
                }
            }
            if (restaurantMapMarkerList != null) {
                for (RestaurantMapMarker restaurantMapMarker: restaurantMapMarkerList) {
                    Restaurant restaurant = restaurantMapMarker.getRestaurant();
                    Marker marker = new Marker(mapView);
                    marker.setRelatedObject(restaurant.getId());
                    marker.setTitle(restaurant.getName());
                    marker.setPosition(new GeoPoint(restaurant.getLatitude(),restaurant.getLongitude()));
                    marker.setIcon(AppCompatResources.getDrawable(context,R.mipmap.icon_map_restaurant_marker));
                    marker.setOnMarkerClickListener(onMarkerClickListener);
                    if (restaurantMapMarker.getWorkmateLunchOnRestaurant()) {
                        marker.setIcon(AppCompatResources.getDrawable(context,R.mipmap.icon_map_restaurant_marker_green));
                    } else {
                        marker.setIcon(AppCompatResources.getDrawable(context,R.mipmap.icon_map_restaurant_marker));
                    }
                    overlayList.add(marker);
                }
            }
        }
    };

    private final Marker.OnMarkerClickListener onMarkerClickListener = (marker, mapView) -> {
        Intent intent = new Intent(requireContext(), RestaurantDetailActivity.class);
        intent.putExtra("restaurantId", (long) marker.getRelatedObject());
        ActivityCompat.startActivity(requireActivity(), intent, null);
        return false;
    };

    private void getCurrentLocation() {
        if (!checkLocationPermission()) {
            return;
        }
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(location -> viewModel.updateLocation(location));
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return false;
        }
        return true;
    }
}

