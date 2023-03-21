package com.example.masluli;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.masluli.Model.Maslul;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback ,GoogleMap.OnInfoWindowClickListener{

    private static final int DEFAULT_ZOOM = 13;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;
    private final LatLng defaultLocation = new LatLng(31.8747353, 34.9175069);
    private GoogleMap map;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    MaslulimListViewModel viewModel;
    View view;
    ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register the permissions callback, which handles the user's response to the
        // system permissions dialog.
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onActivityResult(Boolean isGranted) {
                if(isGranted) {
                    map.setMyLocationEnabled(true);
                    getDeviceLocation();

                } else {
                    map.setMyLocationEnabled(false);
                }
            }
        });

        // Remove 'back' icon from action bar
        ((AppCompatActivity)getActivity()).getSupportActionBar(). setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_map, container, false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        Log.d("TAG", "Map ready");

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        googleMap.setOnMapClickListener(latLng -> {
            // Initialize marker options
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(latLng.latitude + ": " + latLng.longitude);
            // set position of marker
            markerOptions.position(latLng);

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    latLng, 10
            ));
            googleMap.addMarker(markerOptions);
        });

        loadMaslulimMarkers();

        map.setOnInfoWindowClickListener(this);
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (map.isMyLocationEnabled()) {
                @SuppressLint("MissingPermission") Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d("TAG", "Current location is null. Using defaults.");
                            Log.e("TAG", "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.d("Exception: %s", e.getMessage(), e);
        }
    }
    /**
     * Prompts the user for permission to use the device location.
     */
    @SuppressLint("MissingPermission")
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The registered ActivityResultCallback gets the result of this request.
         */
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            Log.d("TAG", "location permission granted");
        } else {
            Log.d("TAG", "location permission not granted, requesting permission");
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (map.isMyLocationEnabled()) {
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void loadMaslulimMarkers() {
        List<Maslul> maslulimList = viewModel.getData().getValue();
        for(int i=0; i< maslulimList.size(); i++) {
            Maslul maslul = maslulimList.get(i);
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(maslul.getLatitude(), maslul.getLongitude()))
                    .title(maslul.getTitle()));
            marker.setTag(maslul.getId());
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new MaslulimListViewModel(MaslulimListViewModel.ListMode.AllMaslulim);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Object tag = marker.getTag();
        if(tag != null) {
            String maslulId = (String) tag;
//            MapFragmentDirections.ActionMapFragmentToViewMaslulFragment action = MapFragmentDirections.actionMapFragmentToViewMaslulFragment(maslulId);
            Navigation.findNavController(view).navigate(MapFragmentDirections.actionMapFragmentToMaslulDetailsFragment(maslulId));
        }
    }
}