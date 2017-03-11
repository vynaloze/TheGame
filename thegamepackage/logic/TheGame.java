package thegamepackage.logic;

import thegamepackage.creatures.Monster;
import thegamepackage.util.Coordinates;
import thegamepackage.util.MonsterID;
import thegamepackage.util.GameMessage;

import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public class TheGame implements Runnable {

    private GameConditions conditions;

    private final int SIZE = 8;
    private Tile[][] board = new Tile[SIZE][SIZE];

    private GameTimer timer;
    private Player playerOne, playerTwo, activePlayer;
    private PlayerHandlerInterface playerOneHandler, playerTwoHandler, activePlayerHandler;

    public enum GameState {ONE_PLAYING, TWO_PLAYING, ONE_ELEMINATED, TWO_ELEMINATED, ONE_NOTIME, TWO_NOTIME}

    private GameState gameState;

    private ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);


    public TheGame(GameConditions conditions) {
        this.conditions = conditions;

        playerOne = new Player();
        playerTwo = new Player();
        activePlayer = playerOne;

        playerOne.setName(conditions.getPlayerOneName());
        playerTwo.setName(conditions.getPlayerTwoName());
        playerOne.setColor(conditions.getPlayerOneColor());
        playerTwo.setColor(conditions.getPlayerTwoColor());

        timer = new GameTimer(conditions.getTimeOfGame(), conditions.getTimeAdded());

        createNewBoard();
        spawnMonsters();
    }

    private void createNewBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = new Tile(i, j);
            }
        }

        for (Coordinates c: conditions.getStones()){
            board[c.getY()][c.getX()].makeNewStone();
        }
    }

    private void spawnMonsters() {
        ArrayList<MonsterID> list = conditions.getMonsterList();
        //just spawn the lower monsters
        board[6][0].setMonster(Monster.spawnNewMonster(list.get(0), playerOne));
        board[7][0].setMonster(Monster.spawnNewMonster(list.get(1), playerOne));
        board[7][1].setMonster(Monster.spawnNewMonster(list.get(2), playerOne));
        //spawn & rotate 180 degrees the upper monsters
        board[0][6].setMonster(Monster.spawnNewMonster(list.get(3), playerTwo));
        board[0][6].getMonster().rotate(180);
        board[0][7].setMonster(Monster.spawnNewMonster(list.get(4), playerTwo));
        board[0][7].getMonster().rotate(180);
        board[1][7].setMonster(Monster.spawnNewMonster(list.get(5), playerTwo));
        board[1][7].getMonster().rotate(180);
    }

    public void setPlayers(PlayerHandlerInterface playerOneHandler, PlayerHandlerInterface playerTwoHandler) {
        this.playerOneHandler = playerOneHandler;
        this.activePlayerHandler = playerOneHandler;
        this.playerTwoHandler = playerTwoHandler;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public GameTimer getTimer() {
        return timer;
    }

    public Tile[][] getBoard() {
        return board;
    }

   /* private void readAndApplyProperties(TypeOfGame type) {
        if (type == TypeOfGame.LOCAL || type == TypeOfGame.SERVER) {
            // first option means we play locally OR host a server - so this instance chooses options, monsters
            randomizeMonsters();
            try {
                File file = new File("game.properties");
                FileInputStream fileIn = new FileInputStream(file);
                Properties properties = new Properties();
                properties.load(fileIn);
                fileIn.close();

                howManyStones = Integer.parseInt(properties.getProperty("stones"));
                playerOne.setName(properties.getProperty("player1"));
                playerTwo.setName(properties.getProperty("player2"));
                time = Integer.parseInt(properties.getProperty("time"));
                timeAdded = Integer.parseInt(properties.getProperty("time_added"));

            } catch (IOException x) {
                howManyStones = 8;
                playerOne.setName("P13RV52Y");
                playerTwo.setName("DRU61");
                time = 3;
                timeAdded = 10;
            }
            // if we are playing online, send initial data to client
            if(type== TypeOfGame.SERVER){

            }
//            // watch out. This is "else" clause fo the first "if" in this function
   //     } else {
            // the second option means we are a client - so we ask server for options

       //     Properties message = playerOneHandler.getInitialProperties();
            //         //todo - apply that
        }
    }*/


    @Override
    public void run() {
        gameState = GameState.ONE_PLAYING;
        timer.start();
        scheduler.scheduleAtFixedRate(new GameFlow(), 0, 100, TimeUnit.MILLISECONDS);
    }

    public void stopGame() {
        timer.stop();
        scheduler.shutdown();
        //todo other "closing everything" shit
    }

    private class GameFlow implements Runnable {

        @Override
        public void run() {
            checkForWinner();
            waitForActions();
            waitForEndTurn();
        }

        private void checkForWinner() {
            // check if someone won by elimination
            if (playerOne.getMonstersAlive() == 0) {
                gameState = GameState.ONE_ELEMINATED;
                stopGame();
            }
            if (playerTwo.getMonstersAlive() == 0) {
                gameState = GameState.TWO_ELEMINATED;
                stopGame();
            }

            // check if someone ran out of time
            if (timer.getTimeRemaining1() <= 0) {
                gameState = GameState.ONE_NOTIME;
                stopGame();
            }
            if (timer.getTimeRemaining2() <= 0) {
                gameState = GameState.TWO_NOTIME;
                stopGame();
            }
        }

        private void waitForActions() {
            SkillHandler skillHandler = new SkillHandler(board);


            GameMessage move = activePlayerHandler.getMove();

            if (move != null) {
                // here we create a new move handler object and ask it to perform given move
                // if it is valid, we confirm this move in both handlers
                if (new MoveHandler(board, activePlayer).move(move)) {
                    playerOneHandler.performedMove(move);
                    playerTwoHandler.performedMove(move);
                    skillHandler.updateProtectedMonsters();
                }
            }


            GameMessage attack = activePlayerHandler.getAttack();

            if (attack != null) {
                // no need to validate, it's handled by GUI
                new AttackHandler(board, activePlayer).attack(attack);
                playerOneHandler.performedAttack(attack);
                playerTwoHandler.performedAttack(attack);
                skillHandler.updateProtectedMonsters();
            }


            GameMessage skill = activePlayerHandler.getSkill();

            if (skill != null) {
                skillHandler.addActiveTile(board[skill.srcY][skill.srcX]);
                // no need to validate, too. However...
                // there's a problem with stone/field creating/removing: we have to wait until
                // player clicks on the target tile, where he wants to create (or remove) sth
                if (SkillHandler.SkillList.isNonInstantSkill(skill.skill)) {
                    if (skill.destX != -1) {
                        skillHandler.useSkill(skill.skill, skill.destX, skill.destY);
                        playerOneHandler.performedSkill(skill);
                        playerTwoHandler.performedSkill(skill);
                    }
                }
                // if there is one of other "simple" skills, there are no problems
                // (unnecessary parameters x & y, but I'm too lazy to deal with it)
                else {
                    skillHandler.useSkill(skill.skill, -1, -1);
                    playerOneHandler.performedSkill(skill);
                    playerTwoHandler.performedSkill(skill);
                }
            }


            GameMessage rotation = activePlayerHandler.getRotation();

            if (rotation != null) {
                board[rotation.srcY][rotation.srcX].getMonster().rotate(rotation.rotation);
                playerOneHandler.performedRotation(rotation);
                playerTwoHandler.performedRotation(rotation);
                skillHandler.updateProtectedMonsters();
            }
        }

        private void waitForEndTurn() {
            if (activePlayerHandler.isTurnOver() != null) {

                activePlayer.setIfCanMove(true);
                activePlayer.setParalysed(false);
                activePlayer.changeMana(2);
                timer.changePlayer();

                if (gameState == GameState.ONE_PLAYING) {
                    activePlayerHandler = playerTwoHandler;
                    activePlayer = playerTwo;
                    gameState = GameState.TWO_PLAYING;
                } else {
                    activePlayerHandler = playerOneHandler;
                    activePlayer = playerOne;
                    gameState = GameState.ONE_PLAYING;
                }

                playerOneHandler.confirmEndTurn();
                playerTwoHandler.confirmEndTurn();
            }
        }
    }
}
