package thegamepackage.logic;

import thegamepackage.util.Coordinates;
import thegamepackage.util.GameMessage;

import java.util.List;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class AttackHandler {
    private Tile tiles[][];
    private Player activePlayer;

    public AttackHandler(Tile board[][], Player activePlayer) {
        this.tiles = board;
        this.activePlayer = activePlayer;
    }

    public void attack(GameMessage position) {
        Tile tile = tiles[position.getSrcY()][position.getSrcX()];

        for (Coordinates coordinates : tile.getMonster().getAttackedTiles()) {
            int x = tile.getX() + coordinates.getX();
            int y = tile.getY() + coordinates.getY();
            if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
                if (tiles[y][x].getMonster() != null && !tiles[y][x].getMonster().isUnderProtection()) {
                    if (tiles[y][x].getMonster().getPlayer() != activePlayer) {
                        tiles[y][x].getMonster().getPlayer().changeMonstersAlive(-1);
                        tiles[y][x].removeMonster();
                    }
                }
            }
        }
    }

    public void attack(List<Coordinates> attackedTiles, int rootX, int rootY) {
        for (Coordinates coordinates : attackedTiles) {
            int x = rootX + coordinates.getX();
            int y = rootY + coordinates.getY();
            if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
                if (tiles[y][x].getMonster() != null && !tiles[y][x].getMonster().isUnderProtection()) {
                    if (tiles[y][x].getMonster().getPlayer() != activePlayer) {
                        tiles[y][x].getMonster().getPlayer().changeMonstersAlive(-1);
                        tiles[y][x].removeMonster();
                    }
                }
            }
        }
    }

}
