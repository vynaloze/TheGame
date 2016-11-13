package thegamepackage.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import thegamepackage.creatures.Monster;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */

public class TheGame extends Application {

    private final int SIZE = 8;
    private final int HEIGHT = 950;
    private final int WIDTH = 1250;
    private Tile tiles[][] = new Tile[8][8];        //todo: organise this shit
    private int howManyStones;
    private Tile previouslyClickedTile = null;
    private Player p1 = new Player();
    private Player p2 = new Player();
    private Player currentlyActivePlayer = p1;

    private GameTimer timer;
    private Label timeLabel = new Label();
    private int time, timeAdded;
    private HBox root = new HBox();
    private VBox leftVbox = new VBox();
    private VBox rightVbox = new VBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("THE GAME");
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMaxWidth(WIDTH);
        primaryStage.setMaxHeight(HEIGHT);

        p1.setColor("orange");      //todo: properties
        p2.setColor("blue");

        readAndApplyProperties();
        drawMenu(leftVbox);             //todo: later remove, only playLocal
        drawBoard(leftVbox);
        drawSidepanel(rightVbox);
        drawTimer();

        root.getChildren().addAll(leftVbox, rightVbox);

        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> timer.stop());
    }


    //methods to draw a board
    private void drawBoard(VBox vbox) {
        createNewBoard();
        spawnMonsters();
        createGUI(vbox);
    }

    private void createNewBoard() {
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
                if (alreadyStoned[j][0] == x
                        && alreadyStoned[j][1] == y) {
                    thereIsAStoneOnThisTile = true;
                }
            }

            if (!thereIsAStoneOnThisTile) {
                alreadyStoned[i][0] = x;
                alreadyStoned[i][1] = y;
                tiles[x][y].changeStoneValue();
                i++;
            }
        }
    }

    private void spawnMonsters() {
        final int NUMBER_OF_MONSTERS = ID.values().length;
        ID[] alreadySpawned = new ID[NUMBER_OF_MONSTERS];

        for (int i = 0; i < NUMBER_OF_MONSTERS; ) {
            boolean thereIsSuchAMonster = false;
            ID t = ID.randomID();

            for (int j = 0; j < NUMBER_OF_MONSTERS; j++) {
                if (t == alreadySpawned[j]) {
                    thereIsSuchAMonster = true;
                }
            }

            if (!thereIsSuchAMonster) {
                alreadySpawned[i] = t;
                i++;
            }
        }
        //just spawn the lower monsters
        tiles[6][0].setMonster(Monster.spawnNewMonster(alreadySpawned[0], p1));
        tiles[7][0].setMonster(Monster.spawnNewMonster(alreadySpawned[1], p1));
        tiles[7][1].setMonster(Monster.spawnNewMonster(alreadySpawned[2], p1));
        //spawn & rotate 180 degrees the upper monsters
        tiles[0][6].setMonster(Monster.spawnNewMonster(alreadySpawned[3], p2));
        tiles[0][6].getMonster().rotate(180);
        tiles[0][7].setMonster(Monster.spawnNewMonster(alreadySpawned[4], p2));
        tiles[0][7].getMonster().rotate(180);
        tiles[1][7].setMonster(Monster.spawnNewMonster(alreadySpawned[5], p2));
        tiles[1][7].getMonster().rotate(180);
    }

    private void createGUI(VBox vbox) {
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
            int x = (int) e.getX() / 106;
            int y = (int) e.getY() / 106;
            if (e.getButton() == MouseButton.SECONDARY) {
                openActionsMenu(x, y, e);
            } else {
                highlightAttackedTiles(x, y);
                performProperMovementAction(x, y);
            }
        });


        vbox.getChildren().add(tilepane);
    }


    //method to draw menu
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

    //methods to draw side panel
    private void drawSidepanel(VBox vbox) {
        //general properties
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setAlignment(Pos.TOP_CENTER);

        createEndTurnButton(vbox);
        createPlayerLabel(vbox);
        createInfoLabels(vbox);
    }

    private void createEndTurnButton(VBox vbox) {
        Button endTurnButton = new Button("END TURN");
        endTurnButton.setFont(Font.font("Bell MT", 40));
        endTurnButton.setAlignment(Pos.CENTER);
        endTurnButton.setOnAction(event -> nextTurn(vbox));
        vbox.getChildren().add(endTurnButton);
    }

    private void createPlayerLabel(VBox vbox) {
        HBox players = new HBox();
        Label[] m = new Label[3];
        for (int i = 0; i < 3; i++) {
            m[i] = new Label();
            m[i].setStyle("-fx-font:35px Impact ; -fx-padding: 5,5,5,5;");
        }
        m[0].setText(p1.getName());
        m[0].setTextFill(javafx.scene.paint.Paint.valueOf(p1.getColor()));
        m[1].setText(" - ");
        m[2].setText(p2.getName());
        m[2].setTextFill(javafx.scene.paint.Paint.valueOf(p2.getColor()));
        for (int i = 0; i < 3; i++) {
            players.getChildren().add(m[i]);
        }
        vbox.getChildren().add(players);
    }

    private void createInfoLabels(VBox vbox) {
        Label[] l = new Label[5];
        for (int i = 0; i < 5; i++) {
            l[i] = new Label();
            l[i].setStyle("-fx-font:25px LucidaBright ; -fx-padding: 5,5,5,5;");
        }
        l[0].setText("Monsters alive");
        l[1].setText(p1.getMonstersAlive() + " - " + p2.getMonstersAlive());
        l[2].setText("Mana");
        l[3].setText(p1.getMana() + " - " + p2.getMana());
        l[4].setText("TIME");
        for (int i = 0; i < 5; i++) {
            vbox.getChildren().add(l[i]);
        }
        timeLabel.setStyle("-fx-font:30px LucidaBright ; -fx-padding: 5,5,5,5;");
        vbox.getChildren().add(timeLabel);
    }


    //method to draw timer
    private void drawTimer() {
        timer = new GameTimer(timeLabel, time, timeAdded, p1.getName(), p2.getName());
        timer.start();
    }


    //methods to start a new game - local or network
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

        //restore monsters value
        p1.modifyMonstersAliveValue(3 - p1.getMonstersAlive());
        p2.modifyMonstersAliveValue(3 - p2.getMonstersAlive());

        drawMenu(leftVbox);
        drawBoard(leftVbox);
        drawSidepanel(rightVbox);
        drawTimer();

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


    //methods to enable changes of options
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
            timeAdded = Integer.parseInt(properties.getProperty("time_added"));

        } catch (FileNotFoundException x) {
            howManyStones = 8;
            p1.setName("P13RV52Y");
            p2.setName("DRU61");
            time = 3;
            timeAdded = 10;
        } catch (IOException x) {
            x.printStackTrace();
        }

    }


    //methods called every turn, check for winner & repaint gui
    private void nextTurn(VBox vbox) {

        checkAndAnnounceWinner();

        currentlyActivePlayer.setMoveValue(true);
        currentlyActivePlayer.setParalysed(false);

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

    private void checkAndAnnounceWinner() {
        if (p1.getMonstersAlive() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("The End");
            alert.setHeaderText("GG WP");
            alert.setContentText("I am proud to announce that our fellow player, " + p2.getName() + " has won this match because of his deeply covered bloodlust.");
            alert.showAndWait();
            timer.stop();
        }
        if (p2.getMonstersAlive() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("The End");
            alert.setHeaderText("GG WP");
            alert.setContentText("I am proud to announce that our fellow player, " + p1.getName() + " has won this match because of his deeply covered bloodlust.");
            alert.showAndWait();
            timer.stop();
        }
    }


    //methods to handle movement
    private void performProperMovementAction(int x, int y) {
        Tile tile = tiles[y][x];

        if (previouslyClickedTile == null) {
            previouslyClickedTile = tile;
            return;
        }

        if (tile.getMonster() == null && isMoveValid(x, y)) {
            tile.setMonster(previouslyClickedTile.getMonster());
            previouslyClickedTile.removeMonster();
            currentlyActivePlayer.setMoveValue(false);
        }
        previouslyClickedTile = tile;
    }

    private boolean isMoveValid(int x, int y) {
        Tile tile = tiles[y][x];
        //MOVING PIECE: check if previously clicked tile has a monster and if player tries to move his own monster
        //and if he moved already and if there is no stone on target tile and if the target tile is within range of this monster
        //plus quite complicated check to avoid "jumping" over stones and monsters

        return previouslyClickedTile.getMonster() != null
                && previouslyClickedTile.getMonster().getPlayer() == currentlyActivePlayer
                && currentlyActivePlayer.canMove()
                && tile.isOccupied() == false
                && checkIfDistanceIsValid(x, y)
                && checkIfSpecificMoveIsValid(x, y);
    }

    private boolean checkIfDistanceIsValid(int x, int y) {
        Tile tile = tiles[y][x];
        if (currentlyActivePlayer.isParalysed()) {
            return abs(tile.getX() - previouslyClickedTile.getX()) + abs(tile.getY() - previouslyClickedTile.getY()) == 1;
        } else {
            return abs(tile.getX() - previouslyClickedTile.getX()) + abs(tile.getY() - previouslyClickedTile.getY()) <= previouslyClickedTile.getMonster().getSpeed();
        }
    }

    private boolean checkIfSpecificMoveIsValid(int x, int y) {
        //if monster can jump, nothing here is important
        if(previouslyClickedTile.getMonster().getPossibleSkills().contains(Skills.SkillList.JUMPING4)){
            return true;
        }

        //if not, we check all 24 possible destinations
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


    //methods to handle attack, rotation & skills usage
    private void openActionsMenu(int x, int y, MouseEvent e) {
        Tile tile = tiles[y][x];
        Monster m = tile.getMonster();
        Skills skills = new Skills(tiles, tile);

        if (m != null) {
            ContextMenu cm = new ContextMenu();
            MenuItem shortInfo = new MenuItem(m.getId() + "  speed: " + m.getSpeed());
            cm.getItems().add(shortInfo);

            if (m.getPlayer() == currentlyActivePlayer) {
                MenuItem menuAttack = new MenuItem("Attack");
                MenuItem menuRotateR = new MenuItem("Rotate to the right", new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("rotate-right.png"))));
                MenuItem menuRotateL = new MenuItem("Rotate to the left", new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("rotate-left.png"))));
                menuAttack.setOnAction(event -> performAttack(tile));
                menuRotateR.setOnAction(event -> m.rotate(90));
                menuRotateL.setOnAction(event -> m.rotate(-90));
                cm.getItems().addAll(menuAttack, menuRotateR, menuRotateL);
            }

            for (Skills.SkillList s : m.getPossibleSkills()) {
                MenuItem item = new MenuItem(s.toString());
                if (m.getPlayer() != currentlyActivePlayer) {
                    item.setStyle("-fx-text-fill: grey;");
                } else if (m.getPlayer().getMana() >= s.getCost()) {
                    item.setOnAction(ev -> skills.useSkill(s));
                    item.setStyle("-fx-text-fill: green;");
                } else {
                    item.setStyle("-fx-text-fill: red;");
                }
                cm.getItems().add(item);
            }

            cm.show(tile.getSquare(), e.getScreenX(), e.getScreenY());
        }
    }

    private void highlightAttackedTiles(int x, int y) {
        Tile tile = tiles[y][x];

        //remove old highlights
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (tiles[i][j].getMonster() == null) {
                    tiles[i][j].removeHighlight();
                } else {
                    tiles[i][j].highlight(tiles[i][j].getMonster().getPlayer().getColor());
                }
            }
        }
        if (tile.getMonster() == null) {
            return;
        }
        for (Coordinates coordinates : tile.getMonster().getAttackedTiles()) {
            int a = x + coordinates.getX();
            int b = y + coordinates.getY();
            if (a >= 0 && a <= 7 && b >= 0 && b <= 7) {
                tiles[b][a].highlight("red");
            }
        }
    }

    private void performAttack(Tile tile) {
        for (Coordinates coordinates : tile.getMonster().getAttackedTiles()) {
            int x = tile.getX() + coordinates.getX();
            int y = tile.getY() + coordinates.getY();
            if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
                if (tiles[y][x].getMonster() != null) {
                    if (tiles[y][x].getMonster().getPlayer() != currentlyActivePlayer) {
                        tiles[y][x].getMonster().getPlayer().modifyMonstersAliveValue(-1);
                        tiles[y][x].removeMonster();
                    }
                }
            }
        }
    }


}
