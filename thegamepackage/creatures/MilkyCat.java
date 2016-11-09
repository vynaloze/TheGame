package thegamepackage.creatures;

import thegamepackage.ui.Coordinates;
import thegamepackage.ui.ID;
import thegamepackage.ui.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class MilkyCat extends Monster {

    public MilkyCat (Player player){
        this.id = ID.MILKY_CAT;
        this.player = player;
        this.speed = 3;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("milky_cat.png")));

        this.attackedTiles.add(new Coordinates(-1, -1));
        this.attackedTiles.add(new Coordinates(1, -1));
        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(0, -2));
    }

}