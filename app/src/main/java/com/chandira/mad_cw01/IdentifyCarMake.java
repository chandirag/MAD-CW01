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

public class IdentifyCarMake extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final Quiz quiz = new Quiz();
    private int mDisplayedImage;
    private int mPreviousImage;
    private Button mButton;
    private Spinner mDropdownMenu;
    private TextView mCountdownText;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_car_make);

        mCountdownText = findViewById(R.id.timerText);
        mButton = findViewById(R.id.buttonIdentifyCarMake);
        mButton.setOnClickListener(v -> handleIdentify());
        mTimer = new Timer();

        // Check switch state and initiate timer
        SharedPreferences state = getSharedPreferences("preferences", 0);
        boolean switchState = state.getBoolean("switchState", false);
        if (switchState) {
            mTimer.setCountDownTimer(new CountDownTimer(mTimer.getTimeLeftInMilliSeconds(), 1000) {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimer.setTimeLeftInMilliSeconds(millisUntilFinished);
                    mTimer.updateTimer(mCountdownText, getColor(R.color.primaryColor), getColor(R.color.incorrect));
                }

                @Override
                public void onFinish() {
                    handleIdentify();
                }
            }.start());
        } else {
            mTimer.setCountDownTimer(new CountDownTimer(mTimer.getTimeLeftInMilliSeconds(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                }
            }.start());
            mCountdownText.setVisibility(View.INVISIBLE);
        }
        onCreateHelper();
    }

    // Helper method to select new image and change ImageView
    private void onCreateHelper() {
        // Reset the timer
        mTimer.resetTimer();

        // Apply color state to button
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mButton.setBackgroundTintList(
                    getApplicationContext().getResources().getColorStateList(R.color.button_color));
        }

        // Display random image
        ImageView imageView = findViewById(R.id.imageViewCar);
        mDisplayedImage = quiz.returnRandomImage();
        while (mPreviousImage == mDisplayedImage) {
            mDisplayedImage = quiz.returnRandomImage();
        }
        mButton.setText(R.string.identify);
        imageView.setImageResource(mDisplayedImage);

        // Create spinner
        mDropdownMenu = findViewById(R.id.spinnerCarMakes);
        if (mDropdownMenu != null) {
            mDropdownMenu.setOnItemSelectedListener(this);
        }
        mDropdownMenu.setEnabled(true);

        // Create ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.car_makes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appear
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        if (mDropdownMenu != null) {
            mDropdownMenu.setAdapter(adapter);
        }
    }

    // OnClick listener for the'Identify' button
    public void handleIdentify() {
        ConstraintLayout layout = findViewById(R.id.identifyCarMakeLayout);
        String selectedText = mDropdownMenu.getSelectedItem().toString().toLowerCase();

        if (mButton.getText().equals("IDENTIFY")) {
            // Check if user has selected the correct choice
            boolean answerIsCorrect = quiz.answerIsCorrect(mDisplayedImage, selectedText);
            mButton.setText(R.string.next);

            // Show alert accordingly
            if (answerIsCorrect) {
                int snackBarColor = getResources().getColor(R.color.correct);
                showSnackBar(layout, "Correct!", snackBarColor);
                mTimer.stopTimer();
            } else {
                mButton.setEnabled(false); // Disable button until correct answer is shown
                mTimer.stopTimer();
                int snackBarColor = getResources().getColor(R.color.incorrect);
                Snackbar snackbar = showSnackBar(layout, "Wrong!", snackBarColor);
                snackbar.addCallback(new Snackbar.Callback() {
                    // Once the first SnackBar times out, display the correct answer
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        Snackbar correctAnswer = showSnackBar(layout,
                                "The correct answer is: " + quiz.getCorrectAnswer(mDisplayedImage).toUpperCase(),
                                getResources().getColor(R.color.secondaryLightColor));
                        correctAnswer.setTextColor(getResources().getColor(R.color.black));
                        correctAnswer.getView().setBackgroundColor(getResources().getColor(R.color.secondaryLightColor));
                        Button button = IdentifyCarMake.this.mButton;
                        button.setEnabled(true); // Re-enable button
                    }
                });
            }
            mDropdownMenu.setEnabled(false);
        } else {
            mPreviousImage = mDisplayedImage;
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}