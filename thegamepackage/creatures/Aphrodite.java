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
public class Aphrodite extends Monster {

    public Aphrodite(Player player) {
        this.id = MonsterID.APHRODITE;
        this.player = player;
        this.speed = 3;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("afrodyta.png")));

        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(-1, 0));
        this.attackedTiles.add(new Coordinates(1, 0));
        this.attackedTiles.add(new Coordinates(0, 1));

        this.possibleSkills.add(SkillHandler.SkillList.WALL_CROSSING);
        this.possibleSkills.add(SkillHandler.SkillList.FURY);
        this.possibleSkills.add(SkillHandler.SkillList.BLOW_OF_WIND);
    }
}