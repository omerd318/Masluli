package com.example.masluli;

import static com.example.masluli.Model.Model.areas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.graphics.Color;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.masluli.Model.Model;
import com.example.masluli.Model.User;
import com.example.masluli.databinding.FragmentRegisterBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class RegisterFragment extends Fragment {
    String area;
    FragmentRegisterBinding binding;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    Boolean isAvatarSelected = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    binding.registerImageImv.setImageBitmap(result);
                    isAvatarSelected = true;
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null){
                    binding.registerImageImv.setImageURI(result);
                    isAvatarSelected = true;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        initSpinner(view);
        binding.registerProgressbar.setVisibility(View.GONE);

        binding.registerCameraBtn.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        binding.registerGalleryBtn.setOnClickListener(view1->{
            galleryLauncher.launch("image/*");
        });

        binding.registerRegisterBtn.setOnClickListener(v -> {
            register();
        });

        return view;
    }

    private void register() {

        String name = binding.registerNameEt.getText().toString();
        String email = binding.registerEmailEt.getText().toString();
        String password = binding.registerPasswordEt.getText().toString();
        String age = binding.registerAgeEt.getText().toString();

        if(!email.equals("") && !password.equals("") && !name.equals("") &&
                !age.equals("") && !area.equals("")) {

            binding.registerProgressbar.setVisibility(View.VISIBLE);

            Model.instance.register(email, password, user -> {
                if(user != null) {
                    User newUser = new User(name, email, age, area);

                    if (isAvatarSelected) {
                        binding.registerImageImv.setDrawingCacheEnabled(true);
                        binding.registerImageImv.buildDrawingCache();
                        Bitmap imageBitmap = ((BitmapDrawable) binding.registerImageImv.getDrawable()).getBitmap();

                        // Add to storage account and save url
                        Model.instance().uploadImage(email, imageBitmap, url->{
                            if (url != null){
                                newUser.setImageUrl(url);
                            }
                            Model.instance().addUser(newUser, usr -> {
                                binding.registerProgressbar.setVisibility(View.GONE);
                                toMainScreen();
                            });
                        });
                    } else {
                        // Save without img
                        Model.instance().addUser(newUser, usr -> {
                            binding.registerProgressbar.setVisibility(View.GONE);
                            toMainScreen();
                        });
                    }
                } else { binding.registerProgressbar.setVisibility(View.GONE); }
            });
        }
        else {
            Toast.makeText(getContext(), "All the fields are required",
                    Toast.LENGTH_LONG).show();
        }

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

        binding.registerAreaDd.setOnItemSelectedListener(
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

        binding.registerAreaDd.setAdapter(spinnerArrayAdapter);
    }

    private void toMainScreen() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}