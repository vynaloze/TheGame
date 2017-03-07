package thegamepackage.gui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import thegamepackage.logic.Tile;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class TileGUI {
    private Tile tile;
    private String color;
    private StackPane square = new StackPane();

    public TileGUI(Tile tile) {
        this.tile = tile;
        this.makeWhiteOrBlack(square);
        this.square.setAlignment(Pos.CENTER);
    }


    public void refresh() {
        square.getChildren().clear();
        removeHighlight();

        if (tile.isUnderAttack()) {
            highlight("red");
        }
        if (tile.getMonster() != null) {
            square.getChildren().add(tile.getMonster().getPic());
            if (tile.getMonster().isUnderProtection()) {
                highlight("green");
            } else if (!tile.isUnderAttack()) {
                highlight(tile.getMonster().getPlayer().getColor());
            }
            return;
        }
        if (tile.getField() != null) {
            square.setStyle("-fx-background-color: " + tile.getField() + ";");
            return;
        }
        if (tile.isOccupied()) {  // this means there is a stone
            square.getChildren().add(new javafx.scene.image.ImageView(new Image(getClass().getClassLoader().getResourceAsStream("stone-" + color + ".jpg"))));
        }


    }

    private void highlight(String color) {
        square.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf(color),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
    }

    private void removeHighlight() {
        square.setBorder(null);
    }

    public StackPane getSquare() {
        return square;
    }

    private void makeWhiteOrBlack(StackPane square) {
        if ((tile.getX() + tile.getY()) % 2 == 0) {
            color = "white";
        } else {
            color = "black";
        }
        square.setStyle("-fx-background-color: " + color + ";");
    }
}
