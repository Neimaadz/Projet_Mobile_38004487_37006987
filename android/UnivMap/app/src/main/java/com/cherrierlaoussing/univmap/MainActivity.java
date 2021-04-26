package com.cherrierlaoussing.univmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //private MapView mapView;
    BottomNavigationView navBar;
    NavHostFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navBar = findViewById(R.id.tabBar);
        fragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmen1);
        NavigationUI.setupWithNavController(navBar, fragment.getNavController());
    }

}