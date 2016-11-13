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
public class CrazyDemon extends Monster {

    public CrazyDemon (Player player){
        this.id = ID.CRAZY_DEMON;
        this.player = player;
        this.speed = 2;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("crazy_demon.png")));

        this.attackedTiles.add(new Coordinates(0, -2));
        this.attackedTiles.add(new Coordinates(1, -3));
        this.attackedTiles.add(new Coordinates(-1, -3));
        this.attackedTiles.add(new Coordinates(2, -2));
        this.attackedTiles.add(new Coordinates(-2, -2));

        this.possibleSkills.add(Skills.SkillList.DEATH_STRIKE);
        this.possibleSkills.add(Skills.SkillList.PARALYSE);
        this.possibleSkills.add(Skills.SkillList.FURY);
    }

}