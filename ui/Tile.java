package thegamepackage.ui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import thegamepackage.creatures.Monster;
import javafx.scene.layout.*;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */

public class Tile {
    private int column;
    private int row;
    private boolean occupied = false;
    private Monster monster = null;
    private boolean selected = false;
    private StackPane square = new StackPane();

    public Tile(int row, int column) {
        this.column = column;
        this.row = row;
        this.makeWhiteOrBlack(square);
        this.square.setAlignment(Pos.CENTER);
    }

    private void makeWhiteOrBlack(StackPane square) {
        String color;
        if ((row + column) % 2 == 0) {
            color = "white";
        } else {
            color = "black";
        }
        square.setStyle("-fx-background-color: " + color + ";");
    }

    public void changeStoneValue() {
        String color;
        if ((row + column) % 2 == 0) {
            color = "white";
        } else {
            color = "black";
        }
        if (occupied) {
            occupied = false;
            square.getChildren().clear();
        } else {
            occupied = true;
            square.getChildren().add(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("stone-" + color + ".jpg"), 98, 98, true, true)));
        }
    }

    public StackPane getSquare() {
        return square;
    }

    public void setMonster(Monster m) {
        monster = m;
        occupied = true;
        highlight(monster.getPlayer().getColor());
        square.getChildren().add(monster.getPic());
    }

    public void removeMonster() {
        square.getChildren().remove(monster.getPic());
        removeHighlight();
        occupied = false;
        monster = null;
    }

    public Monster getMonster() {
        return monster;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public int getX() {
        return column;
    }

    public int getY() {
        return row;
    }

    public void select()     //todo: change this
    {
        selected = true;
    }

    public void deselect() {
        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void highlight(String color)
    {
        square.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf(color),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
    }

    public void removeHighlight()
    {
        square.setBorder(null);
    }



}
