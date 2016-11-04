package TheGamePackage.Creatures;

import TheGamePackage.Interface.Player;
import javafx.scene.image.ImageView;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public abstract class Monster {
    protected Player player;
    protected int id;
    protected ImageView pic;
    protected int speed = 3;


   public abstract void attack();

   public static Monster spawnNewMonster(int id, Player player)
    {
        switch(id)
        {
            case 1:
                return new StonedSleepwalker(player);
            case 2:
                return new BenedictXVI(player);
            case 3:
                return new AndrzejDuda(player);
            case 4:
                return new FuriousBlacksmith(player);
            case 5:
                return new LawnmoverOperator(player);
            case 6:
                return new Jesus(player);
            default:
                return null;
        }
    }

    public ImageView getPic()
    {
        return pic;
    }

    public int getSpeed()
    {
        return speed;
    }

    public Player getPlayer()
    {
        return player;
    }
}
