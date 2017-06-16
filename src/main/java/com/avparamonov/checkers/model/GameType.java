package com.avparamonov.checkers.model;

/**
 * Checker type enum.
 *
 * Created by AVParamonov on 25.05.17.
 */
public enum GameType {

    NORMAL(8),
    INTERNATIONAL(10);

    private int size;

    GameType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

}
