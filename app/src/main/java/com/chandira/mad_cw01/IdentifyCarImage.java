package com.chandira.mad_cw01;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

public class IdentifyCarImage extends AppCompatActivity {

    private final Quiz quiz = new Quiz();
    private Button mButtonNext;
    private TextView mTextViewCarMake;
    private ImageView mImageView1;
    private ImageView mImageView2;
    private ImageView mImageView3;
    private TextView mCountdownText;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_car_image);

        mButtonNext = findViewById(R.id.submitCharacter);
        mImageView1 = findViewById(R.id.carImage1);
        mImageView2 = findViewById(R.id.carImage2);
        mImageView3 = findViewById(R.id.carImage3);

        ConstraintLayout layout = findViewById(R.id.identifyCarImageLayout);
        mCountdownText = findViewById(R.id.timerText3);
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
                    showSnackBar(layout, "You ran out of time!", getResources().getColor(R.color.incorrect));
                    mButtonNext.setEnabled(true);
                    setEnabledImageView(false, mImageView1, mImageView2, mImageView3);
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

    // Helper method to select new images and change ImageViews
    public void onCreateHelper() {
        mTimer.resetTimer();
        mTextViewCarMake = findViewById(R.id.textCarMake);

        int image1ResID = quiz.returnRandomImage();
        int image2ResID = quiz.returnRandomImage();
        while (image1ResID == image2ResID) {
            // If image2 is the same as image1 pick new image as image2
            image2ResID = quiz.returnRandomImage();
        }
        int image3ResID = quiz.returnRandomImage();
        while (image3ResID == image2ResID || image3ResID == image1ResID) {
            // If image3 is the same as (image1 OR image2) pick new image as image3
            image3ResID = quiz.returnRandomImage();
        }

        // Set randomly generated images
        mImageView1.setImageResource(image1ResID);
        mImageView2.setImageResource(image2ResID);
        mImageView3.setImageResource(image3ResID);

        // Set tags for imageViews so the resourceID can be fetched back later
        mImageView1.setTag(image1ResID);
        mImageView2.setTag(image2ResID);
        mImageView3.setTag(image3ResID);

        // Select random car make from the 3 selected images and set that as a TextView
        int[] selectedImageResIDs = {image1ResID, image2ResID, image3ResID};
        Random random = new Random();
        int i = random.nextInt(selectedImageResIDs.length);
        String carMake = quiz.getCorrectAnswer(selectedImageResIDs[i]).toUpperCase();
        mTextViewCarMake.setText(carMake);

        mButtonNext = findViewById(R.id.buttonNext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mButtonNext.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.button_color));
        }

        // Disable next button until user selects an image
        mButtonNext.setEnabled(false);
    }

    // OnClick handler for the 3 clickable ImageViews
    public void handleImageViewClick(View view) {
        mTimer.stopTimer();
        ConstraintLayout layout = findViewById(R.id.identifyCarImageLayout);
        int imageResID = (int) view.getTag();

        // Check whether answer is correct
        boolean answerIsCorrect = quiz.answerIsCorrect(imageResID, mTextViewCarMake.getText().toString());

        // Show alert accordingly
        if (answerIsCorrect) {
            showSnackBar(layout, "Correct!", getResources().getColor(R.color.correct));
        } else {
            showSnackBar(layout, "Wrong!", getResources().getColor(R.color.incorrect));
        }

        // Enable 'Next' button and disable image buttons
        mButtonNext.setEnabled(true);
        setEnabledImageView(false, mImageView1, mImageView2, mImageView3);
    }

    // OnClick handler for 'Next' button
    public void handleNext(View view) {
        onCreateHelper();
        setEnabledImageView(true, mImageView1, mImageView2, mImageView3);
    }

    // Helper method to enable/disable ImageViews
    private void setEnabledImageView(boolean state, ImageView... imageViews) {
        for (ImageView imageView : imageViews) {
            imageView.setEnabled(state);
        }
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