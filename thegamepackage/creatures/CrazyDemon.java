package thegamepackage.creatures;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import thegamepackage.logic.Player;
import thegamepackage.logic.SkillHandler;
import thegamepackage.util.Coordinates;
import thegamepackage.util.MonsterID;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class CrazyDemon extends Monster {

    public CrazyDemon (Player player){
        this.id = MonsterID.CRAZY_DEMON;
        this.player = player;
        this.speed = 2;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("crazy_demon.png")));

        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(1, -2));
        this.attackedTiles.add(new Coordinates(-1, -2));
        this.attackedTiles.add(new Coordinates(2, -1));
        this.attackedTiles.add(new Coordinates(-2, -1));

        this.possibleSkills.add(SkillHandler.SkillList.DEATH_STRIKE);
        this.possibleSkills.add(SkillHandler.SkillList.PARALYSE);
        this.possibleSkills.add(SkillHandler.SkillList.FURY);
    }

}