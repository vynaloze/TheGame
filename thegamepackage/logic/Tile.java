package thegamepackage.logic;

import thegamepackage.creatures.Monster;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */

public class Tile {
    private int column;
    private int row;
    private boolean occupied = false;
    private boolean underAttack = false;
    private String field = null;
    private Monster monster = null;

    public Tile(int row, int column) {
        this.column = column;
        this.row = row;
    }


    public void makeNewStone() {
        occupied = true;
    }

    public void removeStone() {
        occupied = false;
    }

    public void setMonster(Monster m) {
        monster = m;
        occupied = true;
    }

    public void removeMonster() {
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

    public String getField() {
        return field;
    }

    public void setField(String color) {
        this.field = color;
        this.occupied = true;
    }

    public boolean isUnderAttack() {
        return underAttack;
    }

    public void setUnderAttack(boolean underAttack) {
        this.underAttack = underAttack;
    }
}
