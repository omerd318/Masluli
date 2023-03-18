package com.example.masluli;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.masluli.Model.Model;
import com.example.masluli.databinding.FragmentMaslulDetailsBinding;

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
        Log.d("TAG", "maslul id: " + maslulId);

        Model.instance().getMaslulById(maslulId, (maslul) -> {
            // TODO: show maslul details
        });
        return view;
    }
}