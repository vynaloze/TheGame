package thegamepackage.creatures;

import thegamepackage.ui.Coordinates;
import thegamepackage.ui.ID;
import thegamepackage.ui.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class LawnmoverOperator extends Monster {

    public LawnmoverOperator(Player player) {
        this.id = ID.LAWNMOVER_OPERATOR;
        this.player = player;
        this.speed = 2;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("lawnmover.jpg")));

        this.attackedTiles.add(new Coordinates(-1, -1));
        this.attackedTiles.add(new Coordinates(1, -1));
        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(1, 0));
        this.attackedTiles.add(new Coordinates(-1, 0));
        this.attackedTiles.add(new Coordinates(-1, 1));
        this.attackedTiles.add(new Coordinates(1, 1));
        this.attackedTiles.add(new Coordinates(0, 1));
    }
}
