package TheGamePackage.Creatures;

import TheGamePackage.Interface.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class LawnmoverOperator extends Monster {

    public LawnmoverOperator (Player player){
        id = 5;
        this.player = player;
        pic = new ImageView(new Image(getClass().getResourceAsStream("lawnmover.jpg"),98, 98, true,true));
    }



    @Override
    public void attack() {

    }
}

