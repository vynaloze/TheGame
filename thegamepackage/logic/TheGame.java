package thegamepackage.logic;

import thegamepackage.creatures.Monster;
import thegamepackage.network.NetworkHandler;
import thegamepackage.util.Coordinates;
import thegamepackage.util.GameMessage;
import thegamepackage.util.MonsterID;

import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
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

    //------------------------------------------
    // preparing the game to run
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
        for (Coordinates c : conditions.getStones()) {
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

    //------------------------------------------
    // bunch of getters and setters
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

    public GameState getGameState() {
        return gameState;
    }

    //------------------------------------------
    // and here is the hearth of the game
    @Override
    public void run() {
        gameState = GameState.ONE_PLAYING;
        timer.start();
        //the game refreshes 60 times a second, the same as GUI
        scheduler.scheduleAtFixedRate(new GameFlow(), 0, 16, TimeUnit.MILLISECONDS);
    }

    public void stopGame() {
        timer.stop();
        scheduler.shutdown();
        if (playerOneHandler instanceof NetworkHandler) {
            NetworkHandler n = (NetworkHandler) playerOneHandler;
            n.closeConnection();
        }
        if (playerTwoHandler instanceof NetworkHandler) {
            NetworkHandler n = (NetworkHandler) playerTwoHandler;
            n.closeConnection();
        }
    }

    private class GameFlow implements Runnable {

        @Override
        public void run() {
            checkForWinner();

            waitForActions();
            waitForChatMessage();
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
                        playerOneHandler.performedSkill(skill, activePlayer.getName());
                        playerTwoHandler.performedSkill(skill, activePlayer.getName());
                    }
                }
                // if there is one of other "simple" skills, there are no problems
                // (unnecessary parameters x & y, but I'm too lazy to deal with it)
                else {
                    skillHandler.useSkill(skill.skill, -1, -1);
                    playerOneHandler.performedSkill(skill, activePlayer.getName());
                    playerTwoHandler.performedSkill(skill, activePlayer.getName());
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

        private void waitForChatMessage() {
            GameMessage message = activePlayerHandler.getChatMessage();
            if (message != null) {
                String name;
                if (activePlayerHandler == playerOneHandler) {
                    name = playerOne.getName();
                } else {
                    name = playerTwo.getName();
                }
                playerOneHandler.sendChatMessage(message, name);
                playerTwoHandler.sendChatMessage(message, name);
            }
            //todo: allow player to write messages even if it's not his turn
//             message = playerTwoHandler.getChatMessage();
//            if(message != null){
//                playerOneHandler.sendChatMessage(message, playerTwo.getName());
//                playerTwoHandler.sendChatMessage(message, playerTwo.getName());
//            }
        }

        private void waitForEndTurn() {
            GameMessage message = activePlayerHandler.isTurnOver();
            if (message != null) {
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

                playerOneHandler.confirmEndTurn(message);
                playerTwoHandler.confirmEndTurn(message);
            }
        }
    }
}
