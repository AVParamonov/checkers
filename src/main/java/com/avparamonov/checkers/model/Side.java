package com.avparamonov.checkers.model;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Player's or Checker's side.
 *
 * Created by AVParamonov on 22.05.17.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Side {

    WHITE(1, "white"),
    BLACK(2, "black");

    private int id;
    private String label;

    Side(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }
}
