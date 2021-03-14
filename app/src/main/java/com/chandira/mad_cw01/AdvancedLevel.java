package com.chandira.mad_cw01;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class AdvancedLevel extends AppCompatActivity {
    private final Quiz quiz = new Quiz();
    private int mImage1ResID;
    private int mImage2ResID;
    private int mImage3ResID;
    private int mSubmitCount = 0;
    private int mPoints = 0;
    private Button mButtonSubmit;
    private Button mPointsView;
    private TextView mCorrectAnswer1;
    private TextView mCorrectAnswer2;
    private TextView mCorrectAnswer3;
    private TextView mCountdownText;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_level);

        mButtonSubmit = findViewById(R.id.buttonSubmit);
        mButtonSubmit.setOnClickListener(v -> handleSubmit());

        mCountdownText = findViewById(R.id.timerText4);
        mTimer = new Timer();

        // Check the switch state and initiate timer
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
                    handleSubmit();
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

    public void onCreateHelper() {
        mTimer.resetTimer();
        mPointsView = findViewById(R.id.buttonPoints);
        mButtonSubmit = findViewById(R.id.buttonSubmit);

        ImageView imageView1 = findViewById(R.id.imageView1);
        ImageView imageView2 = findViewById(R.id.imageView2);
        ImageView imageView3 = findViewById(R.id.imageView3);

        mImage1ResID = quiz.returnRandomImage();
        mImage2ResID = quiz.returnRandomImage();
        while (mImage1ResID == mImage2ResID) {
            // If image2 is the same as image1 pick new image as image2
            mImage2ResID = quiz.returnRandomImage();
        }
        mImage3ResID = quiz.returnRandomImage();
        while (mImage3ResID == mImage2ResID || mImage3ResID == mImage1ResID) {
            // If image3 is the same as (image1 OR image2) pick new image as image3
            mImage3ResID = quiz.returnRandomImage();
        }

        // Set randomly generated images
        imageView1.setImageResource(mImage1ResID);
        imageView2.setImageResource(mImage2ResID);
        imageView3.setImageResource(mImage3ResID);

        // Set tags for imageViews so the resourceID can be fetched back later
        imageView1.setTag(mImage1ResID);
        imageView2.setTag(mImage2ResID);
        imageView3.setTag(mImage3ResID);

        // Create TextViews with the correct answer and hide it initially
        mCorrectAnswer1 = findViewById(R.id.textCorrectAnswer1);
        mCorrectAnswer2 = findViewById(R.id.textCorrectAnswer2);
        mCorrectAnswer3 = findViewById(R.id.textCorrectAnswer3);

        mCorrectAnswer1.setVisibility(View.INVISIBLE);
        mCorrectAnswer2.setVisibility(View.INVISIBLE);
        mCorrectAnswer3.setVisibility(View.INVISIBLE);

        mCorrectAnswer1.setText(Objects.requireNonNull(quiz.getCars().get(mImage1ResID)).toUpperCase());
        mCorrectAnswer2.setText(Objects.requireNonNull(quiz.getCars().get(mImage2ResID)).toUpperCase());
        mCorrectAnswer3.setText(Objects.requireNonNull(quiz.getCars().get(mImage3ResID)).toUpperCase());
    }

    // Onclick handler for the submit button
    public void handleSubmit() {
        ConstraintLayout layout = findViewById(R.id.advancedLevelLayout);
        EditText editText1 = findViewById(R.id.editText1);
        EditText editText2 = findViewById(R.id.editText2);
        EditText editText3 = findViewById(R.id.editText3);

        String userInput1 = editText1.getText().toString();
        String userInput2 = editText2.getText().toString();
        String userInput3 = editText3.getText().toString();

        if (mButtonSubmit.getText().toString().equalsIgnoreCase("SUBMIT")) {
            mTimer.resetTimer();
            mSubmitCount++;
            boolean answer1 = checkAnswer(quiz, editText1, mImage1ResID, userInput1);
            boolean answer2 = checkAnswer(quiz, editText2, mImage2ResID, userInput2);
            boolean answer3 = checkAnswer(quiz, editText3, mImage3ResID, userInput3);
            boolean[] answers = {answer1, answer2, answer3};
            TextView[] textViews = {mCorrectAnswer1, mCorrectAnswer2, mCorrectAnswer3};

            if (mSubmitCount <= 3 && (answer1 && answer2 && answer3)) {
                showSnackBar(layout, "Correct!", getResources().getColor(R.color.correct));
                mButtonSubmit.setText(R.string.next);
                for (boolean answer : answers) {
                    if (answer) mPoints++;
                }
                mTimer.stopTimer();
            } else if (mSubmitCount == 3) {
                showSnackBar(layout, "Wrong!", getResources().getColor(R.color.incorrect));
                mButtonSubmit.setText(R.string.next);
                for (int i = 0; i < answers.length; i++) {
                    if (answers[i]) {
                        mPoints++;
                    } else {
                        // If the answer is incorrect, display the correct answer
                        TextView textView = textViews[i];
                        textView.setVisibility(View.VISIBLE);
                    }
                }
                mTimer.stopTimer();
            }
            String pointsString = "Points: " + mPoints;
            mPointsView.setText(pointsString);

        } else {
            mSubmitCount = 0;
            onCreateHelper();

            // Reset the EditTexts and Submit Button text
            editText1.setText("");
            editText1.setEnabled(true);
            editText1.setTextColor(getResources().getColor(R.color.black));
            editText2.setText("");
            editText2.setEnabled(true);
            editText2.setTextColor(getResources().getColor(R.color.black));
            editText3.setText("");
            editText3.setEnabled(true);
            editText3.setTextColor(getResources().getColor(R.color.black));
            mButtonSubmit.setText(R.string.submit);
        }
    }

    // Helper method to create a SnackBar
    private Snackbar showSnackBar(ConstraintLayout layout, String message, int snackBarColor) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT)
                .setTextColor(getResources().getColor(R.color.white));
        snackbar.show();
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(snackBarColor);
        return snackbar;
    }

    // Helper method to check if answer is correct
    private boolean checkAnswer(Quiz quiz, EditText editText, int imageResID, String userInput) {
        if (userInput.trim().equals("")) return false;
        if (quiz.answerIsCorrect(imageResID, userInput.trim())) {
            editText.setEnabled(false);
            editText.setTextColor(getResources().getColor(R.color.correct));
            return true;
        } else {
            editText.setTextColor(getResources().getColor(R.color.incorrect));
            return false;
        }
    }

}