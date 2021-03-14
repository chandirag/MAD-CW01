package com.chandira.mad_cw01;

import android.content.SharedPreferences;
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

    private final Quiz quiz = new Quiz();
    private int mIncorrectGuesses = 0;
    private int mCorrectGuesses = 0;
    private int mDisplayedImage;
    private int mPreviousImage;
    private LinearLayout mLinearLayout;
    private Button mButton;
    private EditText mUserInput;
    private ArrayList<String> mLettersInCarMake;
    private ArrayList<String> mLettersGuessed;
    private TextView mCountdownText;
    private Timer mTimer;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hints);

        mButton = findViewById(R.id.submitCharacter);
        mButton.setOnClickListener(v -> handleHintButtonClick());
        mCountdownText = findViewById(R.id.timerText2);
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
                    handleHintButtonClick();
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
    public void onCreateHelper() {
        mTimer.resetTimer();

        mCorrectGuesses = 0;
        mIncorrectGuesses = 0;

        mButton = findViewById(R.id.submitCharacter);
        mUserInput = findViewById(R.id.textHangmanUserInput);
        mLinearLayout = findViewById(R.id.linearLayout);
        ImageView imageView = findViewById(R.id.imageViewCar);

        mUserInput.setEnabled(true);

        // Apply color state to button
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mButton.setBackgroundTintList(
                    getApplicationContext().getResources().getColorStateList(R.color.button_color));
        }

        // Display random image
        mDisplayedImage = quiz.returnRandomImage();
        while (mPreviousImage == mDisplayedImage) {
            mDisplayedImage = quiz.returnRandomImage();
        }
        imageView.setImageResource(mDisplayedImage);
        mButton.setText(R.string.submit);

        // Display a number of disabled TextViews according to the number of letters
        // in car make
        mLettersInCarMake = new ArrayList<>();
        mLettersGuessed = new ArrayList<>();
        int count = quiz.getCars().get(mDisplayedImage).length();
        for (int i = 0; i < count; i++) {
            addEditText(i);
            mLettersInCarMake.add(quiz.getCars().get(mDisplayedImage).substring(i, i + 1).toUpperCase());
        }
    }

    // OnClick handler for button
    public void handleHintButtonClick() {
        if (mButton.getText().toString().equalsIgnoreCase("SUBMIT")) {
            ConstraintLayout layout = findViewById(R.id.hintLayout);
            mUserInput = findViewById(R.id.textHangmanUserInput);
            String input = mUserInput.getText().toString().toUpperCase();
            boolean isGuessCorrect = false;

            // Check if inputted letter exists in lettersGuessed
            if (!mLettersGuessed.contains(input)) { // If not:
                // Find and update EditTexts if the input is correct
                for (int y = 0; y <= mLettersInCarMake.size() - 1; y++) {
                    if (input.equalsIgnoreCase(mLettersInCarMake.get(y))) {
                        EditText editText = findViewById(R.id.hintText + y);
                        editText.setText(mLettersInCarMake.get(y).toUpperCase());
                        mLettersGuessed.add(input); // Add the letter to lettersGuessed
                        mCorrectGuesses++; // Increment no. of correct guesses
                        isGuessCorrect = true;
                    }
                }
            } else {
                // If it exists: do nothing
                isGuessCorrect = true;
            }
            mTimer.resetTimer();

            if (!isGuessCorrect) {
                mIncorrectGuesses++;
                mTimer.resetTimer();
            }

            if (mCorrectGuesses == mLettersInCarMake.size()) {
                showSnackBar(layout, "Correct!", getResources().getColor(R.color.correct));
                mUserInput.setEnabled(false);
                mButton.setText(R.string.next);
                mTimer.stopTimer();
            } else if (mIncorrectGuesses >= 3) {
                mTimer.stopTimer();
                mButton.setEnabled(false);
                Snackbar snackbar = showSnackBar(layout, "Wrong!", getResources().getColor(R.color.incorrect));
                snackbar.addCallback(new Snackbar.Callback() {
                    // Once the first SnackBar times out, display the correct answer
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        Snackbar correctAnswer = Snackbar.make(layout,
                                "The correct answer is: " + quiz.getCorrectAnswer(mDisplayedImage).toUpperCase(),
                                Snackbar.LENGTH_SHORT)
                                .setTextColor(getResources().getColor(R.color.black));
                        correctAnswer.show();
                        View snackBarView = correctAnswer.getView();
                        snackBarView.setBackgroundColor(getResources().getColor(R.color.secondaryLightColor));
                        Button button = Hints.this.mButton;
                        button.setEnabled(true);
                    }
                });
                mUserInput.setEnabled(false);
                mButton.setText(R.string.next);
            }
            mUserInput.setText(""); // Clear EditText
        } else {
            mTimer.stopTimer();
            mPreviousImage = mDisplayedImage;
            mLinearLayout.removeAllViews(); // Remove the EditTexts
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
        mLinearLayout.addView(editText);
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