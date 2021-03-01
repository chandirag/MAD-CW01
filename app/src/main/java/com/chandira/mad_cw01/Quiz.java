package com.chandira.mad_cw01;

import java.util.HashMap;
import java.util.Random;

public class Quiz {

    private final int[] carImages = {R.drawable.audi, R.drawable.bmw, R.drawable.benz, R.drawable.nissan};
    HashMap<Integer, String> cars;

    public Quiz() {
        cars = new HashMap<>();
        cars.put(R.drawable.audi, "audi");
        cars.put(R.drawable.bmw, "bmw");
        cars.put(R.drawable.benz, "mercedes");
        cars.put(R.drawable.nissan, "nissan");
    }

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
