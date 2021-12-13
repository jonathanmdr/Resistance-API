package com.starwars.resistance.core.common.model;

public enum Item {

    WEAPON(4),
    AMMUNITION(3),
    WATER(2),
    FOOD(1);

    private final int score;

    Item(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

}
