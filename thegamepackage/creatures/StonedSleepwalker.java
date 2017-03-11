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
public class StonedSleepwalker extends Monster {

    public StonedSleepwalker(Player player) {
        this.id = MonsterID.STONED_SLEEPWALKER;
        this.player = player;
        this.speed = 2;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("sleepwalker.jpg")));

        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(0, -2));
        this.attackedTiles.add(new Coordinates(0, -3));

        this.possibleSkills.add(SkillHandler.SkillList.WALL_CROSSING);
        this.possibleSkills.add(SkillHandler.SkillList.PARALYSE);
        this.possibleSkills.add(SkillHandler.SkillList.FURY);
        this.possibleSkills.add(SkillHandler.SkillList.STONE_MAKING);
    }
}