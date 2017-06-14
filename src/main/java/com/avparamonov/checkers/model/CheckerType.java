package com.avparamonov.checkers.model;

/**
 * Checker type enum.
 *
 * Created by andrey.paramonov@sigma.software on 25.05.17.
 */
public enum CheckerType {

    REGULAR(1),
    KING(2);

    private int id;

    CheckerType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
