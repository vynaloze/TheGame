package thegamepackage.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Copyright (c) 2016 by Piotr Pawluk. All rights reserved.
 */
public enum ID {
    STONED_SLEEPWALKER, BENEDICT_XVI, ANDRZEJ_DUDA, BLUE_MAGE, LAWNMOVER_OPERATOR, JESUS,
    INFERNALIST, JUGGERNAUT, LASIODORA_PARAHYBANA, HUNTER, MILKY_CAT, CRAZY_DEMON,
    DONALD_TRUMP, HIPPOPOTAMUS_AMPHIBIUS, APHRODITE, CTHULHU, FLUFFY_UNICORN,
    PAN_TADEUSZ, VLADIMIR_PUTIN, COBRA;


    @Override
    public String toString() {
        return super.toString().replace("_", " ");
    }

    public static ID randomID() {
        return ID.values()[ThreadLocalRandom.current().nextInt(ID.values().length)];
    }
}
