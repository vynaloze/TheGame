package thegamepackage.ui;

import javafx.scene.control.Alert;
import thegamepackage.creatures.Cobra;
import thegamepackage.creatures.Infernalist;
import thegamepackage.creatures.LasiodoraParahybana;
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
    private Tile tileWithJesus;
    private int jesusRotation;

    public Skills(Tile[][] tiles) {
        this.tiles = tiles;
        this.tileWithJesus = findTileWithJesus();
        if (tileWithJesus != null) {
            this.jesusRotation = convertRotation(tileWithJesus.getMonster().getCurrentRotation());
        }
    }

    public Skills(Tile[][] tiles, Tile tileWithActiveMonster) {
        this.tileWithActiveMonster = tileWithActiveMonster;
        this.tiles = tiles;
        this.rotation = convertRotation(tileWithActiveMonster.getMonster().getCurrentRotation());
        this.tileWithJesus = findTileWithJesus();
        if (tileWithJesus != null) {
            this.jesusRotation = convertRotation(tileWithJesus.getMonster().getCurrentRotation());
        }
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
            case PARALYSE:
                useParalyse();
                break;
            case FURY:
                useFury();
                break;
            case BLOW_OF_WIND:
                useBlowOfWind();
                break;
            case STONE_MAKING:
                useStoneMakingOrRemoving(true);
                break;
            case STONE_REMOVING:
                useStoneMakingOrRemoving(false);
                break;
            case STONE_PUSHING:
                useStonePushing();
                break;
            case HASTE:
                useHaste();
                break;
            case LIGHTNING:
                useLightning();
                break;
            case FIRE_FIELD:
                useField("red");
                break;
            case POISON_FIELD:
                useField("green");
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

    private void useParalyse() {
        if (findOpponentPlayer() != null) {
            findOpponentPlayer().setParalysed(true);
            tileWithActiveMonster.getMonster().getPlayer().modifyManaValue(-SkillList.PARALYSE.getCost());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("To your information");
            alert.setHeaderText(null);
            alert.setContentText(tileWithActiveMonster.getMonster().getPlayer().getName() + " used PARALYSE!");
            alert.showAndWait();
        }
    }

    private void useFury() {
        attackedTiles.add(new Coordinates(1, 0));
        attackedTiles.add(new Coordinates(2, 0));
        attackedTiles.add(new Coordinates(-1, 0));
        attackedTiles.add(new Coordinates(-2, 0));
        attackedTiles.add(new Coordinates(0, 1));
        attackedTiles.add(new Coordinates(0, 2));
        attackedTiles.add(new Coordinates(0, -1));
        attackedTiles.add(new Coordinates(0, -2));
        attackedTiles.add(new Coordinates(-1, 1));
        attackedTiles.add(new Coordinates(1, 1));
        attackedTiles.add(new Coordinates(-1, -1));
        attackedTiles.add(new Coordinates(1, -1));

        performAttack(attackedTiles, tileWithActiveMonster.getX(), tileWithActiveMonster.getY());
        tileWithActiveMonster.getMonster().getPlayer().modifyManaValue(-SkillList.FURY.getCost());
    }

    private void useBlowOfWind() {
        Coordinates c = new Coordinates(0, -1);
        c.rotateCoordinates(rotation);
        ArrayList<Tile> tileArrayList = findTilesWithOpponentMonsters();

        for (Tile tile : tileArrayList) {
            int x = tile.getX() + c.getX();
            int y = tile.getY() + c.getY();
            if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
                Tile target = tiles[y][x];
                if (!target.isOccupied()) {
                    target.setMonster(tile.getMonster());
                    tile.removeMonster();
                    updateProtectedMonsters();
                }
            }
        }
        tileWithActiveMonster.getMonster().getPlayer().modifyManaValue(-SkillList.BLOW_OF_WIND.getCost());
    }

    private void useStoneMakingOrRemoving(boolean make) {
        // not exactly coordinates as before, but Xs and Ys of the board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tilesAffectedBySkill.add(new Coordinates(i, j));
            }
        }
        // in this way, lambda expression's parameters are effectively final
        for (Coordinates c : tilesAffectedBySkill) {
            int x = c.getX();
            int y = c.getY();
            tiles[y][x].getSquare().setOnMouseClicked(e -> handleChangeOfStone(tilesAffectedBySkill, x, y, make));
        }
    }

    private void useStonePushing() {
        Coordinates currentStone = new Coordinates(0, -1);
        Coordinates targetStone = new Coordinates(0, -2);
        currentStone.rotateCoordinates(rotation);
        targetStone.rotateCoordinates(rotation);

        int x = tileWithActiveMonster.getX() + currentStone.getX();
        int y = tileWithActiveMonster.getY() + currentStone.getY();
        int a = tileWithActiveMonster.getX() + targetStone.getX();
        int b = tileWithActiveMonster.getY() + targetStone.getY();
        if (x >= 0 && x <= 7 && y >= 0 && y <= 7 && a >= 0 && a <= 7 && b >= 0 && b <= 7) {
            Tile root = tiles[y][x];
            Tile target = tiles[b][a];
            if (root.isOccupied() && !root.isField() && root.getMonster() == null && !target.isOccupied()) {
                root.removeStone();
                target.makeNewStone();
            }
        }
    }

    private void useHaste() {
        tileWithActiveMonster.getMonster().setHasted(true);
        tileWithActiveMonster.getMonster().getPlayer().modifyManaValue(-SkillList.HASTE.getCost());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("To your information");
        alert.setHeaderText(null);
        alert.setContentText(tileWithActiveMonster.getMonster().getPlayer().getName() + " used HASTE!");
        alert.showAndWait();
    }

    private void useLightning() {
        attackedTiles.add(new Coordinates(-1, -1));
        attackedTiles.add(new Coordinates(-2, -2));
        attackedTiles.add(new Coordinates(-1, 1));
        attackedTiles.add(new Coordinates(-2, 2));
        attackedTiles.add(new Coordinates(1, -1));
        attackedTiles.add(new Coordinates(2, -2));
        attackedTiles.add(new Coordinates(1, 1));
        attackedTiles.add(new Coordinates(2, 2));

        for (Coordinates c : attackedTiles) {
            c.rotateCoordinates(rotation);
        }
        performAttack(attackedTiles, tileWithActiveMonster.getX(), tileWithActiveMonster.getY());
        tileWithActiveMonster.getMonster().getPlayer().modifyManaValue(-SkillList.LIGHTNING.getCost());
    }

    private void useField(String color) {
        attackedTiles = tileWithActiveMonster.getMonster().getAttackedTiles();
        for (Coordinates c : attackedTiles) {
            c.rotateCoordinates(rotation);
            int x = tileWithActiveMonster.getX() + c.getX();
            int y = tileWithActiveMonster.getY() + c.getY();
            if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
                tiles[y][x].getSquare().setOnMouseClicked(e -> handleFieldCreation(attackedTiles, x, y, color));
            }
        }
    }

    public void updateProtectedMonsters() {
        //clear previous protections
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j].getMonster() != null) {
                    tiles[i][j].getMonster().setUnderProtection(false);
                }
            }
        }
        //set new ones
        if (tileWithJesus != null) {
            tilesAffectedBySkill.add(new Coordinates(1, 0));
            tilesAffectedBySkill.add(new Coordinates(-1, 0));
            for (Coordinates c : tilesAffectedBySkill) {
                c.rotateCoordinates(jesusRotation);
                int x = tileWithJesus.getX() + c.getX();
                int y = tileWithJesus.getY() + c.getY();
                if (x >= 0 && x <= 7 && y >= 0 && y <= 7 && tiles[y][x].getMonster() != null) {
                    tiles[y][x].highlight("green");
                    tiles[y][x].getMonster().setUnderProtection(true);
                }
            }
        }
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
        updateProtectedMonsters();
    }

    private void performAttack(List<Coordinates> attackedTiles, int rootX, int rootY) {
        for (Coordinates coordinates : attackedTiles) {
            int x = rootX + coordinates.getX();
            int y = rootY + coordinates.getY();
            if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
                tiles[y][x].highlight("purple");
                if (tiles[y][x].getMonster() != null && !tiles[y][x].getMonster().isUnderProtection()) {
                    if (tiles[y][x].getMonster().getPlayer() != tileWithActiveMonster.getMonster().getPlayer()) {
                        tiles[y][x].getMonster().getPlayer().modifyMonstersAliveValue(-1);
                        tiles[y][x].removeMonster();
                        updateProtectedMonsters();
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

    private Player findOpponentPlayer() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                Tile t = tiles[i][j];
                if (t.getMonster() != null) {
                    if (t.getMonster().getPlayer() != tileWithActiveMonster.getMonster().getPlayer()) {
                        return t.getMonster().getPlayer();
                    }
                }
            }
        }
        return null;
    }

    private ArrayList<Tile> findTilesWithOpponentMonsters() {
        ArrayList<Tile> tileArrayList = new ArrayList<>();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                Tile t = tiles[i][j];
                if (t.getMonster() != null) {
                    if (t.getMonster().getPlayer() != tileWithActiveMonster.getMonster().getPlayer()) {
                        tileArrayList.add(t);
                    }
                }
            }
        }
        return tileArrayList;
    }

    private void handleChangeOfStone(List<Coordinates> wholeBoardOfTiles, int a, int b, boolean make) {
        if (make) {
            tiles[b][a].makeNewStone();
            tileWithActiveMonster.getMonster().getPlayer().modifyManaValue(-SkillList.STONE_MAKING.getCost());

        } else {
            tiles[b][a].removeStone();
            tileWithActiveMonster.getMonster().getPlayer().modifyManaValue(-SkillList.STONE_REMOVING.getCost());
        }
        for (Coordinates c : wholeBoardOfTiles) {
            tiles[c.getY()][c.getX()].getSquare().setOnMouseClicked(null);
        }
    }

    private void handleFieldCreation(List<Coordinates> attackedTiles, int a, int b, String color) {
        final int MAX_FIELDS = 3;

        // clear clickability of tiles
        for (Coordinates c : attackedTiles) {
            c.rotateCoordinates(rotation);
            int x = tileWithActiveMonster.getX() + c.getX();
            int y = tileWithActiveMonster.getY() + c.getY();
            if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
                tiles[y][x].getSquare().setOnMouseClicked(null);
            }
        }

        // check if not too many
        switch (tileWithActiveMonster.getMonster().getId()) {
            case INFERNALIST:
                if (Infernalist.getFields() == MAX_FIELDS) {
                    return;
                }
                Infernalist.addField();
                break;
            case LASIODORA_PARAHYBANA:
                if (LasiodoraParahybana.getFields() == MAX_FIELDS) {
                    return;
                }
                LasiodoraParahybana.addField();
                break;
            case COBRA:
                if (Cobra.getFields() == MAX_FIELDS) {
                    return;
                }
                Cobra.addField();
        }

        // create a new field
        tiles[b][a].setField(true);
        tiles[b][a].getSquare().setStyle("-fx-background-color: " + color + ";");
        tileWithActiveMonster.getMonster().getPlayer().modifyManaValue(-SkillList.FIRE_FIELD.getCost());
    }

    private Tile findTileWithJesus() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j].getMonster() != null && tiles[i][j].getMonster().getId() == ID.JESUS) {
                    return tiles[i][j];
                }
            }
        }
        return null;
    }


    public enum SkillList {
        WALL_CROSSING(5), DEATH_STRIKE(10), FIREBALL(7), WATER_STREAM(5), PARALYSE(4),
        FURY(5), JUMPING4(0), BLOW_OF_WIND(4), STONE_MAKING(5), STONE_REMOVING(4),
        STONE_PUSHING(0), HASTE(5), LIGHTNING(6), FIRE_FIELD(5), POISON_FIELD(5), PROTECTION4(0);

        private int cost;

        SkillList(int cost) {
            this.cost = cost;
        }

        public int getCost() {
            return cost;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase().replace("_", " ").replace("4", " (passive)") + "  cost: " + cost;
        }
    }

    private enum Direction {
        RIGHT, LEFT, UP, DOWN
    }
}
