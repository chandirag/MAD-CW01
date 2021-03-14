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

public class AdvancedLevel extends AppCompatActivity {
    private int image1ResID;
    private int image2ResID;
    private int image3ResID;
    private int submitCount = 0;
    private int points = 0;
    private Button buttonSubmit;
    private Button pointsView;
    private TextView correctAnswer1;
    private TextView correctAnswer2;
    private TextView correctAnswer3;
    private final Quiz quiz = new Quiz();

    private TextView countdownText;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_level);

        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(v -> handleSubmit());

        countdownText = findViewById(R.id.timerText4);
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
                    handleSubmit();
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

    public void onCreateHelper() {
        timer.resetTimer();
        pointsView = findViewById(R.id.buttonPoints);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        ImageView imageView1 = findViewById(R.id.imageView1);
        ImageView imageView2 = findViewById(R.id.imageView2);
        ImageView imageView3 = findViewById(R.id.imageView3);

        image1ResID = quiz.returnRandomImage();
        image2ResID = quiz.returnRandomImage();
        while (image1ResID == image2ResID) {
            // If image2 is the same as image1 pick new image as image2
            image2ResID = quiz.returnRandomImage();
        }
        image3ResID = quiz.returnRandomImage();
        while (image3ResID == image2ResID || image3ResID == image1ResID) {
            // If image3 is the same as (image1 OR image2) pick new image as image3
            image3ResID = quiz.returnRandomImage();
        }

        // Set randomly generated images
        imageView1.setImageResource(image1ResID);
        imageView2.setImageResource(image2ResID);
        imageView3.setImageResource(image3ResID);

        // Set tags for imageViews so the resourceID can be fetched back later
        imageView1.setTag(image1ResID);
        imageView2.setTag(image2ResID);
        imageView3.setTag(image3ResID);

        // Create TextViews with the correct answer and hide it initially
        correctAnswer1 = findViewById(R.id.textCorrectAnswer1);
        correctAnswer2 = findViewById(R.id.textCorrectAnswer2);
        correctAnswer3 = findViewById(R.id.textCorrectAnswer3);

        correctAnswer1.setVisibility(View.INVISIBLE);
        correctAnswer2.setVisibility(View.INVISIBLE);
        correctAnswer3.setVisibility(View.INVISIBLE);

        correctAnswer1.setText(quiz.getCars().get(image1ResID).toUpperCase());
        correctAnswer2.setText(quiz.getCars().get(image2ResID).toUpperCase());
        correctAnswer3.setText(quiz.getCars().get(image3ResID).toUpperCase());
    }


    public void handleSubmit() {
        ConstraintLayout layout = findViewById(R.id.advancedLevelLayout);
        EditText editText1 = findViewById(R.id.editText1);
        EditText editText2 = findViewById(R.id.editText2);
        EditText editText3 = findViewById(R.id.editText3);

        String userInput1 = editText1.getText().toString();
        String userInput2 = editText2.getText().toString();
        String userInput3 = editText3.getText().toString();

        if (buttonSubmit.getText().toString().equalsIgnoreCase("SUBMIT")) {
            timer.resetTimer();
            submitCount++;
            boolean answer1 = checkAnswer(quiz, editText1, image1ResID, userInput1);
            boolean answer2 = checkAnswer(quiz, editText2, image2ResID, userInput2);
            boolean answer3 = checkAnswer(quiz, editText3, image3ResID, userInput3);
            boolean[] answers = {answer1, answer2, answer3};
            TextView[] textViews = {correctAnswer1, correctAnswer2, correctAnswer3};

            if (submitCount <= 3 && (answer1 && answer2 && answer3)) {
                showSnackBar(layout, "Correct!", getResources().getColor(R.color.correct));
                buttonSubmit.setText(R.string.next);
                for(boolean answer: answers) {
                    if (answer) {
                        points++;
                    }
                }
                timer.stopTimer();
            } else if (submitCount == 3) {
                showSnackBar(layout, "Wrong!", getResources().getColor(R.color.incorrect));
                buttonSubmit.setText(R.string.next);
                for(int i = 0; i < answers.length; i++) {
                    if (answers[i]) {
                        points++;
                    } else {
                        // If the answer is incorrect, display the correct answer
                        TextView textView = textViews[i];
                        textView.setVisibility(View.VISIBLE);
                    }
                }
                timer.stopTimer();
            }
            String pointsString = "Points: " + points;
            pointsView.setText(pointsString);

        } else {
            submitCount = 0;
            onCreateHelper();

            editText1.setText("");
            editText1.setEnabled(true);
            editText1.setTextColor(getResources().getColor(R.color.black));
            editText2.setText("");
            editText2.setEnabled(true);
            editText2.setTextColor(getResources().getColor(R.color.black));
            editText3.setText("");
            editText3.setEnabled(true);
            editText3.setTextColor(getResources().getColor(R.color.black));
            buttonSubmit.setText(R.string.submit);
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

    private boolean checkAnswer(Quiz quiz, EditText editText, int imageResID, String userInput) {
        if (userInput.trim().equals("")) { return false; }

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