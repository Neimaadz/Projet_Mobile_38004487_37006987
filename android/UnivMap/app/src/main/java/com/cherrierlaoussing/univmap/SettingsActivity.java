package com.cherrierlaoussing.univmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.model.univmap.ColorCustomPopUp;
import com.model.univmap.ItemSettings;
import com.model.univmap.ItemSettingsApdater;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private BottomNavigationView navBar;
    private ListView listView;
    private SettingsActivity activity;
    private ConstraintLayout constraintLayout;

    /// =============================== Data Persist =============================
    private SharedPreferences preferences;
    private int r,g,b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /// =============================== initialisation Data Persist =============================
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        constraintLayout = findViewById(R.id.settingsLayout);
        r = preferences.getInt("red",91);
        g = preferences.getInt("green",107);
        b = preferences.getInt("blue",128);
        constraintLayout.setBackgroundColor(Color.argb(255, r, g, b));

        // =============================== Assistance Popup =============================
        this.activity = this;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newActivity;
                switch(position){
                    case 3:
                        newActivity = new Intent(getApplicationContext(),AppearanceActivity.class);
                        startActivity(newActivity);
                        break;
                    case 4:
                        newActivity = new Intent(getApplicationContext(),LanguageActivity.class);
                        startActivity(newActivity);
                        break;
                    case 5:
                        newActivity = new Intent(getApplicationContext(), ConfidentialityActivity.class);
                        startActivity(newActivity);
                        break;
                    case 7:
                        newActivity = new Intent(getApplicationContext(),UnivMapActivity.class);
                        startActivity(newActivity);
                        break;
                    case 8:
                        AlertDialog.Builder assistancePopUp = new AlertDialog.Builder(activity);
                        assistancePopUp.setTitle(getString(R.string.assistance));
                        assistancePopUp.setMessage(getString(R.string.assistanceView_title));
                        assistancePopUp.setPositiveButton(getString(R.string.send), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("message/rfc822");
                                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"assistance@univMap.com"});
                                try {
                                    startActivity(Intent.createChooser(i, "Send mail..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        assistancePopUp.setNegativeButton(getString(R.string.back), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        assistancePopUp.show();

                        break;
                    case 10:
                        newActivity = new Intent(getApplicationContext(), LegalNoticeActivity.class);
                        startActivity(newActivity);
                        break;
                    default:
                        break;
                }
            }
        });

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