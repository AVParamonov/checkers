package com.avparamonov.checkers.model;

/**
 * Created by andrey.paramonov@sigma.software on 22.05.17.
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
