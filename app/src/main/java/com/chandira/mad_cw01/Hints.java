package com.chandira.mad_cw01;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Hints extends AppCompatActivity {

    private int incorrectGuesses = 0;
    private int correctGuesses = 0;
    private int displayedImage;
    private int previousImage;
    private LinearLayout linearLayout;
    private Button button;
    private EditText userInput;
    private ArrayList<String> lettersInCarMake;
    private ArrayList<String> lettersGuessed;
    private final Quiz quiz = new Quiz();

    private TextView countdownText;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliSeconds = 21000;
    private boolean timerRunning;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hints);

        button = findViewById(R.id.submitCharacter);
        button.setOnClickListener(v -> handleHintButtonClick());

        countdownText = findViewById(R.id.timerText2);

        timer = new Timer(7000);
        timer.countDownTimer = new CountDownTimer(timer.getTimeLeftInMilliSeconds(), 1000) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setTimeLeftInMilliSeconds(millisUntilFinished);
                timer.updateTimer(countdownText, getColor(R.color.primaryColor), getColor(R.color.incorrect));
            }

            @Override
            public void onFinish() {
                handleHintButtonClick();
            }
        }.start();
        onCreateHelper();
    }

    // Helper method to select new image and change ImageView
    public void onCreateHelper() {
        timer.resetTimer();

        correctGuesses = 0;
        incorrectGuesses = 0;

        button = findViewById(R.id.submitCharacter);
        userInput = findViewById(R.id.textHangmanUserInput);
        linearLayout = findViewById(R.id.linearLayout);
        ImageView imageView = findViewById(R.id.imageViewCar);

        userInput.setEnabled(true);

        // Apply color state to button
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.setBackgroundTintList(
                    getApplicationContext().getResources().getColorStateList(R.color.button_color));
        }

        // Display random image
        displayedImage = quiz.returnRandomImage();
        while (previousImage == displayedImage) {
            displayedImage = quiz.returnRandomImage();
        }
        imageView.setImageResource(displayedImage);
        button.setText(R.string.submit);

        // Display a number of disabled TextViews according to the number of letters
        // in car make
        lettersInCarMake = new ArrayList<>();
        lettersGuessed = new ArrayList<>();
        int count = quiz.getCars().get(displayedImage).length();
        for (int i = 0; i < count; i++) {
            addEditText(i);
            lettersInCarMake.add(quiz.getCars().get(displayedImage).substring(i, i + 1).toUpperCase());
        }
    }

    // OnClick handler for button
    public void handleHintButtonClick() {
        if (button.getText().toString().equalsIgnoreCase("SUBMIT")) {
            ConstraintLayout layout = findViewById(R.id.hintLayout);
            userInput = findViewById(R.id.textHangmanUserInput);
            String input = userInput.getText().toString().toUpperCase();
            boolean isGuessCorrect = false;

            // Check if inputted letter exists in lettersGuessed
            // If not:
            if (!lettersGuessed.contains(input)) {
                // Find and update EditTexts if the input is correct
                for (int y = 0; y <= lettersInCarMake.size() - 1; y++) {
                    if (input.equalsIgnoreCase(lettersInCarMake.get(y))) {
                        EditText editText = findViewById(R.id.hintText + y);
                        editText.setText(lettersInCarMake.get(y).toUpperCase());
                        lettersGuessed.add(input); // Add the letter to lettersGuessed
                        correctGuesses++; // Increment no. of correct guesses
                        isGuessCorrect = true;
                    }
                }
            } else {
                // If it does exist: Do nothing
                isGuessCorrect = true;
            }

            if (!isGuessCorrect) incorrectGuesses++;

            if (correctGuesses == lettersInCarMake.size()) {
                showSnackBar(layout, "Correct!", getResources().getColor(R.color.correct));
                userInput.setEnabled(false);
                button.setText(R.string.next);
                timer.stopTimer();
            } else if (incorrectGuesses >= 3) {
                timer.stopTimer();
                button.setEnabled(false);
                Snackbar snackbar = showSnackBar(layout, "Wrong!", getResources().getColor(R.color.incorrect));
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
                        Button button = Hints.this.button;
                        button.setEnabled(true);
                    }
                });
                userInput.setEnabled(false);
                button.setText(R.string.next);
            }
            userInput.setText(""); // Clear EditText
            timer.resetTimer();
        } else {
            timer.stopTimer();
            previousImage = displayedImage;
            linearLayout.removeAllViews(); // Remove the EditTexts
            onCreateHelper();
        }
    }

    // Utility method to add an EditText
    private void addEditText(int number) {
        EditText editText = new EditText(Hints.this);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        editText.setText("-");
        editText.setEnabled(false);
        editText.setId(R.id.hintText + number);
        linearLayout.addView(editText);
    }

    // Utility method to create SnackBar
    public Snackbar showSnackBar(ConstraintLayout layout, String message, int snackBarColor) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT)
                .setTextColor(getResources().getColor(R.color.white));
        snackbar.show();
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(snackBarColor);
        return snackbar;
    }
}