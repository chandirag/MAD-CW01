package com.chandira.mad_cw01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private boolean switchState = false;
    private SwitchCompat simpleSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpleSwitch = findViewById(R.id.switch1);
        SharedPreferences state = getSharedPreferences("preferences", 0);
        boolean switchState = state.getBoolean("switchState", false);
        simpleSwitch.setChecked(switchState);
    }

    public void launchIdentifyCarMakeActivity(View view) {
        Intent intent = new Intent(this, IdentifyCarMake.class);
//        if (simpleSwitch.isChecked())
//            intent.putExtra("switchStateON", 1);
//        else
//            intent.putExtra("switchStateOFF", 0);

        SharedPreferences state = getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = state.edit();
        editor.putBoolean("switchState", simpleSwitch.isChecked());
        // Commit the edits!
        editor.commit();


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

    // Save switch state when switching between activities
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1){
//            if (resultCode == RESULT_OK) {
//                switchState = data.getBooleanExtra("switchState", false);
//                simpleSwitch.setChecked(switchState);
//            }
//        }
//    }
}