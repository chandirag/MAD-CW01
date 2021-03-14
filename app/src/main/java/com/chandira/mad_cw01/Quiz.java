package com.chandira.mad_cw01;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Quiz {
    private final int[] carImages = {R.drawable.audi, R.drawable.bmw, R.drawable.benz, R.drawable.nissan};
    private Map<Integer, String> cars;

    public Quiz() {
        cars = new HashMap<>();
        cars.put(R.drawable.audi, "audi");
        cars.put(R.drawable.bmw, "bmw");
        cars.put(R.drawable.benz, "mercedes");
        cars.put(R.drawable.nissan, "nissan");
    }

    public Map<Integer, String> getCars() {
        return cars;
    }

    public void setCars(Map<Integer, String> cars) {
        this.cars = cars;
    }

    public int returnRandomImage() {
        Random random = new Random();
        int i = random.nextInt(carImages.length);
        return carImages[i];
    }

    public String returnRandomCarMake(Context context) {
        Random random = new Random();
        String[] carMakes = context.getResources().getStringArray(R.array.car_makes);
        int i = random.nextInt(carMakes.length);
        return carMakes[i];
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
