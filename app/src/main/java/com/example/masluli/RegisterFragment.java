package com.example.masluli;

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
        FragmentActivity parentActivity = getActivity();

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

            String name = binding.registerNameEt.getText().toString();
            String email = binding.registerEmailEt.getText().toString();
            String password = binding.registerPasswordEt.getText().toString();
            String age = binding.registerAgeEt.getText().toString();

            if(!email.equals("") && !password.equals("") && !name.equals("") &&
                    !age.equals("") && !area.equals("")) {

                binding.registerProgressbar.setVisibility(View.VISIBLE);
                User newUser = new User(name, email, age, area);

                if(isAvatarSelected) {
                    binding.registerImageImv.setDrawingCacheEnabled(true);
                    binding.registerImageImv.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) binding.registerImageImv.getDrawable()).getBitmap();

                    // TODO: add to storage account and save url
//                    Model.instance().uploadImage(stId, bitmap, url->{
//                        if (url != null){
//                            st.setAvatarUrl(url);
//                        }
//                        Model.instance().addStudent(st, (unused) -> {
//                            Navigation.findNavController(view1).popBackStack();
//                        });
//                    });
                } else {
                    // TODO: save without img
//                    Model.instance().addStudent(st, (unused) -> {
//                        Navigation.findNavController(view1).popBackStack();
//                    });
                }

                // TODO: save to db
                toMainScreen();
            }
            else {
                Toast.makeText(getContext(), "All the fields are required",
                        Toast.LENGTH_LONG).show();
            }

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

    private void toMainScreen() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}