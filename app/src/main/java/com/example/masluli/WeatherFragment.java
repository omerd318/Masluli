package com.example.masluli;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.masluli.databinding.FragmentWeatherBinding;

public class WeatherFragment extends Fragment {
    private static final String ARG_MASLUL_LATLNG = "maslulLatlng";
    private String maslulLatlng;

    FragmentWeatherBinding binding;
    WeatherListAdapter adapter;
    WeatherListViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            maslulLatlng = getArguments().getString(ARG_MASLUL_LATLNG);
            viewModel = new WeatherListViewModel(maslulLatlng);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.weatherListRv.setHasFixedSize(true);
        binding.weatherListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new WeatherListAdapter(getLayoutInflater(),viewModel.getData().getValue());

        binding.weatherListRv.setAdapter(adapter);

        binding.progressBar.setVisibility(View.GONE);

        viewModel.getData().observe(getViewLifecycleOwner(),list->{
            adapter.setData(list);
        });

        return view;
    }
}