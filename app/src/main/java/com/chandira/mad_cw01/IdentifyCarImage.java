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

    private Button buttonNext;
    private TextView textViewCarMake;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private final Quiz quiz = new Quiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_car_image);
        onCreateHelper();
    }

    // Helper method to select new images and change ImageViews
    public void onCreateHelper() {
        textViewCarMake = findViewById(R.id.textCarMake);
        imageView1 = findViewById(R.id.carImage1);
        imageView2 = findViewById(R.id.carImage2);
        imageView3 = findViewById(R.id.carImage3);

        int image1ResID = quiz.returnRandomImage();
        int image2ResID = quiz.returnRandomImage();
        while(image1ResID == image2ResID) {
            // If image2 is the same as image1 pick new image as image2
            image2ResID = quiz.returnRandomImage();
        }
        int image3ResID = quiz.returnRandomImage();
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

        // Select random car make from the 3 selected images and set that as a TextView
        int[] selectedImageResIDs = {image1ResID, image2ResID, image3ResID};
        Random random = new Random();
        int i = random.nextInt(selectedImageResIDs.length);
        String carMake = quiz.getCorrectAnswer(selectedImageResIDs[i]).toUpperCase();
        textViewCarMake.setText(carMake);

        buttonNext = findViewById(R.id.buttonNext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            buttonNext.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.button_color));
        }

        // Disable next button until user selects an image
        buttonNext.setEnabled(false);
    }

    // OnClick handler for the 3 clickable ImageViews
    public void handleImageViewClick(View view) {
        ConstraintLayout layout = findViewById(R.id.identifyCarImageLayout);
        int imageResID = (int) view.getTag();

        // Check whether answer is correct
        boolean answerIsCorrect = quiz.answerIsCorrect(imageResID, textViewCarMake.getText().toString());

        // Show alert accordingly
        if(answerIsCorrect) {
            showSnackBar(layout, "Correct!", getResources().getColor(R.color.correct));
        } else {
            showSnackBar(layout, "Wrong!", getResources().getColor(R.color.incorrect));
        }

        // Enable 'Next' button and disable image buttons
        buttonNext.setEnabled(true);
        imageView1.setEnabled(false);
        imageView2.setEnabled(false);
        imageView3.setEnabled(false);
    }

    // OnClick handler for 'Next' button
    public void handleNext(View view) {
        onCreateHelper();
        imageView1.setEnabled(true);
        imageView2.setEnabled(true);
        imageView3.setEnabled(true);
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