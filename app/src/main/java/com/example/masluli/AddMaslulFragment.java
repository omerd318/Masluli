package com.example.masluli;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.masluli.Model.Maslul;
import com.example.masluli.Model.Model;
import com.example.masluli.databinding.FragmentAddMaslulBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

public class AddMaslulFragment extends Fragment implements OnMapReadyCallback {
    public enum MaslulMode {
        Edit,
        Add
    }

    private static final int DEFAULT_ZOOM = 13;
    private final LatLng defaultLocation = new LatLng(31.8747353, 34.9175069);

    FragmentAddMaslulBinding binding;
    ActivityResultLauncher<String> galleryLauncher;
    Boolean isImageSelected = false;
    View view;
    MapView mapView;
    GoogleMap map;
    MaslulMode mode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null){
                binding.addMaslulImg.setImageURI(result);
                binding.addMaslulGalleryBtn.setVisibility(View.INVISIBLE);
                binding.addMaslulImg.setVisibility(View.VISIBLE);
                isImageSelected = true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddMaslulBinding.inflate(inflater,container,false);
        view = binding.getRoot();

        String[] difficulties = getResources().getStringArray(R.array.difficulties_array);
        binding.addMaslulDiffLvlAc.setAdapter(
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, difficulties));

        binding.addMaslulSaveBtn.setOnClickListener(view1 -> {
            saveMaslul(binding, view1);
        });

        binding.addMaslulGalleryBtn.setOnClickListener(view1->{
            galleryLauncher.launch("image/*");
        });

        MapsInitializer.initialize(this.getActivity());
        mapView = view.findViewById(R.id.add_maslul_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    private void saveMaslul(FragmentAddMaslulBinding binding, View view) {
        String name = binding.addMaslulNameEt.getText().toString();
        String location = binding.addMaslulLocationEt.getText().toString();
        int length = Integer.parseInt(binding.addMaslulLengthEt.getText().toString());
        Maslul.Difficulty difficulty = Maslul.Difficulty.valueOf(binding.addMaslulDiffLvlAc.getText().toString());
        boolean isAccessible = binding.addMaslulAccessibleToggleBtn.isChecked();
        boolean isWater = binding.addMaslulWaterToggleBtn.isChecked();
        boolean isRounded = binding.addMaslulRoundToggleBtn.isChecked();
        String userId = Model.instance().getUserEmail();
        String description = binding.addMaslulDescriptionEt.getText().toString();
        int rating = (int) binding.addMaslulRatingBar.getRating();      // TODO: Add rating to Model
        GeoPoint geoPoint = new GeoPoint(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);

            // TODO: Replace - lating, check if ID generates
            Maslul maslul = new Maslul("", name, location, length, difficulty, isAccessible,
                                       isWater, isRounded, description, userId, geoPoint);

            if (isImageSelected) {
                binding.addMaslulImg.setDrawingCacheEnabled(true);
                binding.addMaslulImg.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) binding.addMaslulImg.getDrawable()).getBitmap();
                Model.instance().uploadImage(name, bitmap, url->{
                    if (url != null){
                        maslul.setImageUrl(url);
                    }
                    Model.instance().addMaslul(maslul, (unused) -> {
                        Navigation.findNavController(view).popBackStack();
                    });
                });
            } else {
                Model.instance().addMaslul(maslul, (unused) -> {
                    Navigation.findNavController(view).popBackStack();
                });
            }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));

        googleMap.setOnMapClickListener(latLng -> {
            // Clear past markers
            map.clear();

            // Initialize marker options
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(latLng.latitude + ": " + latLng.longitude);
            // set position of marker
            markerOptions.position(latLng);

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    latLng, 15
            ));
            map.addMarker(markerOptions);
        });

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}