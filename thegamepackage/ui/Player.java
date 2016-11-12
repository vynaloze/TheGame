package thegamepackage.ui;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class Player {
    private String name;
    private String color;
    private int mana = 10;      //todo: mana 0
    private int monstersAlive = 3;
    private boolean canMove = true;

    public String getName() {
        return name;
    }

    public void setName(String s) {
        this.name = s;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String s) {
        this.color = s;
    }

    public boolean canMove() {
        return canMove;
    }

    public void setMoveValue(boolean value) {
        canMove = value;
    }

    public void modifyManaValue(int value) {
        mana += value;
    }

    public int getMana() {
        return mana;
    }

    public int getMonstersAlive() {
        return monstersAlive;
    }

    public void modifyMonstersAliveValue(int value) {
        monstersAlive += value;
    }


}
