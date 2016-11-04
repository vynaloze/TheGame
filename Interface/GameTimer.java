package TheGamePackage.Interface;

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
    private int timeRemaining1, timeRemaining2;
    private boolean pause1 = false;
    private boolean pause2 = true;


    public GameTimer(Label label, int minutes) {
        timeRemaining1 = minutes * 60;
        timeRemaining2 = minutes * 60;
        this.label = label;
    }

    private String sec (int time)
    {
        Integer sec = time % 60;
        if(sec<10)
            return "0"+sec.toString();
        return sec.toString();
    }

    public void start() {

        timer.scheduleAtFixedRate(new TimerTask() {
                                                        //TODO: it's awful piece of code, needs rewriting (swamps the event queue with millions of little Runnables)
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        label.setText(timeRemaining1 / 60 + ":" + sec(timeRemaining1) + "  -  " + timeRemaining2 / 60 + ":" + sec(timeRemaining2));

                        if (!pause2 && timeRemaining2 > 0)
                            timeRemaining2--;
                        if ((!pause1 && timeRemaining1 > 0))
                            timeRemaining1--;

                        if ((timeRemaining2 == 0 || timeRemaining1 == 0)) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Hello :/");
                            alert.setHeaderText(null);
                            alert.setContentText("GameTimer up");
                            alert.show();
                            timer.cancel();
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    public void changePlayer() {
        if (!pause2)
            timeRemaining2 += 10;
        if (!pause1)
            timeRemaining1 += 10;

        pause1 = !pause1;
        pause2 = !pause2;
    }

    public void stop()
    {
        timer.cancel();
    }
}
