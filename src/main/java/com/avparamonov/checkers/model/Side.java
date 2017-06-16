package com.avparamonov.checkers.model;

/**
 * Player's or Checker's side.
 *
 * Created by AVParamonov on 22.05.17.
 */
public enum Side {

    WHITE(1),
    BLACK(2);

    private int id;

    Side(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
