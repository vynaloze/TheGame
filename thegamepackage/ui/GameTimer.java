package thegamepackage.ui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class GameTimer {
    private Timer timer = new Timer();
    private Label label;
    private int timeRemaining1, timeRemaining2, timeAdded;
    private boolean pause1 = false;
    private boolean pause2 = true;
    private String p1, p2;


    public GameTimer(Label label, int minutesOfGame, int secondsAddedEveryTurn, String p1, String p2) {
        this.timeRemaining1 = minutesOfGame * 60000;
        this.timeRemaining2 = minutesOfGame * 60000;
        this.timeAdded = secondsAddedEveryTurn * 1000;
        this.label = label;
        this.p1 = p1;
        this.p2 = p2;
    }

    private String sec(int time) {
        Integer sec = time % 60;
        if (sec < 10)
            return "0" + sec;
        return sec.toString();
    }

    private String milisec(int time) {
        Integer milisec = time % 1000;
        if(milisec<10)
            return "00"+milisec;
        if(milisec<100)
            return "0"+ milisec;
        return milisec.toString();
    }

    public void start() {
        Runnable runnable = new UpdateAndCheckForWinner();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(runnable);
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, 1);
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

    private class UpdateAndCheckForWinner implements Runnable {

        @Override
        public void run() {
            label.setText(timeRemaining1 / 60000 + ":" + sec(timeRemaining1/1000) + ":" + milisec(timeRemaining1)
                    + "  -  "
                    + timeRemaining2 / 60000 + ":" + sec(timeRemaining2/1000) + ":" + milisec(timeRemaining2));

            if (!pause2 && timeRemaining2 > 0)
                timeRemaining2--;
            if ((!pause1 && timeRemaining1 > 0))
                timeRemaining1--;

            if ((timeRemaining2 == 0 || timeRemaining1 == 0)) {
                timer.cancel();
                announceWinner();
            }
        }

        private void announceWinner() {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("The End");
            alert.setHeaderText("GG WP");
            if (timeRemaining1 == 0) {
                alert.setContentText("I am proud to announce that our fellow player, " + p2 + " has won this match because of his fast clicks.");
            } else {
                alert.setContentText("I am proud to announce that our fellow player, " + p1 + " has won this match because of his fast clicks.");
            }
            alert.showAndWait();
        }
    }
}
