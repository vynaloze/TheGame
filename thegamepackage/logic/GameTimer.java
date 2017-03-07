package thegamepackage.logic;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class GameTimer {
    private Timer timer = new Timer();
    private int timeRemaining1, timeRemaining2, timeAdded;
    private boolean pause1 = false;
    private boolean pause2 = true;



    public GameTimer(int minutesOfGame, int secondsAddedEveryTurn) {
        this.timeRemaining1 = minutesOfGame * 6000;
        this.timeRemaining2 = minutesOfGame * 6000;
        this.timeAdded = secondsAddedEveryTurn * 100;
    }

    public String sec(int time) {
        Integer sec = time % 60;
        if (sec < 10)
            return "0" + sec;
        return sec.toString();
    }

    public String milisec(int time) {
        Integer milisec = time % 100;
        if (milisec < 10)
            return "0" + milisec;
        return milisec.toString();
    }

    public void start() {
        timer.scheduleAtFixedRate(new Update(), 0, 10);
    }

    public void changePlayer() {
        if (!pause2)
            timeRemaining2 += timeAdded;
        if (!pause1)
            timeRemaining1 += timeAdded;

        pause1 = !pause1;
        pause2 = !pause2;
    }

    public void stop() {
        timer.cancel();
    }

    public int getTimeRemaining1() {
        return timeRemaining1;
    }

    public int getTimeRemaining2() {
        return timeRemaining2;
    }

    private class Update extends TimerTask {

        @Override
        public void run() {
            if (!pause2)
                timeRemaining2--;
            if (!pause1)
                timeRemaining1--;
        }
    }
}
