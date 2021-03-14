package com.chandira.mad_cw01;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Quiz {
    private final int[] carImages = {
            R.drawable.alfa_romeo, R.drawable.aston_martin, R.drawable.audi, R.drawable.bentley, R.drawable.benz,
            R.drawable.bmw, R.drawable.bugatti, R.drawable.cadillac, R.drawable.chevrolet, R.drawable.ferrari,
            R.drawable.fiat, R.drawable.ford, R.drawable.gmc, R.drawable.honda, R.drawable.jeep,
            R.drawable.kia, R.drawable.lamborghini, R.drawable.landrover, R.drawable.lexus, R.drawable.mazda,
            R.drawable.mclaren, R.drawable.mitsubishi, R.drawable.nissan, R.drawable.rolls_royce, R.drawable.seat,
            R.drawable.skoda, R.drawable.suzuki, R.drawable.tesla, R.drawable.toyota, R.drawable.volkswagon
    };
    private Map<Integer, String> cars;

    public Quiz() {
        cars = new HashMap<>();
        cars.put(R.drawable.alfa_romeo, "alfa romeo");
        cars.put(R.drawable.aston_martin, "aston martin");
        cars.put(R.drawable.audi, "audi");
        cars.put(R.drawable.bentley, "bentley");
        cars.put(R.drawable.bmw, "bmw");
        cars.put(R.drawable.bugatti, "bugatti");
        cars.put(R.drawable.cadillac, "cadillac");
        cars.put(R.drawable.chevrolet, "chevrolet");
        cars.put(R.drawable.ferrari, "ferrari");
        cars.put(R.drawable.fiat, "fiat");
        cars.put(R.drawable.ford, "ford");
        cars.put(R.drawable.gmc, "gmc");
        cars.put(R.drawable.honda, "honda");
        cars.put(R.drawable.jeep, "jeep");
        cars.put(R.drawable.kia, "kia");
        cars.put(R.drawable.lamborghini, "lamborghini");
        cars.put(R.drawable.landrover, "land rover");
        cars.put(R.drawable.lexus, "lexus");
        cars.put(R.drawable.mazda, "mazda");
        cars.put(R.drawable.mclaren, "mclaren");
        cars.put(R.drawable.benz, "mercedes");
        cars.put(R.drawable.mitsubishi, "mitsubishi");
        cars.put(R.drawable.nissan, "nissan");
        cars.put(R.drawable.rolls_royce, "rolls royce");
        cars.put(R.drawable.seat, "seat");
        cars.put(R.drawable.skoda, "skoda");
        cars.put(R.drawable.suzuki, "suzuki");
        cars.put(R.drawable.tesla, "tesla");
        cars.put(R.drawable.toyota, "toyota");
        cars.put(R.drawable.volkswagon, "volkswagon");
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
