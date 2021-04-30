package com.model.univmap;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.SeekBar;

import com.cherrierlaoussing.univmap.R;

public class ColorCustomPopUp extends Dialog {

    private String title;
    private SeekBar seekR, seekG, seekB;
    private Button yesButton, noButton;

    public ColorCustomPopUp(Activity activity){
        super(activity);
        setContentView(R.layout.color_popup_template);
        seekR = findViewById(R.id.seekBarR);
        seekR.setMax(255);
        seekG = findViewById(R.id.seekBarG);
        seekB = findViewById(R.id.seekBarB);
        seekR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                System.out.println("de");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        yesButton = findViewById(R.id.saveButton);
        noButton = findViewById(R.id.backButton);
    }

    public Button getYesButton(){
        return yesButton;
    }

    public Button getNoButton(){
        return noButton;
    }

    public SeekBar getSeekG(){
        return seekG;
    }

    public void build(){
        show();
    }
}
