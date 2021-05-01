package com.model.univmap;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cherrierlaoussing.univmap.R;

public class ColorCustomPopUp extends Dialog {

    private LinearLayout view;
    private TextView title;
    private SeekBar seekR, seekG, seekB;
    private Button yesButton, noButton;
    private int color;
    SharedPreferences preferences;
    private int r,g,b;

    public ColorCustomPopUp(Activity activity){
        super(activity);
        setContentView(R.layout.color_popup_template);

        //================ Initialisation Data persist
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        r = preferences.getInt("red",91);
        g = preferences.getInt("green",107);
        b = preferences.getInt("blue",128);
        color = Color.argb(255, r, g, b);

        //================ Initialisation YES / No button / linearLayout
        view = findViewById(R.id.colorPopUp);
        yesButton = findViewById(R.id.saveButton);
        noButton = findViewById(R.id.backButton);
        title = findViewById(R.id.colorTextView);


        //================ Initilisation seekBar
        seekR = findViewById(R.id.seekBarR);
        seekG = findViewById(R.id.seekBarG);
        seekB = findViewById(R.id.seekBarB);
        seekR.setMax(255);
        seekG.setMax(255);
        seekB.setMax(255);

        //================
        view.setBackgroundColor(color);
        seekR.setProgress(r);
        seekG.setProgress(g);
        seekB.setProgress(b);

        //================ Ecouteur sur chaque seekBar
        seekR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                r = progress;
                color = Color.argb(255, r, g, b);
                title.setTextColor(adaptativeColor(r,g,b));
                view.setBackgroundColor(color);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        seekG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                g = progress;
                color = Color.argb(255, r, g, b);
                title.setTextColor(adaptativeColor(r,g,b));
                view.setBackgroundColor(color);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        seekB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                b = progress;
                color = Color.argb(255, r, g, b);
                title.setTextColor(adaptativeColor(r,g,b));
                view.setBackgroundColor(color);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    public void saveColor(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("red", r);
        editor.putInt("green", g);
        editor.putInt("blue",b);
        editor.commit();
    }



    public static int adaptativeColor(int rV, int gV, int bV){
        if(rV+gV+bV >= 255){
            return Color.BLACK ;
        }else{
            return Color.WHITE ;
        }
    }

    public Button getYesButton(){
        return yesButton;
    }

    public Button getNoButton(){
        return noButton;
    }

    public void build(){
        show();
    }
}
