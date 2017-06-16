package com.avparamonov.checkers.model;

/**
 * Directions by the board diagonals.
 *
 * Created by AVParamonov on 16.06.17.
 */
public enum Directions {

    UP_LEFT(new int[] {1, -1}),
    UP_RIGHT(new int[] {1, 1}),
    DOWN_RIGHT(new int[] {-1, 1}),
    DOWN_LEFT(new int[] {-1, -1});

    static {
        UP_LEFT.oppositeDirection = Directions.DOWN_RIGHT;
        UP_RIGHT.oppositeDirection = Directions.DOWN_LEFT;
        DOWN_RIGHT.oppositeDirection = Directions.UP_LEFT;
        DOWN_LEFT.oppositeDirection = Directions.UP_RIGHT;
    }

    private final int[] signs;
    private Directions oppositeDirection;

    Directions(int[] signs) {
        this.signs = signs;
    }

    public int[] getSigns() { return signs; }

    public Directions getOpposite() { return this.oppositeDirection; }
}
