package thegamepackage.options;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class OptionsController {

    @FXML
    private Button submit;
    @FXML
    private TextField stones, player1, player2, time, time_added;


    @FXML
    void initialize() {

        submit.setOnAction(e -> {
            writeProperties();
            submit.setText("Changes applied");
        });
    }

    private void writeProperties() {
        String howManyStones, nameOfPlayer1, nameOfPlayer2, timeSet, timeAdd;

        if (stones.getText().isEmpty() || Integer.parseInt(stones.getText()) > 58)
            howManyStones = "8";
        else
            howManyStones = stones.getText();

        if (player1.getText().isEmpty())
            nameOfPlayer1 = "P13RV52Y";
        else
            nameOfPlayer1 = player1.getText();

        if (player2.getText().isEmpty())
            nameOfPlayer2 = "DRU61";
        else
            nameOfPlayer2 = player2.getText();

        if (time.getText().isEmpty())
            timeSet = "3";
        else
            timeSet = time.getText();

        if (time_added.getText().isEmpty())
            timeAdd = "10";
        else
            timeAdd = time_added.getText();

        try {
            Properties properties = new Properties();
            properties.setProperty("stones", howManyStones);
            properties.setProperty("player1", nameOfPlayer1);
            properties.setProperty("player2", nameOfPlayer2);
            properties.setProperty("time", timeSet);
            properties.setProperty("time_added", timeAdd);

            File file = new File("game.properties");
            FileOutputStream fileOut = new FileOutputStream(file);
            properties.store(fileOut, "Game properties");
            fileOut.close();

        } catch (IOException x) {
            x.printStackTrace();
        }

    }

}
