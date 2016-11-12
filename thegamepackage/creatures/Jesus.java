package thegamepackage.creatures;

import thegamepackage.ui.ID;
import thegamepackage.ui.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import thegamepackage.ui.Skills;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class Jesus extends Monster {

    public Jesus(Player player) {
        this.id = ID.JESUS;
        this.player = player;
        this.speed = 1;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("jesus.png")));

        this.possibleSkills.add(Skills.SkillList.WALL_CROSSING);
        this.possibleSkills.add(Skills.SkillList.DEATH_STRIKE);
    }

}
