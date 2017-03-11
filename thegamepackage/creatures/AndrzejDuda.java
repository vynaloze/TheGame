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
public class AndrzejDuda extends Monster {

    public AndrzejDuda(Player player) {
        this.id = MonsterID.ANDRZEJ_DUDA;
        this.player = player;
        this.speed = 2;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("andrzej.jpg")));

        this.attackedTiles.add(new Coordinates(-1, 0));
        this.attackedTiles.add(new Coordinates(1, 0));
        this.attackedTiles.add(new Coordinates(-1, -1));
        this.attackedTiles.add(new Coordinates(1, -1));
        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(0, -2));

        this.possibleSkills.add(SkillHandler.SkillList.FIREBALL);
        this.possibleSkills.add(SkillHandler.SkillList.BLOW_OF_WIND);
        this.possibleSkills.add(SkillHandler.SkillList.STONE_MAKING);
    }

}