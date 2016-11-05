package thegamepackage.creatures;

import thegamepackage.ui.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class AndrzejDuda extends Monster {

    public AndrzejDuda (Player player){
        id = 3;
        this.player = player;
        pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("andrzej.jpg"),98, 98, true,true));
    }

    @Override
    public void attack() {

    }
}