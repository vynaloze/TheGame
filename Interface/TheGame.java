package TheGamePackage.Interface;

import TheGamePackage.Creatures.Monster;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */

public class TheGame extends Application {

    private Tile tiles[][] = new Tile[8][8];        //todo: organise this shit
    private int howManyStones;
    private Tile previouslyClickedTile = null;

    private final int SIZE = 8;
    private final int HEIGHT = 900;
    private final int WIDTH = 1120;

    private Player p1 = new Player();
    private Player p2 = new Player();
    private Player currentlyActivePlayer = p1;

    private GameTimer timer;
    private Label timeLabel = new Label();
    private int time;
    private HBox root = new HBox();
    private VBox leftVbox = new VBox();
    private VBox rightVbox = new VBox();

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("THE GAME");
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMaxWidth(WIDTH);
        primaryStage.setMaxHeight(HEIGHT);

        readAndApplyProperties();
        drawMenu(leftVbox);             //todo: later remove, only playLocal
        drawBoard(leftVbox);
        drawSidepanel(rightVbox);
        drawTimer(rightVbox);

        root.getChildren().addAll(leftVbox, rightVbox);

        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }

    private void drawBoard(VBox vbox) {

        //****** create board
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                tiles[i][j] = new Tile(i, j);
            }
        }

        int alreadyStoned[][] = new int[howManyStones][2];

        for (int i = 0; i < howManyStones; ) {
            boolean thereIsAStoneOnThisTile = false;
            int x = ThreadLocalRandom.current().nextInt(0, 8);
            int y = ThreadLocalRandom.current().nextInt(0, 8);

            // to avoid stone in a starting corner
            if (x == 5 && y == 0) continue;
            if (x == 6 && y == 0) continue;
            if (x == 7 && y == 0) continue;
            if (x == 6 && y == 1) continue;
            if (x == 7 && y == 1) continue;
            if (x == 7 && y == 2) continue;

            if (x == 0 && y == 5) continue;
            if (x == 0 && y == 6) continue;
            if (x == 0 && y == 7) continue;
            if (x == 1 && y == 6) continue;
            if (x == 1 && y == 7) continue;
            if (x == 2 && y == 7) continue;

            //check for "doubled" position
            for (int j = 0; j < i; j++) {
                if (alreadyStoned[j][0] == x &&
                        alreadyStoned[j][1] == y)
                    thereIsAStoneOnThisTile = true;
            }

            if (!thereIsAStoneOnThisTile) {
                alreadyStoned[i][0] = x;
                alreadyStoned[i][1] = y;
                tiles[x][y].changeStoneValue();
                i++;
            }
        }

        //spawn monsters on board
        int[] alreadySpawned = new int[6];

        for (int i = 0; i < 6; ) {
            boolean thereIsSuchAMonster = false;
            int x = ThreadLocalRandom.current().nextInt(1, 7); //todo: jak wiecej monsterow to tutaj poprawic

            for (int j = 0; j < 6; j++) {
                if (x == alreadySpawned[j])
                    thereIsSuchAMonster = true;
            }

            if (!thereIsSuchAMonster) {
                alreadySpawned[i] = x;
                i++;
            }
        }
        tiles[6][0].setMonster(Monster.spawnNewMonster(alreadySpawned[0], p1));
        tiles[7][0].setMonster(Monster.spawnNewMonster(alreadySpawned[1], p1));
        tiles[7][1].setMonster(Monster.spawnNewMonster(alreadySpawned[2], p1));
        tiles[0][6].setMonster(Monster.spawnNewMonster(alreadySpawned[3], p2));
        tiles[0][7].setMonster(Monster.spawnNewMonster(alreadySpawned[4], p2));
        tiles[1][7].setMonster(Monster.spawnNewMonster(alreadySpawned[5], p2));

        //*******


        //*****create gui for the board
        //visuals
        TilePane tilepane = new TilePane();
        tilepane.setPadding(new Insets(0, 0, 10, 10));
        tilepane.setPrefColumns(SIZE);
        tilepane.setPrefRows(SIZE);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tilepane.getChildren().add(tiles[i][j].getSquare());
            }
        }
        //movement
        tilepane.setOnMouseClicked(e -> {
            int x = (int) e.getX() / 100;
            int y = (int) e.getY() / 100;
            performProperMovementAction(x, y);
        });


        vbox.getChildren().add(tilepane);
    }

    private void drawMenu(VBox vbox) {
        String s = "GOAL: Win the game\nHOW: Kill all hostile monsters or distract your opponent using yo mama jokes so he will run out of time\nWHAT ELSE: One move per turn and infinite number of attacks/skills/rotations. Oh, and you gain 2 mana nad 10 seconds.\n\nGL HF, EZ WIN";

        MenuBar menuBar = new MenuBar();

        // "new" menu
        Menu menuNew = new Menu("New");

        MenuItem newLocal = new MenuItem("Local Game");
        newLocal.setOnAction(event -> playLocal());

        MenuItem newHost = new MenuItem("Play as a host");
        newHost.setOnAction(event -> playAsHost());

        MenuItem newClient = new MenuItem("Play as a client");
        newClient.setOnAction(event -> playAsClient());

        menuNew.getItems().addAll(newLocal, newHost, newClient);

        // options + rules
        Menu menuOptions = new Menu("Options");

        MenuItem op = new MenuItem("Change...");
        op.setOnAction(event -> openOptionsWindow());

        MenuItem rules = new MenuItem("Rules");
        rules.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("bunch of rules");
            alert.setHeaderText("READ AND OBEY, MORTAL");
            alert.setContentText(s);
            alert.showAndWait();
        });

        menuOptions.getItems().addAll(op, new SeparatorMenuItem(), rules);


        menuBar.getMenus().addAll(menuNew, menuOptions);
        vbox.getChildren().addAll(menuBar);
    }

    private void drawSidepanel(VBox vbox) {
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setAlignment(Pos.TOP_CENTER);

        Button endTurnButton = new Button("END TURN");
        endTurnButton.setFont(Font.font("Bell MT", 40));
        endTurnButton.setAlignment(Pos.CENTER);
        endTurnButton.setOnAction(event -> nextTurn(vbox));
        vbox.getChildren().add(endTurnButton);

        //all info labels
        Label[] l = new Label[6];
        for (int i = 0; i < 6; i++) {
            l[i] = new Label();
            l[i].setStyle("-fx-font:25px LucidaBright ; -fx-padding: 5,5,5,5;");
        }
        l[0].setText(p1.getName() + " - " + p2.getName());
        l[0].setStyle("-fx-font:35px Impact");
        l[1].setText("Monsters alive");
        l[2].setText("3 - 3");
        l[3].setText("Mana");
        l[4].setText(p1.getMana() + " - " + p2.getMana());
        l[5].setText("TIME");
        for (int i = 0; i < 6; i++) {
            vbox.getChildren().add(l[i]);
        }
        timeLabel.setStyle("-fx-font:30px LucidaBright ; -fx-padding: 5,5,5,5;");
        vbox.getChildren().add(timeLabel);
    }

    private void drawTimer(VBox vbox) {
        timer = new GameTimer(timeLabel, time);
        timer.start();
    }

    private void playLocal() {

        readAndApplyProperties();

        root.getChildren().clear();
        leftVbox.getChildren().clear();
        rightVbox.getChildren().clear();
        timer.stop();

        currentlyActivePlayer = p1;

        //clear mana after previous game
        p1.modifyManaValue(-p1.getMana());
        p2.modifyManaValue(-p2.getMana());

        drawMenu(leftVbox);
        drawBoard(leftVbox);
        drawSidepanel(rightVbox);
        drawTimer(rightVbox);

        root.getChildren().addAll(leftVbox, rightVbox);

    }

    private void playAsHost() {
        //TODO host body

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hello :/");
        alert.setHeaderText(null);
        alert.setContentText("Not available yet");
        alert.showAndWait();
    }

    private void playAsClient() {
        //TODO client body

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hello :/");
        alert.setHeaderText(null);
        alert.setContentText("Not available yet");
        alert.showAndWait();
    }

    private void openOptionsWindow() {
        Stage stage = new Stage();
        try {                               //fxml approach, to try sth new
            Parent parent = FXMLLoader.load(getClass().getResource("options_scene.fxml"));
            stage.setTitle("Options");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(root.getScene().getWindow());
            stage.setScene(new Scene(parent, 600, 400));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readAndApplyProperties() {
        try {
            File file = new File("game.properties");
            FileInputStream fileIn = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fileIn);
            fileIn.close();

            howManyStones = Integer.parseInt(properties.getProperty("stones"));
            p1.setName(properties.getProperty("player1"));
            p2.setName(properties.getProperty("player2"));
            time = Integer.parseInt(properties.getProperty("time"));

        } catch (IOException x) {
            x.printStackTrace();
        }

    }

    private void nextTurn(VBox vbox) {
        //update kill count; later skills


        currentlyActivePlayer.setMoveValue(true);
        if (currentlyActivePlayer == p1) {
            currentlyActivePlayer = p2;
            p1.modifyManaValue(2);
        } else {
            currentlyActivePlayer = p1;
            p2.modifyManaValue(2);
        }
        vbox.getChildren().clear();
        drawSidepanel(vbox);

        timer.changePlayer();
    }

    private boolean advancedCheck(int x, int y) {
        //checking all 24 possible destinations
        int a = previouslyClickedTile.getX();
        int b = previouslyClickedTile.getY();

        //"straight lines" - distance 3
        if (a == x && b + 3 == y) {
            if (tiles[b + 2][a].isOccupied() || tiles[b + 1][a].isOccupied())
                return false;
            else
                return true;
        }
        if (a == x && b - 3 == y) {
            if (tiles[b - 2][a].isOccupied() || tiles[b - 1][a].isOccupied())
                return false;
            else
                return true;
        }
        if (a + 3 == x && b == y) {
            if (tiles[b][a + 2].isOccupied() || tiles[b][a + 1].isOccupied())
                return false;
            else
                return true;
        }
        if (a - 3 == x && b == y) {
            if (tiles[b][a - 2].isOccupied() || tiles[b][a - 1].isOccupied())
                return false;
            else
                return true;
        }
        //straight lines - dist 2
        if (a == x && b + 2 == y) {
            if (tiles[b + 1][a].isOccupied())
                return false;
            else
                return true;
        }
        if (a == x && b - 2 == y) {
            if (tiles[b - 1][a].isOccupied())
                return false;
            else
                return true;
        }
        if (a + 2 == x && b == y) {
            if (tiles[b][a + 1].isOccupied())
                return false;
            else
                return true;
        }
        if (a - 2 == x && b == y) {
            if (tiles[b][a - 1].isOccupied())
                return false;
            else
                return true;
        }
        //no need to check "straight lines - dist 1" - you cant move to occupied tile, it's already checked
        //"on corner"
        if (a + 1 == x && b + 1 == y) {
            if (tiles[b + 1][a].isOccupied() && tiles[b][a + 1].isOccupied())
                return false;
            else
                return true;
        }
        if (a - 1 == x && b - 1 == y) {
            if (tiles[b - 1][a].isOccupied() && tiles[b][a - 1].isOccupied())
                return false;
            else
                return true;
        }
        if (a - 1 == x && b + 1 == y) {
            if (tiles[b + 1][a].isOccupied() && tiles[b][a - 1].isOccupied())
                return false;
            else
                return true;
        }
        if (a + 1 == x && b - 1 == y) {
            if (tiles[b - 1][a].isOccupied() && tiles[b][a + 1].isOccupied())
                return false;
            else
                return true;
        }
        //last 8 cases
        if (a + 1 == x && b + 2 == y) {
            if ((!tiles[b + 1][a].isOccupied() && !tiles[b + 2][a].isOccupied())
                    || (!tiles[b + 1][a].isOccupied() && !tiles[b + 1][a + 1].isOccupied())
                    || (!tiles[b + 1][a + 1].isOccupied() && !tiles[b][a + 1].isOccupied()))
                return true;
            else
                return false;
        }
        if (a - 1 == x && b + 2 == y) {
            if ((!tiles[b + 1][a].isOccupied() && !tiles[b + 2][a].isOccupied())
                    || (!tiles[b + 1][a].isOccupied() && !tiles[b + 1][a - 1].isOccupied())
                    || (!tiles[b + 1][a - 1].isOccupied() && !tiles[b][a - 1].isOccupied()))
                return true;
            else
                return false;
        }
        if (a - 2 == x && b + 1 == y) {
            if ((!tiles[b][a - 1].isOccupied() && !tiles[b][a - 2].isOccupied())
                    || (!tiles[b + 1][a - 1].isOccupied() && !tiles[b][a - 1].isOccupied())
                    || (!tiles[b + 1][a - 1].isOccupied() && !tiles[b + 1][a].isOccupied()))
                return true;
            else
                return false;
        }
        if (a - 2 == x && b - 1 == y) {
            if ((!tiles[b][a - 1].isOccupied() && !tiles[b][a - 2].isOccupied())
                    || (!tiles[b - 1][a - 1].isOccupied() && !tiles[b][a - 1].isOccupied())
                    || (!tiles[b - 1][a - 1].isOccupied() && !tiles[b - 1][a].isOccupied()))
                return true;
            else
                return false;
        }
        if (a + 1 == x && b - 2 == y) {
            if ((!tiles[b - 1][a].isOccupied() && !tiles[b - 2][a].isOccupied())
                    || (!tiles[b - 1][a].isOccupied() && !tiles[b - 1][a + 1].isOccupied())
                    || (!tiles[b - 1][a + 1].isOccupied() && !tiles[b][a + 1].isOccupied()))
                return true;
            else
                return false;
        }
        if (a - 1 == x && b - 2 == y) {
            if ((!tiles[b - 1][a].isOccupied() && !tiles[b - 2][a].isOccupied())
                    || (!tiles[b - 1][a].isOccupied() && !tiles[b - 1][a - 1].isOccupied())
                    || (!tiles[b - 1][a - 1].isOccupied() && !tiles[b][a - 1].isOccupied()))
                return true;
            else
                return false;
        }
        if (a + 2 == x && b + 1 == y) {
            if ((!tiles[b][a + 1].isOccupied() && !tiles[b][a + 2].isOccupied())
                    || (!tiles[b + 1][a + 1].isOccupied() && !tiles[b][a + 1].isOccupied())
                    || (!tiles[b + 1][a + 1].isOccupied() && !tiles[b + 1][a].isOccupied()))
                return true;
            else
                return false;
        }
        if (a + 2 == x && b - 1 == y) {
            if ((!tiles[b][a + 1].isOccupied() && !tiles[b][a + 2].isOccupied())
                    || (!tiles[b - 1][a + 1].isOccupied() && !tiles[b][a + 1].isOccupied())
                    || (!tiles[b - 1][a + 1].isOccupied() && !tiles[b - 1][a].isOccupied()))
                return true;
            else
                return false;
        }

        return true;
    }

    private void performProperMovementAction(int x, int y) {
        Tile tile = tiles[y][x];

        if (previouslyClickedTile == null) {
            previouslyClickedTile = tile;
            return;
        }

        previouslyClickedTile.deselect();

        if (tile.getMonster() != null) {
            if (tile.isSelected())
                tile.deselect();
            else
                tile.select();
        } else                 //MOVING PIECE check if previously clicked tile has a monster and if player tries to move his own monster
        //and if he moved already and if there is no stone on target tile and if the target tile is within range of this monster
        //plus "advanced check" - to avoid "jumping" over stones and monsters
        {
            if (previouslyClickedTile.getMonster() != null
                    && previouslyClickedTile.getMonster().getPlayer() == currentlyActivePlayer
                    && currentlyActivePlayer.canMove()
                    && tile.isOccupied() == false
                    && abs(tile.getX() - previouslyClickedTile.getX()) + abs(tile.getY() - previouslyClickedTile.getY()) <= previouslyClickedTile.getMonster().getSpeed()
                    && advancedCheck(x, y)) {
                tile.setMonster(previouslyClickedTile.getMonster());
                previouslyClickedTile.removeMonster();
                currentlyActivePlayer.setMoveValue(false);
            }
        }
        previouslyClickedTile = tile;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
