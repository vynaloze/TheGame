package thegamepackage;/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import thegamepackage.gui.GUIHandler;
import thegamepackage.logic.GameConditions;
import thegamepackage.logic.PlayerHandlerInterface;
import thegamepackage.logic.TheGame;
import thegamepackage.network.NetworkHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main extends Application {

    private TheGame theGame;
    private GameConditions gameConditions;
    private Thread thread;
    private VBox root = new VBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        drawMenu();

        primaryStage.setTitle("THE GAME");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        System.exit(1);
    }

    private void drawMenu() {
        handleLocalGame();
        handleServerNetGame();
        handleClientNetGame();
        Button bOptions = new Button("Options");
        bOptions.setOnAction(e -> openOptionsWindow());

        root.getChildren().add(bOptions);
    }

    private void handleLocalGame() {
        Button bLocal = new Button("Play locally");
        bLocal.setOnAction(e -> {
            // read and apply options
            gameConditions = new GameConditions();
            theGame = new TheGame(gameConditions);

            // both players handled by one GUI instance
            PlayerHandlerInterface localHandler = new GUIHandler(theGame);
            theGame.setPlayers(localHandler, localHandler);

            thread = new Thread(theGame);
            thread.start();
        });
        root.getChildren().add(bLocal);
    }

    private void handleServerNetGame() {
        String ip;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            ip = "unfortunately unknown.";
        }
        HBox hbox = new HBox();
        Label l = new Label("Your IP address is probably:\n" + ip + "\nWrite down port number or leave it default.");
        TextField t = new TextField("13");
        Button bServer = new Button("Play as server: click to wait for client to connect.");
        hbox.getChildren().addAll(l, t, bServer);
        root.getChildren().add(hbox);

        bServer.setOnAction(e -> {
            gameConditions = new GameConditions();
            theGame = new TheGame(gameConditions);

            PlayerHandlerInterface client = new NetworkHandler(theGame, true, "", Integer.parseInt(t.getText()));
            client.sendGameConditions(gameConditions);

            PlayerHandlerInterface local = new GUIHandler(theGame);
            theGame.setPlayers(local, client);

            thread = new Thread(theGame);
            thread.start();
        });
    }

    private void handleClientNetGame() {
        HBox hbox = new HBox();
        Label l = new Label("Write IP and port:");
        TextField t1 = new TextField("localhost");
        TextField t2 = new TextField("13");
        Button bClient = new Button("Play as client: try to connect with server.");
        hbox.getChildren().addAll(l, t1, t2, bClient);
        root.getChildren().add(hbox);

        bClient.setOnAction(e -> {
            PlayerHandlerInterface server = new NetworkHandler(theGame, false, t1.getText(), Integer.parseInt(t2.getText()));
            // get options from a host
            gameConditions = server.getGameConditions();

            theGame = new TheGame(gameConditions);
            PlayerHandlerInterface local = new GUIHandler(theGame);

            theGame.setPlayers(server, local);
            thread = new Thread(theGame);
            thread.start();
        });
    }

    private void openOptionsWindow() {
        Stage stage = new Stage();
        try {                               //fxml approach, to try sth new
            Parent parent = FXMLLoader.load(getClass().getResource("./options/options_scene.fxml"));
            stage.setTitle("Options");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(root.getScene().getWindow());
            stage.setScene(new Scene(parent, 600, 400));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
