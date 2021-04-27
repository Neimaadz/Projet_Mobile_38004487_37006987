package com.cherrierlaoussing.univmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.model.univmap.ItemSettings;
import com.model.univmap.ItemSettingsApdater;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    BottomNavigationView navBar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /// =============================== List Settings =============================
        List<Object> list = new ArrayList<Object>();
        list.add(getString(R.string.account));
        list.add(new ItemSettings(getString(R.string.unavailable)));
        list.add(getString(R.string.general));
        list.add(new ItemSettings(getString(R.string.appearence)));
        list.add(new ItemSettings(getString(R.string.languages)));
        list.add(new ItemSettings(getString(R.string.confidentiality)));
        list.add(getString(R.string.learnMore));
        list.add(new ItemSettings(getString(R.string.univMap)));
        list.add(new ItemSettings(getString(R.string.assistance)));
        list.add(getString(R.string.none));
        list.add(new ItemSettings(getString(R.string.legalNotice)));

        listView = findViewById(R.id.listeSettings);
        listView.setAdapter(new ItemSettingsApdater(this,list));

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
}