package com.example.masluli;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.masluli.MyApplication;
import com.example.masluli.R;
import com.example.masluli.MainActivity;
//import com.example.masluli.model.Model;
//import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {
    EditText emailEt;
    EditText passwordEt;
    Button loginBtn;
    Button moveToRegBtn;
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        progressBar = view.findViewById(R.id.login_progressbar);
        progressBar.setVisibility(View.GONE);

        moveToRegBtn = view.findViewById(R.id.login_move_to_register_btn);
        moveToRegBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
        });

        loginBtn = view.findViewById(R.id.login_login_btn);
        loginBtn.setOnClickListener(v -> {
            emailEt = view.findViewById(R.id.login_email_et);
            passwordEt = view.findViewById(R.id.login_password_et);

            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();

            if(!email.equals("") && !password.equals("")) {
                progressBar.setVisibility(View.VISIBLE);
                toMainScreen();
//                Model.instance.login(email, password, user -> {
//                    progressBar.setVisibility(View.GONE);
//                    if (user != null) {
//                        toMainScreen();
//                    }
//                });
            }
//            else {
//                Toast.makeText(MyApplication.getContext(), "Email and password cannot be empty",
//                        Toast.LENGTH_SHORT).show();
//            }
        });

        return view;
    }

    private void toMainScreen() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}