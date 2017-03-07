package thegamepackage.util;

import thegamepackage.logic.SkillHandler;

import java.io.Serializable;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class GameMessage implements Serializable{
    public int srcX;
    public int srcY;
    public int destX;
    public int destY;
    public SkillHandler.SkillList skill;
    public int rotation;
    public Type type;

    public enum Type {MOVE, ATTACK, SKILL, ROTATION, ENDTURN}

}
