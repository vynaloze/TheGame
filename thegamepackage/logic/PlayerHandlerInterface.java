package thegamepackage.logic;

import thegamepackage.util.GameMessage;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public interface PlayerHandlerInterface {

    GameMessage getMove();
    void performedMove(GameMessage move);

    GameMessage getAttack();
    void performedAttack(GameMessage position);

    GameMessage getSkill();
    void performedSkill(GameMessage position, String player);

    GameMessage getRotation();
    void performedRotation(GameMessage position);

    GameMessage isTurnOver();
    void confirmEndTurn(GameMessage message);

    GameMessage getChatMessage();
    void sendChatMessage(GameMessage message, String player);

    GameConditions getGameConditions();
    void sendGameConditions(GameConditions message);
}
