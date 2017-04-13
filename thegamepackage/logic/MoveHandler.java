package thegamepackage.logic;

import thegamepackage.util.GameMessage;

import static java.lang.Math.abs;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class MoveHandler {
    private Tile tiles[][];
    private Player activePlayer;

    public MoveHandler(Tile board[][], Player activePlayer) {
        this.tiles = board;
        this.activePlayer = activePlayer;
    }

    public boolean move(GameMessage move) {
        if (!isMoveValid(move)) {
            return false;
        }
        Tile src = tiles[move.getSrcY()][move.getSrcX()];
        Tile dest = tiles[move.getDestY()][move.getDestX()];
        dest.setMonster(src.getMonster());
        src.removeMonster();
        activePlayer.setIfCanMove(false);
        tiles[move.getDestY()][move.getDestX()].getMonster().setHasted(false);
        return true;
    }

    private boolean isMoveValid(GameMessage move) {
        //MOVING PIECE: check if previously clicked tile has a monster and if player tries to move his own monster
        //and if he moved already and if there is no stone on target tile and if the target tile is within range of this monster
        //plus quite complicated check to avoid "jumping" over stones and monsters
        return tiles[move.getSrcY()][move.getSrcX()].getMonster() != null
                && tiles[move.getSrcY()][move.getSrcX()].getMonster().getPlayer() == activePlayer
                && activePlayer.canMove()
                && tiles[move.getDestY()][move.getDestX()].isOccupied() == false
                && checkIfDistanceIsValid(move)
                && checkIfSpecificMoveIsValid(move);
    }

    private boolean checkIfDistanceIsValid(GameMessage move) {
        Tile src = tiles[move.getSrcY()][move.getSrcX()];
        Tile dest = tiles[move.getDestY()][move.getDestX()];

        if (activePlayer.isParalysed() && src.getMonster().isHasted()) {
            return abs(dest.getX() - src.getX()) + abs(dest.getY() - src.getY()) <= 2;
        }
        if (activePlayer.isParalysed()) {
            return abs(dest.getX() - src.getX()) + abs(dest.getY() - src.getY()) == 1;
        }
        if (src.getMonster().isHasted()) {
            return abs(dest.getX() - src.getX()) + abs(dest.getY() - src.getY()) <= src.getMonster().getSpeed() + 1;
        }
        return abs(dest.getX() - src.getX()) + abs(dest.getY() - src.getY()) <= src.getMonster().getSpeed();
    }

    private boolean checkIfSpecificMoveIsValid(GameMessage move) {
        int x = move.getDestX();
        int y = move.getDestY();
        int a = move.getSrcX();
        int b = move.getSrcY();

        //if monster can jump, nothing here is important
        if (tiles[b][a].getMonster().getPossibleSkills().contains(SkillHandler.SkillList.JUMPING4)) {
            return true;
        }

        //if not, we check all 24 possible destinations
        //"straight lines" - distance 3
        if (a == x && b + 3 == y) {
            if (tiles[b + 2][a].isOccupied() || tiles[b + 1][a].isOccupied())
                return false;
            else
                return true;
        }
        if (a == x && b - 3 == y) {
            if (tiles[b - 2][a].isOccupied() || tiles[b - 1][a].isOccupied())
                return false;
            else
                return true;
        }
        if (a + 3 == x && b == y) {
            if (tiles[b][a + 2].isOccupied() || tiles[b][a + 1].isOccupied())
                return false;
            else
                return true;
        }
        if (a - 3 == x && b == y) {
            if (tiles[b][a - 2].isOccupied() || tiles[b][a - 1].isOccupied())
                return false;
            else
                return true;
        }
        //straight lines - dist 2
        if (a == x && b + 2 == y) {
            if (tiles[b + 1][a].isOccupied())
                return false;
            else
                return true;
        }
        if (a == x && b - 2 == y) {
            if (tiles[b - 1][a].isOccupied())
                return false;
            else
                return true;
        }
        if (a + 2 == x && b == y) {
            if (tiles[b][a + 1].isOccupied())
                return false;
            else
                return true;
        }
        if (a - 2 == x && b == y) {
            if (tiles[b][a - 1].isOccupied())
                return false;
            else
                return true;
        }
        //no need to check "straight lines - dist 1" - you cant move to occupied tile, it's already checked
        //"on corner"
        if (a + 1 == x && b + 1 == y) {
            if (tiles[b + 1][a].isOccupied() && tiles[b][a + 1].isOccupied())
                return false;
            else
                return true;
        }
        if (a - 1 == x && b - 1 == y) {
            if (tiles[b - 1][a].isOccupied() && tiles[b][a - 1].isOccupied())
                return false;
            else
                return true;
        }
        if (a - 1 == x && b + 1 == y) {
            if (tiles[b + 1][a].isOccupied() && tiles[b][a - 1].isOccupied())
                return false;
            else
                return true;
        }
        if (a + 1 == x && b - 1 == y) {
            if (tiles[b - 1][a].isOccupied() && tiles[b][a + 1].isOccupied())
                return false;
            else
                return true;
        }
        //last 8 cases
        if (a + 1 == x && b + 2 == y) {
            if ((!tiles[b + 1][a].isOccupied() && !tiles[b + 2][a].isOccupied())
                    || (!tiles[b + 1][a].isOccupied() && !tiles[b + 1][a + 1].isOccupied())
                    || (!tiles[b + 1][a + 1].isOccupied() && !tiles[b][a + 1].isOccupied()))
                return true;
            else
                return false;
        }
        if (a - 1 == x && b + 2 == y) {
            if ((!tiles[b + 1][a].isOccupied() && !tiles[b + 2][a].isOccupied())
                    || (!tiles[b + 1][a].isOccupied() && !tiles[b + 1][a - 1].isOccupied())
                    || (!tiles[b + 1][a - 1].isOccupied() && !tiles[b][a - 1].isOccupied()))
                return true;
            else
                return false;
        }
        if (a - 2 == x && b + 1 == y) {
            if ((!tiles[b][a - 1].isOccupied() && !tiles[b][a - 2].isOccupied())
                    || (!tiles[b + 1][a - 1].isOccupied() && !tiles[b][a - 1].isOccupied())
                    || (!tiles[b + 1][a - 1].isOccupied() && !tiles[b + 1][a].isOccupied()))
                return true;
            else
                return false;
        }
        if (a - 2 == x && b - 1 == y) {
            if ((!tiles[b][a - 1].isOccupied() && !tiles[b][a - 2].isOccupied())
                    || (!tiles[b - 1][a - 1].isOccupied() && !tiles[b][a - 1].isOccupied())
                    || (!tiles[b - 1][a - 1].isOccupied() && !tiles[b - 1][a].isOccupied()))
                return true;
            else
                return false;
        }
        if (a + 1 == x && b - 2 == y) {
            if ((!tiles[b - 1][a].isOccupied() && !tiles[b - 2][a].isOccupied())
                    || (!tiles[b - 1][a].isOccupied() && !tiles[b - 1][a + 1].isOccupied())
                    || (!tiles[b - 1][a + 1].isOccupied() && !tiles[b][a + 1].isOccupied()))
                return true;
            else
                return false;
        }
        if (a - 1 == x && b - 2 == y) {
            if ((!tiles[b - 1][a].isOccupied() && !tiles[b - 2][a].isOccupied())
                    || (!tiles[b - 1][a].isOccupied() && !tiles[b - 1][a - 1].isOccupied())
                    || (!tiles[b - 1][a - 1].isOccupied() && !tiles[b][a - 1].isOccupied()))
                return true;
            else
                return false;
        }
        if (a + 2 == x && b + 1 == y) {
            if ((!tiles[b][a + 1].isOccupied() && !tiles[b][a + 2].isOccupied())
                    || (!tiles[b + 1][a + 1].isOccupied() && !tiles[b][a + 1].isOccupied())
                    || (!tiles[b + 1][a + 1].isOccupied() && !tiles[b + 1][a].isOccupied()))
                return true;
            else
                return false;
        }
        if (a + 2 == x && b - 1 == y) {
            if ((!tiles[b][a + 1].isOccupied() && !tiles[b][a + 2].isOccupied())
                    || (!tiles[b - 1][a + 1].isOccupied() && !tiles[b][a + 1].isOccupied())
                    || (!tiles[b - 1][a + 1].isOccupied() && !tiles[b - 1][a].isOccupied()))
                return true;
            else
                return false;
        }

        return true;
    }
}

