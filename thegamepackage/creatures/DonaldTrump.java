package thegamepackage.creatures;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import thegamepackage.logic.Player;
import thegamepackage.logic.SkillHandler;
import thegamepackage.util.Coordinates;
import thegamepackage.util.ID;

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

        this.possibleSkills.add(SkillHandler.SkillList.FIREBALL);
        this.possibleSkills.add(SkillHandler.SkillList.PARALYSE);
        this.possibleSkills.add(SkillHandler.SkillList.STONE_MAKING);
    }

}