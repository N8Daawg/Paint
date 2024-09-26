package com.example.paint.Timer;

public class countdownClock {
    int minutes;
    int seconds;
    public countdownClock(int Minutes){
        minutes = Minutes;
        seconds = 0;
    }
    public void countDown(){
        seconds -=1;
        if(seconds<0){
            seconds=59;
            minutes-=1;
        }
    }
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
