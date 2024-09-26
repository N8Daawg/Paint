package com.example.paint.Timer;

/**
 * The type Countdown clock.
 */
public class countdownClock {
    /**
     * The Minutes.
     */
    int minutes;
    /**
     * The Seconds.
     */
    int seconds;

    /**
     * Instantiates a new Countdown clock.
     *
     * @param Minutes the minutes
     */
    public countdownClock(int Minutes){
        minutes = Minutes;
        seconds = 0;
    }

    /**
     * Count down.
     */
    public void countDown(){
        seconds -=1;
        if(seconds<0){
            seconds=59;
            minutes-=1;
        }
    }

    /**
     * Ended boolean.
     *
     * @return the boolean
     */
    public boolean ended(){
        return minutes == 0 && seconds == 0;
    }
    public String toString(){
        if(seconds<10){
            return minutes+":0"+seconds;
        } else {
            return minutes + ":" + seconds;
        }
    }
}
