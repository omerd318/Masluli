package com.example.masluli;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.masluli.Model.Model;
import com.example.masluli.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.loginProgressbar.setVisibility(View.GONE);

        binding.loginMoveToRegisterBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
        });

        binding.loginLoginBtn.setOnClickListener(v -> {
            String email = binding.loginEmailEt.getText().toString();
            String password = binding.loginPasswordEt.getText().toString();

            if(!email.equals("") && !password.equals("")) {
                binding.loginProgressbar.setVisibility(View.VISIBLE);
                Model.instance.login(email, password, user -> {
                    binding.loginProgressbar.setVisibility(View.GONE);
                    if (user != null) {
                        toMainScreen();
                    }
                });
            }
            else {
                Toast.makeText(MyApplication.getContext(), "Email and password cannot be empty",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void toMainScreen() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}