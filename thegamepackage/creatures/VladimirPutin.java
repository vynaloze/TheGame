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
public class VladimirPutin extends Monster {

    public VladimirPutin(Player player) {
        this.id = ID.VLADIMIR_PUTIN;
        this.player = player;
        this.speed = 1;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("putin.png")));

        this.attackedTiles.add(new Coordinates(1, 0));
        this.attackedTiles.add(new Coordinates(2, 0));
        this.attackedTiles.add(new Coordinates(-1, 0));
        this.attackedTiles.add(new Coordinates(-2, 0));
        this.attackedTiles.add(new Coordinates(0, 1));
        this.attackedTiles.add(new Coordinates(0, 2));
        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(0, -2));
        this.attackedTiles.add(new Coordinates(-1, 1));
        this.attackedTiles.add(new Coordinates(1, 1));
        this.attackedTiles.add(new Coordinates(-1, -1));
        this.attackedTiles.add(new Coordinates(1, -1));

        this.possibleSkills.add(SkillHandler.SkillList.DEATH_STRIKE);
        this.possibleSkills.add(SkillHandler.SkillList.PARALYSE);
        this.possibleSkills.add(SkillHandler.SkillList.BLOW_OF_WIND);
        this.possibleSkills.add(SkillHandler.SkillList.HASTE);
    }
}
