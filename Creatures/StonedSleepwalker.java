package TheGamePackage.Creatures;

import TheGamePackage.Interface.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class StonedSleepwalker extends Monster {

    public StonedSleepwalker(Player player) {
        id = 1;
        this.player = player;
        pic = new ImageView(new Image(getClass().getResourceAsStream("sleepwalker.jpg"), 98, 98, true, true));
    }

    @Override
    public void attack() {

    }
}