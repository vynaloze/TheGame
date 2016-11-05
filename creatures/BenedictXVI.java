package thegamepackage.creatures;

import thegamepackage.ui.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class BenedictXVI extends Monster {

    public BenedictXVI (Player player){
        id = 2;
        this.player = player;
        pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("benedict.jpg"),98, 98, true,true));
    }


    @Override
    public void attack() {

    }
}
