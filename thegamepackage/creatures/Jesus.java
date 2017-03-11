package thegamepackage.creatures;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import thegamepackage.logic.Player;
import thegamepackage.logic.SkillHandler;
import thegamepackage.util.MonsterID;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class Jesus extends Monster {

    public Jesus(Player player) {
        this.id = MonsterID.JESUS;
        this.player = player;
        this.speed = 1;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("jesus.png")));

        this.possibleSkills.add(SkillHandler.SkillList.WALL_CROSSING);
        this.possibleSkills.add(SkillHandler.SkillList.DEATH_STRIKE);
        this.possibleSkills.add(SkillHandler.SkillList.PARALYSE);
        this.possibleSkills.add(SkillHandler.SkillList.BLOW_OF_WIND);
        this.possibleSkills.add(SkillHandler.SkillList.STONE_REMOVING);
        this.possibleSkills.add(SkillHandler.SkillList.PROTECTION4);
    }

}
