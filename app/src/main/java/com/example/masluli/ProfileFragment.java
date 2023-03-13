package com.example.masluli;

import static com.example.masluli.Model.Model.areas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.masluli.Model.Model;
import com.example.masluli.Model.User;
import com.example.masluli.databinding.FragmentProfileBinding;
import com.example.masluli.databinding.FragmentRegisterBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfileFragment extends Fragment {
    String area;
    FragmentProfileBinding binding;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    Boolean isAvatarSelected = false;
    String initialUserUrl = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    binding.profileImageImv.setImageBitmap(result);
                    isAvatarSelected = true;
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null){
                    binding.profileImageImv.setImageURI(result);
                    isAvatarSelected = true;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        initSpinner(view);
        binding.profileProgressbar.setVisibility(View.GONE);

        String signedUser = Model.instance.getUserEmail();
        Model.instance.getUserById(signedUser, user -> {
            binding.profileEmailEt.setText(user.getEmail());
            binding.profileEmailEt.setEnabled(false);
            binding.profileNameEt.setText(user.getName());
            binding.profileAreaDd.setSelection(findIndexOf(user.getArea()));
//            binding.profileAreaDd.setSelection(1);
            binding.profileAgeEt.setText(user.getAge());
            if (user.getImageUrl() != null && !user.getImageUrl().equals("")) {
                initialUserUrl = user.getImageUrl();
                Picasso.get()
                        .load(user.getImageUrl())
                        .into(binding.profileImageImv);
            }
        });

        binding.profileCameraBtn.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        binding.profileGalleryBtn.setOnClickListener(view1->{
            galleryLauncher.launch("image/*");
        });

        // TODO: move to actionbar menu
        binding.profileSignOutBtn.setOnClickListener(v -> {
            Model.instance().signOut();
            toLoginActivity();
        });

        binding.profileSaveBtn.setOnClickListener(v -> {

            String name = binding.profileNameEt.getText().toString();
            String email = binding.profileEmailEt.getText().toString();
            String age = binding.profileAgeEt.getText().toString();


            if (!email.equals("") && !name.equals("") &&
                    !age.equals("") && !area.equals("")) {

                binding.profileProgressbar.setVisibility(View.VISIBLE);

                        User newUser = new User(name, email, age, area);
                        newUser.setImageUrl(initialUserUrl);

                        if (isAvatarSelected) {
                            binding.profileImageImv.setDrawingCacheEnabled(true);
                            binding.profileImageImv.buildDrawingCache();
                            Bitmap imageBitmap = ((BitmapDrawable) binding.profileImageImv.getDrawable()).getBitmap();

                            // Add to storage account and save url
                            Model.instance().uploadImage(email, imageBitmap, url -> {
                                if (url != null) {
                                    newUser.setImageUrl(url);
                                }
                                Model.instance().addUser(newUser, usr -> {
                                    binding.profileProgressbar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), "User saved successfully",
                                            Toast.LENGTH_SHORT).show();
                                });
                            });
                        } else {
                            // Save without img
                            Model.instance().addUser(newUser, usr -> {
                                binding.profileProgressbar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "User saved successfully",
                                        Toast.LENGTH_SHORT).show();
                            });
                        }

            } else {
                Toast.makeText(getContext(), "All the fields are required",
                        Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void initSpinner(View view) {
        ArrayAdapter<String> spinnerArrayAdapter
                = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                new ArrayList<String>(Arrays.asList(areas))
        ){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }

            @Override
            public View getDropDownView(
                    int position, View convertView,
                    @NonNull ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;

                if(position == 0){
                    textView.setTextColor(Color.GRAY);
                }
                else { textView.setTextColor(Color.BLACK); }
                return view;
            }
        };

        binding.profileAreaDd.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent, View view,
                            int position, long id) {

                        if (position > 0) {
                            area = (String)parent.getItemAtPosition(position);
                        }
                    }
                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent) {
                    }
                });

        binding.profileAreaDd.setAdapter(spinnerArrayAdapter);
    }

    private int findIndexOf(String area) {
        int i = 0;
        while (i < areas.length) {
            if (areas[i].equals(area)) {
                return i;
            }
            else {
                i = i + 1;
            }
        }
        return -1;
    }

    // TODO: temp
    private void toLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}