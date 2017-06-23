package com.avparamonov.checkers.model;

/**
 * Game status enum.
 *
 * Created by AVParamonov on 08.06.17.
 */
public enum GameStatus {

    READY(1),
    IN_PROGRESS(2),
    FINISHED(3),
    FORBIDDEN(4);

    private int id;

    GameStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
