package com.example.masluli;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.graphics.Color;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.example.masluli.Model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    ActivityResultLauncher<String> galleryContent;
    Button regBtn;
    ProgressBar progressBar;
    String area;
    ImageView userImv;
    Bitmap imageBitmap;
    ImageButton cameraBtn;
    ImageButton galleryBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initSpinner(view);
        progressBar = view.findViewById(R.id.register_progressbar);
        progressBar.setVisibility(View.GONE);

        cameraBtn = view.findViewById(R.id.register_camera_btn);
        galleryBtn = view.findViewById(R.id.register_gallery_btn);

        cameraBtn.setOnClickListener(v -> {
            openCamera();
        });

        galleryBtn.setOnClickListener(v -> {
            galleryContent.launch("image/*");
        });

        regBtn = view.findViewById(R.id.register_register_btn);
        regBtn.setOnClickListener(v -> {

//            String name = ((EditText)view.findViewById(R.id.register_name_et)).getText().toString();
//            String email = ((EditText)view.findViewById(R.id.register_email_et)).getText().toString();
//            String password = ((EditText)view.findViewById(R.id.register_password_et)).getText().toString();
//            String age = ((EditText)view.findViewById(R.id.register_age_et)).getText().toString();

//            if(!email.equals("") && !password.equals("") && !name.equals("") &&
//                    !age.equals("") && !area.equals("")) {
//
//                progressBar.setVisibility(View.VISIBLE);
//                User newUser = new User(name, email, age, area);
//
//                if(imageBitmap != null) {
//                    // TODO: add to storage account and save url
//                        newUser.setImageUrl("url");
//                }
//
//                // TODO: save to db
//            }
//            else {
//                Toast.makeText(MyApplication.getContext(), "All the fields are required",
//                        Toast.LENGTH_SHORT).show();
//            }

            toMainScreen();
        });

        return view;
    }

    private void initSpinner(View view) {
        Spinner spinner = view.findViewById(R.id.register_area_dd);

        String[] areas = new String[]{
                getResources().getString(R.string.area),
                "North",
                "Center",
                "South"
        };

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

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent, View view,
                            int position, long id) {

                        if(position > 0){

                            area = (String)parent.getItemAtPosition(position);
                        }
                    }
                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent) {
                    }
                });

        spinner.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        galleryContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    // Handle the returned Uri
                    if (uri == null) {
                        return;
                    }

                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (imageBitmap == null) return;

                    userImv.setImageBitmap(imageBitmap);
                });
    }

    private void openCamera() {
        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePhoto,REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA){
            if (resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                userImv.setImageBitmap(imageBitmap);
            }
        }
    }

    private void toMainScreen() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}