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
public class Infernalist extends Monster {
    private static int fields = 0;

    public Infernalist(Player player) {
        this.id = ID.INFERNALIST;
        this.player = player;
        this.speed = 1;
        this.pic = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("infernalist.png")));

        this.attackedTiles.add(new Coordinates(-1, -1));
        this.attackedTiles.add(new Coordinates(1, -1));
        this.attackedTiles.add(new Coordinates(0, -1));
        this.attackedTiles.add(new Coordinates(2, -1));
        this.attackedTiles.add(new Coordinates(-2, -1));
        this.attackedTiles.add(new Coordinates(-1, -2));
        this.attackedTiles.add(new Coordinates(1, -2));
        this.attackedTiles.add(new Coordinates(0, -2));
        this.attackedTiles.add(new Coordinates(0, -3));

        this.possibleSkills.add(SkillHandler.SkillList.WALL_CROSSING);
        this.possibleSkills.add(SkillHandler.SkillList.FIREBALL);
        this.possibleSkills.add(SkillHandler.SkillList.STONE_MAKING);
        this.possibleSkills.add(SkillHandler.SkillList.LIGHTNING);
        this.possibleSkills.add(SkillHandler.SkillList.FIRE_FIELD);
    }

    public static int getFields() {
        return fields;
    }

    public static void addField() {
        Infernalist.fields++;
    }
}