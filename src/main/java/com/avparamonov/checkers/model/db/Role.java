package com.avparamonov.checkers.model.db;


public enum Role {

    ADMIN,
    USER;

    public static Role fromName(String name) {
        if (name != null) {
            return valueOf(name.toLowerCase());
        }
        return Role.USER;
    }
}
