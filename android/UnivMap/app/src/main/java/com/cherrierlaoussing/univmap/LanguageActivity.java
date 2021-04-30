package com.cherrierlaoussing.univmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    /// =============================== TabBar =============================
    BottomNavigationView navBar;

    /// =============================== View and its content =============================
    ToggleButton toggleButton;
    Button englishButton, frenchButton;

    /// =============================== Data Persist =============================
    private SharedPreferences preferences;
    private Boolean defaultLanguage;
    private String  appLanguage;
    private final String DEFAULT_LANGUAGE = "defaultLanguage", APP_LANGUAGE = "appLanguage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        /// =============================== Initialization Data Persist =============================
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        defaultLanguage = preferences.getBoolean(DEFAULT_LANGUAGE, true);
        appLanguage = preferences.getString(APP_LANGUAGE, null);

        /// =============================== Initialization Button =============================
        toggleButton = findViewById(R.id.toggleButton);
        englishButton = findViewById(R.id.englishButton);
        frenchButton = findViewById(R.id.frenchButton);

        initToggleButton();

        /// =============================== ActionBar =============================
        getSupportActionBar().setTitle(getString(R.string.languages));
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

    /// =============================== Toggle Button Method =============================

    public void chooseDefaultLanguage(View v){
        //final boolean isChecked = toggleButton.isChecked();
        if(toggleButton.isChecked()){
            enableOrDesactivate(!toggleButton.isChecked());
            saveDefaultLanguage(true);
            setLocale(this,currentLanguage());
            refreshActivity();
        }else{
            enableOrDesactivate(!toggleButton.isChecked());
            saveDefaultLanguage(false);
        }
    }

    private void initToggleButton(){
        if(defaultLanguage){
            toggleButton.setChecked(true);
            enableOrDesactivate(!defaultLanguage);
        }else{
            toggleButton.setChecked(false);
            enableOrDesactivate(!defaultLanguage);
        }
    }

    /// =============================== Button Method =============================
    public void chooseFrenchLanguage(View v){
        // change language on French
        setLocale(this,"fr");
        // refresh the activity
        refreshActivity();
        // save data
        saveChoosedLanguage("fr");
    }

    public void chooseEnglishLanguage(View v){
        // change language on English
        setLocale(this,"en");
        // refresh the activity
        refreshActivity();
        // save data
        saveChoosedLanguage("en");
    }

    private void enableOrDesactivate(Boolean bool){
        englishButton.setEnabled(bool);
        frenchButton.setEnabled(bool);
    }

    private void refreshActivity(){
        overridePendingTransition(0, 0);
        finish();
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    /// =============================== Data Persist Methods =============================

    private void saveDefaultLanguage(Boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(DEFAULT_LANGUAGE, value);
        editor.commit();
    }

    private void saveChoosedLanguage(String language){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(APP_LANGUAGE, language);
        editor.commit();
    }

    /// =============================== Language Methods =============================

    // pass a new language of app (display language give in parameter)
    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        // check version API for choose methods
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, resources.getDisplayMetrics());

    }

    // get current Language (language of device)
    public static String currentLanguage(){
        return Resources.getSystem().getConfiguration().locale.getLanguage();
    }

}