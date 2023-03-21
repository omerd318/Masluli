package com.example.masluli;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.masluli.Model.Maslul;
import com.example.masluli.Model.Model;
import com.example.masluli.databinding.FragmentMaslulimBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MaslulimFragment} factory method to
 * create an instance of this fragment.
 */
public class MaslulimFragment extends Fragment {

    FragmentMaslulimBinding binding;
    MaslulimListAdapter adapter;
    MaslulimListViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMaslulimBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.maslulimListRv.setHasFixedSize(true);
        binding.maslulimListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MaslulimListAdapter(getLayoutInflater(),viewModel.getData().getValue());

        binding.maslulimListRv.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            Maslul maslul = viewModel.getData().getValue().get(position);
            Navigation.findNavController(v).navigate(MaslulimFragmentDirections.
                            actionMaslulimFragmentToMaslulDetailsFragment(maslul.getId()));

        });

        View addButton = view.findViewById(R.id.maslulimList_add_btn);
        NavDirections action = MaslulimFragmentDirections.actionGlobalAddMaslulFragment(null);
        addButton.setOnClickListener(Navigation.createNavigateOnClickListener(action));

        viewModel.getData().observe(getViewLifecycleOwner(),list->{
            adapter.setData(list);
        });

        Model.instance().EventMaslulimListLoadingState.observe(getViewLifecycleOwner(),status->{
            binding.maslulimListRvSwipeRefresh.setRefreshing(status == Model.MaslulimListLoadingState.LOADING);
        });

        binding.maslulimListRvSwipeRefresh.setOnRefreshListener(()->{
            reloadData();
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new MaslulimListViewModel(MaslulimListViewModel.ListMode.AllMaslulim);
    }

    void reloadData(){
        Model.instance().refreshAllMaslulim();
    }

}