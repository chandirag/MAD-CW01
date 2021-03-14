package com.chandira.mad_cw01;

import android.os.CountDownTimer;
import android.widget.TextView;

public class Timer {
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliSeconds = 21000;

    public Timer() { }
    public Timer(long timeLeftInMilliSeconds) {
        this.timeLeftInMilliSeconds = timeLeftInMilliSeconds;
    }

    public long getTimeLeftInMilliSeconds() {
        return this.timeLeftInMilliSeconds;
    }

    public void setTimeLeftInMilliSeconds(long timeLeftInMilliSeconds) {
        this.timeLeftInMilliSeconds = timeLeftInMilliSeconds;
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        this.countDownTimer = countDownTimer;
    }

    public void stopTimer() {
        countDownTimer.cancel();
    }

    public void updateTimer(TextView textView, int primaryColor, int warningColor) {
        int seconds = (int) timeLeftInMilliSeconds % 60000 / 1000;

        textView.setTextColor(primaryColor);
        String timeLeftText = "";
        if (seconds <= 5)
            textView.setTextColor(warningColor);
        if (seconds < 10)
            timeLeftText += "0";
        timeLeftText += seconds;

        textView.setText(timeLeftText);
    }

    public void resetTimer() {
        countDownTimer.start();
    }
}
