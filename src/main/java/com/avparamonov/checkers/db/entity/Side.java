package com.avparamonov.checkers.db.entity;

/**
 * Created by andrey.paramonov@sigma.software on 22.05.17.
 */
public enum Side {

    BLACK(1),
    RED(2);

    private int id;

    Side(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
