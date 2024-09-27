package com.example.paint.Timer;

import javafx.scene.control.Label;

/**
 * The type Auto save timer.
 */
public class autoSaveTimer implements Runnable {
    /**
     * The starting time left on the countdown.
     */
    final int minutesToMidnight = 1;
    private final Label timerLabel;
    private countdownClock timer;
    private boolean showTimer;
    private boolean visibilityChanged;

    /**
     * Instantiates a new Auto save timer.
     *
     * @param TimerLabel the timer label
     */
    public autoSaveTimer(Label TimerLabel){
        timerLabel = TimerLabel;
        timer = new countdownClock(minutesToMidnight);
        showTimer = true;
        visibilityChanged = false;
    }

    /**
     * Reset timer.
     */
    public void resetTimer(){
        timer = new countdownClock(minutesToMidnight);
    }

    /**
     * Ended boolean.
     *
     * @return the boolean
     */
    public boolean ended(){
        return timer.ended();
    }

    /**
     * Set visibility.
     *
     * @param visible the visible
     */
    public void setVisibility(Boolean visible){
        if(visible){ showTimer();
        } else { hideTimer();
        }
    }
    private void showTimer(){
        showTimer = true;
        visibilityChanged = true;
    }
    private void hideTimer(){
        showTimer = false;
        visibilityChanged = true;
    }

    @Override
    public void run() {
        if(timer.ended()){
            resetTimer();
        }
        if(visibilityChanged){
            timerLabel.setVisible(showTimer);
            visibilityChanged = false;
        }
        timerLabel.setText("Time until AutoSave: " +timer.toString());
        timer.countDown();
    }
}
