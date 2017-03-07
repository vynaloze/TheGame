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
public class BlueMage extends Monster {

    public BlueMage(Player player){
        this.id = ID.BLUE_MAGE;
        this.player = player;
        this.speed = 2;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("blue_mage.png")));

        this.attackedTiles.add(new Coordinates(-1, -2));
        this.attackedTiles.add(new Coordinates(1, -2));
        this.attackedTiles.add(new Coordinates(0, -3));

        this.possibleSkills.add(SkillHandler.SkillList.WALL_CROSSING);
        this.possibleSkills.add(SkillHandler.SkillList.WATER_STREAM);
        this.possibleSkills.add(SkillHandler.SkillList.BLOW_OF_WIND);
        this.possibleSkills.add(SkillHandler.SkillList.HASTE);
    }


}