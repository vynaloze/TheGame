package thegamepackage.ui;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class Player {
    private String name;
    private int mana=0;
    private boolean canMove=true;
    private boolean canAttackOrSpell=true;


  //  public Player(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String s)
    {
        this.name = s;
    }

    public boolean canMove()
    {
        return canMove;
    }

    public boolean canAttackOrSpell()
    {
        return canAttackOrSpell;
    }

    public void setMoveValue(boolean value)
    {
        canMove = value;
    }

    public void setAttackOrSpellValue(boolean value)
    {
            canAttackOrSpell = value;
    }

    public void modifyManaValue(int value)
    {
        mana += value;
    }

    public int getMana()
    {
        return mana;
    }








}
