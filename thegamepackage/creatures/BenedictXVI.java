package thegamepackage.creatures;

import thegamepackage.ui.Coordinates;
import thegamepackage.ui.ID;
import thegamepackage.ui.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import thegamepackage.ui.Skills;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class BenedictXVI extends Monster {

    public BenedictXVI (Player player){
        this.id = ID.BENEDICT_XVI;
        this.player = player;
        this.speed = 1;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("benedict.jpg")));

        this.attackedTiles.add(new Coordinates(-1, -1));
        this.attackedTiles.add(new Coordinates(1, -1));
        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(0, -2));

        this.possibleSkills.add(Skills.SkillList.DEATH_STRIKE);
        this.possibleSkills.add(Skills.SkillList.PARALYSE);
        this.possibleSkills.add(Skills.SkillList.STONE_MAKING);
        this.possibleSkills.add(Skills.SkillList.STONE_REMOVING);
        this.possibleSkills.add(Skills.SkillList.LIGHTNING);
    }

}
