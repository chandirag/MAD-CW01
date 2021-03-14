package com.chandira.mad_cw01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class MainActivity extends AppCompatActivity {
    private SwitchCompat mSimpleSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSimpleSwitch = findViewById(R.id.switch1);
        SharedPreferences state = getSharedPreferences("preferences", 0);
        boolean switchState = state.getBoolean("switchState", false);
        mSimpleSwitch.setChecked(switchState);
    }

    public void launchIdentifyCarMakeActivity(View view) {
        Intent intent = new Intent(this, IdentifyCarMake.class);
        SharedPreferences state = getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = state.edit();
        editor.putBoolean("switchState", mSimpleSwitch.isChecked());
        editor.apply();
        startActivity(intent);
    }

    public void launchHintActivity(View view) {
        Intent intent = new Intent(this, Hints.class);
        SharedPreferences state = getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = state.edit();
        editor.putBoolean("switchState", mSimpleSwitch.isChecked());
        editor.apply();
        startActivity(intent);
    }

    public void launchIdentifyCarImageActivity(View view) {
        Intent intent = new Intent(this, IdentifyCarImage.class);
        SharedPreferences state = getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = state.edit();
        editor.putBoolean("switchState", mSimpleSwitch.isChecked());
        editor.apply();
        startActivity(intent);

    }

    public void launchAdvancedLevelActivity(View view) {
        Intent intent = new Intent(this, AdvancedLevel.class);
        SharedPreferences state = getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = state.edit();
        editor.putBoolean("switchState", mSimpleSwitch.isChecked());
        editor.apply();
        startActivity(intent);

    }

}