package com.example.masluli;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.masluli.Model.Model;
import com.example.masluli.databinding.FragmentMaslulDetailsBinding;
import com.squareup.picasso.Picasso;

public class MaslulDetailsFragment extends Fragment {

    private static final String ARG_MASLUL_ID = "maslulId";

    private String maslulId;
    FragmentMaslulDetailsBinding binding;

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

        Model.instance().getMaslulById(maslulId, (maslul) -> {
            binding.maslulDetailsNameTv.setText(maslul.getTitle());
            binding.maslulDetailsLocationTv.setText(maslul.getLocation());
            binding.maslulDetailsDetailsTv.setText(maslul.getDifficulty().name() + ", " + maslul.getLength() + " Km");
            Model.instance().getUserById(maslul.getUserId(), user -> {
                binding.maslulDetailsUserTv.setText(user.getName());
            });
            binding.maslulDetailsDescriptionTv.setText(maslul.getDescription());
            binding.maslulDetailsImg.setImageResource(R.drawable.no_image_maslul);
            if (maslul.getImageUrl() != null && !maslul.getImageUrl().equals("")) {
                Picasso.get().load(maslul.getImageUrl()).into(binding.maslulDetailsImg);
            }
        });
        return view;
    }
}