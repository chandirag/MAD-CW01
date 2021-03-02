package com.chandira.mad_cw01;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

public class IdentifyCarMake extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner dropdownMenu;
    int displayedImage;
    int previousImage;
    Quiz quiz = new Quiz();
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_car_make);

        onCreateHelper();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void handleIdentify(View view) {
        ConstraintLayout layout = findViewById(R.id.identifyCarMakeLayout);
        String selectedText = dropdownMenu.getSelectedItem().toString().toLowerCase();

        if (button.getText().equals("IDENTIFY")) {
            // Check if user has selected the correct choice
            boolean answerIsCorrect = quiz.answerIsCorrect(displayedImage, selectedText);

            // Show alert accordingly
            if (answerIsCorrect) {
                int snackBarColor = getResources().getColor(R.color.correct);
                showSnackBar(layout, "Correct!", snackBarColor);
            } else {
                int snackBarColor = getResources().getColor(R.color.incorrect);
                Snackbar snackbar = showSnackBar(layout, "Wrong!", snackBarColor);
                snackbar.addCallback(new Snackbar.Callback() {
                    // Once the first SnackBar times out, display the correct answer
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        Snackbar correctAnswer = Snackbar.make(layout,
                                "The correct answer is: " + quiz.getCorrectAnswer(displayedImage).toUpperCase(),
                                Snackbar.LENGTH_SHORT)
                                .setTextColor(getResources().getColor(R.color.black));
                        correctAnswer.show();
                        View snackBarView = correctAnswer.getView();
                        snackBarView.setBackgroundColor(getResources().getColor(R.color.secondaryLightColor));
                    }
                });
            }
//            quiz.removeCarFromList(displayedImage);
            button.setText("NEXT");
        } else {
            previousImage = displayedImage;
//            finish();
//            startActivity(getIntent());
            onCreateHelper();
        }
    }

    private void onCreateHelper() {
        button = findViewById(R.id.buttonIdentifyCarMake);

        // Display random image
        ImageView imageView = findViewById(R.id.imageViewCar);
        displayedImage = quiz.returnRandomImage();
        while (previousImage == displayedImage) {
            displayedImage = quiz.returnRandomImage();
        }
        button.setText("IDENTIFY");
        imageView.setImageResource(displayedImage);

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

    public Snackbar showSnackBar(ConstraintLayout layout, String message, int snackBarColor) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT)
                .setTextColor(getResources().getColor(R.color.white));
        snackbar.show();
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(snackBarColor);
        return snackbar;
    }
}