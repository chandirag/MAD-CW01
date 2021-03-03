package com.chandira.mad_cw01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void launchIdentifyCarMakeActivity(View view) {
        Intent intent = new Intent(this, IdentifyCarMake.class);
        startActivity(intent);
    }

    public void launchHintActivity(View view) {
        Intent intent = new Intent(this, Hints.class);
        startActivity(intent);
    }

    public void launchIdentifyCarImageActivity(View view) {
        Intent intent = new Intent(this, IdentifyCarImage.class);
        startActivity(intent);
    }

    public void launchAdvancedLevelActivity(View view) {
        Intent intent = new Intent(this, AdvancedLevel.class);
        startActivity(intent);
    }
}