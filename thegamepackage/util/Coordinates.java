package thegamepackage.util;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void rotateCoordinates(int degree) {
        if (degree == 90) {
            int temp = x;
            x = -y;
            y = temp;
        }
        if (degree == 180) {
            x = -x;
            y = -y;
        }
        if (degree == -90) {
            int temp = x;
            x = y;
            y = -temp;
        }
    }
}
