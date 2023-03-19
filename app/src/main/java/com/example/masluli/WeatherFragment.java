package com.example.masluli;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.masluli.databinding.FragmentWeatherBinding;

public class WeatherFragment extends Fragment {
    FragmentWeatherBinding binding;
    WeatherListAdapter adapter;
    WeatherListViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new WeatherListViewModel();
    }
}