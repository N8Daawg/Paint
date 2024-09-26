package paint.Timer;

import javafx.scene.control.Label;

/**
 * The type Auto save timer.
 */
public class autoSaveTimer implements Runnable {
    /**
     * The Minutes to midnight.
     */
    final int minutesToMidnight = 1;
    private Label timerLabel;
    private countdownClock timer;

    /**
     * Instantiates a new Auto save timer.
     *
     * @param TimerLabel the timer label
     */
    public autoSaveTimer(Label TimerLabel){
        timerLabel = TimerLabel;
        timer = new countdownClock(minutesToMidnight);
    }

    /**
     * Reset timer.
     */
    public void resetTimer(){
        timer = new countdownClock(minutesToMidnight);
    }

    /**
     * Auto save.
     */
    public void autoSave(){

    }

    /**
     * Ended boolean.
     *
     * @return the boolean
     */
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
