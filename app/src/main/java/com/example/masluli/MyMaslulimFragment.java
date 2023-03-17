package com.example.masluli;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.masluli.Model.Maslul;
import com.example.masluli.Model.Model;
import com.example.masluli.databinding.FragmentMaslulimBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyMaslulimFragment} factory method to
 * create an instance of this fragment.
 */
public class MyMaslulimFragment extends Fragment {

    FragmentMaslulimBinding binding;
    MaslulimListAdapter adapter;
    MaslulimListViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.findItem(R.id.menu_my_maslulim).setVisible(false);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        },this, Lifecycle.State.RESUMED);

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
            // TODO: navigate to view maslul screen
//            Navigation.findNavController(v).navigate(MyMaslulimListDirections.
//                    actionMyMaslulimListToViewMAslulFragment(maslul));

        });

        binding.progressBar.setVisibility(View.GONE);

        viewModel.getData().observe(getViewLifecycleOwner(),list->{
            adapter.setData(list);
        });

        Model.instance().EventMaslulimListLoadingState.observe(getViewLifecycleOwner(), status->{
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
        viewModel = new MaslulimListViewModel(MaslulimListViewModel.ListMode.MyMaslulim);
    }

    void reloadData(){
        binding.progressBar.setVisibility(View.VISIBLE);
        Model.instance().refreshAllMaslulim();
    }

}