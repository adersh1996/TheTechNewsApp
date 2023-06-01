package com.project.thetechnewsapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    private static int NAVIGATION_HOME = R.id.navigation_home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView
                = findViewById(R.id.btm_nav);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

    }

    HomeFragment homeFragment = new HomeFragment();
    TopicsFragment topicsFragment = new TopicsFragment();
    FavoriteFragment favoriteFragment = new FavoriteFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.navigation_home){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, homeFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.navigation_topic) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, topicsFragment)
                    .commit();
            return true;
        }else if (item.getItemId() == R.id.navigation_favorite) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, favoriteFragment)
                    .commit();
            return true;
        }else if (item.getItemId() == R.id.navigation_profile) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, profileFragment)
                    .commit();
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do You Want To Exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}