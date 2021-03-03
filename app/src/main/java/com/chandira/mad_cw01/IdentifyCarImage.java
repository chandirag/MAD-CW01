package com.chandira.mad_cw01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

public class IdentifyCarImage extends AppCompatActivity {

    Quiz quiz = new Quiz();
    int image1ResID;
    int image2ResID;
    int image3ResID;
    Button buttonNext;
    TextView textViewCarMake;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_car_image);

        onCreateHelper();
    }

    public void onCreateHelper() {
        textViewCarMake = findViewById(R.id.textCarMake);
        imageView1 = findViewById(R.id.carImage1);
        imageView2 = findViewById(R.id.carImage2);
        imageView3 = findViewById(R.id.carImage3);

        image1ResID = quiz.returnRandomImage();
        image2ResID = quiz.returnRandomImage();
        while(image1ResID == image2ResID) {
            // If image2 is the same as image1 pick new image as image2
            image2ResID = quiz.returnRandomImage();
        }
        image3ResID = quiz.returnRandomImage();
        while(image3ResID == image2ResID || image3ResID == image1ResID) {
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

        int[] selectedImageResIDs = {image1ResID, image2ResID, image3ResID};
        Random random = new Random();
        int i = random.nextInt(selectedImageResIDs.length);
        String carMake = quiz.getCorrectAnswer(selectedImageResIDs[i]).toUpperCase();
        textViewCarMake.setText(carMake);

        buttonNext = findViewById(R.id.buttonNext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            buttonNext.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.button_color));
        }
        buttonNext.setEnabled(false);

    }

    public void handleImageViewClick(View view) {
        ConstraintLayout layout = findViewById(R.id.identifyCarImageLayout);
        int imageResID = (int) view.getTag();

        boolean answerIsCorrect = quiz.answerIsCorrect(imageResID, textViewCarMake.getText().toString());

        if(answerIsCorrect) {
            int snackBarColor = getResources().getColor(R.color.correct);
            showSnackBar(layout, "Correct!", snackBarColor);
        } else {
            int snackBarColor = getResources().getColor(R.color.incorrect);
            showSnackBar(layout, "Wrong!", snackBarColor);
        }
        buttonNext.setEnabled(true);
        imageView1.setEnabled(false);
        imageView2.setEnabled(false);
        imageView3.setEnabled(false);
    }

    public void handleNext(View view) {
        onCreateHelper();
        imageView1.setEnabled(true);
        imageView2.setEnabled(true);
        imageView3.setEnabled(true);
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