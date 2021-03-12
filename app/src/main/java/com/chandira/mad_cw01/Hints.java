package com.chandira.mad_cw01;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Hints extends AppCompatActivity {
    // TODO: Fix issue where repeating a correct letter multiple times shows "Correct!" snackBar.
    // TODO: Fix issue where pressing next before Correct answer is displayed reveals answer for the new image.

    private int incorrectGuesses = 0;
    private int correctGuesses = 0;
    private int displayedImage;
    private int previousImage;
    private LinearLayout linearLayout;
    private Button button;
    private EditText userInput;
    private ArrayList<String> lettersInCarMake;
    private final Quiz quiz = new Quiz();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hints);

        onCreateHelper();
    }

    public void onCreateHelper() {
        button = findViewById(R.id.submitCharacter);
        userInput = findViewById(R.id.textHangmanUserInput);
        userInput.setEnabled(true);
        correctGuesses = 0;
        incorrectGuesses = 0;
        linearLayout = findViewById(R.id.linearLayout);

        ImageView imageView = findViewById(R.id.imageViewCar);
        displayedImage = quiz.returnRandomImage();

        while (previousImage == displayedImage) {
            displayedImage = quiz.returnRandomImage();
        }
        imageView.setImageResource(displayedImage);
        button.setText(R.string.submit);

        lettersInCarMake = new ArrayList<>();
        int count = quiz.cars.get(displayedImage).length();
        for (int i = 0; i < count; i++) {
            addEditText(i);
            lettersInCarMake.add(quiz.cars.get(displayedImage).substring(i, i + 1).toUpperCase());
        }
    }


    public void handleHintButtonClick(View view) {
        if (button.getText().toString().equalsIgnoreCase("SUBMIT")) {
            ConstraintLayout layout = findViewById(R.id.hintLayout);
            userInput = findViewById(R.id.textHangmanUserInput);
            String input = userInput.getText().toString().toUpperCase();
            boolean isGuessCorrect = false;
            for (int y = 0; y <= lettersInCarMake.size() - 1; y++) {
                if (input.equalsIgnoreCase(lettersInCarMake.get(y))) {
                    EditText editText = findViewById(R.id.hintText + y);
                    editText.setText(lettersInCarMake.get(y).toUpperCase());
                    isGuessCorrect = true;
                    correctGuesses++;
                }
            }
            if (!isGuessCorrect) incorrectGuesses++;
            if (correctGuesses == lettersInCarMake.size()) {
                showSnackBar(layout, "Correct!", getResources().getColor(R.color.correct));
                userInput.setEnabled(false);
                button.setText(R.string.next);
            } else if (incorrectGuesses >= 3) {
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
                    }
                });
                userInput.setEnabled(false);
                button.setText(R.string.next);
            }
            userInput.setText("");
        } else {
            previousImage = displayedImage;
            linearLayout.removeAllViews();
            onCreateHelper();
        }
    }

    private void addEditText(int number) {
        EditText editText = new EditText(Hints.this);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        editText.setText("-");
        editText.setEnabled(false);
        editText.setId(R.id.hintText + number);
        linearLayout.addView(editText);
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