package com.avparamonov.checkers.model;

/**
 * Created by andrey.paramonov@sigma.software on 22.05.17.
 */
public enum PlayerType {

    HUMAN("Human"),
    COMPUTER("Computer");

    private String value;

    PlayerType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    public static PlayerType getEnum(String value) {
        for(PlayerType v : values())
            if(v.getValue().equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException();
    }
}
