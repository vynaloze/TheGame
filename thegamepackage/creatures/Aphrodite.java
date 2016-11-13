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
public class Aphrodite extends Monster {

    public Aphrodite(Player player) {
        this.id = ID.APHRODITE;
        this.player = player;
        this.speed = 3;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("afrodyta.png")));

        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(-1, 0));
        this.attackedTiles.add(new Coordinates(1, 0));
        this.attackedTiles.add(new Coordinates(0, 1));

        this.possibleSkills.add(Skills.SkillList.WALL_CROSSING);
        this.possibleSkills.add(Skills.SkillList.FURY);
        this.possibleSkills.add(Skills.SkillList.BLOW_OF_WIND);
    }
}