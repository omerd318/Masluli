package com.example.masluli;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.masluli.Model.Maslul;
import com.example.masluli.Model.Model;
import com.example.masluli.Model.User;
import com.example.masluli.databinding.FragmentAddMaslulBinding;
import com.google.firebase.firestore.GeoPoint;

public class AddMaslulFragment extends Fragment {
    FragmentAddMaslulBinding binding;
    ActivityResultLauncher<String> galleryLauncher;
    Boolean isImageSelected = false;

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
        View view = binding.getRoot();

        String[] locations = getResources().getStringArray(R.array.locations_array);
        binding.addMaslulLocationAc.setAdapter(
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, locations));

        String[] difficulties = getResources().getStringArray(R.array.difficulties_array);
        binding.addMaslulDiffLvlAc.setAdapter(
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, difficulties));

        binding.addMaslulSaveBtn.setOnClickListener(view1 -> {
            String name = binding.addMaslulNameEt.getText().toString();
            String location = binding.addMaslulLocationAc.getText().toString();
            int length = Integer.parseInt(binding.addMaslulLengthEt.getText().toString());
            Maslul.Difficulty difficulty = Maslul.Difficulty.valueOf(binding.addMaslulDiffLvlAc.getText().toString());
            boolean isAccessible = binding.addMaslulAccessibleToggleBtn.isChecked();
            boolean isWater = binding.addMaslulWaterToggleBtn.isChecked();
            boolean isRounded = binding.addMaslulRoundToggleBtn.isChecked();
            String userId = Model.instance().getUserEmail();
            String description = binding.addMaslulDescriptionEt.getText().toString();
//            GeoPoint latlng = ??

//            // TODO: Replace - id, defficulty, lating
//            Maslul maslul = new Maslul("123", name, location, length, difficulty,
//                                       isAccessible, isWater, isRounded, description, userId, null);
//
//            if (isImageSelected) {
//                binding.addMaslulImg.setDrawingCacheEnabled(true);
//                binding.addMaslulImg.buildDrawingCache();
//                Bitmap bitmap = ((BitmapDrawable) binding.addMaslulImg.getDrawable()).getBitmap();
//                Model.instance().uploadImage(name, bitmap, url->{
//                    if (url != null){
//                        maslul.setImageUrl(url);
//                    }
//                    Model.instance().addMaslul(maslul, (unused) -> {
//                        Navigation.findNavController(view1).popBackStack();
//                    });
//                });
//            } else {
//                Model.instance().addMaslul(maslul, (unused) -> {
//                    Navigation.findNavController(view1).popBackStack();
//                });
//            }
        });

        binding.addMaslulGalleryBtn.setOnClickListener(view1->{
            galleryLauncher.launch("image/*");
        });

        return view;
    }

    private static final String[] LOCATIONS = new String[] {
            "north", "east", "south", "west"
    };
}