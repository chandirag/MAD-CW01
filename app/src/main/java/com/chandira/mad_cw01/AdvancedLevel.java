package com.chandira.mad_cw01;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

public class AdvancedLevel extends AppCompatActivity {

    Quiz quiz = new Quiz();
    int image1ResID;
    int image2ResID;
    int image3ResID;
    Button buttonSubmit;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    EditText editText1;
    EditText editText2;
    EditText editText3;
    int submitCount = 0;
    int points = 0;
    TextView textViewPoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_level);

        onCreateHelper();
    }

    public void onCreateHelper() {
        textViewPoints = findViewById(R.id.textViewPoints);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);

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
    }


    public void handleSubmit(View view) {
        ConstraintLayout layout = findViewById(R.id.advancedLevelLayout);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);

        String userInput1 = editText1.getText().toString();
        String userInput2 = editText2.getText().toString();
        String userInput3 = editText3.getText().toString();

        if (buttonSubmit.getText().toString().equalsIgnoreCase("SUBMIT")) {
            submitCount++;
            boolean answer1 = checkAnswer(quiz, editText1, image1ResID, userInput1);
            boolean answer2 = checkAnswer(quiz, editText2, image2ResID, userInput2);
            boolean answer3 = checkAnswer(quiz, editText3, image3ResID, userInput3);
            boolean[] answers = {answer1, answer2, answer3};

            if (submitCount < 3 && (answer1 && answer2 && answer3)) {
                showSnackBar(layout, "Correct!", getResources().getColor(R.color.correct));
                buttonSubmit.setText("NEXT");
                for(boolean answer: answers) {
                    if (answer) {
                        points++;
                    }
                }
            } else if (submitCount == 3 && (answer1 && answer2 && answer3)) {
                showSnackBar(layout, "Correct!", getResources().getColor(R.color.correct));
                buttonSubmit.setText("NEXT");
                for(boolean answer: answers) {
                    if (answer) {
                        points++;
                    }
                }
            } else if (submitCount == 3) {
                showSnackBar(layout, "Wrong!", getResources().getColor(R.color.incorrect));
                buttonSubmit.setText("NEXT");
                for(boolean answer: answers) {
                    if (answer) {
                        points++;
                    }
                }
            }
            String pointsString = "" + points;
            textViewPoints.setText(pointsString);
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
            buttonSubmit.setText("SUBMIT");
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

        if (userInput.trim().equals("")) {
            return false;
        }

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