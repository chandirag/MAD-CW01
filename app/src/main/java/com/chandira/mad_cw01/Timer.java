//package com.chandira.mad_cw01;
//
//import android.os.CountDownTimer;
//import android.widget.TextView;
//
//public class Timer {
//    private CountDownTimer countDownTimer;
//    private long timeLeftInMilliSeconds;
//    private boolean timerRunning;
//
//    public Timer(long timeLeftInMilliSeconds) {
//        this.timeLeftInMilliSeconds = timeLeftInMilliSeconds;
//    }
//
//    public void setTimeLeftInMilliSeconds(long timeLeftInMilliSeconds) {
//        this.timeLeftInMilliSeconds = timeLeftInMilliSeconds;
//    }
//
//    public long getTimeLeftInMilliSeconds() {
//        return timeLeftInMilliSeconds;
//    }
//
//    public void startTimer() {
//        countDownTimer = new CountDownTimer(timeLeftInMilliSeconds, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                timeLeftInMilliSeconds = millisUntilFinished;
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        }.start();
//    }
//
//    public void stopTimer() {
//        countDownTimer.cancel();
//        this.timerRunning = false;
//    }
//
//    public void updateTimer(TextView textView) {
//        int seconds = (int)  timeLeftInMilliSeconds % 60000 / 1000;
//
//        String timeLeftText = "";
//        if (seconds < 10) timeLeftText += "0";
//        timeLeftText += seconds;
//
//        textView.setText(timeLeftText);
//    }
//}
