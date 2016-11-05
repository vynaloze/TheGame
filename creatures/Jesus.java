package thegamepackage.creatures;

import thegamepackage.ui.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class Jesus extends Monster {

    public Jesus (Player player){
        id = 6;
        this.player = player;
        pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("obiwan.jpg"),98, 98, true,true));
    }


    @Override
    public void attack() {

    }
}
