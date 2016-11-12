package thegamepackage.ui;

import thegamepackage.creatures.Monster;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class Skills {
    private List<Coordinates> tilesAffectedBySkill = new ArrayList<>();
    private List<Coordinates> attackedTiles = new ArrayList<>();
    private Tile[][] tiles;
    private Tile tileWithActiveMonster;
    private int rotation;

    public Skills(Tile[][] tiles, Tile tileWithActiveMonster) {
        this.tileWithActiveMonster = tileWithActiveMonster;
        this.tiles = tiles;
        this.rotation = convertRotation(tileWithActiveMonster.getMonster().getCurrentRotation());
    }

    public void useSkill(SkillList skill) {
        tilesAffectedBySkill.clear();
        attackedTiles.clear();

        switch (skill) {
            case WALL_CROSSING:
                useWallCrossing();
                break;
            case DEATH_STRIKE:
                useDeathStrike();
                break;
            case FIREBALL:
                useFireball();
                break;
            case WATER_STREAM:
                useWaterStream();
                break;
        }
    }

    private void useWallCrossing() {
        int x = tileWithActiveMonster.getX();
        int y = tileWithActiveMonster.getY();

        // if in corner, there are 2 possible directions of wall-crossing
        if (x == 0 && y == 0) {
            if (checkIfVerticalDirection(rotation)) {
                crossThroughWall(Direction.UP, x, y);
            } else {
                crossThroughWall(Direction.LEFT, x, y);
            }
            return;
        }
        if (x == 7 && y == 0) {
            if (checkIfVerticalDirection(rotation)) {
                crossThroughWall(Direction.UP, x, y);
            } else {
                crossThroughWall(Direction.RIGHT, x, y);
            }
            return;
        }
        if (x == 0 && y == 7) {
            if (checkIfVerticalDirection(rotation)) {
                crossThroughWall(Direction.DOWN, x, y);
            } else {
                crossThroughWall(Direction.LEFT, x, y);
            }
            return;
        }
        if (x == 7 && y == 7) {
            if (checkIfVerticalDirection(rotation)) {
                crossThroughWall(Direction.DOWN, x, y);
            } else {
                crossThroughWall(Direction.RIGHT, x, y);
            }
            return;
        }

        // if not in corner, simple transition, independently of current rotation
        if (x == 0) {
            crossThroughWall(Direction.LEFT, x, y);
            return;
        }
        if (x == 7) {
            crossThroughWall(Direction.RIGHT, x, y);
            return;
        }
        if (y == 0) {
            crossThroughWall(Direction.UP, x, y);
            return;
        }
        if (y == 7) {
            crossThroughWall(Direction.DOWN, x, y);
        }
    }

    private void useDeathStrike() {
        attackedTiles.add(new Coordinates(0, 0));
        Tile t = null;

        // adding all tiles in a straight line, rotated to the proper direction
        for (int i = -1; i > -8; i--) {
            tilesAffectedBySkill.add(new Coordinates(0, i));
        }
        for (Coordinates c : tilesAffectedBySkill) {
            c.rotateCoordinates(rotation);
        }

        // loop for checking every next tile in the line
        for (int i = 0; i < tilesAffectedBySkill.size(); i++) {
            Coordinates coordinates = tilesAffectedBySkill.get(i);
            int x = tileWithActiveMonster.getX() + coordinates.getX();
            int y = tileWithActiveMonster.getY() + coordinates.getY();
            tiles[y][x].highlight("purple");
            t = tiles[y][x];

            // if this tile has a monster (kills it btw) or it's border tile, stop and bounce
            if (((x == 0 || x == 7) && !checkIfVerticalDirection(rotation))
                    || ((y == 0 || y == 7) && checkIfVerticalDirection(rotation))
                    || tiles[y][x].getMonster() != null) {
                performAttack(attackedTiles, x, y);
                break;
            }
            // if the next tile has stone, stop and bounce
            else {
                Coordinates nextCoordinates = tilesAffectedBySkill.get(i + 1);
                Tile nextTile = tiles[tileWithActiveMonster.getY() + nextCoordinates.getY()][tileWithActiveMonster.getX() + nextCoordinates.getX()];
                if (nextTile.getMonster() == null && nextTile.isOccupied()) {
                    performAttack(attackedTiles, x, y);
                    break;
                }
            }
        }

        // after "bounce" line goes in both directions, until the end of board/stone (and kills monsters on the way)
        if (checkIfVerticalDirection(rotation)) {
            for (int i = t.getX() + 1; i <= 7 && (!tiles[t.getY()][i].isOccupied() || tiles[t.getY()][i].getMonster() != null); i++) {
                performAttack(attackedTiles, i, t.getY());
            }
            for (int i = t.getX() - 1; i >= 0 && (!tiles[t.getY()][i].isOccupied() || tiles[t.getY()][i].getMonster() != null); i--) {
                performAttack(attackedTiles, i, t.getY());
            }

        } else {
            for (int i = t.getY() + 1; i <= 7 && (!tiles[i][t.getX()].isOccupied() || tiles[i][t.getX()].getMonster() != null); i++) {
                performAttack(attackedTiles, t.getX(), i);
            }
            for (int i = t.getY() - 1; i >= 0 && (!tiles[i][t.getX()].isOccupied() || tiles[i][t.getX()].getMonster() != null); i--) {
                performAttack(attackedTiles, t.getX(), i);
            }
        }
        tileWithActiveMonster.getMonster().getPlayer().modifyManaValue(-SkillList.DEATH_STRIKE.getCost());
    }

    private void useFireball() {
        // the same as death strike but without "bounce", but with 9-tile "splash"
        attackedTiles.add(new Coordinates(0, 0));
        attackedTiles.add(new Coordinates(-1, -1));
        attackedTiles.add(new Coordinates(1, -1));
        attackedTiles.add(new Coordinates(0, -1));
        attackedTiles.add(new Coordinates(1, 0));
        attackedTiles.add(new Coordinates(-1, 0));
        attackedTiles.add(new Coordinates(-1, 1));
        attackedTiles.add(new Coordinates(1, 1));
        attackedTiles.add(new Coordinates(0, 1));

        for (int i = -1; i > -8; i--) {
            tilesAffectedBySkill.add(new Coordinates(0, i));
        }

        for (Coordinates c : tilesAffectedBySkill) {
            c.rotateCoordinates(rotation);
        }

        for (int i = 0; i < tilesAffectedBySkill.size(); i++) {
            Coordinates coordinates = tilesAffectedBySkill.get(i);
            int x = tileWithActiveMonster.getX() + coordinates.getX();
            int y = tileWithActiveMonster.getY() + coordinates.getY();
            tiles[y][x].highlight("purple");

            if (((x == 0 || x == 7) && !checkIfVerticalDirection(rotation))
                    || ((y == 0 || y == 7) && checkIfVerticalDirection(rotation))
                    || tiles[y][x].getMonster() != null) {
                performAttack(attackedTiles, x, y);
                break;
            } else {
                Coordinates nextCoordinates = tilesAffectedBySkill.get(i + 1);
                Tile nextTile = tiles[tileWithActiveMonster.getY() + nextCoordinates.getY()][tileWithActiveMonster.getX() + nextCoordinates.getX()];
                if (nextTile.getMonster() == null && nextTile.isOccupied()) {
                    performAttack(attackedTiles, x, y);
                    break;
                }
            }
        }
        tileWithActiveMonster.getMonster().getPlayer().modifyManaValue(-SkillList.FIREBALL.getCost());
    }

    private void useWaterStream() {
        // the same as above but without "bounce" or "splash"
        attackedTiles.add(new Coordinates(0, 0));

        for (int i = -1; i > -8; i--) {
            tilesAffectedBySkill.add(new Coordinates(0, i));
        }

        for (Coordinates c : tilesAffectedBySkill) {
            c.rotateCoordinates(rotation);
        }

        for (int i = 0; i < tilesAffectedBySkill.size(); i++) {
            Coordinates coordinates = tilesAffectedBySkill.get(i);
            int x = tileWithActiveMonster.getX() + coordinates.getX();
            int y = tileWithActiveMonster.getY() + coordinates.getY();
            performAttack(attackedTiles, x, y);

            if (((x == 0 || x == 7) && !checkIfVerticalDirection(rotation))
                    || ((y == 0 || y == 7) && checkIfVerticalDirection(rotation))
                    || (tiles[y][x].isOccupied() && tiles[y][x].getMonster() == null)) {
                break;
            }
        }
        tileWithActiveMonster.getMonster().getPlayer().modifyManaValue(-SkillList.WATER_STREAM.getCost());
    }

    private void performAttack(List<Coordinates> attackedTiles, int rootX, int rootY) {
        for (Coordinates coordinates : attackedTiles) {
            int x = rootX + coordinates.getX();
            int y = rootY + coordinates.getY();
            if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
                tiles[y][x].highlight("purple");
                if (tiles[y][x].getMonster() != null) {
                    if (tiles[y][x].getMonster().getPlayer() != tileWithActiveMonster.getMonster().getPlayer()) {
                        tiles[y][x].getMonster().getPlayer().modifyMonstersAliveValue(-1);
                        tiles[y][x].removeMonster();
                    }
                }
            }
        }
    }

    private int convertRotation(int currentRotation) {
        // to <-90,180> degrees set
        if (currentRotation < 0) {
            while (currentRotation <= -270) {
                currentRotation += 360;
            }
        } else {
            while (currentRotation >= 270) {
                currentRotation -= 360;
            }
        }
        return currentRotation;
    }

    private boolean checkIfVerticalDirection(int rotation) {
        return (rotation / 90) % 2 == 0;
    }

    private void crossThroughWall(Direction d, int x, int y) {
        Monster m = tileWithActiveMonster.getMonster();

        // left => from left to right etc.
        if (d == Direction.LEFT) {
            Tile target = tiles[y][7];
            if (!target.isOccupied()) {
                target.setMonster(m);
                tileWithActiveMonster.removeMonster();
                m.getPlayer().modifyManaValue(-SkillList.WALL_CROSSING.getCost());
            }
        }
        if (d == Direction.RIGHT) {
            Tile target = tiles[y][0];
            if (!target.isOccupied()) {
                target.setMonster(m);
                tileWithActiveMonster.removeMonster();
                m.getPlayer().modifyManaValue(-SkillList.WALL_CROSSING.getCost());
            }
        }
        if (d == Direction.UP) {
            Tile target = tiles[7][x];
            if (!target.isOccupied()) {
                target.setMonster(m);
                tileWithActiveMonster.removeMonster();
                m.getPlayer().modifyManaValue(-SkillList.WALL_CROSSING.getCost());
            }
        }
        if (d == Direction.DOWN) {
            Tile target = tiles[0][x];
            if (!target.isOccupied()) {
                target.setMonster(m);
                tileWithActiveMonster.removeMonster();
                m.getPlayer().modifyManaValue(-SkillList.WALL_CROSSING.getCost());
            }
        }
    }


    public enum SkillList {
        WALL_CROSSING(5), DEATH_STRIKE(10), FIREBALL(7), WATER_STREAM(5), PARALYSE(4),
        FURY(5), JUMPING(0), BLOW_OF_WIND(4), STONE_MAKING(5), STONE_REMOVING(4),
        STONE_PUSHING(0), HASTE(5), LIGHTNING(6), FIRE_FIELD(5), POISON_FIELD(5);       //TODO: protection

        private int cost;

        SkillList(int cost) {
            this.cost = cost;
        }

        public int getCost() {
            return cost;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase().replace("_", " ") + "  cost: " + cost;
        }
    }

    private enum Direction {
        RIGHT, LEFT, UP, DOWN
    }
}
