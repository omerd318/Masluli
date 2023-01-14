package com.example.masluli;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.masluli.databinding.FragmentRoutesBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutesFragment} factory method to
 * create an instance of this fragment.
 */
public class RoutesFragment extends Fragment {

    FragmentRoutesBinding binding;
    RoutesListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRoutesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.routesListRv.setHasFixedSize(true);
        binding.routesListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RoutesListAdapter(getLayoutInflater());
        binding.routesListRv.setAdapter(adapter);

        return view;
    }
}