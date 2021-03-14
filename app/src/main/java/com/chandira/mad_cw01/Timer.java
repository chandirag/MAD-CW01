package com.chandira.mad_cw01;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.lang.reflect.Method;

public class Timer {
    public CountDownTimer countDownTimer;
    private long timeLeftInMilliSeconds;
    private boolean timerRunning;

    public Timer(long timeLeftInMilliSeconds) {
        this.timeLeftInMilliSeconds = timeLeftInMilliSeconds;
    }

    public void setTimeLeftInMilliSeconds(long timeLeftInMilliSeconds) {
        this.timeLeftInMilliSeconds = timeLeftInMilliSeconds;
    }

    public long getTimeLeftInMilliSeconds() {
        return timeLeftInMilliSeconds;
    }

    public void stopTimer() {
        countDownTimer.cancel();
        this.timerRunning = false;
    }

    public void updateTimer(TextView textView, int primaryColor, int warningColor) {
        int seconds = (int)  timeLeftInMilliSeconds % 60000 / 1000;

        textView.setTextColor(primaryColor);
        String timeLeftText = "";
        if (seconds <= 5)
            textView.setTextColor(warningColor);
        timeLeftText += seconds;

        textView.setText(timeLeftText);
    }

    public void resetTimer() {
        countDownTimer.start();
    }
}
