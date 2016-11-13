package thegamepackage.creatures;

import thegamepackage.ui.Coordinates;
import thegamepackage.ui.ID;
import thegamepackage.ui.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import thegamepackage.ui.Skills;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class DonaldTrump extends Monster {

    public DonaldTrump(Player player) {
        this.id = ID.DONALD_TRUMP;
        this.player = player;
        this.speed = 2;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("trump.png")));

        this.attackedTiles.add(new Coordinates(1, 0));
        this.attackedTiles.add(new Coordinates(2, 0));
        this.attackedTiles.add(new Coordinates(-1, 0));
        this.attackedTiles.add(new Coordinates(-2, 0));
        this.attackedTiles.add(new Coordinates(0, 1));
        this.attackedTiles.add(new Coordinates(0, 2));
        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(0, -2));

        this.possibleSkills.add(Skills.SkillList.FIREBALL);
        this.possibleSkills.add(Skills.SkillList.PARALYSE);
    }

}