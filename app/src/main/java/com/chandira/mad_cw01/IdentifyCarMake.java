package com.chandira.mad_cw01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Random;

public class IdentifyCarMake extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    HashMap<String, Integer> cars;
    Spinner dropdownMenu;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_car_make);

        int[] carImages = {R.drawable.audi, R.drawable.bmw, R.drawable.benz, R.drawable.nissan};
        Random random = new Random();
        i = random.nextInt(carImages.length);

        cars = new HashMap<>();
        cars.put("Audi", R.drawable.audi);
        cars.put("BMW", R.drawable.bmw);
        cars.put("Mercedes", R.drawable.benz);
        cars.put("Nissan", R.drawable.nissan);


        ImageView imageView = findViewById(R.id.imageViewCar);
        imageView.setImageResource(carImages[i]);

        // Create spinner
        dropdownMenu = findViewById(R.id.spinnerCarMakes);
        if (dropdownMenu != null) {
            dropdownMenu.setOnItemSelectedListener(this);
        }

        // Create ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.car_makes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appear
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        if (dropdownMenu != null) {
            dropdownMenu.setAdapter(adapter);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void handleIdentify(View view) {
//        Toast toast = Toast.makeText(getBaseContext(), "Button clicked", Toast.LENGTH_SHORT);
//        toast.show();
        String selectedText = dropdownMenu.getSelectedItem().toString();

//        if(selectedText == cars.get(i) {
//
//        }

        ConstraintLayout layout = findViewById(R.id.identifyCarMakeLayout);
        Snackbar snackbar = Snackbar.make(layout, "Hello", Snackbar.LENGTH_SHORT)
                .setTextColor(getResources().getColor(R.color.secondaryLightColor));
        snackbar.show();
    }
}