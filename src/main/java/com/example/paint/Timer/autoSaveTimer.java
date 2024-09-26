package com.example.paint.Timer;

import javafx.scene.control.Label;

public class autoSaveTimer implements Runnable {
    final int minutesToMidnight = 1;
    private Label timerLabel;
    private countdownClock timer;
    public autoSaveTimer(Label TimerLabel){
        timerLabel = TimerLabel;
        timer = new countdownClock(minutesToMidnight);
    }

    public void resetTimer(){
        timer = new countdownClock(minutesToMidnight);
    }
    public void autoSave(){

    }
    public boolean ended(){
        return timer.ended();
    }

    @Override
    public void run() {
        if(timer.ended()){
            resetTimer();
        }
        timerLabel.setText("Time until AutoSave: " +timer.toString());
        timer.countDown();
    }
}
