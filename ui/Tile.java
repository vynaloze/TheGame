package thegamepackage.ui;

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

    private Pane square = new Pane();

    public Tile(int row, int column) {
        this.column = column;
        this.row = row;
        makeWhiteOrBlack(square);

    }

    private void makeWhiteOrBlack(Pane square) {
        String color;
        if ((row + column) % 2 == 0) {
            color = "white";
        } else {
            color = "black";
        }
        square.setStyle("-fx-background-color: " + color + ";");
    }

    public void changeStoneValue() {

        if (occupied) {
            occupied = false;
            this.makeWhiteOrBlack(square);
        } else {
            occupied = true;
            square.setStyle("-fx-background-color: blue;");
        }
    }

    public Pane getSquare() {
        return square;
    }

    public void setMonster(Monster m)
    {
        monster = m;
        occupied = true;
        square.getChildren().add(monster.getPic());
    }

    public void removeMonster()
    {
       square.getChildren().remove(monster.getPic());
        occupied = false;
        monster = null;
    }

    public Monster getMonster() {
        return monster;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public int getX()
    {
        return column;
    }
    public int getY()
    {
        return row;
    }

   public void select()     //todo: change this
    {
        square.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        selected = true;
    }

    public void deselect()
    {
        square.setBorder(null);
        selected = false;
    }

    public boolean isSelected()
    {
        return selected;
    }

}
