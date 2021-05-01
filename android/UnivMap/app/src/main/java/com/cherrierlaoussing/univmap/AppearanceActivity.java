package com.cherrierlaoussing.univmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.model.univmap.ColorCustomPopUp;
//import com.google.firebase.firestore.core.View;

public class AppearanceActivity extends AppCompatActivity {

    private BottomNavigationView navBar;
    private ConstraintLayout constraintLayout;
    private TextView firstTitle;

    /// =============================== Data Persist =============================
    private SharedPreferences preferences;
    private int r,g,b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appearance);

        /// =============================== initialisation Data Persist =============================
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        constraintLayout = findViewById(R.id.appearanceLayout);
        r = preferences.getInt("red",91);
        g = preferences.getInt("green",107);
        b = preferences.getInt("blue",128);
        constraintLayout.setBackgroundColor(Color.argb(255, r, g, b));
        firstTitle = findViewById(R.id.appearance_firstTitle);
        firstTitle.setTextColor(ColorCustomPopUp.adaptativeColor(r,g,b));

        /// =============================== ActionBar =============================
        getSupportActionBar().setTitle(getString(R.string.appearence));
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

    public void chooseColor(View v){
        ColorCustomPopUp popup = new ColorCustomPopUp(this);
        popup.getNoButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        popup.getYesButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.saveColor();
                popup.dismiss();
                overridePendingTransition(0, 0);
                finish();
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
        popup.build();

    }
}