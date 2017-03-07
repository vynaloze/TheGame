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
public class Cobra extends Monster {
    private static int fields = 0;

    public Cobra(Player player) {
        this.id = ID.COBRA;
        this.player = player;
        this.speed = 3;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("cobra.png")));

        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(0, -2));
        this.attackedTiles.add(new Coordinates(0, -3));
        this.attackedTiles.add(new Coordinates(1, -2));
        this.attackedTiles.add(new Coordinates(-1, -2));

        this.possibleSkills.add(SkillHandler.SkillList.PARALYSE);
        this.possibleSkills.add(SkillHandler.SkillList.HASTE);
        this.possibleSkills.add(SkillHandler.SkillList.POISON_FIELD);
    }

    public static int getFields() {
        return fields;
    }

    public static void addField() {
        Cobra.fields++;
    }
}
