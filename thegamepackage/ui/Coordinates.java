package thegamepackage.ui;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinates that = (Coordinates) o;

        if (x != that.x) return false;
        return y == that.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void rotateCoordinates(boolean toRight)
    {
        if(toRight)
        {
            int temp = x;
            x = -y;
            y = temp;
        }
        else
        {
            int temp = x;
            x = y;
            y = -temp;
        }
    }
}
