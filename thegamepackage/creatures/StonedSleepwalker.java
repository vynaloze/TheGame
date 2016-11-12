package thegamepackage.creatures;

import thegamepackage.ui.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class StonedSleepwalker extends Monster {

    public StonedSleepwalker(Player player) {
        this.id = ID.STONED_SLEEPWALKER;
        this.player = player;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("sleepwalker.jpg")));

        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(0, -2));
        this.attackedTiles.add(new Coordinates(0, -3));

        this.possibleSkills.add(Skills.SkillList.WALL_CROSSING);
    }
}