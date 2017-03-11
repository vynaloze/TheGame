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
public class Cthulhu extends Monster {

    public Cthulhu(Player player) {
        this.id = MonsterID.CTHULHU;
        this.player = player;
        this.speed = 1;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("cthulhu.jpg")));

        this.attackedTiles.add(new Coordinates(-1, -2));
        this.attackedTiles.add(new Coordinates(1, -2));
        this.attackedTiles.add(new Coordinates(0, -2));
        this.attackedTiles.add(new Coordinates(-1, -3));
        this.attackedTiles.add(new Coordinates(1, -3));
        this.attackedTiles.add(new Coordinates(0, -3));

        this.possibleSkills.add(SkillHandler.SkillList.WATER_STREAM);
        this.possibleSkills.add(SkillHandler.SkillList.BLOW_OF_WIND);
        this.possibleSkills.add(SkillHandler.SkillList.STONE_REMOVING);
        this.possibleSkills.add(SkillHandler.SkillList.STONE_PUSHING);
        this.possibleSkills.add(SkillHandler.SkillList.HASTE);
        this.possibleSkills.add(SkillHandler.SkillList.LIGHTNING);
    }
}

