package thegamepackage.creatures;

import thegamepackage.ui.Coordinates;
import thegamepackage.ui.ID;
import thegamepackage.ui.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class VladimirPutin extends Monster {

    public VladimirPutin(Player player) {
        this.id = ID.VLADIMIR_PUTIN;
        this.player = player;
        this.speed = 1;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("putin.png")));

        this.attackedTiles.add(new Coordinates(1, 0));
        this.attackedTiles.add(new Coordinates(2, 0));
        this.attackedTiles.add(new Coordinates(-1, 0));
        this.attackedTiles.add(new Coordinates(-2, 0));
        this.attackedTiles.add(new Coordinates(0, 1));
        this.attackedTiles.add(new Coordinates(0, 2));
        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(0, -2));
        this.attackedTiles.add(new Coordinates(-1, 1));
        this.attackedTiles.add(new Coordinates(1, 1));
        this.attackedTiles.add(new Coordinates(-1, -1));
        this.attackedTiles.add(new Coordinates(1, -1));
    }
}
