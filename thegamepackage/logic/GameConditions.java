package thegamepackage.logic;

import thegamepackage.util.Coordinates;
import thegamepackage.util.MonsterID;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class GameConditions implements Serializable {
    private ArrayList<MonsterID> monsterList;

    private String playerOneName;
    private String playerTwoName;
    private String playerOneColor;
    private String playerTwoColor;

    private int timeOfGame;
    private int timeAdded;

    private HashSet<Coordinates> stones;

    public GameConditions() {
        int howManyStones = 8;
        try {
            File file = new File("game.properties");
            FileInputStream fileIn = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fileIn);
            fileIn.close();

            howManyStones = Integer.parseInt(properties.getProperty("stones"));
            this.playerOneName = properties.getProperty("player1");
            this.playerTwoName = properties.getProperty("player2");
            this.timeOfGame = Integer.parseInt(properties.getProperty("time"));
            this.timeAdded = Integer.parseInt(properties.getProperty("time_added"));

        } catch (IOException x) {
            // default values
            this.playerOneName = "P13RV52Y";
            this.playerTwoName = "DRU61";
            this.timeOfGame = 3;
            this.timeAdded = 10;
        }
        // maybe I'll add changeability of this later
        this.playerOneColor = "orange";
        this.playerTwoColor = "blue";
        randomizeMonsters();
        randomizeStones(howManyStones);
    }

    public ArrayList<MonsterID> getMonsterList() {
        return monsterList;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public String getPlayerOneColor() {
        return playerOneColor;
    }

    public String getPlayerTwoColor() {
        return playerTwoColor;
    }

    public int getTimeOfGame() {
        return timeOfGame;
    }

    public int getTimeAdded() {
        return timeAdded;
    }

    public HashSet<Coordinates> getStones() {
        return stones;
    }

    private void randomizeMonsters() {
        //randomize monsters and leave just 6 on the list:
        //0-2 for playerOne and 3-5 for playerTwo
        monsterList = new ArrayList<>(EnumSet.allOf(MonsterID.class));
        Collections.shuffle(monsterList);
        monsterList.subList(6, monsterList.size()).clear();
    }

    private void randomizeStones(int howMany) {
        stones = new HashSet<>();

        for (int i = 0; i < howMany; ) {

            int x = ThreadLocalRandom.current().nextInt(0, 8);
            int y = ThreadLocalRandom.current().nextInt(0, 8);

            // to avoid stone in a starting corner
            if (x == 5 && y == 0) continue;
            if (x == 6 && y == 0) continue;
            if (x == 7 && y == 0) continue;
            if (x == 6 && y == 1) continue;
            if (x == 7 && y == 1) continue;
            if (x == 7 && y == 2) continue;

            if (x == 0 && y == 5) continue;
            if (x == 0 && y == 6) continue;
            if (x == 0 && y == 7) continue;
            if (x == 1 && y == 6) continue;
            if (x == 1 && y == 7) continue;
            if (x == 2 && y == 7) continue;

            // increment only if not duplicated
            if (stones.add(new Coordinates(x, y))) {
                i++;
            }
        }
    }

}

