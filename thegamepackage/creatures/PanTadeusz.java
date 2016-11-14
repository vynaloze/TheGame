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
public class PanTadeusz extends Monster {

    public PanTadeusz(Player player) {
        this.id = ID.PAN_TADEUSZ;
        this.player = player;
        this.speed = 2;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("pan_tadeusz.png")));

        this.attackedTiles.add(new Coordinates(0, -3));
        this.attackedTiles.add(new Coordinates(-1, -2));
        this.attackedTiles.add(new Coordinates(1, -2));
        this.attackedTiles.add(new Coordinates(0, -2));
        this.attackedTiles.add(new Coordinates(-1, -1));
        this.attackedTiles.add(new Coordinates(1, -1));
        this.attackedTiles.add(new Coordinates(0, -1));

        this.possibleSkills.add(Skills.SkillList.FURY);
        this.possibleSkills.add(Skills.SkillList.JUMPING4);
        this.possibleSkills.add(Skills.SkillList.BLOW_OF_WIND);
        this.possibleSkills.add(Skills.SkillList.STONE_REMOVING);
    }
}
