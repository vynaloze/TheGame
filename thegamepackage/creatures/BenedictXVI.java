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
public class BenedictXVI extends Monster {

    public BenedictXVI (Player player){
        this.id = MonsterID.BENEDICT_XVI;
        this.player = player;
        this.speed = 1;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("benedict.jpg")));

        this.attackedTiles.add(new Coordinates(-1, -1));
        this.attackedTiles.add(new Coordinates(1, -1));
        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(0, -2));

        this.possibleSkills.add(SkillHandler.SkillList.DEATH_STRIKE);
        this.possibleSkills.add(SkillHandler.SkillList.PARALYSE);
        this.possibleSkills.add(SkillHandler.SkillList.STONE_MAKING);
        this.possibleSkills.add(SkillHandler.SkillList.STONE_REMOVING);
        this.possibleSkills.add(SkillHandler.SkillList.LIGHTNING);
    }

}
