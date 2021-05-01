package com.cherrierlaoussing.univmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.model.univmap.ColorCustomPopUp;

public class UnivMapActivity extends AppCompatActivity {

    BottomNavigationView navBar;
    private ConstraintLayout constraintLayout;
    private TextView firstTitle, firstSubTitle, secondTitle;

    /// =============================== Data Persist =============================
    private SharedPreferences preferences;
    private int r,g,b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_univ_map);

        /// =============================== initialisation Data Persist =============================
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        constraintLayout = findViewById(R.id.univMapLayout);
        r = preferences.getInt("red",91);
        g = preferences.getInt("green",107);
        b = preferences.getInt("blue",128);
        constraintLayout.setBackgroundColor(Color.argb(255, r, g, b));
        firstTitle = findViewById(R.id.univMap_firstTitle);
        firstTitle.setTextColor(ColorCustomPopUp.adaptativeColor(r,g,b));
        firstSubTitle = findViewById(R.id.univMap_firstSubTitle);
        firstSubTitle.setTextColor(ColorCustomPopUp.adaptativeColor(r,g,b));
        secondTitle = findViewById(R.id.univMap_secondTitle);
        secondTitle.setTextColor(ColorCustomPopUp.adaptativeColor(r,g,b));

        /// =============================== ActionBar =============================
        getSupportActionBar().setTitle(getString(R.string.univMap));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /// =============================== TabBar =============================
        navBar = findViewById(R.id.tabBar);

        navBar.setSelectedItemId(R.id.settings);

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    public void goToWebSiteDamien(View v){
        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse("https://damienlaoussing.com/") );
        startActivity( browse );
    }

}