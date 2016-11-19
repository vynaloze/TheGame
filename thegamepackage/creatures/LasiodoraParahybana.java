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
public class LasiodoraParahybana extends Monster {
    private static int fields = 0;

    public LasiodoraParahybana(Player player) {
        this.id = ID.LASIODORA_PARAHYBANA;
        this.player = player;
        this.speed = 3;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("lasiodora.png")));

        this.attackedTiles.add(new Coordinates(-1, -1));
        this.attackedTiles.add(new Coordinates(1, -1));
        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(1, 0));
        this.attackedTiles.add(new Coordinates(-1, 0));
        this.attackedTiles.add(new Coordinates(-1, 1));
        this.attackedTiles.add(new Coordinates(1, 1));
        this.attackedTiles.add(new Coordinates(0, 1));

        this.possibleSkills.add(Skills.SkillList.PARALYSE);
        this.possibleSkills.add(Skills.SkillList.POISON_FIELD);
    }

    public static int getFields() {
        return fields;
    }

    public static void addField() {
        LasiodoraParahybana.fields++;
    }
}