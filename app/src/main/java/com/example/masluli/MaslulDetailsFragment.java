package com.example.masluli;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.masluli.Model.Maslul;
import com.example.masluli.Model.Model;
import com.example.masluli.databinding.FragmentMaslulDetailsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

public class MaslulDetailsFragment extends Fragment
//        implements OnMapReadyCallback
{

    private static final String ARG_MASLUL_ID = "maslulId";
//    private static final int DEFAULT_ZOOM = 13;
//    private static LatLng defaultLocation;

    private String maslulId;
    FragmentMaslulDetailsBinding binding;
    Maslul maslul;
//    MapView mapView;
//    GoogleMap map;
//    AddMaslulFragment.MaslulMode mode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            maslulId = getArguments().getString(ARG_MASLUL_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMaslulDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Model.instance().getMaslulById(maslulId, (currMaslul) -> {
            maslul = currMaslul;
        });

        setFragmentData(maslul);

//        MapsInitializer.initialize(this.getActivity());
//        mapView = view.findViewById(R.id.maslul_details_map);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(this);

        binding.maslulDetailsWeatherBtn.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(MaslulDetailsFragmentDirections.
                                                         actionMaslulDetailsFragmentToWeatherFragment());
        });

        return view;
    }

    private int visibilityBoolToInt(boolean visibility) {
        if(visibility == true) {
            return View.VISIBLE;
        }

        return View.GONE;
    }

    private void setFragmentData(Maslul maslul) {
        binding.maslulDetailsNameTv.setText(maslul.getTitle());
        binding.maslulDetailsLocationTv.setText(maslul.getLocation());
        binding.maslulDetailsDetailsTv.setText(maslul.getDifficulty().name() + ", " + maslul.getLength() + " Km");
        Model.instance().getUserById(maslul.getUserId(), user -> {
            binding.maslulDetailsUserTv.setText(user.getName());
        });
        binding.maslulDetailsDescriptionTv.setText(maslul.getDescription());
        binding.maslulDetailsAccessibleIv.setVisibility(visibilityBoolToInt((maslul.getAccessible())));
        binding.maslulDetailsWaterIv.setVisibility(visibilityBoolToInt((maslul.getWater())));
        binding.maslulDetailsRoundIv.setVisibility(visibilityBoolToInt((maslul.getRounded())));
        binding.maslulDetailsImg.setImageResource(R.drawable.no_image_maslul);
        if (maslul.getImageUrl() != null && !maslul.getImageUrl().equals("")) {
            Picasso.get().load(maslul.getImageUrl()).into(binding.maslulDetailsImg);
        }
//        defaultLocation = new LatLng(maslul.getLatitude(), maslul.getLongitude());
    }

//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        map = googleMap;
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
//
//        googleMap.setOnMapClickListener(latLng -> {
//            // Clear past markers
//            map.clear();
//
//            // Initialize marker options
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.title(latLng.latitude + ": " + latLng.longitude);
//            // set position of marker
//            markerOptions.position(latLng);
//
//            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                    latLng, 15
//            ));
////            map.addMarker(markerOptions);
//        });
//    }
}