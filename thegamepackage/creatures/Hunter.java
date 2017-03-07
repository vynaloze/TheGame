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
public class Hunter extends Monster {

    public Hunter(Player player) {
        this.id = ID.HUNTER;
        this.player = player;
        this.speed = 3;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("hunter.png")));

        this.attackedTiles.add(new Coordinates(-1, -2));
        this.attackedTiles.add(new Coordinates(1, -2));

        this.possibleSkills.add(SkillHandler.SkillList.JUMPING4);
        this.possibleSkills.add(SkillHandler.SkillList.BLOW_OF_WIND);
    }
}