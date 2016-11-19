package thegamepackage.creatures;

import thegamepackage.ui.Coordinates;
import thegamepackage.ui.ID;
import thegamepackage.ui.Player;
import javafx.scene.image.ImageView;
import thegamepackage.ui.Skills;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public abstract class Monster {
    protected Player player;
    protected ID id;
    protected ImageView pic;
    protected int speed;
    protected ArrayList<Coordinates> attackedTiles = new ArrayList<>();
    private int currentRotation;
    protected ArrayList<Skills.SkillList> possibleSkills = new ArrayList<>();
    protected boolean underProtection = false;
    protected boolean hasted = false;

    public static Monster spawnNewMonster(ID id, Player player) {
        switch (id) {
            case STONED_SLEEPWALKER:
                return new StonedSleepwalker(player);
            case BENEDICT_XVI:
                return new BenedictXVI(player);
            case ANDRZEJ_DUDA:
                return new AndrzejDuda(player);
            case BLUE_MAGE:
                return new BlueMage(player);
            case LAWNMOVER_OPERATOR:
                return new LawnmoverOperator(player);
            case JESUS:
                return new Jesus(player);
            case INFERNALIST:
                return new Infernalist(player);
            case JUGGERNAUT:
                return new Juggernaut(player);
            case LASIODORA_PARAHYBANA:
                return new LasiodoraParahybana(player);
            case HUNTER:
                return new Hunter(player);
            case MILKY_CAT:
                return new MilkyCat(player);
            case CRAZY_DEMON:
                return new CrazyDemon(player);
            case DONALD_TRUMP:
                return new DonaldTrump(player);
            case HIPPOPOTAMUS_AMPHIBIUS:
                return new HippopotamusAmphibius(player);
            case APHRODITE:
                return new Aphrodite(player);
            case CTHULHU:
                return new Cthulhu(player);
            case FLUFFY_UNICORN:
                return new FluffyUnicorn(player);
            case PAN_TADEUSZ:
                return new PanTadeusz(player);
            case VLADIMIR_PUTIN:
                return new VladimirPutin(player);
            case COBRA:
                return new Cobra(player);
            default:
                return new Cobra(player);
        }
    }

    public ID getId() {
        return id;
    }

    public ImageView getPic() {
        return pic;
    }

    public int getSpeed() {
        return speed;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Coordinates> getAttackedTiles() {
        return attackedTiles;
    }

    public void rotate(int degree) {
        currentRotation += degree;
        pic.setRotate(currentRotation);
        for (Coordinates coordinates : attackedTiles) {
            coordinates.rotateCoordinates(degree);
        }
    }

    public ArrayList<Skills.SkillList> getPossibleSkills() {
        return possibleSkills;
    }

    public int getCurrentRotation() {
        return currentRotation;
    }

    public boolean isUnderProtection() {
        return underProtection;
    }

    public void setUnderProtection(boolean underProtection) {
        this.underProtection = underProtection;
    }

    public boolean isHasted() {
        return hasted;
    }

    public void setHasted(boolean hasted) {
        this.hasted = hasted;
    }
}
