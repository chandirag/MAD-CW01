package com.chandira.mad_cw01;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Time;

public class IdentifyCarMake extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int displayedImage;
    private int previousImage;
    private Button button;
    private Spinner dropdownMenu;
    private final Quiz quiz = new Quiz();

    private TextView countdownText;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliSeconds = 6000;
    private boolean timerRunning;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_car_make);

        countdownText = findViewById(R.id.timerText);

//        startTimer();
        button = findViewById(R.id.buttonIdentifyCarMake);
        button.setOnClickListener(v -> handleIdentify());

        timer = new Timer();

        SharedPreferences state = getSharedPreferences("preferences", 0);
        boolean switchState = state.getBoolean("switchState", false);
        if (switchState) {
            timer.countDownTimer = new CountDownTimer(timer.getTimeLeftInMilliSeconds(), 1000) {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onTick(long millisUntilFinished) {
                    timer.setTimeLeftInMilliSeconds(millisUntilFinished);
                    timer.updateTimer(countdownText, getColor(R.color.primaryColor), getColor(R.color.incorrect));
                }

                @Override
                public void onFinish() {
                    handleIdentify();
                }
            }.start();
        } else {
            timer.countDownTimer = new CountDownTimer(timer.getTimeLeftInMilliSeconds(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {

                }
            };
            countdownText.setVisibility(View.INVISIBLE);
        }
        onCreateHelper();
    }



    // Helper method to select new image and change ImageView
    private void onCreateHelper() {
        // Reset the timer
//        countDownTimer.start();
        timer.resetTimer();

        // Apply color state to button
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.setBackgroundTintList(
                    getApplicationContext().getResources().getColorStateList(R.color.button_color));
        }

        // Display random image
        ImageView imageView = findViewById(R.id.imageViewCar);
        displayedImage = quiz.returnRandomImage();
        while (previousImage == displayedImage) {
            displayedImage = quiz.returnRandomImage();
        }
        button.setText(R.string.identify);
        imageView.setImageResource(displayedImage);

        // Create spinner
        dropdownMenu = findViewById(R.id.spinnerCarMakes);
        if (dropdownMenu != null) {
            dropdownMenu.setOnItemSelectedListener(this);
        }
        dropdownMenu.setEnabled(true);

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

    // OnClick listener for the'Identify' button
    public void handleIdentify() {
        ConstraintLayout layout = findViewById(R.id.identifyCarMakeLayout);
        String selectedText = dropdownMenu.getSelectedItem().toString().toLowerCase();

        if (button.getText().equals("IDENTIFY")) {
            // Check if user has selected the correct choice
            boolean answerIsCorrect = quiz.answerIsCorrect(displayedImage, selectedText);
            button.setText(R.string.next);

            // Show alert accordingly
            if (answerIsCorrect) {
                int snackBarColor = getResources().getColor(R.color.correct);
                showSnackBar(layout, "Correct!", snackBarColor);
                timer.stopTimer();
            } else {
                button.setEnabled(false); // Disable button until correct answer is shown
                timer.stopTimer();
                int snackBarColor = getResources().getColor(R.color.incorrect);
                Snackbar snackbar = showSnackBar(layout, "Wrong!", snackBarColor);
                snackbar.addCallback(new Snackbar.Callback() {
                    // Once the first SnackBar times out, display the correct answer
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        Snackbar correctAnswer = showSnackBar(layout,
                                "The correct answer is: " + quiz.getCorrectAnswer(displayedImage).toUpperCase(),
                                getResources().getColor(R.color.secondaryLightColor));
                        correctAnswer.setTextColor(getResources().getColor(R.color.black));
                        correctAnswer.getView().setBackgroundColor(getResources().getColor(R.color.secondaryLightColor));
                        Button button = IdentifyCarMake.this.button;
                        button.setEnabled(true); // Re-enable button
                    }
                });
            }
            dropdownMenu.setEnabled(false);
        } else {
            previousImage = displayedImage;
            onCreateHelper();
        }
    }

    // Utility method to create SnackBar
    private Snackbar showSnackBar(ConstraintLayout layout, String message, int snackBarColor) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT)
                .setTextColor(getResources().getColor(R.color.white));
        snackbar.show();
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(snackBarColor);
        return snackbar;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}