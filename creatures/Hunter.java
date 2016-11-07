package thegamepackage.creatures;

import thegamepackage.ui.Coordinates;
import thegamepackage.ui.ID;
import thegamepackage.ui.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class Hunter extends Monster {

    public Hunter(Player player) {
        this.id = ID.HUNTER;
        this.player = player;
        this.speed = 3;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("hunter.png")));

        this.attackedTiles.add(new Coordinates(-1, -2));
        this.attackedTiles.add(new Coordinates(1, -2));
    }
}