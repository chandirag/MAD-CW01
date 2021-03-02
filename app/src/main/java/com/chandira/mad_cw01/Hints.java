package com.chandira.mad_cw01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Hints extends AppCompatActivity {

    int displayedImage;
    int previousImage;
    Button button;
    LinearLayout linearLayout;
    Quiz quiz = new Quiz();
    EditText userInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hints);

        linearLayout = findViewById(R.id.linearLayout);

        ImageView imageView = findViewById(R.id.imageViewCar);
        displayedImage = quiz.returnRandomImage();
//        while (previousImage == displayedImage) {
//            displayedImage = quiz.returnRandomImage();
//        }
//        button.setText(R.string.hintSubmit);
        imageView.setImageResource(displayedImage);
//
        int count = quiz.cars.get(displayedImage).length();

        for (int i = 0; i < count; i++) {
            addEditText();
        }

    }


    public void handleHintButtonClick(View view) {
        String displayedCarMake = quiz.cars.get(displayedImage);
        userInput = findViewById(R.id.textHangmanUserInput);
        String input = userInput.getText().toString().toLowerCase();

//        for (int i = 0; i < input.length(); i++) {
//            char  = input.charAt(i);
//            if
//        }

    }

    private void addEditText() {
        EditText editText = new EditText(Hints.this);
        editText.setText("H");
        editText.setEnabled(false);
        linearLayout.addView(editText);
    }
}