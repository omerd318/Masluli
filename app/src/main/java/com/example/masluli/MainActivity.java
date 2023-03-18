package com.example.masluli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.masluli.Model.Model;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.main_navhost);
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this,navController);

        BottomNavigationView navView = findViewById(R.id.main_bottomNavigationView);
        NavigationUI.setupWithNavController(navView,navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            navController.popBackStack();
        } else if(item.getItemId() == R.id.menu_logout) {
            signOut();
        } else if(item.getItemId() == R.id.menu_my_maslulim) {
            NavDirections action = MyMaslulimFragmentDirections.actionGlobalMyMaslulimFragment();
            navController.navigate(action);
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        Model.instance().signOut();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}