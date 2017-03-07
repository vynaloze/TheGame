package thegamepackage.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import thegamepackage.creatures.Monster;
import thegamepackage.logic.*;
import thegamepackage.network.NetworkHandler;
import thegamepackage.util.Coordinates;
import thegamepackage.util.GameMessage;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */

public class GUIHandler extends Application implements PlayerHandlerInterface, Runnable {

    private final int SIZE = 8;
    private final int HEIGHT = 950;
    private final int WIDTH = 1250;
    private Tile tiles[][];        //todo: organise this shit

    private HBox root = new HBox();
    private VBox leftVbox = new VBox();
    private VBox rightVbox = new VBox();

    private NetworkHandler networkHandler;
    private boolean online = false;


    private TheGame theGame;
    private GameMessage currentMove, currentAttack, currentSkill, currentRotation, isTurnOver;
    private ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);

    private TileGUI tilesGUI[][] = new TileGUI[SIZE][SIZE];

    private Label timeLabel, manaLabel, monsterLabel;


    public GUIHandler(TheGame theGame) {
        this.theGame = theGame;
        this.tiles = theGame.getBoard();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tilesGUI[i][j] = new TileGUI(tiles[i][j]);
            }
        }

        try {
            this.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("THE GAME");
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMaxWidth(WIDTH);
        primaryStage.setMaxHeight(HEIGHT);

//        p1.setColor("orange");      //todo: properties
//        p2.setColor("blue");

        // readAndApplyProperties();
        createGUI(leftVbox);
        drawSidepanel(rightVbox);

        root.getChildren().addAll(leftVbox, rightVbox);

        // refreshing itself at 10FPS
        scheduler.scheduleAtFixedRate(this, 0, 100, TimeUnit.MILLISECONDS);

        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        theGame.stopGame();
        scheduler.shutdown();
    }

    //------------------------------------------
    //method to draw a board
    private void createGUI(VBox vbox) {
        // visuals
        TilePane tilepane = new TilePane();
        tilepane.setPadding(new Insets(0, 0, 10, 10));
        tilepane.setPrefColumns(SIZE);
        tilepane.setPrefRows(SIZE);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tilepane.getChildren().add(tilesGUI[i][j].getSquare());
            }
        }
        // clickability
        tilepane.setOnMouseClicked(e -> {
            int x = (int) e.getX() / 106;
            int y = (int) e.getY() / 106;
            if (e.getButton() == MouseButton.SECONDARY) {
                openActionsMenu(x, y, e);
            } else {
                highlightAttackedTiles(x, y);
                tryToMove(x, y);
                tryToUseNonInstantSkill(x, y);
            }
        });
        vbox.getChildren().add(tilepane);
    }


    //------------------------------------------
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
        endTurnButton.setOnAction(event -> nextTurn());
        vbox.getChildren().add(endTurnButton);
    }

    private void createPlayerLabel(VBox vbox) {
        HBox players = new HBox();
        Label[] m = new Label[3];
        for (int i = 0; i < 3; i++) {
            m[i] = new Label();
            m[i].setStyle("-fx-font:35px Impact ; -fx-padding: 5,5,5,5;");
        }
        m[0].setText(theGame.getPlayerOne().getName());
        m[0].setTextFill(javafx.scene.paint.Paint.valueOf(theGame.getPlayerOne().getColor()));
        m[1].setText(" - ");
        m[2].setText(theGame.getPlayerTwo().getName());
        m[2].setTextFill(javafx.scene.paint.Paint.valueOf(theGame.getPlayerTwo().getColor()));
        for (int i = 0; i < 3; i++) {
            players.getChildren().add(m[i]);
        }
        vbox.getChildren().add(players);
    }

    private void createInfoLabels(VBox vbox) {
        timeLabel = new Label();
        timeLabel.setStyle("-fx-font:30px LucidaBright ; -fx-padding: 5,5,5,5;");
        manaLabel = new Label();
        manaLabel.setStyle("-fx-font:25px LucidaBright ; -fx-padding: 5,5,5,5;");
        monsterLabel = new Label();
        monsterLabel.setStyle("-fx-font:25px LucidaBright ; -fx-padding: 5,5,5,5;");
        Label[] l = new Label[3];
        for (int i = 0; i < 3; i++) {
            l[i] = new Label();
            l[i].setStyle("-fx-font:25px LucidaBright ; -fx-padding: 5,5,5,5;");
        }
        l[0].setText("Monsters alive");
        l[1].setText("Mana");
        l[2].setText("TIME");
        vbox.getChildren().addAll(l[0], monsterLabel, l[1], manaLabel, l[2], timeLabel);
    }


    //------------------------------------------
    // refreshing GUI
    @Override
    public void run() {
        GameTimer timer = theGame.getTimer();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
                        tilesGUI[i][j].refresh();
                    }
                }
                timeLabel.setText(timer.getTimeRemaining1() / 6000 + ":" + timer.sec(timer.getTimeRemaining1() / 100) + "." + timer.milisec(timer.getTimeRemaining1())
                        + "  -  "
                        + timer.getTimeRemaining2() / 6000 + ":" + timer.sec(timer.getTimeRemaining2() / 100) + "." + timer.milisec(timer.getTimeRemaining2()));
                monsterLabel.setText(theGame.getPlayerOne().getMonstersAlive() + " - " + theGame.getPlayerTwo().getMonstersAlive());
                manaLabel.setText(theGame.getPlayerOne().getMana() + " - " + theGame.getPlayerTwo().getMana());
            }
        });

        //todo: game state if end
    }

    //------------------------------------------
    // methods to process new turn
    private void nextTurn() {
        isTurnOver = new GameMessage();
        isTurnOver.type = GameMessage.Type.ENDTURN;
    }

    public GameMessage isTurnOver() {
        return isTurnOver;
    }

    public void confirmEndTurn() {
        isTurnOver = null;
    }


    //------------------------------------------
    // methods to handle movement

    private void tryToMove(int x, int y) {
        if (currentMove != null && tiles[y][x].getMonster() == null) {
            currentMove.destX = x;
            currentMove.destY = y;
            return;
        }
        currentMove = new GameMessage();
        currentMove.srcX = x;
        currentMove.srcY = y;
        currentMove.type = GameMessage.Type.MOVE;
    }

    public GameMessage getMove() {
        return currentMove;
    }

    public void performedMove(GameMessage move) {
        currentMove = null;
    }


    //------------------------------------------
    // methods to handle attack, rotation & skills usage
    private void highlightAttackedTiles(int x, int y) {
        Tile tile = tiles[y][x];

        //remove old highlights
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tiles[i][j].setUnderAttack(false);
            }
        }
        if (tile.getMonster() == null) {
            return;
        }
        for (Coordinates coordinates : tile.getMonster().getAttackedTiles()) {
            int a = x + coordinates.getX();
            int b = y + coordinates.getY();
            if (a >= 0 && a <= 7 && b >= 0 && b <= 7) {
                tiles[b][a].setUnderAttack(true);
            }
        }
    }

    private void tryToAttack(int x, int y) {
        currentAttack = new GameMessage();
        currentAttack.srcX = x;
        currentAttack.srcY = y;
        currentAttack.type = GameMessage.Type.ATTACK;
    }

    public GameMessage getAttack() {
        return currentAttack;
    }

    public void performedAttack(GameMessage position) {
        currentAttack = null;
    }


    private void openActionsMenu(int x, int y, MouseEvent e) {
        Tile tile = tiles[y][x];
        Monster m = tile.getMonster();

        if (m != null) {
            SkillHandler skillHandler = new SkillHandler(tiles);
            skillHandler.addActiveTile(tile);
            ContextMenu cm = new ContextMenu();
            MenuItem shortInfo = new MenuItem(m.getId() + "  speed: " + m.getSpeed());
            cm.getItems().add(shortInfo);

            if (m.getPlayer() == theGame.getActivePlayer()) {
                MenuItem menuAttack = new MenuItem("Attack");
                MenuItem menuRotateR = new MenuItem("Rotate to the right", new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("rotate-right.png"))));
                MenuItem menuRotateL = new MenuItem("Rotate to the left", new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("rotate-left.png"))));
                menuAttack.setOnAction(event -> tryToAttack(x, y));
                menuRotateR.setOnAction(event -> tryToRotate(x, y, 90));
                menuRotateL.setOnAction(event -> tryToRotate(x, y, -90));
                cm.getItems().addAll(menuAttack, menuRotateR, menuRotateL);
            }

            for (SkillHandler.SkillList s : m.getPossibleSkills()) {
                MenuItem item = new MenuItem(s.toString());
                if (m.getPlayer() != theGame.getActivePlayer()) {
                    item.setStyle("-fx-text-fill: grey;");
                } else if (m.getPlayer().getMana() >= s.getCost()) {
                    item.setOnAction(ev -> tryToskill(x, y, s));
                    item.setStyle("-fx-text-fill: green;");
                } else {
                    item.setStyle("-fx-text-fill: red;");
                }
                cm.getItems().add(item);
            }

            cm.show(tilesGUI[y][x].getSquare(), e.getScreenX(), e.getScreenY());
        }
    }

    private void tryToRotate(int x, int y, int degree) {
        currentRotation = new GameMessage();
        currentRotation.srcX = x;
        currentRotation.srcY = y;
        currentRotation.rotation = degree;
        currentRotation.type = GameMessage.Type.ROTATION;
    }

    public GameMessage getRotation() {
        return currentRotation;
    }

    public void performedRotation(GameMessage position) {
        currentRotation = null;
    }


    private void tryToUseNonInstantSkill(int x, int y) {
        if (currentSkill != null && SkillHandler.SkillList.isNonInstantSkill(currentSkill.skill)) {
            currentSkill.destX = x;
            currentSkill.destY = y;
        }
    }

    private void tryToskill(int x, int y, SkillHandler.SkillList skill) {
        if (currentSkill == null) {
            currentSkill = new GameMessage();
            currentSkill.destX = -1;
        }
        currentSkill.srcX = x;
        currentSkill.srcY = y;
        currentSkill.skill = skill;
        currentSkill.type = GameMessage.Type.SKILL;
    }

    public GameMessage getSkill() {
        return currentSkill;
    }

    public void performedSkill(GameMessage position) {
        currentSkill = null;
    }


    //------------------------------------------
    // not used method from PlayerHandlerInterface
    @Override
    public GameMessage getInitialProperties() {
        return null;
    }
}
