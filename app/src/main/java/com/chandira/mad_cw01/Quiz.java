package com.chandira.mad_cw01;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Quiz implements Parcelable {

    private int[] carImages = {R.drawable.audi, R.drawable.bmw, R.drawable.benz, R.drawable.nissan};
    Map<Integer, String> cars;

    public Quiz() {
        cars = new HashMap<>();
        cars.put(R.drawable.audi, "audi");
        cars.put(R.drawable.bmw, "bmw");
        cars.put(R.drawable.benz, "mercedes");
        cars.put(R.drawable.nissan, "nissan");
    }


    protected Quiz(Parcel in) {
        carImages = in.createIntArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(carImages);
        dest.writeMap(cars);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    public int returnRandomImage() {
        Random random = new Random();
        int i = random.nextInt(carImages.length);
        return carImages[i];
    }

    public boolean answerIsCorrect(int displayedImage, String userChoice) {
        String carMake = cars.get(displayedImage);
        assert carMake != null;
        return carMake.equalsIgnoreCase(userChoice);
    }

    public String getCorrectAnswer(int displayedImage) {
        return cars.get(displayedImage);
    }
}
