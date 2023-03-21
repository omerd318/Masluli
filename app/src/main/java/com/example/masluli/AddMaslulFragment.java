package com.example.masluli;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;

public class AddMaslulFragment extends Fragment implements OnMapReadyCallback {
    public enum MaslulMode {
        Edit,
        Add
    }

    private static final String ARG_MASLUL_ID = "maslulId";
    private static final int DEFAULT_ZOOM = 13;
    private final LatLng defaultLocation = new LatLng(31.8747353, 34.9175069);

    private String maslulId;
    FragmentAddMaslulBinding binding;
    ActivityResultLauncher<String> galleryLauncher;
    Boolean isImageSelected = false;
    View view;
    MapView mapView;
    GoogleMap map;
    MaslulMode mode = MaslulMode.Add;
    Maslul currMaslul;
    String[] difficulties;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            maslulId = getArguments().getString(ARG_MASLUL_ID);

            if(maslulId != null) {
                mode = MaslulMode.Edit;
            }
        }

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null){
                binding.addMaslulImg.setImageURI(result);
                if(mode == MaslulMode.Add) {
                    binding.addMaslulImg.setVisibility(View.GONE);
                } else {
                    binding.addMaslulGalleryBtn.setVisibility(View.VISIBLE);
                    binding.addMaslulImg.setVisibility(View.VISIBLE);
                }
                isImageSelected = true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddMaslulBinding.inflate(inflater,container,false);
        view = binding.getRoot();

        difficulties = getResources().getStringArray(R.array.difficulties_array);
        binding.addMaslulDiffLvlAc.setAdapter(
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, difficulties));

        binding.addMaslulProgressBar.setVisibility(View.GONE);

        MapsInitializer.initialize(this.getActivity());
        mapView = view.findViewById(R.id.add_maslul_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        if(mode == MaslulMode.Edit) {
            Model.instance().getMaslulById(maslulId, (maslul) -> {
                currMaslul = maslul;
            });

            setEditMaslulFragment();
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Edit Maslul");
        }

        binding.addMaslulSaveBtn.setOnClickListener(view1 -> {
            if(validateFields(container)) {
                binding.addMaslulProgressBar.setVisibility(View.VISIBLE);
                saveNewMaslul(view1);
            };
        });

        binding.addMaslulGalleryBtn.setOnClickListener(view1->{
            galleryLauncher.launch("image/*");
        });

        return view;
    }

    private void saveNewMaslul(View view) {
        String id = "";
        String name = binding.addMaslulNameEt.getText().toString();
        String location = binding.addMaslulLocationEt.getText().toString();
        int length = Integer.parseInt(binding.addMaslulLengthEt.getText().toString());
        Maslul.Difficulty difficulty = Maslul.Difficulty.valueOf(binding.addMaslulDiffLvlAc.getText().toString());
        boolean isAccessible = binding.addMaslulAccessibleToggleBtn.isChecked();
        boolean isWater = binding.addMaslulWaterToggleBtn.isChecked();
        boolean isRounded = binding.addMaslulRoundToggleBtn.isChecked();
        String userId = Model.instance().getUserEmail();
        String description = binding.addMaslulDescriptionEt.getText().toString();
        int rating = (int) binding.addMaslulRatingBar.getRating();
        GeoPoint geoPoint;
        String imgId = name + "_" + new Timestamp(System.currentTimeMillis());

        if(mode == MaslulMode.Edit) {
            id = currMaslul.getId();
            geoPoint = new GeoPoint(currMaslul.getLatitude(), currMaslul.getLongitude());
        } else {
            geoPoint = new GeoPoint(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
        }

        Maslul maslul = new Maslul(id, name, location, length, difficulty, isAccessible,
                                    isWater, isRounded, description, userId, rating, geoPoint);

            if (isImageSelected) {
                binding.addMaslulImg.setDrawingCacheEnabled(true);
                binding.addMaslulImg.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) binding.addMaslulImg.getDrawable()).getBitmap();
                Model.instance().uploadImage(imgId, bitmap, url-> {
                    if (url != null) {
                        maslul.setImageUrl(url);
                    }
                    Model.instance().addMaslul(maslul, (unused) -> {
                        binding.addMaslulProgressBar.setVisibility(View.GONE);
                        Navigation.findNavController(view).popBackStack();
                    });
                });
            } else {
                Model.instance().addMaslul(maslul, (unused) -> {
                    binding.addMaslulProgressBar.setVisibility(View.GONE);
                    Navigation.findNavController(view).popBackStack();
                });
            }
    }

    private boolean validateFields(ViewGroup viewGroup) {
        if(TextUtils.isEmpty(binding.addMaslulNameEt.getText()) ||
           TextUtils.isEmpty(binding.addMaslulLocationEt.getText()) ||
           TextUtils.isEmpty(binding.addMaslulLengthEt.getText()) ||
           TextUtils.isEmpty(binding.addMaslulDiffLvlAc.getText()) ||
           TextUtils.isEmpty(binding.addMaslulDescriptionEt.getText()) ||
           binding.addMaslulRatingBar.getRating() < 1 ||
           binding.addMaslulRatingBar.getRating() > 5) {
            Toast.makeText(getContext(), "All fields are required",
                    Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    private void setEditMaslulFragment() {
        binding.addMaslulNameEt.setText(currMaslul.getTitle());
        binding.addMaslulLocationEt.setText(currMaslul.getLocation());
        binding.addMaslulLengthEt.setText(Integer.toString(currMaslul.getLength()));
        binding.addMaslulDiffLvlAc.setText(currMaslul.getDifficulty().name());
        binding.addMaslulRatingBar.setRating(currMaslul.getRating());
        binding.addMaslulDiffLvlAc.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, difficulties));
        binding.addMaslulAccessibleToggleBtn.setChecked(currMaslul.getAccessible());
        binding.addMaslulWaterToggleBtn.setChecked(currMaslul.getWater());
        binding.addMaslulRoundToggleBtn.setChecked(currMaslul.getRounded());
        binding.addMaslulDescriptionEt.setText(currMaslul.getDescription());
        if (currMaslul.getImageUrl() != null && !currMaslul.getImageUrl().equals("")) {
            isImageSelected = true;
            Picasso.get().load(currMaslul.getImageUrl()).into(binding.addMaslulImg);
            binding.addMaslulGalleryBtn.setVisibility(View.VISIBLE);
            binding.addMaslulImg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));

        if(mode == MaslulMode.Edit) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(currMaslul.getLatitude(), currMaslul.getLongitude()), DEFAULT_ZOOM));
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(currMaslul.getLatitude(), currMaslul.getLongitude()))
                    .title(currMaslul.getTitle()));
        } else {
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
