package thegamepackage.util;

import thegamepackage.logic.SkillHandler;

import java.io.Serializable;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class GameMessage implements Serializable {
    private int srcX;
    private int srcY;
    private int destX;
    private int destY;
    private SkillHandler.SkillList skill;
    private int rotation;
    private String chatMessage;
    private TypeOfMessage type;

    public int getSrcX() {
        return srcX;
    }

    public void setSrcX(int srcX) {
        this.srcX = srcX;
    }

    public int getSrcY() {
        return srcY;
    }

    public void setSrcY(int srcY) {
        this.srcY = srcY;
    }

    public int getDestX() {
        return destX;
    }

    public void setDestX(int destX) {
        this.destX = destX;
    }

    public int getDestY() {
        return destY;
    }

    public void setDestY(int destY) {
        this.destY = destY;
    }

    public SkillHandler.SkillList getSkill() {
        return skill;
    }

    public void setSkill(SkillHandler.SkillList skill) {
        this.skill = skill;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public TypeOfMessage getType() {
        return type;
    }

    public void setType(TypeOfMessage type) {
        this.type = type;
    }

    public enum TypeOfMessage {MOVE, ATTACK, SKILL, ROTATION, ENDTURN, CHAT}

}
