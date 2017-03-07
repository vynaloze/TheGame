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
public class HippopotamusAmphibius extends Monster {

    public HippopotamusAmphibius(Player player) {
        this.id = ID.HIPPOPOTAMUS_AMPHIBIUS;
        this.player = player;
        this.speed = 2;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("hippo.png")));

        this.attackedTiles.add(new Coordinates(-1, -1));
        this.attackedTiles.add(new Coordinates(1, -1));
        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(-1, -2));
        this.attackedTiles.add(new Coordinates(1, -2));
        this.attackedTiles.add(new Coordinates(0, -2));
        this.attackedTiles.add(new Coordinates(-1, 0));
        this.attackedTiles.add(new Coordinates(1, 0));
        this.attackedTiles.add(new Coordinates(1, 1));
        this.attackedTiles.add(new Coordinates(0, 1));
        this.attackedTiles.add(new Coordinates(-1, 1));

        this.possibleSkills.add(SkillHandler.SkillList.WATER_STREAM);
        this.possibleSkills.add(SkillHandler.SkillList.STONE_REMOVING);
        this.possibleSkills.add(SkillHandler.SkillList.STONE_PUSHING);
    }
}